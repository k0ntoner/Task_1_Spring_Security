package repositories;

import configs.TestWebConfig;
import org.example.repositories.TraineeDao;
import org.example.repositories.entities.Trainee;
import org.example.utils.UserUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;


import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.Collection;

public class TraineeDaoImplTest {
    private TraineeDao traineeDao;
    private AnnotationConfigApplicationContext context;
    private Trainee testTrainee;

    @BeforeEach
    public void setUp() {
        context = new AnnotationConfigApplicationContext(TestWebConfig.class);
        traineeDao = context.getBean(TraineeDao.class);
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

    @Test
    @DisplayName("Should return new Trainee")
    public void save_ShouldSaveNewTrainee() {
        Trainee newTrainee = traineeDao.save(testTrainee);

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
    @DisplayName("Should throw exception when update not existing trainee")
    public void update_ShouldThrowExceptionWhenUpdateNotExistingTrainee() {
        testTrainee.setId(1L);
        assertThrows(IllegalArgumentException.class, () -> traineeDao.update(testTrainee));
        assertFalse(traineeDao.findById(testTrainee.getId()).isPresent());
    }

    @Test
    @DisplayName("Should delete Trainee")
    public void delete_ShouldDeleteTrainee() {
        Trainee newTrainee = traineeDao.save(testTrainee);
        Long id = newTrainee.getId();

        traineeDao.delete(newTrainee);

        assertFalse(traineeDao.findById(id).isPresent());
    }

    @Test
    @DisplayName("Should find Trainee by id")
    public void findById_ShouldFindTraineeById() {

        Trainee newTrainee = traineeDao.save(testTrainee);
        Long id = newTrainee.getId();

        traineeDao.delete(newTrainee);

        assertFalse(traineeDao.findById(id).isPresent());

        newTrainee = traineeDao.save(buildTraineeForAdding());

        assertTrue(traineeDao.findById(newTrainee.getId()).isPresent());

    }

    @Test
    @DisplayName("Should find Collection of all Trainees")
    public void findAll_ShouldFindAllTrainees() {
        Trainee newTrainee = traineeDao.save(testTrainee);

        assertNotNull(newTrainee);

        Collection<Trainee> trainees = traineeDao.findAll();

        assertTrue(trainees.size() > 0);
    }

    @Test
    @DisplayName("Should find Trainee by username")
    public void findByUsername_ShouldFindTraineeByUserName() {
        Trainee newTrainee = traineeDao.save(testTrainee);
        traineeDao.delete(newTrainee);

        assertFalse(traineeDao.findByUsername(testTrainee.getUsername()).isPresent());

        newTrainee = traineeDao.save(buildTraineeForAdding());

        assertTrue(traineeDao.findByUsername(newTrainee.getUsername()).isPresent());
    }


}
