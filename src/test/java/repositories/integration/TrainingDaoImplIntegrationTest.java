package repositories.integration;

import org.example.configs.Config;
import org.example.repositories.TraineeDao;
import org.example.repositories.TrainerDao;
import org.example.repositories.TrainingDao;
import org.example.repositories.entities.Trainee;
import org.example.repositories.entities.Trainer;
import org.example.repositories.entities.Training;
import org.example.repositories.entities.TrainingType;
import org.example.utils.UserUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import static org.junit.jupiter.api.Assertions.*;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Optional;

public class TrainingDaoImplIntegrationTest {
    private TrainingDao trainingDao;
    private TrainerDao trainerDao;
    private TraineeDao traineeDao;
    private AnnotationConfigApplicationContext context;
    private Training testTraining;
    private Trainer testTrainer;
    private Trainee testTrainee;

    @BeforeEach
    public void setUp() {
        context = new AnnotationConfigApplicationContext(Config.class);
        trainingDao = context.getBean(TrainingDao.class);
        trainerDao = context.getBean(TrainerDao.class);
        traineeDao = context.getBean(TraineeDao.class);
        testTraining = buildTrainingForAdding();
        testTrainer = buildTrainerForAdding();
        testTrainee = buildTraineeForAdding();
    }

    @AfterEach
    public void tearDown() {
        context.close();
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
                .trainingDate(LocalDateTime.of(2024,12,12,15,30))
                .trainingDuration(Duration.ofHours(1))
                .trainingType(TrainingType.STRENGTH)
                .build();
    }
    @Test
    @DisplayName("Should return saved training")
    public void save_ShouldReturnSavedTraining() {
        Optional<Trainer> newTrainer= trainerDao.save(testTrainer);
        assertTrue(newTrainer.isPresent());
        assertNotNull(newTrainer.get().getId());
        Optional<Trainee> newTrainee= traineeDao.save(testTrainee);
        assertTrue(newTrainee.isPresent());
        assertNotNull(newTrainee.get().getId());

        testTraining.setTrainee(newTrainee.get());
        testTraining.setTrainer(newTrainer.get());
        Optional<Training> newTraining = trainingDao.save(testTraining);

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
        Optional<Trainer> newTrainer= trainerDao.save(testTrainer);
        assertTrue(newTrainer.isPresent());
        assertNotNull(newTrainer.get().getId());

        Optional<Trainee> newTrainee= traineeDao.save(testTrainee);
        assertTrue(newTrainee.isPresent());
        assertNotNull(newTrainee.get().getId());

        testTraining.setTrainee(newTrainee.get());
        testTraining.setTrainer(newTrainer.get());

        Optional<Training> newTraining = trainingDao.save(testTraining);
        assertTrue(newTraining.isPresent());
        assertNotNull(newTraining.get().getId());

        Optional<Training> foundTraining = trainingDao.findById(newTraining.get().getId());
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
        Optional<Trainer> newTrainer= trainerDao.save(testTrainer);
        assertTrue(newTrainer.isPresent());
        assertNotNull(newTrainer.get().getId());

        Optional<Trainee> newTrainee= traineeDao.save(testTrainee);
        assertTrue(newTrainee.isPresent());
        assertNotNull(newTrainee.get().getId());

        testTraining.setTrainee(newTrainee.get());
        testTraining.setTrainer(newTrainer.get());

        Optional<Training> newTraining = trainingDao.save(testTraining);
        assertTrue(newTraining.isPresent());
        assertNotNull(newTraining.get().getId());

        Optional<Collection<Training>> trainings = trainingDao.findAll();
        assertTrue(trainings.isPresent());
        assertEquals(trainings.get().size(), 1);
        trainings.get().forEach(training -> {
            assertNotNull(training.getId());
            assertNotNull(training.getTrainee());
            assertNotNull(training.getTrainer());
            assertNotNull(training.getTrainingType());
            assertNotNull(training.getTrainingDate());
            assertNotNull(training.getTrainingDuration());
            assertNotNull(training.getTrainingType());
        });
    }
    @Test
    @DisplayName("Should find training by trainer username and criteria")
    public void findByTrainer_ShouldReturnTrainingByTrainer() {
        Optional<Trainer> newTrainer= trainerDao.save(testTrainer);
        assertTrue(newTrainer.isPresent());
        assertNotNull(newTrainer.get().getId());

        Optional<Trainee> newTrainee= traineeDao.save(testTrainee);
        assertTrue(newTrainee.isPresent());
        assertNotNull(newTrainee.get().getId());

        testTraining.setTrainee(newTrainee.get());
        testTraining.setTrainer(newTrainer.get());

        Optional<Training> newTraining = trainingDao.save(testTraining);
        assertTrue(newTraining.isPresent());
        assertNotNull(newTraining.get().getId());

        Optional<Collection<Training>> trainings = trainingDao.findByTrainer(testTrainer.getUsername()
                ,LocalDateTime.of(2024,12,12,0,0)
                ,LocalDateTime.of(2024,12,14,0,0)
                , testTrainee.getUsername());
        assertTrue(trainings.isPresent());
        assertEquals(trainings.get().size(), 1);
        trainings.get().forEach(training -> {
            assertNotNull(training.getId());
            assertNotNull(training.getTrainee());
            assertNotNull(training.getTrainer());
            assertNotNull(training.getTrainingType());
            assertNotNull(training.getTrainingDate());
            assertNotNull(training.getTrainingDuration());
            assertNotNull(training.getTrainingType());
        });
    }
    @Test
    @DisplayName("Should find training by trainee username and criteria")
    public void findByTrainee_ShouldReturnTrainingByTrainee() {
        Optional<Trainer> newTrainer = trainerDao.save(testTrainer);
        assertTrue(newTrainer.isPresent());
        assertNotNull(newTrainer.get().getId());

        Optional<Trainee> newTrainee = traineeDao.save(testTrainee);
        assertTrue(newTrainee.isPresent());
        assertNotNull(newTrainee.get().getId());

        testTraining.setTrainee(newTrainee.get());
        testTraining.setTrainer(newTrainer.get());

        Optional<Training> newTraining = trainingDao.save(testTraining);
        assertTrue(newTraining.isPresent());
        assertNotNull(newTraining.get().getId());
        Optional<Collection<Training>> trainings = trainingDao.findByTrainee(testTrainee.getUsername()
                , LocalDateTime.of(2024, 12, 12, 0, 0)
                , LocalDateTime.of(2024, 12, 14, 0, 0)
                , testTrainer.getUsername(),TrainingType.STRENGTH);
        assertTrue(trainings.isPresent());
        assertEquals(trainings.get().size(), 1);
        trainings.get().forEach(training -> {
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
