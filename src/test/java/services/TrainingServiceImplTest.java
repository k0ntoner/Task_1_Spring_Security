package services;

import org.example.repositories.*;
import org.example.repositories.entities.Trainee;
import org.example.repositories.entities.Trainer;
import org.example.repositories.entities.Training;
import org.example.repositories.entities.TrainingType;
import org.example.services.TraineeServiceImpl;
import org.example.services.TrainerServiceImpl;
import org.example.services.TrainingServiceImpl;
import org.example.utils.UserUtils;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
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

    private Training testTraining;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        testTraining = buildTrainingForAdding();
        doAnswer(invocation -> {
            Training training = invocation.getArgument(0);
            training.setId(1L);
            return Optional.of(training);
        }).when(trainingDao).save(any(Training.class));
    }

    public Trainee buildTrainee() {
        return Trainee.builder()
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
    public Trainer buildTrainer() {
        return Trainer.builder()
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
    public Training buildTrainingForAdding() {
        return Training.builder()
                .trainer(buildTrainer())
                .trainee(buildTrainee())
                .trainingName("name")
                .trainingDate(LocalDateTime.of(2024,12,12,15,30))
                .trainingDuration(Duration.ofHours(1))
                .trainingType(TrainingType.STRENGTH)
                .build();
    }
    @Test
    @DisplayName("Should return saved training")
    public void save_ShouldReturnSavedTraining() {


        Optional<Training> newTraining = trainingService.add(testTraining);

        assertTrue(newTraining.isPresent());
        assertNotNull(newTraining.get().getId());
        assertEquals(testTraining.getTrainingName(), newTraining.get().getTrainingName());
        assertEquals(testTraining.getTrainingDate(), newTraining.get().getTrainingDate());
        assertEquals(testTraining.getTrainingDuration(), newTraining.get().getTrainingDuration());
        assertEquals(testTraining.getTrainingType(), newTraining.get().getTrainingType());
        assertEquals(testTraining.getTrainee().getId(), newTraining.get().getTrainee().getId());
        assertEquals(testTraining.getTrainer().getId(), newTraining.get().getTrainer().getId());
    }
    @Test
    @DisplayName("Should find training by id")
    public void findById_ShouldReturnTrainingById() {


        Optional<Training> newTraining = trainingDao.save(testTraining);

        when(trainingDao.findById(newTraining.get().getId())).thenReturn(Optional.of(testTraining));
        Optional<Training> foundTraining = trainingService.findById(newTraining.get().getId());
        assertTrue(foundTraining.isPresent());
        assertNotNull(foundTraining.get().getId());

        assertEquals(testTraining.getTrainingName(), foundTraining.get().getTrainingName());
        assertEquals(testTraining.getTrainingDate(), foundTraining.get().getTrainingDate());
        assertEquals(testTraining.getTrainingDuration(), foundTraining.get().getTrainingDuration());
        assertEquals(testTraining.getTrainingType(), foundTraining.get().getTrainingType());
        assertEquals(testTraining.getTrainer().getId(), foundTraining.get().getTrainer().getId());
        assertEquals(testTraining.getTrainee().getId(), foundTraining.get().getTrainee().getId());
    }
    @Test
    @DisplayName("Should find collection of trainings")
    public void findAllTrainings_ShouldReturnAllTrainings() {


        Optional<Training> newTraining = trainingDao.save(testTraining);
        assertTrue(newTraining.isPresent());
        assertNotNull(newTraining.get().getId());

        List<Training> trainings=new ArrayList<>();
        trainings.add(newTraining.get());

        when(trainingDao.findAll()).thenReturn(Optional.of(trainings));


        Optional<Collection<Training>> foundTrainings = trainingService.findAll();
        assertTrue(foundTrainings.isPresent());
        assertEquals(foundTrainings.get().size(), 1);
        foundTrainings.get().forEach(training -> {
            assertNotNull(training.getId());
            assertNotNull(training.getTrainee());
            assertNotNull(training.getTrainer());
            assertNotNull(training.getTrainingType());
            assertNotNull(training.getTrainingDate());
            assertNotNull(training.getTrainingDuration());
            assertNotNull(training.getTrainingType());
        });
    }
}
