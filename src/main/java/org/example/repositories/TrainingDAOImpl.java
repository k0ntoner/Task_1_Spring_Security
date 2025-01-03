package org.example.repositories;

import lombok.extern.slf4j.Slf4j;
import org.example.models.Trainee;
import org.example.models.Trainer;
import org.example.models.Training;
import org.example.utils.LoggerManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.util.Map;
@Repository
public class TrainingDAOImpl implements TrainingDAO<Training> {
    private LoggerManager log;
    private Map<Long, Training> trainings;
    private long head;
    @Autowired
    public TrainingDAOImpl(@Qualifier("trainingStorage") Map<Long, Training> trainings, @Qualifier("loggerManager") LoggerManager loggerManager) {
        this.trainings = trainings;
        this.log = loggerManager;
    }
    @PostConstruct
    public void init() {
        head=trainings.entrySet().stream().mapToLong(entry->entry.getKey()).max().orElse(0);
        log.info("TrainingDAOImpl initialized");
    }

    @Override
    public Training add(Training entity) {
        trainings.put(++head, entity);
        log.info("Added new training: {}", entity);
        return entity;
    }

    @Override
    public Training update(long id, Training entity) {
        trainings.put(id, entity);
        log.info("Updated training with id {} to: {}", id, entity);
        return entity;
    }

    @Override
    public Training findByTrainer(long trainerId, LocalDateTime dateTime) {
        Training training=trainings.values().stream()
                .filter(t -> t.getTrainerId() == trainerId)
                .filter(t -> t.getTrainingDate()
                .equals(dateTime)).findFirst()
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
        Training training=trainings.values().stream().filter(t -> t.getTrainerId() == traineeId).filter(t -> t.getTrainingDate().equals(dateTime)).findFirst().orElse(null);
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
    public Map<Long, Training> findAll() {
        log.info("Retrieving all trainings");
        return trainings;
    }

    @Override
    public boolean delete(long id) {
        try{
            trainings.remove(id);
            log.info("Deleted training with id {}", id);
            return true;
        }
        catch(Exception e){
            log.error("Failed to delete training with id {}", id, e);
        }
        return false;
    }
}
