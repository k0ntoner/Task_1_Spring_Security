import org.example.configs.ConfigTest;
import org.example.loaders.Loader;
import org.example.loaders.TrainerLoader;
import org.example.models.Trainer;
import org.example.repositories.TrainerDAOImpl;
import org.example.repositories.UserDAO;
import org.example.services.TrainerServiceImpl;
import org.example.models.TrainingType;
import org.example.services.TrainingService;
import org.example.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
public class TrainerServiceTest {
    private UserDAO<Trainer> trainerMockDAO;
    private UserService<Trainer> trainerService;

    @BeforeEach
    public void setUp() {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(ConfigTest.class);
        Loader<Trainer> trainerLoader = context.getBean(TrainerLoader.class);
        trainerLoader.clear();
        context.close();
        trainerMockDAO = Mockito.mock(TrainerDAOImpl.class);
        trainerService=new TrainerServiceImpl(trainerMockDAO);
    }

    @Test
    public void testAddingTrainer() {
        Trainer trainer = Trainer.builder()
                .userId(1L)
                .firstName("firstName")
                .lastName("lastName")
                .username("username")
                .password("password")
                .isActive(true)
                .specialization("Fitness Coach")
                .trainingType(TrainingType.STRENGTH)
                .build();

        when(trainerMockDAO.findById(1L)).thenReturn(trainer);
        trainerService.add(trainer);

        Trainer checkTrainer = trainerService.findById(trainer.getUserId());
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
        Trainer trainer = Trainer.builder()
                .userId(1L)
                .firstName("firstName1")
                .lastName("lastName1")
                .username("username1")
                .password("password1")
                .isActive(true)
                .specialization("Cardio Coach")
                .trainingType(TrainingType.CARDIO)
                .build();
        trainerService.add(trainer);

        Trainer updatedTrainer = Trainer.builder()
                .userId(1L)
                .firstName("updatedFirstName")
                .lastName("updatedLastName")
                .username("updatedUsername")
                .password("updatedPassword")
                .isActive(false)
                .specialization("Yoga Coach")
                .trainingType(TrainingType.FLEXIBILITY)
                .build();

        trainerService.update(1L, updatedTrainer);
        when(trainerMockDAO.findById(1L)).thenReturn(updatedTrainer);

        Trainer checkTrainer = trainerService.findById(updatedTrainer.getUserId());
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
        Trainer trainer = Trainer.builder()
                .userId(1L)
                .firstName("firstName")
                .lastName("lastName")
                .username("username")
                .password("password")
                .isActive(true)
                .specialization("Fitness Coach")
                .trainingType(TrainingType.ENDURANCE)
                .build();

        trainerService.add(trainer);
        when(trainerMockDAO.findById(1L)).thenReturn(null);
        trainerService.delete(1L);

        assertNull(trainerService.findById(1L));
    }
}
