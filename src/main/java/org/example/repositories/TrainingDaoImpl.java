package org.example.repositories;

import lombok.extern.slf4j.Slf4j;
import org.example.models.Training;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Map;
@Repository
@Slf4j
public class TrainingDaoImpl implements TrainingDao {
    private Map<Long, Training> trainings;
    private long head;
    @Autowired
    public TrainingDaoImpl(@Qualifier("trainingStorage") Map<Long, Training> trainings) {
        this.trainings = trainings;
    }
    @Override
    public Training add(Training entity) {
        entity.setId(++head);
        trainings.put(entity.getId(), entity);
        log.info("Added new training: {}", entity);
        return entity;
    }

    @Override
    public Training findByTrainer(long trainerId, LocalDateTime dateTime) {
        Training training=trainings.values().stream()
                .filter(t -> t.getTrainerId() == trainerId)
                .filter(t -> t.getTrainingDate().equals(dateTime))
                .findFirst()
                .orElse(null);

        if (training != null) {
            log.info("Found training with trainerId {} and localDateTime {}: {}", trainerId, dateTime,training);
        } else {
            log.warn("No training found with trainerId {} and localDateTime {}", trainerId, dateTime);
        }
        return training;
    }

    @Override
    public Training findByTrainee(long traineeId, LocalDateTime dateTime) {
        Training training=trainings.values().stream()
                .filter(t -> t.getTrainerId() == traineeId)
                .filter(t -> t.getTrainingDate().equals(dateTime))
                .findFirst()
                .orElse(null);
        if (training != null) {
            log.info("Found training with traineeId {} and localDateTime {}: {}", traineeId, dateTime,training);
        } else {
            log.warn("No training found with traineeId {} and localDateTime {}", traineeId, dateTime);
        }
        return training;
    }

    @Override
    public Training findById(long id) {
        Training training = trainings.get(id);
        if (training != null) {
            log.info("Found training with id {}: {}", id, training);
        } else {
            log.warn("No training found with id {}", id);
        }
        return training;
    }

    @Override
    public Collection<Training> findAll() {
        log.info("Retrieving all trainings");
        return trainings.values().stream().toList();
    }

}
