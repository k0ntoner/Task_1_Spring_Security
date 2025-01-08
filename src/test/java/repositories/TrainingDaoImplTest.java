package repositories;

import org.example.models.Training;
import org.example.models.TrainingType;
import org.example.repositories.TrainingDao;
import org.example.repositories.TrainingDaoImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TrainingDaoImplTest {
    TrainingDao trainingDao;

    @BeforeEach
    public void setUp() {
        trainingDao = new TrainingDaoImpl(new HashMap<>());
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
    public void testAddingTrainee() {
        Training training = buildTrainingForAdding(1L);

        trainingDao.add(training);

        Training checkTraining = trainingDao.findByTrainer(1L, LocalDateTime.of(2024, 12, 12, 12, 12));

        assertNotNull(checkTraining);
        assertNotEquals(0, checkTraining.getId());
        assertEquals(training.getId(), checkTraining.getId());
        assertEquals(training.getTraineeId(), checkTraining.getTraineeId());
        assertEquals(training.getTrainerId(), checkTraining.getTrainerId());
        assertEquals(training.getTrainingName(), checkTraining.getTrainingName());
        assertEquals(training.getTrainingType(), checkTraining.getTrainingType());
        assertEquals(training.getTrainingDate(), checkTraining.getTrainingDate());
        assertEquals(training.getTrainingDuration(), checkTraining.getTrainingDuration());
    }

    @Test
    void testFindAllTrainee() {
        Training training = trainingDao.add(buildTrainingForAdding(1L));

        Training secondTraining = trainingDao.add(buildTrainingForAdding(2L));
        Collection<Training> trainingList = trainingDao.findAll();
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
