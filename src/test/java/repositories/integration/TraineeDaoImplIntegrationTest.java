package repositories.integration;

import jakarta.persistence.OptimisticLockException;
import org.example.configs.Config;
import org.example.repositories.TraineeDao;
import org.example.repositories.entities.Trainee;
import org.example.utils.UserUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.Collection;
import java.util.NoSuchElementException;
import java.util.Optional;

public class TraineeDaoImplIntegrationTest {
    private TraineeDao traineeDao;
    private AnnotationConfigApplicationContext context;
    private Trainee testTrainee;

    @BeforeEach
    public void setUp() {
        context = new AnnotationConfigApplicationContext(Config.class);
        traineeDao = context.getBean(TraineeDao.class);
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
    @Test
    @DisplayName("Should return new Trainee")
    public void save_ShouldSaveNewTrainee() {
        Optional<Trainee> newTrainee = traineeDao.save(testTrainee);
        assertTrue(newTrainee.isPresent());
        assertNotNull(newTrainee.get().getId());
        assertEquals(testTrainee.getUsername(), newTrainee.get().getUsername());
        assertEquals(testTrainee.getFirstName(), newTrainee.get().getFirstName());
        assertEquals(testTrainee.getLastName(), newTrainee.get().getLastName());
        assertEquals(testTrainee.getDateOfBirth(), newTrainee.get().getDateOfBirth());
        assertEquals(testTrainee.getAddress(), newTrainee.get().getAddress());
        assertEquals(testTrainee.getPassword(), newTrainee.get().getPassword());
    }

    @Test
    @DisplayName("Should return updated Trainee")
    public void update_ShouldUpdateTrainee() {

        String newAddress = "newAddress";
        String newFirstName = "newFirstName";
        String newLastName = "newLastName";
        String newUsername = "newUsername";
        LocalDate newDateOfBirth = LocalDate.of(2000, 11, 23);
        String newPassword = UserUtils.hashPassword("newPass");

        Trainee newTrainee = traineeDao.save(testTrainee).get();

        newTrainee.setAddress(newAddress);
        newTrainee.setFirstName(newFirstName);
        newTrainee.setLastName(newLastName);
        newTrainee.setUsername(newUsername);
        newTrainee.setPassword(newPassword);
        newTrainee.setDateOfBirth(newDateOfBirth);
        Optional<Trainee> updatedTrainee = traineeDao.save(newTrainee);

        assertTrue(updatedTrainee.isPresent());
        assertNotNull(updatedTrainee.get().getId());
        assertEquals(newUsername, updatedTrainee.get().getUsername());
        assertEquals(newAddress, updatedTrainee.get().getAddress());
        assertEquals(newFirstName, updatedTrainee.get().getFirstName());
        assertEquals(newLastName, updatedTrainee.get().getLastName());
        assertEquals(newDateOfBirth, updatedTrainee.get().getDateOfBirth());
        assertTrue(UserUtils.passwordMatch("newPass", updatedTrainee.get().getPassword()));

    }

    @Test
    @DisplayName("Should update not existing trainee")
    public void update_ShouldUpdateNotExistingTrainee() {
        testTrainee.setId(1L);
        assertFalse(traineeDao.save(testTrainee).isPresent());
    }
    @Test
    @DisplayName("Should delete Trainee")
    public void delete_ShouldDeleteTrainee() {
        Trainee newTrainee=traineeDao.save(testTrainee).get();
        assertNotNull(newTrainee);
        assertNotNull(newTrainee.getId());
        Long id = newTrainee.getId();
        traineeDao.delete(newTrainee);
        assertFalse(traineeDao.findById(id).isPresent());
    }
    @Test
    @DisplayName("Should find Trainee by id")
    public void findById_ShouldFindTraineeById() {
        Trainee newTrainee = traineeDao.save(testTrainee).get();

        assertNotNull(newTrainee);
        assertNotNull(newTrainee.getId());

        Trainee foundTrainee = traineeDao.findById(newTrainee.getId()).get();

        assertNotNull(foundTrainee);
        assertEquals(newTrainee.getId(), foundTrainee.getId());
        assertEquals(newTrainee.getUsername(), foundTrainee.getUsername());
        assertEquals(newTrainee.getFirstName(), foundTrainee.getFirstName());
        assertEquals(newTrainee.getLastName(), foundTrainee.getLastName());
        assertEquals(newTrainee.getDateOfBirth(), foundTrainee.getDateOfBirth());
        assertEquals(newTrainee.getAddress(), foundTrainee.getAddress());
        assertEquals(newTrainee.getPassword(), foundTrainee.getPassword());
        assertEquals(newTrainee.getIsActive(), foundTrainee.getIsActive());
    }

    @Test
    @DisplayName("Should find Collection of all Trainees")
    public void findAll_ShouldFindAllTrainees() {
        Trainee newTrainee=traineeDao.save(testTrainee).get();
        assertNotNull(newTrainee);
        Collection<Trainee> trainees=traineeDao.findAll().get();

        assertNotNull(trainees);
        assertEquals(trainees.size(), 1);

        trainees.forEach(
                trainee -> {
                    assertNotNull(trainee.getId());
                    assertNotNull(trainee.getUsername());
                    assertNotNull(trainee.getFirstName());
                    assertNotNull(trainee.getLastName());
                    assertNotNull(trainee.getDateOfBirth());
                    assertNotNull(trainee.getAddress());
                    assertNotNull(trainee.getPassword());
                    assertNotNull(trainee.getIsActive());
                }
        );
    }
    @Test
    @DisplayName("Should find Trainee by username")
    public void findByUsername_ShouldFindTraineeByUserName() {
        Trainee newTrainee=traineeDao.save(testTrainee).get();
        assertNotNull(newTrainee);
        assertNotNull(newTrainee.getId());
        Trainee foundTrainee=traineeDao.findByUsername(newTrainee.getUsername()).get();
        assertNotNull(foundTrainee);
        assertEquals(newTrainee.getId(), foundTrainee.getId());
        assertEquals(newTrainee.getUsername(), foundTrainee.getUsername());
        assertEquals(newTrainee.getFirstName(), foundTrainee.getFirstName());
        assertEquals(newTrainee.getLastName(), foundTrainee.getLastName());
        assertEquals(newTrainee.getDateOfBirth(), foundTrainee.getDateOfBirth());
        assertEquals(newTrainee.getAddress(), foundTrainee.getAddress());
        assertEquals(newTrainee.getPassword(), foundTrainee.getPassword());
        assertEquals(newTrainee.getIsActive(), foundTrainee.getIsActive());
    }



}
