package repositories;

import configs.ConfigTest;
import org.example.models.Trainer;
import org.example.models.TrainingType;
import org.example.repositories.UserDao;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TrainerDaoTest {
    AnnotationConfigApplicationContext context;
    UserDao<Trainer> trainerDAO;
    @BeforeEach
    public void setUp() {
        context = new AnnotationConfigApplicationContext(ConfigTest.class);
        trainerDAO = (UserDao<Trainer>) context.getBean("trainerDaoImpl");
    }
    @AfterEach
    public void tearDown() {
        context.close();
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
        Trainer checkTrainer = trainerDAO.add(trainer);

        assertEquals(trainer.getUserId(), checkTrainer.getUserId());
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
        trainer=trainerDAO.add(trainer);
        Trainer updatedTrainer = buildFullTrainer(1L);
        Trainer checkTrainer=trainerDAO.update(updatedTrainer);
        assertEquals(updatedTrainer.getUserId(), checkTrainer.getUserId());
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
        trainer=trainerDAO.add(trainer);
        trainerDAO.delete(trainer);
        assertNull(trainerDAO.findById(trainer.getUserId()));

    }
    @Test void testFindAllTrainee() {

        Trainer trainer=trainerDAO.add(buildTrainerForAdding(1L));

        Trainer secondTrainer=trainerDAO.add(buildTrainerForAdding(2L));
        Collection<Trainer> trainerList = trainerDAO.findAll();
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
        assertDoesNotThrow(() -> {trainerDAO.update(trainer);});


    }
    @Test void testDeleteNotExistingTrainee() {
        //Will not throw an Exception because this logic located in Service class.
        Trainer trainer = buildFullTrainer(1L);
        assertDoesNotThrow(() -> {trainerDAO.delete(trainer);});
    }
}
