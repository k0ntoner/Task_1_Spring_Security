package org.example.repositories;

import lombok.extern.slf4j.Slf4j;
import org.example.models.Training;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.util.Map;
@Repository
public class TrainingDAOImpl implements TrainingDAO<Training> {
    @Autowired
    @Qualifier("trainingStorage")
    private Map<Long, Training> trainings;
    private long head;
    @PostConstruct
    public void init() {
        head=trainings.entrySet().stream().mapToLong(entry->entry.getKey()).max().orElse(0);
    }

    @Override
    public Training add(Training entity) {
        trainings.put(++head, entity);
        return entity;
    }

    @Override
    public Training update(long id, Training entity) {
        trainings.put(id, entity);
        return entity;
    }

    @Override
    public Training findByTrainer(long trainerId, LocalDateTime dateTime) {
        Training training=trainings.values().stream().filter(t -> t.getTrainerId() == trainerId).filter(t -> t.getTrainingDate().equals(dateTime)).findFirst().orElse(null);
        return training;
    }

    @Override
    public Training findByTrainee(long traineeId, LocalDateTime dateTime) {
        Training training=trainings.values().stream().filter(t -> t.getTrainerId() == traineeId).filter(t -> t.getTrainingDate().equals(dateTime)).findFirst().orElse(null);
        return training;
    }

    @Override
    public Training findById(long id) {
        return trainings.get(id);
    }

    @Override
    public Map<Long, Training> findAll() {
        return trainings;
    }

    @Override
    public boolean delete(long id) {
        try{
            trainings.remove(id);
            return true;
        }
        catch(Exception e){
        }
        return false;
    }
}
