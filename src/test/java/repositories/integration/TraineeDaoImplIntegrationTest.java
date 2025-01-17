package repositories.integration;

import jakarta.persistence.OptimisticLockException;
import org.example.configs.Config;
import org.example.repositories.TraineeDao;
import org.example.repositories.entities.Trainee;
import org.example.repositories.impl.TraineeDaoImpl;
import org.example.utils.HibernateTestUtil;
import org.example.utils.UserUtils;
import org.hibernate.SessionFactory;
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
    private SessionFactory sessionFactory;
    ;
    private Trainee testTrainee;

    @BeforeEach
    public void setUp() {
        sessionFactory = HibernateTestUtil.getSessionFactory();
        traineeDao = new TraineeDaoImpl(sessionFactory);
        testTrainee = buildTraineeForAdding();
    }

    @AfterEach
    public void tearDown() {
        HibernateTestUtil.shutdown();
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
        Trainee newTrainee = traineeDao.save(testTrainee);
        assertNotNull(newTrainee);
        assertNotNull(newTrainee.getId());
        assertEquals(testTrainee.getUsername(), newTrainee.getUsername());
        assertEquals(testTrainee.getFirstName(), newTrainee.getFirstName());
        assertEquals(testTrainee.getLastName(), newTrainee.getLastName());
        assertEquals(testTrainee.getDateOfBirth(), newTrainee.getDateOfBirth());
        assertEquals(testTrainee.getAddress(), newTrainee.getAddress());
        assertEquals(testTrainee.getPassword(), newTrainee.getPassword());
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

        Trainee newTrainee = traineeDao.save(testTrainee);

        newTrainee.setAddress(newAddress);
        newTrainee.setFirstName(newFirstName);
        newTrainee.setLastName(newLastName);
        newTrainee.setUsername(newUsername);
        newTrainee.setPassword(newPassword);
        newTrainee.setDateOfBirth(newDateOfBirth);
        Trainee updatedTrainee = traineeDao.update(newTrainee);

        assertNotNull(updatedTrainee);
        assertNotNull(updatedTrainee.getId());
        assertEquals(newUsername, updatedTrainee.getUsername());
        assertEquals(newAddress, updatedTrainee.getAddress());
        assertEquals(newFirstName, updatedTrainee.getFirstName());
        assertEquals(newLastName, updatedTrainee.getLastName());
        assertEquals(newDateOfBirth, updatedTrainee.getDateOfBirth());
        assertTrue(UserUtils.passwordMatch("newPass", updatedTrainee.getPassword()));

    }

    @Test
    @DisplayName("Should update not existing trainee")
    public void update_ShouldUpdateNotExistingTrainee() {
        testTrainee.setId(1L);
        assertNull(traineeDao.update(testTrainee));
    }

    @Test
    @DisplayName("Should delete Trainee")
    public void delete_ShouldDeleteTrainee() {
        Trainee newTrainee = traineeDao.save(testTrainee);
        assertNotNull(newTrainee);
        assertNotNull(newTrainee.getId());
        Long id = newTrainee.getId();
        traineeDao.delete(newTrainee);
        assertFalse(traineeDao.findById(id).isPresent());
    }

    @Test
    @DisplayName("Should find Trainee by id")
    public void findById_ShouldFindTraineeById() {
        Trainee newTrainee = traineeDao.save(testTrainee);

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
    }

    @Test
    @DisplayName("Should find Collection of all Trainees")
    public void findAll_ShouldFindAllTrainees() {
        Trainee newTrainee = traineeDao.save(testTrainee);
        assertNotNull(newTrainee);
        Collection<Trainee> trainees = traineeDao.findAll();

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
                }
        );
    }

    @Test
    @DisplayName("Should find Trainee by username")
    public void findByUsername_ShouldFindTraineeByUserName() {
        Trainee newTrainee = traineeDao.save(testTrainee);
        assertNotNull(newTrainee);
        assertNotNull(newTrainee.getId());
        Trainee foundTrainee = traineeDao.findByUsername(newTrainee.getUsername()).get();
        assertNotNull(foundTrainee);
        assertEquals(newTrainee.getId(), foundTrainee.getId());
        assertEquals(newTrainee.getUsername(), foundTrainee.getUsername());
        assertEquals(newTrainee.getFirstName(), foundTrainee.getFirstName());
        assertEquals(newTrainee.getLastName(), foundTrainee.getLastName());
        assertEquals(newTrainee.getDateOfBirth(), foundTrainee.getDateOfBirth());
        assertEquals(newTrainee.getAddress(), foundTrainee.getAddress());
        assertEquals(newTrainee.getPassword(), foundTrainee.getPassword());
    }


}
