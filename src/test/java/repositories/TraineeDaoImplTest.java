package repositories;

import jakarta.persistence.PersistenceException;
import org.example.Application;
import org.example.repositories.entities.Trainee;
import org.example.repositories.impl.TraineeDaoImpl;
import org.example.utils.UserUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataAccessException;
import org.springframework.test.context.ActiveProfiles;


import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.Collection;

@SpringBootTest(classes = Application.class)
@ActiveProfiles("test")
public class TraineeDaoImplTest {

    @Autowired
    private TraineeDaoImpl traineeDaoImpl;
    private Trainee testTrainee;

    @BeforeEach
    public void setUp() {
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
        Trainee newTrainee = traineeDaoImpl.save(testTrainee);

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

        Trainee newTrainee = traineeDaoImpl.save(testTrainee);

        newTrainee.setAddress(newAddress);
        newTrainee.setFirstName(newFirstName);
        newTrainee.setLastName(newLastName);
        newTrainee.setUsername(newUsername);
        newTrainee.setPassword(newPassword);
        newTrainee.setDateOfBirth(newDateOfBirth);

        Trainee updatedTrainee = traineeDaoImpl.update(newTrainee);

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
        assertThrows(DataAccessException.class, () -> traineeDaoImpl.update(testTrainee));
        assertFalse(traineeDaoImpl.findById(testTrainee.getId()).isPresent());
    }

    @Test
    @DisplayName("Should delete Trainee")
    public void delete_ShouldDeleteTrainee() {
        Trainee newTrainee = traineeDaoImpl.save(testTrainee);
        Long id = newTrainee.getId();

        traineeDaoImpl.delete(newTrainee);

        assertFalse(traineeDaoImpl.findById(id).isPresent());
    }

    @Test
    @DisplayName("Should find Trainee by id")
    public void findById_ShouldFindTraineeById() {

        Trainee newTrainee = traineeDaoImpl.save(testTrainee);
        Long id = newTrainee.getId();

        traineeDaoImpl.delete(newTrainee);

        assertFalse(traineeDaoImpl.findById(id).isPresent());

        newTrainee = traineeDaoImpl.save(buildTraineeForAdding());

        assertTrue(traineeDaoImpl.findById(newTrainee.getId()).isPresent());

    }

    @Test
    @DisplayName("Should find Collection of all Trainees")
    public void findAll_ShouldFindAllTrainees() {
        Trainee newTrainee = traineeDaoImpl.save(testTrainee);

        assertNotNull(newTrainee);

        Collection<Trainee> trainees = traineeDaoImpl.findAll();

        assertTrue(trainees.size() > 0);
    }

    @Test
    @DisplayName("Should find Trainee by username")
    public void findByUsername_ShouldFindTraineeByUserName() {
        Trainee newTrainee = traineeDaoImpl.save(testTrainee);
        traineeDaoImpl.delete(newTrainee);

        assertFalse(traineeDaoImpl.findByUsername(testTrainee.getUsername()).isPresent());

        newTrainee = traineeDaoImpl.save(buildTraineeForAdding());

        assertTrue(traineeDaoImpl.findByUsername(newTrainee.getUsername()).isPresent());
    }


}
