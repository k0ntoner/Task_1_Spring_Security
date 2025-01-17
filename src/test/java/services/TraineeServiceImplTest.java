package services;


import org.example.repositories.TraineeDao;
import org.example.repositories.entities.Trainee;
import org.example.services.TraineeServiceImpl;
import org.example.utils.UserUtils;
import org.hibernate.HibernateException;
import org.hibernate.query.Query;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.context.annotation.EnableLoadTimeWeaving;

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
            return Optional.of(trainee);
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
    public Trainee buildTraineeForUpdating() {
        return Trainee.builder()
                .id(1L)
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
    @DisplayName("Should return saved Trainee")
    public void save_ShouldReturnSavedTrainee() {

        Optional<Trainee> newTrainee=traineeService.add(testTrainee);
        assertTrue(newTrainee.isPresent());
        assertNotNull(newTrainee.get().getId());
        assertEquals(testTrainee.getFirstName(), newTrainee.get().getFirstName());
        assertEquals(testTrainee.getLastName(), newTrainee.get().getLastName());
        assertEquals(testTrainee.getDateOfBirth(), newTrainee.get().getDateOfBirth());
        assertEquals(testTrainee.getAddress(), newTrainee.get().getAddress());
        assertEquals(testTrainee.getIsActive(), newTrainee.get().getIsActive());
        assertEquals(testTrainee.getFirstName()+"."+testTrainee.getLastName(), newTrainee.get().getUsername());
        assertTrue(UserUtils.passwordMatch("JohnDoe", newTrainee.get().getPassword()));


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
        when(traineeDao.save(newTrainee)).thenReturn(Optional.of(newTrainee));
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
        testTrainee=buildTraineeForUpdating();
        when(traineeDao.findById(1L)).thenReturn(Optional.empty());
        assertFalse(traineeService.update(testTrainee).isPresent());
    }
    @Test
    @DisplayName("Should delete Trainee")
    public void delete_ShouldDeleteTrainee() {
        testTrainee=buildTraineeForUpdating();
        when(traineeDao.findById(1L)).thenReturn(Optional.of(testTrainee));

        assertTrue(traineeService.delete(testTrainee));
        verify(traineeDao).delete(testTrainee);

    }
    @Test
    @DisplayName("Should delete not exist Trainee")
    public void delete_ShouldDeleteNotExistTrainee() {
        testTrainee=buildTraineeForUpdating();
        when(traineeDao.findById(1L)).thenReturn(Optional.empty());

        assertFalse(traineeService.delete(testTrainee));
        verify(traineeDao, never()).delete(testTrainee);

    }
    @Test
    @DisplayName("Should delete Trainee by username")
    public void delete_ShouldDeleteTraineeByUsername() {
        testTrainee=buildTraineeForUpdating();
        when(traineeDao.findByUsername(testTrainee.getUsername())).thenReturn(Optional.of(testTrainee));

        assertTrue(traineeService.deleteByUsername(testTrainee.getUsername()));
        verify(traineeDao).delete(testTrainee);

    }
    @Test
    @DisplayName("Should delete not exist Trainee by username")
    public void delete_ShouldDeleteNotExistTraineeByUsername() {
        testTrainee=buildTraineeForUpdating();
        when(traineeDao.findByUsername(testTrainee.getUsername())).thenReturn(Optional.empty());

        assertFalse(traineeService.deleteByUsername(testTrainee.getUsername()));
        verify(traineeDao, never()).delete(testTrainee);

    }
    @Test
    @DisplayName("Should find Trainee by id")
    public void findById_ShouldFindTraineeById() {
        testTrainee=buildTraineeForUpdating();
        when(traineeDao.findById(1L)).thenReturn(Optional.of(testTrainee));

        Trainee foundTrainee=traineeService.findById(1L).get();
        assertNotNull(foundTrainee);
        assertEquals(testTrainee.getId(), foundTrainee.getId());
        assertEquals(testTrainee.getUsername(), foundTrainee.getUsername());
        assertEquals(testTrainee.getFirstName(), foundTrainee.getFirstName());
        assertEquals(testTrainee.getLastName(), foundTrainee.getLastName());
        assertEquals(testTrainee.getDateOfBirth(), foundTrainee.getDateOfBirth());
        assertEquals(testTrainee.getAddress(), foundTrainee.getAddress());
        assertEquals(testTrainee.getPassword(), foundTrainee.getPassword());
        assertEquals(testTrainee.getIsActive(), foundTrainee.getIsActive());
    }

    @Test
    @DisplayName("Should find Collection of all Trainees")
    public void findAll_ShouldFindAllTrainees() {

        testTrainee=buildTraineeForUpdating();
        List<Trainee> trainees=new ArrayList<>();
        trainees.add(testTrainee);
        when(traineeDao.findAll()).thenReturn(Optional.of(trainees));
        Collection<Trainee> foundTrainees=traineeDao.findAll().get();

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
        testTrainee=buildTraineeForUpdating();

        when(traineeDao.findByUsername(testTrainee.getUsername())).thenReturn(Optional.of(testTrainee));

        Trainee foundTrainee=traineeService.findByUsername(testTrainee.getUsername()).get();
        assertNotNull(foundTrainee);
        assertEquals(testTrainee.getId(), foundTrainee.getId());
        assertEquals(testTrainee.getUsername(), foundTrainee.getUsername());
        assertEquals(testTrainee.getFirstName(), foundTrainee.getFirstName());
        assertEquals(testTrainee.getLastName(), foundTrainee.getLastName());
        assertEquals(testTrainee.getDateOfBirth(), foundTrainee.getDateOfBirth());
        assertEquals(testTrainee.getAddress(), foundTrainee.getAddress());
        assertEquals(testTrainee.getPassword(), foundTrainee.getPassword());
        assertEquals(testTrainee.getIsActive(), foundTrainee.getIsActive());
    }
    @Test
    @DisplayName("Should change trainee password")
    public void changePassword_ShouldChangeTraineePassword() {
        testTrainee=buildTraineeForUpdating();
        when(traineeDao.findByUsername(testTrainee.getUsername())).thenReturn(Optional.of(testTrainee));
        when(traineeDao.save(testTrainee)).thenReturn(Optional.of(testTrainee));

        Optional<Trainee> updatedTrainee=traineeService.changePassword(testTrainee.getUsername(),"JohnDoe","newPass");
        assertTrue(updatedTrainee.isPresent());
        assertTrue(UserUtils.passwordMatch("newPass", updatedTrainee.get().getPassword()));
    }
    @Test
    @DisplayName("Should activate Trainee")
    public void activate_ShouldActivateTrainee() {
        testTrainee=buildTraineeForUpdating();
        when(traineeDao.findById(testTrainee.getId())).thenReturn(Optional.of(testTrainee));
        when(traineeDao.save(testTrainee)).thenReturn(Optional.of(testTrainee));
        Optional<Trainee> activatedTrainee=traineeService.deactivate(testTrainee);
        assertTrue(activatedTrainee.isPresent());
        assertTrue(activatedTrainee.get().getIsActive());
        verify(traineeDao).save(testTrainee);

    }
    @Test
    @DisplayName("Should deactivate Trainee")
    public void activate_ShouldDeactivateTrainee() {
        testTrainee=buildTraineeForUpdating();
        when(traineeDao.findById(testTrainee.getId())).thenReturn(Optional.of(testTrainee));
        when(traineeDao.save(testTrainee)).thenReturn(Optional.of(testTrainee));
        Optional<Trainee> deactivatedTrainee=traineeService.deactivate(testTrainee);
        assertTrue(deactivatedTrainee.isPresent());
        assertFalse(deactivatedTrainee.get().getIsActive());
        verify(traineeDao).save(testTrainee);

    }

}