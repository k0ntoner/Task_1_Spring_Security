package repositories;

import org.example.repositories.entities.Trainee;
import org.example.repositories.entities.Trainer;
import org.example.repositories.entities.Training;
import org.example.repositories.entities.TrainingType;
import org.example.repositories.impl.TraineeDaoImpl;
import org.example.repositories.impl.TrainerDaoImpl;
import org.example.repositories.impl.TrainingDaoImpl;
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

public class TrainingDaoImplTest {
    @Mock
    private SessionFactory sessionFactory;
    @Mock
    private Session session;
    @Mock
    private Transaction transaction;
    @InjectMocks
    private TraineeDaoImpl traineeDao;
    @InjectMocks
    private TrainerDaoImpl trainerDao;
    @InjectMocks
    private TrainingDaoImpl trainingDao;
    private Training testTraining;

    private Trainer testTrainer;

    private Trainee testTrainee;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        testTrainee = buildTraineeForAdding();

        when(sessionFactory.openSession()).thenReturn(session);
        when(session.beginTransaction()).thenReturn(transaction);

        when(session.merge(testTrainee)).thenReturn(testTrainee);
        doAnswer(invocation -> {
            Trainee trainee = invocation.getArgument(0);
            trainee.setId(1L);
            return null;
        }).when(session).persist(any(Trainee.class));
        testTrainer = buildTrainerForAdding();

        when(session.merge(testTrainer)).thenReturn(testTrainer);
        doAnswer(invocation -> {
            Trainer trainer = invocation.getArgument(0);
            trainer.setId(1L);
            return null;
        }).when(session).persist(any(Trainer.class));

        testTraining = buildTrainingForAdding();
        when(session.merge(testTraining)).thenReturn(testTraining);
        doAnswer(invocation -> {
            Training training = invocation.getArgument(0);
            training.setId(1L);
            return null;
        }).when(session).persist(any(Training.class));

    }

    public Trainee buildTraineeForAdding() {
        return Trainee.builder()
                .firstName("John")
                .lastName("Doe")
                .username("John.Doe")
                .password(UserUtils.hashPassword("JohnDoe"))
                .dateOfBirth(LocalDate.of(1998, 11, 23))
                .address("Test Street")
                .isActive(true)
                .build();
    }

    public Trainer buildTrainerForAdding() {
        return Trainer.builder()
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
                .trainingName("name")
                .trainingDate(LocalDateTime.of(2024, 12, 12, 15, 30))
                .trainingDuration(Duration.ofHours(1))
                .trainingType(TrainingType.STRENGTH)
                .build();
    }

    @Test
    @DisplayName("Should return saved training")
    public void save_ShouldReturnSavedTraining() {
        Trainer newTrainer = trainerDao.save(testTrainer);
        assertNotNull(newTrainer);
        assertNotNull(newTrainer.getId());

        Trainee newTrainee = traineeDao.save(testTrainee);
        assertNotNull(newTrainee);
        assertNotNull(newTrainee.getId());

        testTraining.setTrainee(newTrainee);
        testTraining.setTrainer(newTrainer);
        Training newTraining = trainingDao.save(testTraining);

        assertNotNull(newTraining);
        assertNotNull(newTraining.getId());
        assertEquals(testTraining.getTrainingName(), newTraining.getTrainingName());
        assertEquals(testTraining.getTrainingDate(), newTraining.getTrainingDate());
        assertEquals(testTraining.getTrainingDuration(), newTraining.getTrainingDuration());
        assertEquals(testTraining.getTrainingType(), newTraining.getTrainingType());
        assertEquals(testTraining.getTrainee().getId(), newTraining.getTrainee().getId());
        assertEquals(testTraining.getTrainer().getId(), newTraining.getTrainer().getId());
    }

    @Test
    @DisplayName("Should find training by id")
    public void findById_ShouldReturnTrainingById() {
        Trainer newTrainer = trainerDao.save(testTrainer);
        assertNotNull(newTrainer);
        assertNotNull(newTrainer.getId());

        Trainee newTrainee = traineeDao.save(testTrainee);
        assertNotNull(newTrainee);
        assertNotNull(newTrainee.getId());

        testTraining.setTrainee(newTrainee);
        testTraining.setTrainer(newTrainer);

        Training newTraining = trainingDao.save(testTraining);
        assertNotNull(newTraining);
        assertNotNull(newTraining.getId());

        when(session.get(Training.class, testTraining.getId())).thenReturn(testTraining);

        Optional<Training> foundTraining = trainingDao.findById(newTraining.getId());
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
        Trainer newTrainer = trainerDao.save(testTrainer);
        assertNotNull(newTrainer);
        assertNotNull(newTrainer.getId());

        Trainee newTrainee = traineeDao.save(testTrainee);
        assertNotNull(newTrainee);
        assertNotNull(newTrainee.getId());

        testTraining.setTrainee(newTrainee);
        testTraining.setTrainer(newTrainer);
        Training newTraining = trainingDao.save(testTraining);
        assertNotNull(newTraining);
        assertNotNull(newTraining.getId());

        List<Training> trainings = new ArrayList<>();
        trainings.add(newTraining);
        Query<Training> mockQuery = mock(Query.class);

        when(session.createQuery("select t from Training t", Training.class)).thenReturn(mockQuery);

        when(mockQuery.list()).thenReturn(trainings);

        Collection<Training> foundTrainings = trainingDao.findAll();
        assertNotNull(foundTrainings);
        assertEquals(foundTrainings.size(), 1);
        foundTrainings.forEach(training -> {
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
