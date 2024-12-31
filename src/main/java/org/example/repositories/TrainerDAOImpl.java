package org.example.repositories;

import lombok.extern.slf4j.Slf4j;
import org.example.models.Trainer;
import org.example.utils.PasswordGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.util.Map;
@Repository
public class TrainerDAOImpl implements UserDAO<Trainer> {
    @Autowired
    @Qualifier("trainerStorage")
    private Map<Long, Trainer> trainers;
    private long head;
    @PostConstruct
    public void init() {
        head=trainers.values().stream().mapToLong(trainer ->trainer.getUserId()).max().orElse(0);
    }

    @Override
    public Trainer add(Trainer entity) {
        prepareModel(entity);
        trainers.put(++head, entity);
        return entity;
    }

    @Override
    public Trainer update(long id, Trainer entity) {
        trainers.put(id, entity);
        return entity;
    }

    @Override
    public Trainer findById(long id) {
        return trainers.get(id);
    }

    @Override
    public Map<Long, Trainer> findAll() {
        return trainers;
    }

    @Override
    public boolean delete(long id) {
        try{
            trainers.remove(id);
            return true;
        }
        catch(Exception e){

        }
        return false;
    }

    private boolean isUsernameExist(Trainer entity) {
        return trainers.values().stream().anyMatch(trainer -> trainer.getUsername().equals(entity.getUsername()));
    }
    private Trainer prepareModel(Trainer entity) {
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
