package repositories;

import configs.ConfigTest;
import org.apache.commons.lang3.NotImplementedException;
import org.example.models.Trainer;
import org.example.models.TrainingType;
import org.example.repositories.TrainerDaoImpl;
import org.example.repositories.UserDao;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TrainerDaoTest {
    UserDao<Trainer> trainerDao;
    @BeforeEach
    public void setUp() {
        trainerDao = new TrainerDaoImpl(new HashMap<>());
    }
    public Trainer buildTrainerForAdding(long id) {
        return Trainer.builder()
                .firstName("firstName"+id)
                .lastName("lastName"+id)
                .isActive(false)
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
                .isActive(false)
                .specialization("Fitness Coach")
                .trainingType(TrainingType.STRENGTH)
                .build();
    }
    @Test
    public void testAddingTrainer() {
        Trainer trainer = buildTrainerForAdding(1L);
        Trainer checkTrainer = trainerDao.add(trainer);

        assertNotEquals(0,checkTrainer.getUserId());
        assertEquals(trainer.getUserId(),checkTrainer.getUserId());
        assertEquals(trainer.getFirstName(), checkTrainer.getFirstName());
        assertEquals(trainer.getLastName(), checkTrainer.getLastName());
        assertEquals(trainer.getUsername(), checkTrainer.getUsername());
        assertEquals(trainer.getPassword(), checkTrainer.getPassword());
        assertEquals(trainer.getSpecialization(), checkTrainer.getSpecialization());
        assertEquals(trainer.isActive(), checkTrainer.isActive());
        assertEquals(trainer.getTrainingType(), checkTrainer.getTrainingType());

    }

    @Test
    public void testUpdatingTrainer() {
        Trainer trainer = buildTrainerForAdding(1L);
        trainer= trainerDao.add(trainer);
        Trainer updatedTrainer = buildFullTrainer(1L);
        Trainer checkTrainer= trainerDao.update(updatedTrainer);

        assertNotEquals(0,checkTrainer.getUserId());
        assertNotEquals(trainer.getFirstName(), checkTrainer.getFirstName());
        assertNotEquals(trainer.getLastName(), checkTrainer.getLastName());
        assertNotEquals(trainer.getUsername(), checkTrainer.getUsername());
        assertNotEquals(trainer.getPassword(), checkTrainer.getPassword());

        assertEquals(trainer.getUserId(),checkTrainer.getUserId());
        assertEquals(updatedTrainer.getFirstName(), checkTrainer.getFirstName());
        assertEquals(updatedTrainer.getLastName(), checkTrainer.getLastName());
        assertEquals(updatedTrainer.getUsername(), checkTrainer.getUsername());
        assertEquals(updatedTrainer.getPassword(), checkTrainer.getPassword());
        assertEquals(updatedTrainer.getSpecialization(), checkTrainer.getSpecialization());
        assertEquals(updatedTrainer.isActive(), checkTrainer.isActive());
        assertEquals(updatedTrainer.getTrainingType(), checkTrainer.getTrainingType());
    }

    @Test
    public void testDeletingTrainer() {
        Trainer trainer = buildTrainerForAdding(1L);
        assertThrows(NotImplementedException.class, () -> {
            trainerDao.delete(trainer);});

    }
    @Test void testFindAllTrainee() {

        Trainer trainer= trainerDao.add(buildTrainerForAdding(1L));

        Trainer secondTrainer= trainerDao.add(buildTrainerForAdding(2L));
        Collection<Trainer> trainerList = trainerDao.findAll();
        assertEquals(2, trainerList.size());
        List<Trainer> trainers = trainerList.stream().toList();

        assertAll(
                () -> assertEquals("firstName1", trainers.get(0).getFirstName()),
                () -> assertEquals("lastName1", trainers.get(0).getLastName()),
                () -> assertEquals("firstName2", trainers.get(1).getFirstName()),
                () -> assertEquals("lastName2", trainers.get(1).getLastName())
        );
        trainerList.forEach(t -> {
            assertNotEquals(0,t.getUserId());
            assertNotNull(t.getFirstName());
            assertNotNull(t.getLastName());
            assertNotNull(t.getSpecialization());
            assertNotNull(t.getTrainingType());
            assertFalse(t.isActive());
        });
    }
    @Test void testUpdateNotExistingTrainee() {
        //Will not throw an Exception because this logic located in Service class.
        Trainer trainer = buildFullTrainer(1L);
        assertDoesNotThrow(() -> {
            trainerDao.update(trainer);});


    }
    @Test void testDeleteNotExistingTrainee() {
        Trainer trainer = buildFullTrainer(1L);
        assertThrows(NotImplementedException.class, () -> {
            trainerDao.delete(trainer);});
    }
}
