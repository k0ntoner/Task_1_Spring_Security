import org.example.configs.ConfigTest;
import org.example.loaders.TrainerLoader;
import org.example.models.Trainer;
import org.example.models.TrainingType;
import org.example.repositories.UserDAO;
import org.example.repositories.TrainerDAOImpl;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import static org.junit.jupiter.api.Assertions.*;

public class TrainerDAOTest {
    @Test
    public void testAddingTrainer() {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(ConfigTest.class);
        try {
            TrainerLoader loader = (TrainerLoader) context.getBean("trainerLoader");
            loader.clear();
            UserDAO<Trainer> trainerDAO = (TrainerDAOImpl) context.getBean("trainerDAOImpl");
            assertNotNull(trainerDAO);
            Trainer trainer = Trainer.builder()
                    .firstName("firstName")
                    .lastName("lastName")
                    .username("username")
                    .password("password")
                    .isActive(true)
                    .specialization("Fitness Coach")
                    .userId(1L)
                    .trainingType(TrainingType.STRENGTH)
                    .build();
            trainerDAO.add(trainer);
            Trainer checkTrainer = trainerDAO.findById(trainer.getUserId());
            assertEquals(trainer.getUserId(), checkTrainer.getUserId());
            assertEquals(trainer.getFirstName(), checkTrainer.getFirstName());
            assertEquals(trainer.getLastName(), checkTrainer.getLastName());
            assertEquals(trainer.getUsername(), checkTrainer.getUsername());
            assertEquals(trainer.getPassword(), checkTrainer.getPassword());
            assertEquals(trainer.getSpecialization(), checkTrainer.getSpecialization());
            assertEquals(trainer.isActive(), checkTrainer.isActive());
            assertEquals(trainer.getTrainingType(), checkTrainer.getTrainingType());
        } catch (Exception e) {
            e.printStackTrace();
        }
        context.close();
    }

    @Test
    public void testUpdatingTrainer() {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(ConfigTest.class);
        try {
            TrainerLoader loader = (TrainerLoader) context.getBean("trainerLoader");
            loader.clear();
            UserDAO<Trainer> trainerDAO = (TrainerDAOImpl) context.getBean("trainerDAOImpl");
            assertNotNull(trainerDAO);
            Trainer trainer = Trainer.builder()
                    .firstName("firstName1")
                    .lastName("lastName1")
                    .username("username1")
                    .password("password1")
                    .isActive(true)
                    .specialization("Fitness Coach")
                    .userId(1L)
                    .trainingType(TrainingType.CARDIO)
                    .build();
            trainerDAO.add(trainer);
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
            trainerDAO.update(updatedTrainer.getUserId(), updatedTrainer);
            Trainer checkTrainer = trainerDAO.findById(updatedTrainer.getUserId());
            assertEquals(updatedTrainer.getUserId(), checkTrainer.getUserId());
            assertEquals(updatedTrainer.getFirstName(), checkTrainer.getFirstName());
            assertEquals(updatedTrainer.getLastName(), checkTrainer.getLastName());
            assertEquals(updatedTrainer.getUsername(), checkTrainer.getUsername());
            assertEquals(updatedTrainer.getPassword(), checkTrainer.getPassword());
            assertEquals(updatedTrainer.getSpecialization(), checkTrainer.getSpecialization());
            assertEquals(updatedTrainer.isActive(), checkTrainer.isActive());
            assertEquals(updatedTrainer.getTrainingType(), checkTrainer.getTrainingType());
        } catch (Exception e) {
            e.printStackTrace();
        }
        context.close();
    }

    @Test
    public void testDeletingTrainer() {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(ConfigTest.class);
        try {
            TrainerLoader loader = (TrainerLoader) context.getBean("trainerLoader");
            loader.clear();
            UserDAO<Trainer> trainerDAO = (TrainerDAOImpl) context.getBean("trainerDAOImpl");
            assertNotNull(trainerDAO);
            Trainer trainer = Trainer.builder()
                    .firstName("firstName")
                    .lastName("lastName")
                    .username("username")
                    .password("password")
                    .isActive(true)
                    .specialization("Fitness Coach")
                    .userId(1L)
                    .trainingType(TrainingType.ENDURANCE)
                    .build();
            trainerDAO.add(trainer);
            trainerDAO.delete(trainer.getUserId());
            assertNull(trainerDAO.findById(trainer.getUserId()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        context.close();
    }
}
