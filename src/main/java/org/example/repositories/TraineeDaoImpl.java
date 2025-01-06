package org.example.repositories;

import lombok.extern.slf4j.Slf4j;
import org.example.models.Trainee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Map;

@Repository
@Slf4j
public class TraineeDaoImpl implements UserDao<Trainee> {
    private Map<Long, Trainee> trainees;
    private long head=0;

    @Autowired
    public TraineeDaoImpl(@Qualifier("traineeStorage") Map<Long, Trainee> trainees) {
        this.trainees = trainees;
    }

    @Override
    public Trainee add(Trainee entity) {
        entity.setUserId(++head);
        trainees.put(entity.getUserId(),entity);
        log.info("Added new trainee: {}", entity);
        return entity;
    }

    @Override
    public Trainee update(Trainee entity) {
        trainees.put(entity.getUserId(), entity);
        log.info("Updated trainee with id {} to: {}", entity.getUserId(), entity);
        return entity;
    }


    @Override
    public Trainee findById(long id) {
        Trainee trainee = trainees.get(id);
        if (trainee != null) {
            log.info("Found trainee with id {}: {}", id, trainee);
        } else {
            log.warn("No trainee found with id {}", id);
        }
        return trainee;
    }

    @Override
    public Collection<Trainee> findAll() {
        log.info("Retrieving all trainees");
        return trainees.values().stream().toList();
    }

    @Override
    public boolean delete(Trainee entity) {
        if(trainees.containsKey(entity.getUserId())) {
            trainees.remove(entity.getUserId());
            log.info("Deleted trainee with id {}", entity.getUserId());
            return true;
        }
        return false;
    }
    @Override
    public boolean isUsernameExist(String username) {
        boolean exists = trainees.values().stream().anyMatch(trainee -> trainee.getUsername().equals(username));
        log.info("Checking if username {} already exists: {}", username, exists);
        return exists;
    }

}
