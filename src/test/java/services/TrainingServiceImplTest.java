package services;

import org.example.models.TraineeDto;
import org.example.models.TrainerDto;
import org.example.models.TrainingDto;
import org.example.repositories.*;
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
    @InjectMocks
    TrainingServiceImpl trainingService;

    private TrainingDto testTrainingDto;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        testTrainingDto = buildTrainingForAdding();
        doAnswer(invocation -> {
            Training training = invocation.getArgument(0);
            training.setId(1L);
            return training;
        }).when(trainingDao).save(any(Training.class));
    }

    public TraineeDto buildTrainee() {
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

    public TrainerDto buildTrainer() {
        return TrainerDto.builder()
                .id(2L)
                .firstName("Mike")
                .lastName("Tyson")
                .username("Iron.Mike")
                .password(UserUtils.hashPassword("Champion"))
                .specialization("Boxing")
                .trainingType(TrainingType.STRENGTH)
                .isActive(true)
                .build();
    }

    public TrainingDto buildTrainingForAdding() {
        return TrainingDto.builder()
                .trainer(buildTrainer())
                .trainee(buildTrainee())
                .trainingName("name")
                .trainingDate(LocalDateTime.of(2024, 12, 12, 15, 30))
                .trainingDuration(Duration.ofHours(1))
                .trainingType(TrainingType.STRENGTH)
                .build();
    }

    @Test
    @DisplayName("Should return saved training")
    public void save_ShouldReturnSavedTraining() {
        TrainingDto newTrainingDto = trainingService.add(testTrainingDto);

        assertNotNull(newTrainingDto.getId());
        verify(trainingDao,times(1)).save(any(Training.class));
    }

    @Test
    @DisplayName("Should find training by id")
    public void findById_ShouldReturnTrainingById() {
        TrainingDto newTraining = trainingService.add(testTrainingDto);

        when(trainingDao.findById(newTraining.getId())).thenReturn(Optional.of(UserUtils.convertTrainingDtoToEntity(newTraining)));
        TrainingDto foundTraining = trainingService.findById(newTraining.getId()).get();

        assertNotNull(foundTraining.getId());
        verify(trainingDao,times(1)).findById(newTraining.getId());

    }

    @Test
    @DisplayName("Should find collection of trainings")
    public void findAllTrainings_ShouldReturnAllTrainings() {
        TrainingDto newTrainingDto = trainingService.add(testTrainingDto);

        List<Training> trainings = new ArrayList<>();
        trainings.add(UserUtils.convertTrainingDtoToEntity(testTrainingDto));

        when(trainingDao.findAll()).thenReturn(trainings);

        Collection<TrainingDto> foundTrainings = trainingService.findAll();

        assertTrue(foundTrainings.size() > 0);

        verify(trainingDao,times(1)).findAll();

    }
}
