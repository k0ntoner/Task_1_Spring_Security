package org.example.repositories;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.NotImplementedException;
import org.example.models.Trainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Map;
@Repository
@Slf4j
public class TrainerDaoImpl implements UserDao<Trainer> {
    private Map<Long, Trainer> trainers;
    private long head;
    @Autowired
    public TrainerDaoImpl(@Qualifier("trainerStorage") Map<Long, Trainer> trainers) {
        this.trainers = trainers;
    }


    @Override
    public Trainer add(Trainer entity) {
        entity.setUserId(++head);
        trainers.put(entity.getUserId(), entity);
        log.info("Added new trainer: {}", entity);
        return entity;
    }

    @Override
    public Trainer update(Trainer entity) {
        trainers.put(entity.getUserId(), entity);
        log.info("Updated trainer with id {} to: {}", entity.getUserId(), entity);
        return entity;
    }

    @Override
    public Trainer findById(long id) {
        Trainer trainer = trainers.get(id);
        if (trainer != null) {
            log.info("Found trainer with id {}: {}", id, trainer);
        } else {
            log.warn("No trainer found with id {}", id);
        }
        return trainer;

    }

    @Override
    public Collection<Trainer> findAll() {
        log.info("Retrieving all trainers");
        return trainers.values().stream().toList();
    }

    @Override
    public boolean delete(Trainer entity) {
        throw new NotImplementedException();

    }
    @Override
    public boolean isUsernameExist(String username) {
        boolean exists = trainers.values().stream().anyMatch(trainer -> trainer.getUsername().equals(username));
        log.info("Checking if username {} already exists: {}",username, exists);
        return exists;
    }

}
