package repositories;

import configs.TestWebConfig;
import org.example.Application;
import org.example.repositories.TrainerDao;
import org.example.repositories.entities.Trainer;
import org.example.enums.TrainingType;
import org.example.utils.UserUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Profile;
import org.springframework.dao.DataAccessException;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Collection;

@SpringBootTest(classes = Application.class)
@ActiveProfiles("test")
public class TrainerDaoImplTest {
    @Autowired
    @Qualifier("trainerDaoImpl")
    private TrainerDao trainerDao;
    private Trainer testTrainer;

    @BeforeEach
    public void setUp() {
        testTrainer = buildTrainerForAdding();
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

    @Test
    @DisplayName("Should save new Trainer")
    public void save_ShouldSaveNewTrainer() {
        Trainer newTrainer = trainerDao.save(testTrainer);

        assertNotNull(newTrainer.getId());
        assertEquals(testTrainer.getUsername(), newTrainer.getUsername());
        assertEquals(testTrainer.getFirstName(), newTrainer.getFirstName());
        assertEquals(testTrainer.getLastName(), newTrainer.getLastName());
        assertEquals(testTrainer.getSpecialization(), newTrainer.getSpecialization());
    }

    @Test
    @DisplayName("Should update existing Trainer")
    public void update_ShouldUpdateTrainer() {
        TrainingType newSpecialization = TrainingType.CARDIO;
        String newFirstName = "newFirstName";
        String newLastName = "newLastName";
        String newUsername = "newUsername";
        String newPassword = UserUtils.hashPassword("newPass");

        Trainer newTrainer = trainerDao.save(testTrainer);

        newTrainer.setSpecialization(newSpecialization);
        newTrainer.setFirstName(newFirstName);
        newTrainer.setLastName(newLastName);
        newTrainer.setUsername(newUsername);
        newTrainer.setPassword(newPassword);

        Trainer updatedTrainer = trainerDao.update(newTrainer);

        assertNotNull(updatedTrainer);
        assertEquals(newSpecialization, updatedTrainer.getSpecialization());
        assertEquals(newFirstName, updatedTrainer.getFirstName());
        assertEquals(newLastName, updatedTrainer.getLastName());
        assertEquals(newUsername, updatedTrainer.getUsername());
        assertTrue(UserUtils.passwordMatch("newPass", updatedTrainer.getPassword()));

    }

    @Test
    @DisplayName("Should throw exception when update not existing trainer")
    public void update_ShouldThrowExceptionWhenUpdateNotExistingTrainer() {
        testTrainer.setId(1L);
        assertThrows(DataAccessException.class, () -> trainerDao.update(testTrainer));
        assertFalse(trainerDao.findById(testTrainer.getId()).isPresent());
    }

    @Test
    @DisplayName("Should find Trainer by ID")
    public void findById_ShouldFindTrainerById() {
        Trainer newTrainer = trainerDao.save(testTrainer);
        assertTrue(trainerDao.findById(newTrainer.getId()).isPresent());

    }

    @Test
    @DisplayName("Should find all Trainers")
    public void findAll_ShouldFindAllTrainers() {
        trainerDao.save(testTrainer);
        Collection<Trainer> trainers = trainerDao.findAll();
        assertTrue(trainers.size() > 0);
    }

    @Test
    @DisplayName("Should find Trainer by username")
    public void findByUsername_ShouldFindTrainerByUsername() {
        Trainer newTrainer = trainerDao.save(testTrainer);
        assertTrue(trainerDao.findByUsername(newTrainer.getUsername()).isPresent());

    }
}
