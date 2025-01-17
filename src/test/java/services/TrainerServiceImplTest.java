package services;


import org.example.repositories.TrainerDao;
import org.example.repositories.entities.Trainee;
import org.example.repositories.entities.Trainer;
import org.example.repositories.entities.TrainingType;
import org.example.services.TrainerServiceImpl;
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
            return Optional.of(trainer);
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
    public Trainer buildTrainerForUpdating() {
        return Trainer.builder()
                .id(1L)
                .firstName("Mike")
                .lastName("Tyson")
                .username("Mike.Tyson")
                .password(UserUtils.hashPassword("MikeTyson"))
                .specialization("Boxing")
                .trainingType(TrainingType.STRENGTH)
                .isActive(true)
                .build();
    }

    @Test
    @DisplayName("Should return saved Trainer")
    public void save_ShouldReturnSavedTrainer() {

        Optional<Trainer> newTrainer= trainerService.add(testTrainer);
        assertTrue(newTrainer.isPresent());
        assertNotNull(newTrainer.get().getId());
        assertEquals(testTrainer.getUsername(), newTrainer.get().getUsername());
        assertEquals(testTrainer.getFirstName(), newTrainer.get().getFirstName());
        assertEquals(testTrainer.getLastName(), newTrainer.get().getLastName());
        assertEquals(testTrainer.getSpecialization(), newTrainer.get().getSpecialization());
        assertEquals(testTrainer.getTrainingType(), newTrainer.get().getTrainingType());
        assertEquals(testTrainer.getPassword(), newTrainer.get().getPassword());
        assertTrue(UserUtils.passwordMatch("MikeTyson", newTrainer.get().getPassword()));


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


        Trainer newTrainer = trainerDao.save(testTrainer).get();

        newTrainer.setFirstName(newFirstName);
        newTrainer.setLastName(newLastName);
        newTrainer.setUsername(newUsername);
        newTrainer.setPassword(newPassword);
        newTrainer.setTrainingType(newTrainingType);
        newTrainer.setSpecialization(newSpecialization);

        when(trainerDao.save(newTrainer)).thenReturn(Optional.of(newTrainer));
        Optional<Trainer> updatedTrainer = trainerDao.save(newTrainer);

        assertTrue(updatedTrainer.isPresent());
        assertNotNull(updatedTrainer.get().getId());
        assertEquals(newUsername, updatedTrainer.get().getUsername());
        assertEquals(newFirstName, updatedTrainer.get().getFirstName());
        assertEquals(newLastName, updatedTrainer.get().getLastName());
        assertEquals(newSpecialization, updatedTrainer.get().getSpecialization());
        assertEquals(newTrainingType, updatedTrainer.get().getTrainingType());
        assertTrue(UserUtils.passwordMatch("newPass", updatedTrainer.get().getPassword()));

    }

    @Test
    @DisplayName("Should update not existing trainer")
    public void update_ShouldUpdateNotExistingTrainer() {
        testTrainer=buildTrainerForUpdating();
        when(trainerDao.findById(1L)).thenReturn(Optional.empty());
        assertFalse(trainerService.update(testTrainer).isPresent());
    }

    @Test
    @DisplayName("Should find Trainer by id")
    public void findById_ShouldFindTrainerById() {
        testTrainer=buildTrainerForUpdating();
        when(trainerDao.findById(1L)).thenReturn(Optional.of(testTrainer));

        Trainer foundTrainer= trainerService.findById(1L).get();
        assertNotNull(foundTrainer);
        assertEquals(testTrainer.getId(), foundTrainer.getId());
        assertEquals(testTrainer.getUsername(), foundTrainer.getUsername());
        assertEquals(testTrainer.getFirstName(), foundTrainer.getFirstName());
        assertEquals(testTrainer.getLastName(), foundTrainer.getLastName());
        assertEquals(testTrainer.getSpecialization(), foundTrainer.getSpecialization());
        assertEquals(testTrainer.getTrainingType(), foundTrainer.getTrainingType());
        assertEquals(testTrainer.getPassword(), foundTrainer.getPassword());
        assertEquals(testTrainer.getIsActive(), foundTrainer.getIsActive());
    }

    @Test
    @DisplayName("Should find Collection of all Trainers")
    public void findAll_ShouldFindAllTrainers() {

        testTrainer=buildTrainerForUpdating();
        List<Trainer> trainers=new ArrayList<>();
        trainers.add(testTrainer);
        when(trainerDao.findAll()).thenReturn(Optional.of(trainers));
        Collection<Trainer> foundTrainers= trainerDao.findAll().get();

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
                    assertNotNull(trainer.getIsActive());
                }
        );
    }
    @Test
    @DisplayName("Should find Trainer by username")
    public void findByUsername_ShouldFindTrainerByUserName() {
        testTrainer=buildTrainerForUpdating();

        when(trainerDao.findByUsername(testTrainer.getUsername())).thenReturn(Optional.of(testTrainer));

        Trainer foundTrainer= trainerService.findByUsername(testTrainer.getUsername()).get();
        assertNotNull(foundTrainer);
        assertEquals(testTrainer.getId(), foundTrainer.getId());
        assertEquals(testTrainer.getUsername(), foundTrainer.getUsername());
        assertEquals(testTrainer.getFirstName(), foundTrainer.getFirstName());
        assertEquals(testTrainer.getLastName(), foundTrainer.getLastName());
        assertEquals(testTrainer.getSpecialization(), foundTrainer.getSpecialization());
        assertEquals(testTrainer.getTrainingType(), foundTrainer.getTrainingType());
        assertEquals(testTrainer.getPassword(), foundTrainer.getPassword());
        assertEquals(testTrainer.getIsActive(), foundTrainer.getIsActive());
    }
    @Test
    @DisplayName("Should change trainer password")
    public void changePassword_ShouldChangeTrainerPassword() {
        testTrainer=buildTrainerForUpdating();
        when(trainerDao.findByUsername(testTrainer.getUsername())).thenReturn(Optional.of(testTrainer));
        when(trainerDao.save(testTrainer)).thenReturn(Optional.of(testTrainer));

        Optional<Trainer> updatedTrainer= trainerService.changePassword(testTrainer.getUsername(),"MikeTyson","newPass");
        assertTrue(updatedTrainer.isPresent());
        assertTrue(UserUtils.passwordMatch("newPass", updatedTrainer.get().getPassword()));
    }
    @Test
    @DisplayName("Should activate Trainer")
    public void activate_ShouldActivateTrainer() {
        testTrainer=buildTrainerForUpdating();
        when(trainerDao.findById(testTrainer.getId())).thenReturn(Optional.of(testTrainer));
        when(trainerDao.save(testTrainer)).thenReturn(Optional.of(testTrainer));
        Optional<Trainer> activatedTrainer = trainerService.activate(testTrainer);
        assertTrue(activatedTrainer.isPresent());
        assertTrue(activatedTrainer.get().getIsActive());
        verify(trainerDao).save(testTrainer);

    }
    @Test
    @DisplayName("Should deactivate Trainer")
    public void activate_ShouldDeactivateTrainer() {
        testTrainer=buildTrainerForUpdating();
        when(trainerDao.findById(testTrainer.getId())).thenReturn(Optional.of(testTrainer));
        when(trainerDao.save(testTrainer)).thenReturn(Optional.of(testTrainer));
        Optional<Trainer> deactivatedTrainer= trainerService.deactivate(testTrainer);
        assertTrue(deactivatedTrainer.isPresent());
        assertFalse(deactivatedTrainer.get().getIsActive());
        verify(trainerDao).save(testTrainer);

    }

}
