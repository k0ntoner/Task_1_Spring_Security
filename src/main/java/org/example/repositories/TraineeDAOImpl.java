package org.example.repositories;

import lombok.extern.slf4j.Slf4j;
import org.example.models.Trainee;
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
        log.info("Trainee DAO post initialization");
    }

    @Override
    public Trainee add(Trainee entity) {
        prepareModel(entity);
        trainees.put(++head,entity);
        log.info("Trainee DAO added new entity: {}", entity);
        return entity;
    }

    @Override
    public Trainee update(long id, Trainee entity) {
        trainees.put(id, entity);
        log.info("Trainee DAO updated entity at {} to: {}",id, entity);
        return entity;
    }


    @Override
    public Trainee findById(long id) {
        return trainees.get(id);
    }

    @Override
    public Map<Long, Trainee> findAll() {
        return trainees;
    }

    @Override
    public boolean delete(long id) {
        trainees.remove(id);
        log.warn("Trainee DAO deleted entity with id: {}", id);
        return true;
    }
    private boolean isUsernameExist(Trainee entity) {
        return trainees.values().stream().anyMatch(trainee -> trainee.getUsername().equals(entity.getUsername()));
    }
    private Trainee prepareModel(Trainee entity) {
        entity.setPassword(PasswordGenerator.generatePassword());
        entity.setUserId(head+1);
        int k=1;
        entity.setUsername(entity.getFirstName()+"."+entity.getLastName());
        if(isUsernameExist(entity)==true)
            entity.setUsername(entity.getUsername().concat("1"));
        while(isUsernameExist(entity)==true){
            StringBuilder username=new StringBuilder(entity.getUsername());
            username.setCharAt(username.length()-1,(char) ++k);

        }
        return entity;
    }
}
