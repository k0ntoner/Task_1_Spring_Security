package services;

import org.example.models.Trainer;
import org.example.repositories.TrainerDaoImpl;
import org.example.repositories.UserDao;
import org.example.services.TrainerServiceImpl;
import org.example.models.TrainingType;
import org.example.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
public class TrainerServiceTest {
    private UserDao<Trainer> trainerMockDAO;
    private UserService<Trainer> trainerService;

    @BeforeEach
    public void setUp() {
        trainerMockDAO = Mockito.mock(TrainerDaoImpl.class);
        trainerService=new TrainerServiceImpl(trainerMockDAO);
    }
    public Trainer buildTrainerForAdding(long id) {
        return Trainer.builder()
                .firstName("firstName"+id)
                .lastName("lastName"+id)
                .isActive(true)
                .specialization("Fitness Coach")
                .trainingType(TrainingType.STRENGTH)
                .build();
    }
    public Trainer buildFullTrainer(long id) {
        return Trainer.builder()
                .userId(id)
                .firstName("fullFirstName"+id)
                .lastName("fullLastName"+id)
                .username("fullUsername"+id)
                .password("fullPassword"+id)
                .isActive(true)
                .specialization("Fitness Coach")
                .trainingType(TrainingType.STRENGTH)
                .build();
    }
    @Test
    public void testAddingTrainer() {
        Trainer trainer = buildFullTrainer(1L);

        when(trainerMockDAO.add(trainer)).thenReturn(trainer);

        Trainer checkTrainer = trainerService.add(trainer);
        assertEquals(trainer.getUserId(), checkTrainer.getUserId());
        assertEquals(trainer.getFirstName(), checkTrainer.getFirstName());
        assertEquals(trainer.getLastName(), checkTrainer.getLastName());
        assertEquals(trainer.getUsername(), checkTrainer.getUsername());
        assertEquals(trainer.getPassword(), checkTrainer.getPassword());
        assertEquals(trainer.getSpecialization(), checkTrainer.getSpecialization());
        assertEquals(trainer.getTrainingType(), checkTrainer.getTrainingType());
        assertEquals(trainer.isActive(), checkTrainer.isActive());
    }

    @Test
    public void testUpdatingTrainer() {
        Trainer trainer = buildFullTrainer(1L);
        trainerService.add(trainer);

        Trainer updatedTrainer = buildFullTrainer(2L);
        updatedTrainer.setUserId(trainer.getUserId());

        when(trainerMockDAO.update(updatedTrainer)).thenReturn(updatedTrainer);
        when(trainerMockDAO.findById(1L)).thenReturn(trainer);
        Trainer checkTrainer = trainerService.update(updatedTrainer);;

        assertEquals(updatedTrainer.getUserId(), checkTrainer.getUserId());
        assertEquals(updatedTrainer.getFirstName(), checkTrainer.getFirstName());
        assertEquals(updatedTrainer.getLastName(), checkTrainer.getLastName());
        assertEquals(updatedTrainer.getUsername(), checkTrainer.getUsername());
        assertEquals(updatedTrainer.getPassword(), checkTrainer.getPassword());
        assertEquals(updatedTrainer.getSpecialization(), checkTrainer.getSpecialization());
        assertEquals(updatedTrainer.getTrainingType(), checkTrainer.getTrainingType());
        assertEquals(updatedTrainer.isActive(), checkTrainer.isActive());
    }

    @Test
    public void testDeletingTrainer() {
        Trainer trainer = buildFullTrainer(1L);

        trainerService.add(trainer);
        when(trainerMockDAO.delete(trainer)).thenReturn(true);
        when(trainerMockDAO.findById(1L)).thenReturn(trainer);
        assertTrue(trainerService.delete(trainer));
    }
    @Test void testFindAllTrainee() {

        Trainer trainer=buildFullTrainer(1L);

        Trainer secondTrainer=buildFullTrainer(2L);
        when(trainerMockDAO.findAll()).thenReturn(List.of(trainer,secondTrainer));
        Collection<Trainer> trainerList = trainerService.findAll();
        assertEquals(2, trainerList.size());
        List<Trainer> trainers = trainerList.stream().toList();

        assertAll(
                () -> assertEquals("fullFirstName1", trainers.get(0).getFirstName()),
                () -> assertEquals("fullLastName1", trainers.get(0).getLastName()),
                () -> assertEquals("fullFirstName2", trainers.get(1).getFirstName()),
                () -> assertEquals("fullLastName2", trainers.get(1).getLastName())
        );
        trainerList.forEach(t -> {
            assertNotEquals(0,t.getUserId());
            assertNotNull(t.getFirstName());
            assertNotNull(t.getLastName());
            assertNotNull(t.getUsername());
            assertNotNull(t.getPassword());
            assertNotNull(t.getSpecialization());
            assertNotNull(t.getTrainingType());
            assertTrue(t.isActive());
        });
    }
    @Test void testUpdateNotExistingTrainee() {
        Trainer trainer = buildFullTrainer(1L);
        when(trainerMockDAO.findById(1L)).thenReturn(null);
        assertThrows(IllegalArgumentException.class, () -> {trainerService.update(trainer);});


    }
    @Test void testDeleteNotExistingTrainee() {
        Trainer trainer = buildFullTrainer(1L);
        when(trainerMockDAO.findById(1L)).thenReturn(null);
        assertThrows(IllegalArgumentException.class, () -> {trainerService.delete(trainer);});
    }
}
