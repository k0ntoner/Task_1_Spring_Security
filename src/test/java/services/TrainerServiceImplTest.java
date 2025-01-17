package services;


import org.example.repositories.TrainerDao;
import org.example.repositories.entities.Trainer;
import org.example.repositories.entities.TrainingType;
import org.example.services.impl.TrainerServiceImpl;
import org.example.utils.UserUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class TrainerServiceImplTest {
    @Mock
    private TrainerDao trainerDao;
    @InjectMocks
    private TrainerServiceImpl trainerService;

    private Trainer testTrainer;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        testTrainer = buildTrainerForAdding();
        doAnswer(invocation -> {
            Trainer trainer = invocation.getArgument(0);
            trainer.setId(1L);
            return trainer;
        }).when(trainerDao).save(any(Trainer.class));

    }

    public Trainer buildTrainerForAdding() {
        return Trainer.builder()
                .firstName("Mike")
                .lastName("Tyson")
                .password("MikeTyson")
                .specialization("Boxing")
                .trainingType(TrainingType.STRENGTH)
                .isActive(true)
                .build();
    }

    @Test
    @DisplayName("Should return saved Trainer")
    public void save_ShouldReturnSavedTrainer() {

        Trainer newTrainer = trainerService.add(testTrainer);

        assertNotNull(newTrainer);
        assertNotNull(newTrainer.getId());
        assertEquals(testTrainer.getUsername(), newTrainer.getUsername());
        assertEquals(testTrainer.getFirstName(), newTrainer.getFirstName());
        assertEquals(testTrainer.getLastName(), newTrainer.getLastName());
        assertEquals(testTrainer.getSpecialization(), newTrainer.getSpecialization());
        assertEquals(testTrainer.getTrainingType(), newTrainer.getTrainingType());
        assertEquals(testTrainer.getPassword(), newTrainer.getPassword());
        assertTrue(UserUtils.passwordMatch("MikeTyson", newTrainer.getPassword()));


    }

    @Test
    @DisplayName("Should return updated Trainer")
    public void update_ShouldUpdateTrainer() {

        String newFirstName = "newFirstName";
        String newLastName = "newLastName";
        String newUsername = "newUsername";
        String newPassword = UserUtils.hashPassword("newPass");
        TrainingType newTrainingType = TrainingType.CARDIO;
        String newSpecialization = "newSpecialization";


        Trainer newTrainer = trainerDao.save(testTrainer);

        newTrainer.setFirstName(newFirstName);
        newTrainer.setLastName(newLastName);
        newTrainer.setUsername(newUsername);
        newTrainer.setPassword(newPassword);
        newTrainer.setTrainingType(newTrainingType);
        newTrainer.setSpecialization(newSpecialization);

        when(trainerDao.update(newTrainer)).thenReturn(newTrainer);
        Trainer updatedTrainer = trainerDao.update(newTrainer);

        assertNotNull(updatedTrainer);
        assertNotNull(updatedTrainer.getId());
        assertEquals(newUsername, updatedTrainer.getUsername());
        assertEquals(newFirstName, updatedTrainer.getFirstName());
        assertEquals(newLastName, updatedTrainer.getLastName());
        assertEquals(newSpecialization, updatedTrainer.getSpecialization());
        assertEquals(newTrainingType, updatedTrainer.getTrainingType());
        assertTrue(UserUtils.passwordMatch("newPass", updatedTrainer.getPassword()));

    }

    @Test
    @DisplayName("Should update not existing trainer")
    public void update_ShouldUpdateNotExistingTrainer() {
        testTrainer = trainerService.add(testTrainer);
        when(trainerDao.findById(1L)).thenReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class, () -> trainerService.update(testTrainer));
    }

    @Test
    @DisplayName("Should find Trainer by id")
    public void findById_ShouldFindTrainerById() {
        testTrainer = trainerService.add(testTrainer);
        when(trainerDao.findById(1L)).thenReturn(Optional.of(testTrainer));

        Trainer foundTrainer = trainerService.findById(1L).get();
        assertNotNull(foundTrainer);
        assertEquals(testTrainer.getId(), foundTrainer.getId());
        assertEquals(testTrainer.getUsername(), foundTrainer.getUsername());
        assertEquals(testTrainer.getFirstName(), foundTrainer.getFirstName());
        assertEquals(testTrainer.getLastName(), foundTrainer.getLastName());
        assertEquals(testTrainer.getSpecialization(), foundTrainer.getSpecialization());
        assertEquals(testTrainer.getTrainingType(), foundTrainer.getTrainingType());
        assertEquals(testTrainer.getPassword(), foundTrainer.getPassword());
    }

    @Test
    @DisplayName("Should find Collection of all Trainers")
    public void findAll_ShouldFindAllTrainers() {

        testTrainer = trainerService.add(testTrainer);
        List<Trainer> trainers = new ArrayList<>();
        trainers.add(testTrainer);
        when(trainerDao.findAll()).thenReturn(trainers);
        Collection<Trainer> foundTrainers = trainerDao.findAll();

        assertNotNull(trainers);
        assertEquals(trainers.size(), 1);

        trainers.forEach(
                trainer -> {
                    assertNotNull(trainer.getId());
                    assertNotNull(trainer.getUsername());
                    assertNotNull(trainer.getFirstName());
                    assertNotNull(trainer.getLastName());
                    assertNotNull(trainer.getSpecialization());
                    assertNotNull(trainer.getTrainingType());
                    assertNotNull(trainer.getPassword());
                }
        );
    }

    @Test
    @DisplayName("Should find Trainer by username")
    public void findByUsername_ShouldFindTrainerByUserName() {
        testTrainer = trainerService.add(testTrainer);

        when(trainerDao.findByUsername(testTrainer.getUsername())).thenReturn(Optional.of(testTrainer));

        Trainer foundTrainer = trainerService.findByUsername(testTrainer.getUsername()).get();
        assertNotNull(foundTrainer);
        assertEquals(testTrainer.getId(), foundTrainer.getId());
        assertEquals(testTrainer.getUsername(), foundTrainer.getUsername());
        assertEquals(testTrainer.getFirstName(), foundTrainer.getFirstName());
        assertEquals(testTrainer.getLastName(), foundTrainer.getLastName());
        assertEquals(testTrainer.getSpecialization(), foundTrainer.getSpecialization());
        assertEquals(testTrainer.getTrainingType(), foundTrainer.getTrainingType());
        assertEquals(testTrainer.getPassword(), foundTrainer.getPassword());
    }

    @Test
    @DisplayName("Should change trainer password")
    public void changePassword_ShouldChangeTrainerPassword() {
        testTrainer = trainerService.add(testTrainer);
        when(trainerDao.findByUsername(testTrainer.getUsername())).thenReturn(Optional.of(testTrainer));
        when(trainerDao.update(testTrainer)).thenReturn(testTrainer);

        trainerService.changePassword(testTrainer.getUsername(), "MikeTyson", "newPass");
        verify(trainerDao).update(testTrainer);
    }

    @Test
    @DisplayName("Should activate Trainer")
    public void activate_ShouldActivateTrainer() {
        testTrainer = trainerService.add(testTrainer);
        when(trainerDao.findById(testTrainer.getId())).thenReturn(Optional.of(testTrainer));
        when(trainerDao.update(testTrainer)).thenReturn(testTrainer);
        trainerService.activate(testTrainer);

        verify(trainerDao).update(testTrainer);

    }

    @Test
    @DisplayName("Should deactivate Trainer")
    public void activate_ShouldDeactivateTrainer() {
        testTrainer = trainerService.add(testTrainer);
        when(trainerDao.findById(testTrainer.getId())).thenReturn(Optional.of(testTrainer));
        when(trainerDao.update(testTrainer)).thenReturn(testTrainer);
        trainerService.deactivate(testTrainer);

        verify(trainerDao).update(testTrainer);

    }

}
