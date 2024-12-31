import org.example.configs.ConfigTest;
import org.example.loaders.TrainingLoader;
import org.example.models.Training;
import org.example.models.TrainingType;
import org.example.repositories.TrainingDAO;
import org.example.repositories.TrainingDAOImpl;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class TrainingDAOTest {
    @Test
    public void testAddingTrainee() {

        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(ConfigTest.class);
        try {
            TrainingLoader loader = (TrainingLoader) context.getBean("trainingLoader");
            loader.clear();
            TrainingDAO<Training> trainingDAO = (TrainingDAOImpl) context.getBean("trainingDAOImpl");
            assertNotNull(trainingDAO);
            Training training=Training.builder()
                    .traineeId(1)
                    .trainerId(1).
                    trainingName("Cardio").
                    trainingType(TrainingType.CARDIO).
                    trainingDate(LocalDateTime.of(2024,12,12,12,12))
                    .trainingDuration(Duration.ofMinutes(90))
                    .build();
            trainingDAO.add(training);
            Training checkTraining= trainingDAO.findByTrainer(1L, LocalDateTime.of(2024,12,12,12,12));
            assertNotNull(checkTraining);
            assertEquals(training.getTraineeId(),checkTraining.getTraineeId());
            assertEquals(training.getTrainerId(),checkTraining.getTrainerId());
            assertEquals(training.getTrainingName(),checkTraining.getTrainingName());
            assertEquals(training.getTrainingType(),checkTraining.getTrainingType());
            assertEquals(training.getTrainingDate(),checkTraining.getTrainingDate());
            assertEquals(training.getTrainingDuration(),checkTraining.getTrainingDuration());

        }
        catch (Exception e) {
            e.printStackTrace();
        }
        context.close();
    }
    @Test
    public void testUpdatingTrainee() {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(ConfigTest.class);
        try {
            TrainingLoader loader = (TrainingLoader) context.getBean("trainingLoader");
            loader.clear();
            TrainingDAO<Training> trainingDAO = (TrainingDAOImpl) context.getBean("trainingDAOImpl");
            assertNotNull(trainingDAO);
            Training training=Training.builder()
                    .traineeId(1)
                    .trainerId(1).
                    trainingName("Cardio").
                    trainingType(TrainingType.CARDIO).
                    trainingDate(LocalDateTime.of(2024,12,12,12,12))
                    .trainingDuration(Duration.ofMinutes(90))
                    .build();
            trainingDAO.add(training);
            Training secondTraining=Training.builder()
                    .traineeId(1)
                    .trainerId(1).
                    trainingName("Strength").
                    trainingType(TrainingType.STRENGTH).
                    trainingDate(LocalDateTime.of(2024,12,12,16,0))
                    .trainingDuration(Duration.ofMinutes(90))
                    .build();
            trainingDAO.update(1L,secondTraining);
            Training checkTraining= trainingDAO.findByTrainer(1L, LocalDateTime.of(2024,12,12,16,0));

            assertNotNull(checkTraining);
            assertEquals(secondTraining.getTraineeId(),checkTraining.getTraineeId());
            assertEquals(secondTraining.getTrainerId(),checkTraining.getTrainerId());
            assertEquals(secondTraining.getTrainingName(),checkTraining.getTrainingName());
            assertEquals(secondTraining.getTrainingType(),checkTraining.getTrainingType());
            assertEquals(secondTraining.getTrainingDate(),checkTraining.getTrainingDate());
            assertEquals(secondTraining.getTrainingDuration(),checkTraining.getTrainingDuration());

        }
        catch (Exception e) {
            e.printStackTrace();
        }
        context.close();
    }
    @Test
    public void testDeletingTrainee() {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(ConfigTest.class);
        try {
            TrainingLoader loader = (TrainingLoader) context.getBean("trainingLoader");
            loader.clear();
            TrainingDAO<Training> trainingDAO = (TrainingDAOImpl) context.getBean("trainingDAOImpl");
            assertNotNull(trainingDAO);
            Training training=Training.builder()
                    .traineeId(1)
                    .trainerId(1).
                    trainingName("Cardio").
                    trainingType(TrainingType.CARDIO).
                    trainingDate(LocalDateTime.of(2024,12,12,12,12))
                    .trainingDuration(Duration.ofMinutes(90))
                    .build();
            trainingDAO.add(training);

            trainingDAO.delete(1L);
            Training checkTraining= trainingDAO.findByTrainer(1L, LocalDateTime.of(2024,12,12,12,12));
            assertNull(checkTraining);

        }
        catch (Exception e) {
            e.printStackTrace();
        }
        context.close();
    }
}
