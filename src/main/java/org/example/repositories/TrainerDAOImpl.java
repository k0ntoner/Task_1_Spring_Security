package org.example.repositories;

import lombok.extern.slf4j.Slf4j;
import org.example.models.Trainee;
import org.example.models.Trainer;
import org.example.utils.LoggerManager;
import org.example.utils.PasswordGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.util.Map;
@Repository
public class TrainerDAOImpl implements UserDAO<Trainer> {
    private Map<Long, Trainer> trainers;
    private LoggerManager log;
    private long head;
    @Autowired
    public TrainerDAOImpl(@Qualifier("trainerStorage") Map<Long, Trainer> trainers, @Qualifier("loggerManager") LoggerManager loggerManager) {
        this.trainers = trainers;
        this.log = loggerManager;
    }
    @PostConstruct
    public void init() {
        head=trainers.values().stream().mapToLong(trainer ->trainer.getUserId()).max().orElse(0);
        log.info("TrainerDAOImpl initialized");
    }

    @Override
    public Trainer add(Trainer entity) {
        prepareModel(entity);
        trainers.put(++head, entity);
        log.info("Added new trainer: {}", entity);
        return entity;
    }

    @Override
    public Trainer update(long id, Trainer entity) {
        trainers.put(id, entity);
        log.info("Updated trainer with id {} to: {}", id, entity);
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
    public Map<Long, Trainer> findAll() {
        log.info("Retrieving all trainers");
        return trainers;
    }

    @Override
    public boolean delete(long id) {
        try{
            trainers.remove(id);
            log.info("Deleted trainer with id {}", id);
            return true;
        }
        catch(Exception e){
            log.error("Failed to delete trainer with id {}", id, e);
        }
        return false;
    }

    private boolean isUsernameExist(Trainer entity) {
        boolean exists = trainers.values().stream().anyMatch(trainer -> trainer.getUsername().equals(entity.getUsername()));
        log.info("Checking if username {} already exists: {}", entity.getUsername(), exists);
        return exists;
    }
    private Trainer prepareModel(Trainer entity) {
        log.info("Preparing model for trainer: {}", entity);

        entity.setPassword(PasswordGenerator.generatePassword());
        entity.setUserId(head+1);

        log.info("Generate id {} for trainer: {}",head+1, entity);
        log.info("Generating username for trainer: {} . . .", entity);
        int k=1;
        entity.setUsername(entity.getFirstName()+"."+entity.getLastName());
        if(isUsernameExist(entity)==true)
            entity.setUsername(entity.getUsername().concat("1"));
        while(isUsernameExist(entity)==true){
            StringBuilder username=new StringBuilder(entity.getUsername());
            username.setCharAt(username.length()-1,(char) ++k);

        }
        log.info("Generated username {} for trainer: {}",entity.getUsername(), entity);
        return entity;
    }
}
