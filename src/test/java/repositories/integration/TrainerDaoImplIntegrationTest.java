package repositories.integration;

import org.example.configs.Config;
import org.example.repositories.TrainerDao;
import org.example.repositories.entities.Trainee;
import org.example.repositories.entities.Trainer;
import org.example.repositories.entities.TrainingType;
import org.example.utils.UserUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Optional;

public class TrainerDaoImplIntegrationTest {

    private TrainerDao trainerDao;
    private AnnotationConfigApplicationContext context;
    private Trainer testTrainer;

    @BeforeEach
    public void setUp() {
        context = new AnnotationConfigApplicationContext(Config.class);
        trainerDao = context.getBean(TrainerDao.class);
        testTrainer = buildTrainerForAdding();
    }

    @AfterEach
    public void tearDown() {
        context.close();
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
    @Test
    @DisplayName("Should save new Trainer")
    public void save_ShouldSaveNewTrainer() {
        Optional<Trainer> newTrainer = trainerDao.save(testTrainer);
        assertTrue(newTrainer.isPresent());
        assertNotNull(newTrainer.get().getId());
        assertEquals(testTrainer.getUsername(), newTrainer.get().getUsername());
        assertEquals(testTrainer.getFirstName(), newTrainer.get().getFirstName());
        assertEquals(testTrainer.getLastName(), newTrainer.get().getLastName());
        assertEquals(testTrainer.getSpecialization(), newTrainer.get().getSpecialization());
        assertEquals(testTrainer.getTrainingType(), newTrainer.get().getTrainingType());
    }

    @Test
    @DisplayName("Should update existing Trainer")
    public void update_ShouldUpdateTrainer() {

        String newSpecialization = "NewSpecialization";
        TrainingType newTrainingType = TrainingType.CARDIO;
        String newFirstName = "newFirstName";
        String newLastName = "newLastName";
        String newUsername = "newUsername";
        String newPassword = UserUtils.hashPassword("newPass");
        Trainer newTrainer = trainerDao.save(testTrainer).get();

        newTrainer.setSpecialization(newSpecialization);
        newTrainer.setTrainingType(newTrainingType);
        newTrainer.setFirstName(newFirstName);
        newTrainer.setLastName(newLastName);
        newTrainer.setUsername(newUsername);
        newTrainer.setPassword(newPassword);

        Optional<Trainer> updatedTrainer = trainerDao.save(newTrainer);
        assertTrue(updatedTrainer.isPresent());
        assertEquals(newSpecialization, updatedTrainer.get().getSpecialization());
        assertEquals(newTrainingType, updatedTrainer.get().getTrainingType());
        assertEquals(newFirstName, updatedTrainer.get().getFirstName());
        assertEquals(newLastName, updatedTrainer.get().getLastName());
        assertEquals(newUsername, updatedTrainer.get().getUsername());
        assertTrue(UserUtils.passwordMatch("newPass", updatedTrainer.get().getPassword()));

    }

    @Test
    @DisplayName("Should update not existing trainer")
    public void update_ShouldUpdateNotExistingTrainee() {
        testTrainer.setId(1L);
        assertFalse(trainerDao.save(testTrainer).isPresent());
    }
    @Test
    @DisplayName("Should find Trainer by ID")
    public void findById_ShouldFindTrainerById() {
        Trainer newTrainer = trainerDao.save(testTrainer).get();
        Trainer foundTrainer = trainerDao.findById(newTrainer.getId()).get();
        assertNotNull(foundTrainer);
        assertEquals(newTrainer.getId(), foundTrainer.getId());
        assertEquals(newTrainer.getUsername(), foundTrainer.getUsername());
        assertEquals(newTrainer.getSpecialization(), foundTrainer.getSpecialization());
    }

    @Test
    @DisplayName("Should find all Trainers")
    public void findAll_ShouldFindAllTrainers() {
        trainerDao.save(testTrainer);
        Collection<Trainer> trainers = trainerDao.findAll().get();
        assertNotNull(trainers);
        assertEquals(1, trainers.size());
    }

    @Test
    @DisplayName("Should find Trainer by username")
    public void findByUsername_ShouldFindTrainerByUsername() {
        Trainer newTrainer = trainerDao.save(testTrainer).get();
        Trainer foundTrainer = trainerDao.findByUsername(newTrainer.getUsername()).get();
        assertNotNull(foundTrainer);
        assertEquals(newTrainer.getId(), foundTrainer.getId());
        assertEquals(newTrainer.getUsername(), foundTrainer.getUsername());
        assertEquals(newTrainer.getSpecialization(), foundTrainer.getSpecialization());
    }
}
