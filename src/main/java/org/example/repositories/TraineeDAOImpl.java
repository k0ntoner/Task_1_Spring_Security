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
public class TraineeDAOImpl implements UserDAO<Trainee> {
    private LoggerManager log;
    private Map<Long, Trainee> trainees;
    private long head=0;

    @Autowired
    public TraineeDAOImpl(@Qualifier("traineeStorage") Map<Long, Trainee> trainees, @Qualifier("loggerManager") LoggerManager loggerManager) {
        this.trainees = trainees;
        this.log = loggerManager;
    }
    @PostConstruct
    public void init() {
        head=trainees.values().stream().mapToLong(trainee ->trainee.getUserId()).max().orElse(0);
        log.info("TraineeDAOImpl initialized");
    }

    @Override
    public Trainee add(Trainee entity) {
        prepareModel(entity);
        trainees.put(++head,entity);
        log.info("Added new trainee: {}", entity);
        return entity;
    }

    @Override
    public Trainee update(long id, Trainee entity) {
        trainees.put(id, entity);
        log.info("Updated trainee with id {} to: {}", id, entity);
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
    public Map<Long, Trainee> findAll() {
        log.info("Retrieving all trainees");
        return trainees;
    }

    @Override
    public boolean delete(long id) {
        try{
            trainees.remove(id);
            log.info("Deleted trainee with id {}", id);
            return true;
        }
        catch(Exception e){
            log.error("Failed to delete trainee with id {}", id, e);
        }
        return false;
    }
    private boolean isUsernameExist(Trainee entity) {
        boolean exists = trainees.values().stream().anyMatch(trainee -> trainee.getUsername().equals(entity.getUsername()));
        log.info("Checking if username {} already exists: {}", entity.getUsername(), exists);
        return exists;
    }
    private Trainee prepareModel(Trainee entity) {
        log.info("Preparing model for trainee: {}", entity);

        entity.setPassword(PasswordGenerator.generatePassword());
        entity.setUserId(head+1);

        log.info("Generate id {} for trainee: {}",head+1, entity);
        log.info("Generating username for trainee: {} . . .", entity);
        int k=1;
        entity.setUsername(entity.getFirstName()+"."+entity.getLastName());
        if(isUsernameExist(entity)==true)
            entity.setUsername(entity.getUsername().concat("1"));
        while(isUsernameExist(entity)==true){
            StringBuilder username=new StringBuilder(entity.getUsername());
            username.setCharAt(username.length()-1,(char) ++k);

        }
        log.info("Generated username {} for trainee: {}",entity.getUsername(), entity);
        return entity;
    }
}
