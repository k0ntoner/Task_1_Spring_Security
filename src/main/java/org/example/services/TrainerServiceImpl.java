package org.example.services;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.NotImplementedException;
import org.example.repositories.TraineeDao;
import org.example.repositories.TrainerDao;
import org.example.repositories.entities.Trainee;
import org.example.repositories.entities.Trainer;
import org.example.repositories.UserDao;
import org.example.utils.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;

@Service
@Slf4j
public class TrainerServiceImpl implements TrainerService {
    @Autowired
    @Qualifier("trainerDaoImpl")
    private TrainerDao trainerDao;

    @Override
    public Optional<Trainer> add(Trainer entity) {
        log.info("Request to save trainer");
        if(!UserUtils.verifyPassword(entity.getPassword())) {
            return Optional.empty();
        }
        entity.setUsername(UserUtils.generateUserName(entity, trainerDao::isUsernameExist));
        entity.setPassword(UserUtils.hashPassword(entity.getPassword()));
        if(!UserUtils.validateUser(entity)) {
            return Optional.empty();
        }
        return trainerDao.save(entity);
    }

    @Override
    public Optional<Trainer> findById(long id) {
        log.info("Request to find trainer by ID: {}", id);
        return trainerDao.findById(id);
    }

    @Override
    public Optional<Collection<Trainer>> findAll() {
        log.info("Request to find all trainers");
        return trainerDao.findAll();
    }

    @Override
    public Optional<Trainer> update(Trainer trainer) {
        log.info("Request to update trainer with ID: {}", trainer.getId());
        if (trainerDao.findById(trainer.getId()).isEmpty()) {
            log.error("Trainer with id " + trainer.getId() + " not found");
            return Optional.empty();
        }

        return trainerDao.save(trainer);
    }

    @Override
    public Optional<Trainer> findByUsername(String username){
        return trainerDao.findByUsername(username);
    }
    @Override
    public Optional<Trainer> changePassword(String username, String oldPassword, String newPassword) {
        if(trainerDao.findByUsername(username).isPresent()) {
            Trainer trainer=trainerDao.findByUsername(username).get();
            if(UserUtils.passwordMatch(oldPassword, trainer.getPassword())) {
                trainer.setPassword(UserUtils.hashPassword(newPassword));
                return trainerDao.save(trainer);
            }
        }
        return Optional.empty();
    }
    @Override
    public Optional<Trainer>  activate(Trainer entity) {
        if(trainerDao.findById(entity.getId()).isPresent()) {
            entity.setIsActive(true);
            return trainerDao.save(entity);
        }
        log.error("Trainer with id " + entity.getId() + " not found");
        return Optional.empty();
    }
    @Override
    public Optional<Trainer>  deactivate(Trainer entity) {
        if(trainerDao.findById(entity.getId()).isPresent()) {
            entity.setIsActive(false);
            return trainerDao.save(entity);
        }
        log.error("Trainer with id " + entity.getId() + " not found");
        return Optional.empty();
    }

    @Override
    public Optional<Collection<Trainer>> findTrainersNotAssignedToTrainee(String traineeUsername) {
        return trainerDao.findTrainersNotAssignedToTrainee(traineeUsername);
    }
}
