package org.example.services;

import lombok.extern.slf4j.Slf4j;

import org.example.repositories.TraineeDao;
import org.example.repositories.UserDao;

import org.example.repositories.entities.Trainee;
import org.example.utils.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;
import java.util.TreeMap;


@Service
@Slf4j
public class TraineeServiceImpl implements TraineeService {

    @Autowired
    @Qualifier("traineeDaoImpl")
    private TraineeDao traineeDao;

    @Override
    public Optional<Trainee> add(Trainee trainee) {
        log.info("Request to save trainee");
        if(!UserUtils.verifyPassword(trainee.getPassword())) {
            return Optional.empty();
        }
        trainee.setPassword(UserUtils.hashPassword(trainee.getPassword()));
        trainee.setUsername(UserUtils.generateUserName(trainee, traineeDao::isUsernameExist));
        if(!UserUtils.validateUser(trainee)) {
            return Optional.empty();
        }
        return traineeDao.save(trainee);
    }

    @Override
    public Optional<Trainee> findById(long id) {
        log.info("Request to find trainee by ID: {}", id);
        return traineeDao.findById(id);
    }

    @Override
    public Optional<Collection<Trainee>> findAll() {
        log.info("Request to find all trainees");
        return traineeDao.findAll();
    }

    @Override
    public boolean delete(Trainee trainee) {
        log.info("Request to delete trainee with ID: {}", trainee.getId());
        if(traineeDao.findById(trainee.getId()).isPresent()) {
            traineeDao.delete(trainee);
            return true;
        }
        return false;
    }


    @Override
    public Optional<Trainee> update(Trainee trainee) {
        log.info("Request to update trainee with ID: {}", trainee.getId());
        if (traineeDao.findById(trainee.getId()).isEmpty()) {
            log.error("Trainee with id " + trainee.getId() + " not found");
            return Optional.empty();
        }

        return traineeDao.save(trainee);
    }
    @Override
    public boolean deleteByUsername(String username){
        log.info("Request to delete trainee with username: {}", username);
        if(traineeDao.findByUsername(username).isPresent()) {
            traineeDao.delete(traineeDao.findByUsername(username).get());
            return true;
        }
        return false;
    }
    @Override
    public Optional<Trainee> findByUsername(String username){
        return traineeDao.findByUsername(username);
    }

    @Override
    public Optional<Trainee> changePassword(String username, String oldPassword, String newPassword) {
        if(traineeDao.findByUsername(username).isPresent()) {
            Trainee trainee=traineeDao.findByUsername(username).get();
            if(UserUtils.passwordMatch(oldPassword, trainee.getPassword())) {
                trainee.setPassword(UserUtils.hashPassword(newPassword));
                return traineeDao.save(trainee);
            }
        }
        return Optional.empty();
    }
    @Override
    public Optional<Trainee>  activate(Trainee entity) {;
        if(traineeDao.findById(entity.getId()).isPresent()) {
            entity.setIsActive(true);
            return traineeDao.save(entity);
        }

        log.error("Trainee with id " + entity.getId() + " not found");
        return Optional.empty();

    }
    @Override
    public Optional<Trainee>  deactivate(Trainee entity) {
        if(traineeDao.findById(entity.getId()).isPresent()) {
            entity.setIsActive(false);
            return traineeDao.save(entity);
        }

        log.error("Trainee with id " + entity.getId() + " not found");
        return Optional.empty();

    }

}