package services;

import org.example.models.Training;
import org.example.models.TrainingType;
import org.example.repositories.TrainingDao;
import org.example.repositories.TrainingDaoImpl;
import org.example.services.TrainingService;
import org.example.services.TrainingServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

public class TrainingServiceImplTest {

    private TrainingDao trainingMockDao;
    private TrainingService trainingService;

    @BeforeEach
    public void setUp() {
        trainingMockDao = Mockito.mock(TrainingDaoImpl.class);
        trainingService = new TrainingServiceImpl(trainingMockDao);
    }

    public Training buildTrainingForAdding(long id) {
        return Training.builder()
                .traineeId(id)
                .trainerId(id)
                .trainingName("Cardio")
                .trainingType(TrainingType.CARDIO)
                .trainingDate(LocalDateTime.of(2024, 12, 12, 12, 12))
                .trainingDuration(Duration.ofMinutes(90))
                .build();
    }

    public Training buildFullTraining(long id) {
        return Training.builder()
                .id(id)
                .traineeId(id)
                .trainerId(id)
                .trainingName("Strength")
                .trainingType(TrainingType.STRENGTH)
                .trainingDate(LocalDateTime.of(2024, 12, 12, 16, 0))
                .trainingDuration(Duration.ofMinutes(90))
                .build();
    }

    @Test
    public void testAddingTraining() {
        Training training = buildFullTraining(1L);

        when(trainingMockDao.add(training)).thenReturn(training);

        Training checkTraining = trainingService.add(training);
        assertNotNull(checkTraining);
        assertNotEquals(0, checkTraining.getId());
        assertEquals(training.getTraineeId(), checkTraining.getTraineeId());
        assertEquals(training.getTrainerId(), checkTraining.getTrainerId());
        assertEquals(training.getTrainingName(), checkTraining.getTrainingName());
        assertEquals(training.getTrainingType(), checkTraining.getTrainingType());
        assertEquals(training.getTrainingDate(), checkTraining.getTrainingDate());
        assertEquals(training.getTrainingDuration(), checkTraining.getTrainingDuration());
    }

    @Test
    void testFindAllTrainee() {
        Training training = buildFullTraining(1L);

        Training secondTraining = buildFullTraining(2L);
        when(trainingMockDao.findAll()).thenReturn(List.of(training, secondTraining));
        Collection<Training> trainingList = trainingService.findAll();
        assertEquals(2, trainingList.size());
        List<Training> trainings = trainingList.stream().toList();

        trainings.forEach(t -> {
            assertNotEquals(0, t.getId());
            assertNotEquals(0, t.getTraineeId());
            assertNotEquals(0, t.getTrainerId());
            assertNotNull(t.getTrainingName());
            assertNotNull(t.getTrainingType());
            assertNotNull(t.getTrainingDate());
            assertNotNull(t.getTrainingDuration());
        });
    }

}
