package repositories;

import configs.TestConfig;
import org.example.repositories.TraineeDao;
import org.example.repositories.TrainerDao;
import org.example.repositories.TrainingDao;
import org.example.repositories.entities.Trainee;
import org.example.repositories.entities.Trainer;
import org.example.repositories.entities.Training;
import org.example.enums.TrainingType;
import org.example.repositories.impl.TraineeDaoImpl;
import org.example.repositories.impl.TrainerDaoImpl;
import org.example.repositories.impl.TrainingDaoImpl;
import org.example.utils.UserUtils;
import org.hibernate.SessionFactory;
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

public class TrainingDaoImplTest {
    private TrainingDao trainingDao;
    private TrainerDao trainerDao;
    private TraineeDao traineeDao;

    private AnnotationConfigApplicationContext context;

    private Training testTraining;
    private Trainer testTrainer;
    private Trainee testTrainee;

    @BeforeEach
    public void setUp() {
        context = new AnnotationConfigApplicationContext(TestConfig.class);

        trainingDao = context.getBean(TrainingDao.class);
        trainerDao = context.getBean(TrainerDao.class);
        traineeDao = context.getBean(TraineeDao.class);

        testTraining = buildTrainingForAdding();
        testTrainer = buildTrainerForAdding();
        testTrainee = buildTraineeForAdding();
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
                .specialization(TrainingType.STRENGTH)
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

        Trainee newTrainee = traineeDao.save(testTrainee);

        testTraining.setTrainee(newTrainee);
        testTraining.setTrainer(newTrainer);

        Training newTraining = trainingDao.save(testTraining);

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
        Trainee newTrainee = traineeDao.save(testTrainee);

        testTraining.setTrainee(newTrainee);
        testTraining.setTrainer(newTrainer);

        Training newTraining = trainingDao.save(testTraining);

        assertTrue(trainingDao.findById(newTraining.getId()).isPresent());
    }

    @Test
    @DisplayName("Should find collection of trainings")
    public void findAllTrainings_ShouldReturnAllTrainings() {
        Trainer newTrainer = trainerDao.save(testTrainer);
        Trainee newTrainee = traineeDao.save(testTrainee);

        testTraining.setTrainee(newTrainee);
        testTraining.setTrainer(newTrainer);

        Training newTraining = trainingDao.save(testTraining);

        Collection<Training> trainings = trainingDao.findAll();
        assertTrue(trainings.size() > 0);
    }

    @Test
    @DisplayName("Should find training by trainer username and criteria")
    public void findByTrainer_ShouldReturnTrainingByTrainer() {
        Trainer newTrainer = trainerDao.save(testTrainer);
        Trainee newTrainee = traineeDao.save(testTrainee);

        testTraining.setTrainee(newTrainee);
        testTraining.setTrainer(newTrainer);

        Training newTraining = trainingDao.save(testTraining);

        Collection<Training> trainings = trainingDao.findByTrainer(testTrainer.getUsername()
                , LocalDateTime.of(2024, 12, 12, 0, 0)
                , LocalDateTime.of(2024, 12, 14, 0, 0)
                , testTrainee.getUsername());
        assertTrue(trainings.size() > 0);
    }

    @Test
    @DisplayName("Should find training by trainee username and criteria")
    public void findByTrainee_ShouldReturnTrainingByTrainee() {
        Trainer newTrainer = trainerDao.save(testTrainer);
        Trainee newTrainee = traineeDao.save(testTrainee);

        testTraining.setTrainee(newTrainee);
        testTraining.setTrainer(newTrainer);

        Training newTraining = trainingDao.save(testTraining);


        Collection<Training> trainings = trainingDao.findByTrainee(testTrainee.getUsername()
                , LocalDateTime.of(2024, 12, 12, 0, 0)
                , LocalDateTime.of(2024, 12, 14, 0, 0)
                , testTrainer.getUsername(), TrainingType.STRENGTH);

        assertTrue(trainings.size() > 0);
    }
    @Test
    @DisplayName("Should delete Trainee and Training by Cascade")
    public void delete_ShouldNotReturnTrainingById() {
        Trainer newTrainer = trainerDao.save(testTrainer);
        Trainee newTrainee = traineeDao.save(testTrainee);

        testTraining.setTrainee(newTrainee);
        testTraining.setTrainer(newTrainer);

        Training newTraining = trainingDao.save(testTraining);

        traineeDao.delete(newTrainee);
        assertFalse(trainingDao.findById(newTraining.getId()).isPresent());
    }

}
