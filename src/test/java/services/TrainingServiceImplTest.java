package services;

import org.example.models.trainee.TraineeDto;
import org.example.models.trainer.TrainerDto;
import org.example.models.training.TrainingDto;
import org.example.repositories.*;
import org.example.repositories.entities.Trainee;
import org.example.repositories.entities.Trainer;
import org.example.repositories.entities.Training;
import org.example.enums.TrainingType;
import org.example.services.impl.TrainingServiceImpl;
import org.example.utils.UserUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.core.convert.ConversionService;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;

public class TrainingServiceImplTest {
    @Mock
    TrainingDao trainingDao;

    @Mock
    private ConversionService conversionService;

    @InjectMocks
    TrainingServiceImpl trainingService;

    private TrainingDto testTrainingDto;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        testTrainingDto = buildTrainingDto();
        doAnswer(invocation -> {
            Training training = invocation.getArgument(0);
            training.setId(1L);
            return training;
        }).when(trainingDao).save(any(Training.class));

        doAnswer(invocation -> {
            Training training = invocation.getArgument(0);
            return TrainingDto.builder()
                    .id(training.getId())
                    .trainerDto(buildTrainerDto())
                    .traineeDto(buildTraineeDto())
                    .trainingDate(training.getTrainingDate())
                    .trainingDuration(training.getTrainingDuration())
                    .trainingType(training.getTrainingType())
                    .trainingName(training.getTrainingName())
                    .build();
        }).when(conversionService).convert(any(Training.class), eq(TrainingDto.class));

        doAnswer(invocation -> {
            TrainingDto trainingDto = invocation.getArgument(0);
            return Training.builder()
                    .id(trainingDto.getId())
                    .trainer(buildTrainer(trainingDto.getTrainerDto()))
                    .trainee(buildTrainee(trainingDto.getTraineeDto()))
                    .trainingDate(trainingDto.getTrainingDate())
                    .trainingDuration(trainingDto.getTrainingDuration())
                    .trainingType(trainingDto.getTrainingType())
                    .trainingName(trainingDto.getTrainingName())
                    .build();
        }).when(conversionService).convert(any(TrainingDto.class), eq(Training.class));
    }

    public TraineeDto buildTraineeDto() {
        return TraineeDto.builder()
                .id(1L)
                .firstName("John")
                .lastName("Doe")
                .username("John.Doe")
                .password(UserUtils.hashPassword("JohnDoe"))
                .dateOfBirth(LocalDate.of(1998, 11, 23))
                .address("Test Street")
                .isActive(true)
                .build();
    }

    public Trainee buildTrainee(TraineeDto traineeDto) {
        return Trainee.builder()
                .id(traineeDto.getId())
                .firstName(traineeDto.getFirstName())
                .lastName(traineeDto.getLastName())
                .password(traineeDto.getPassword())
                .dateOfBirth(traineeDto.getDateOfBirth())
                .address(traineeDto.getAddress())
                .isActive(traineeDto.isActive())
                .build();
    }


    public TrainerDto buildTrainerDto() {
        return TrainerDto.builder()
                .id(2L)
                .firstName("Mike")
                .lastName("Tyson")
                .username("Iron.Mike")
                .password(UserUtils.hashPassword("Champion"))
                .specialization(TrainingType.STRENGTH)
                .isActive(true)
                .build();
    }

    public Trainer buildTrainer(TrainerDto trainerDto) {
        return Trainer.builder()
                .id(trainerDto.getId())
                .firstName(trainerDto.getFirstName())
                .lastName(trainerDto.getLastName())
                .password(trainerDto.getPassword())
                .specialization(trainerDto.getSpecialization())
                .isActive(trainerDto.isActive())
                .build();
    }

    public TrainingDto buildTrainingDto() {
        return TrainingDto.builder()
                .trainerDto(buildTrainerDto())
                .traineeDto(buildTraineeDto())
                .trainingName("name")
                .trainingDate(LocalDateTime.of(2024, 12, 12, 15, 30))
                .trainingDuration(Duration.ofHours(1))
                .trainingType(TrainingType.STRENGTH)
                .build();
    }

    public Training buildTraining(TrainingDto trainingDto) {
        return Training.builder()
                .trainer(buildTrainer(trainingDto.getTrainerDto()))
                .trainee(buildTrainee(trainingDto.getTraineeDto()))
                .trainingName(trainingDto.getTrainingName())
                .trainingDate(trainingDto.getTrainingDate())
                .trainingDuration(trainingDto.getTrainingDuration())
                .trainingType(trainingDto.getTrainingType())
                .build();
    }

    @Test
    @DisplayName("Should return saved training")
    public void save_ShouldReturnSavedTraining() {
        TrainingDto newTrainingDto = trainingService.add(testTrainingDto);

        verify(trainingDao, times(1)).save(any(Training.class));
    }

    @Test
    @DisplayName("Should find training by id")
    public void findById_ShouldReturnTrainingById() {
        TrainingDto newTraining = trainingService.add(testTrainingDto);

        when(trainingDao.findById(newTraining.getId())).thenReturn(Optional.of(buildTraining(newTraining)));
        TrainingDto foundTraining = trainingService.findById(newTraining.getId());

        verify(trainingDao, times(1)).findById(newTraining.getId());
    }

    @Test
    @DisplayName("Should find collection of trainings")
    public void findAllTrainings_ShouldReturnAllTrainings() {
        TrainingDto newTrainingDto = trainingService.add(testTrainingDto);

        List<Training> trainings = new ArrayList<>();
        trainings.add(buildTraining(newTrainingDto));

        when(trainingDao.findAll()).thenReturn(trainings);

        Collection<TrainingDto> foundTrainings = trainingService.findAll();

        assertTrue(foundTrainings.size() > 0);

        verify(trainingDao, times(1)).findAll();
    }
}
