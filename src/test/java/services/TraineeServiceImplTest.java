package services;


import org.example.repositories.TraineeDao;
import org.example.repositories.entities.Trainee;
import org.example.services.impl.TraineeServiceImpl;
import org.example.utils.UserUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;


import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.mock;

public class TraineeServiceImplTest {
    @Mock
    private TraineeDao traineeDao;
    @InjectMocks
    private TraineeServiceImpl traineeService;

    private Trainee testTrainee;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        testTrainee = buildTraineeForAdding();
        doAnswer(invocation -> {
            Trainee trainee = invocation.getArgument(0);
            trainee.setId(1L);
            return trainee;
        }).when(traineeDao).save(any(Trainee.class));

    }

    public Trainee buildTraineeForAdding() {
        return Trainee.builder()
                .firstName("John")
                .lastName("Doe")
                .password("JohnDoe")
                .dateOfBirth(LocalDate.of(1998, 11, 23))
                .address("Test Street")
                .isActive(true)
                .build();
    }


    @Test
    @DisplayName("Should return saved Trainee")
    public void save_ShouldReturnSavedTrainee() {

        Trainee newTrainee = traineeService.add(testTrainee);

        assertNotNull(newTrainee);
        assertNotNull(newTrainee.getId());
        assertEquals(testTrainee.getFirstName(), newTrainee.getFirstName());
        assertEquals(testTrainee.getLastName(), newTrainee.getLastName());
        assertEquals(testTrainee.getDateOfBirth(), newTrainee.getDateOfBirth());
        assertEquals(testTrainee.getAddress(), newTrainee.getAddress());
        assertEquals(testTrainee.isActive(), newTrainee.isActive());
        assertEquals(testTrainee.getFirstName() + "." + testTrainee.getLastName(), newTrainee.getUsername());
        assertTrue(UserUtils.passwordMatch("JohnDoe", newTrainee.getPassword()));


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

        Trainee newTrainee = traineeService.add(testTrainee);

        newTrainee.setAddress(newAddress);
        newTrainee.setFirstName(newFirstName);
        newTrainee.setLastName(newLastName);
        newTrainee.setUsername(newUsername);
        newTrainee.setPassword(newPassword);
        newTrainee.setDateOfBirth(newDateOfBirth);

        when(traineeDao.update(newTrainee)).thenReturn(newTrainee);
        when(traineeDao.findById(newTrainee.getId())).thenReturn(Optional.of(newTrainee));
        Trainee updatedTrainee = traineeService.update(newTrainee);

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
        testTrainee = traineeService.add(testTrainee);
        when(traineeDao.findById(1L)).thenReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class, () -> traineeService.update(testTrainee));
    }

    @Test
    @DisplayName("Should delete Trainee")
    public void delete_ShouldDeleteTrainee() {
        testTrainee = traineeService.add(testTrainee);
        when(traineeDao.findById(1L)).thenReturn(Optional.of(testTrainee));

        traineeService.delete(testTrainee);
        verify(traineeDao).delete(testTrainee);

    }

    @Test
    @DisplayName("Should delete not exist Trainee")
    public void delete_ShouldDeleteNotExistTrainee() {
        testTrainee = traineeService.add(testTrainee);
        when(traineeDao.findById(1L)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> traineeService.delete(testTrainee));
        verify(traineeDao, never()).delete(testTrainee);

    }

    @Test
    @DisplayName("Should delete Trainee by username")
    public void delete_ShouldDeleteTraineeByUsername() {
        testTrainee = traineeService.add(testTrainee);
        when(traineeDao.findByUsername(testTrainee.getUsername())).thenReturn(Optional.of(testTrainee));

        traineeService.deleteByUsername(testTrainee.getUsername());
        verify(traineeDao).delete(testTrainee);

    }

    @Test
    @DisplayName("Should delete not exist Trainee by username")
    public void delete_ShouldDeleteNotExistTraineeByUsername() {
        testTrainee = traineeService.add(testTrainee);
        when(traineeDao.findByUsername(testTrainee.getUsername())).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> traineeService.deleteByUsername(testTrainee.getUsername()));
        verify(traineeDao, never()).delete(testTrainee);

    }

    @Test
    @DisplayName("Should find Trainee by id")
    public void findById_ShouldFindTraineeById() {
        testTrainee = traineeService.add(testTrainee);
        when(traineeDao.findById(1L)).thenReturn(Optional.of(testTrainee));

        Trainee foundTrainee = traineeService.findById(1L).get();
        assertNotNull(foundTrainee);
        assertEquals(testTrainee.getId(), foundTrainee.getId());
        assertEquals(testTrainee.getUsername(), foundTrainee.getUsername());
        assertEquals(testTrainee.getFirstName(), foundTrainee.getFirstName());
        assertEquals(testTrainee.getLastName(), foundTrainee.getLastName());
        assertEquals(testTrainee.getDateOfBirth(), foundTrainee.getDateOfBirth());
        assertEquals(testTrainee.getAddress(), foundTrainee.getAddress());
        assertEquals(testTrainee.getPassword(), foundTrainee.getPassword());
    }

    @Test
    @DisplayName("Should find Collection of all Trainees")
    public void findAll_ShouldFindAllTrainees() {

        testTrainee = traineeService.add(testTrainee);
        List<Trainee> trainees = new ArrayList<>();
        trainees.add(testTrainee);
        when(traineeDao.findAll()).thenReturn(trainees);
        Collection<Trainee> foundTrainees = traineeDao.findAll();

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
        testTrainee = traineeService.add(testTrainee);

        when(traineeDao.findByUsername(testTrainee.getUsername())).thenReturn(Optional.of(testTrainee));

        Trainee foundTrainee = traineeService.findByUsername(testTrainee.getUsername()).get();
        assertNotNull(foundTrainee);
        assertEquals(testTrainee.getId(), foundTrainee.getId());
        assertEquals(testTrainee.getUsername(), foundTrainee.getUsername());
        assertEquals(testTrainee.getFirstName(), foundTrainee.getFirstName());
        assertEquals(testTrainee.getLastName(), foundTrainee.getLastName());
        assertEquals(testTrainee.getDateOfBirth(), foundTrainee.getDateOfBirth());
        assertEquals(testTrainee.getAddress(), foundTrainee.getAddress());
        assertEquals(testTrainee.getPassword(), foundTrainee.getPassword());
    }

    @Test
    @DisplayName("Should change trainee password")
    public void changePassword_ShouldChangeTraineePassword() {
        testTrainee = traineeService.add(testTrainee);
        when(traineeDao.findByUsername(testTrainee.getUsername())).thenReturn(Optional.of(testTrainee));
        when(traineeDao.update(testTrainee)).thenReturn(testTrainee);

        traineeService.changePassword(testTrainee.getUsername(), "JohnDoe", "newPass");
        verify(traineeDao).update(testTrainee);
    }

    @Test
    @DisplayName("Should activate Trainee")
    public void activate_ShouldActivateTrainee() {
        testTrainee = traineeService.add(testTrainee);
        when(traineeDao.findById(testTrainee.getId())).thenReturn(Optional.of(testTrainee));
        when(traineeDao.update(testTrainee)).thenReturn(testTrainee);
        traineeService.deactivate(testTrainee);
        verify(traineeDao).update(testTrainee);

    }

    @Test
    @DisplayName("Should deactivate Trainee")
    public void activate_ShouldDeactivateTrainee() {
        testTrainee = traineeService.add(testTrainee);
        when(traineeDao.findById(testTrainee.getId())).thenReturn(Optional.of(testTrainee));
        when(traineeDao.update(testTrainee)).thenReturn(testTrainee);
        traineeService.deactivate(testTrainee);
        verify(traineeDao).update(testTrainee);

    }

}