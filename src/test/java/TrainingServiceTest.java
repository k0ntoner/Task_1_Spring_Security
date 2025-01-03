import org.example.configs.ConfigTest;
import org.example.loaders.Loader;
import org.example.loaders.TraineeLoader;
import org.example.loaders.TrainingLoader;
import org.example.models.Trainee;
import org.example.models.Training;
import org.example.models.TrainingType;
import org.example.repositories.TrainingDAO;
import org.example.repositories.TrainingDAOImpl;
import org.example.services.TrainingService;
import org.example.services.TrainingServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

public class TrainingServiceTest {

    private TrainingDAO<Training> trainingMockDAO;
    private TrainingService<Training> trainingService;

    @BeforeEach
    public void setUp() {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(ConfigTest.class);
        Loader<Training> traineeLoader=context.getBean(TrainingLoader.class);
        traineeLoader.clear();
        context.close();
        trainingMockDAO = Mockito.mock(TrainingDAOImpl.class);
        trainingService = new TrainingServiceImpl(trainingMockDAO);
    }

    @Test
    public void testAddingTraining() {
        Training training = Training.builder()
                .traineeId(1)
                .trainerId(1)
                .trainingName("Cardio")
                .trainingType(TrainingType.CARDIO)
                .trainingDate(LocalDateTime.of(2024, 12, 12, 12, 12))
                .trainingDuration(Duration.ofMinutes(90))
                .build();

        when(trainingMockDAO.findByTrainer(1L, LocalDateTime.of(2024, 12, 12, 12, 12))).thenReturn(training);
        trainingService.add(training);

        Training checkTraining = trainingService.findByTrainer(1L, LocalDateTime.of(2024, 12, 12, 12, 12));
        assertNotNull(checkTraining);
        assertEquals(training.getTraineeId(), checkTraining.getTraineeId());
        assertEquals(training.getTrainerId(), checkTraining.getTrainerId());
        assertEquals(training.getTrainingName(), checkTraining.getTrainingName());
        assertEquals(training.getTrainingType(), checkTraining.getTrainingType());
        assertEquals(training.getTrainingDate(), checkTraining.getTrainingDate());
        assertEquals(training.getTrainingDuration(), checkTraining.getTrainingDuration());
    }

//    @Test
//    public void testUpdatingTraining() {
//        Training training = Training.builder()
//                .traineeId(1)
//                .trainerId(1)
//                .trainingName("Cardio")
//                .trainingType(TrainingType.CARDIO)
//                .trainingDate(LocalDateTime.of(2024, 12, 12, 12, 12))
//                .trainingDuration(Duration.ofMinutes(90))
//                .build();
//
//        trainingService.add(training);
//
//        Training updatedTraining = Training.builder()
//                .traineeId(1)
//                .trainerId(1)
//                .trainingName("Strength")
//                .trainingType(TrainingType.STRENGTH)
//                .trainingDate(LocalDateTime.of(2024, 12, 12, 16, 0))
//                .trainingDuration(Duration.ofMinutes(90))
//                .build();
//
//        when(trainingMockDAO.findByTrainer(1L, LocalDateTime.of(2024, 12, 12, 16, 0))).thenReturn(updatedTraining);
//        trainingService.update(1L, updatedTraining);
//
//        Training checkTraining = trainingService.findByTrainer(1L, LocalDateTime.of(2024, 12, 12, 16, 0));
//        assertNotNull(checkTraining);
//        assertEquals(updatedTraining.getTraineeId(), checkTraining.getTraineeId());
//        assertEquals(updatedTraining.getTrainerId(), checkTraining.getTrainerId());
//        assertEquals(updatedTraining.getTrainingName(), checkTraining.getTrainingName());
//        assertEquals(updatedTraining.getTrainingType(), checkTraining.getTrainingType());
//        assertEquals(updatedTraining.getTrainingDate(), checkTraining.getTrainingDate());
//        assertEquals(updatedTraining.getTrainingDuration(), checkTraining.getTrainingDuration());
//    }
//
//    @Test
//    public void testDeletingTraining() {
//        Training training = Training.builder()
//                .traineeId(1)
//                .trainerId(1)
//                .trainingName("Cardio")
//                .trainingType(TrainingType.CARDIO)
//                .trainingDate(LocalDateTime.of(2024, 12, 12, 12, 12))
//                .trainingDuration(Duration.ofMinutes(90))
//                .build();
//
//        trainingService.add(training);
//        when(trainingMockDAO.findByTrainer(1L, LocalDateTime.of(2024, 12, 12, 12, 12))).thenReturn(null);
//
//        trainingService.delete(1L);
//        Training checkTraining = trainingService.findByTrainer(1L, LocalDateTime.of(2024, 12, 12, 12, 12));
//        assertNull(checkTraining);
//    }
}
