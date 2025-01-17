package org.example.services.impl;

import lombok.extern.slf4j.Slf4j;

import org.example.repositories.TraineeDao;

import org.example.repositories.entities.Trainee;
import org.example.services.TraineeService;
import org.example.utils.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;


@Service
@Slf4j
public class TraineeServiceImpl implements TraineeService {

    @Autowired
    @Qualifier("traineeDaoImpl")
    private TraineeDao traineeDao;

    @Override
    public Trainee add(Trainee trainee) {
        log.info("Request to save trainee");
        if (!UserUtils.verifyPassword(trainee.getPassword())) {
            throw new IllegalArgumentException("Invalid password");
        }
        trainee.setPassword(UserUtils.hashPassword(trainee.getPassword()));
        trainee.setUsername(UserUtils.generateUserName(trainee, traineeDao::isUsernameExist));
        return traineeDao.save(trainee);
    }

    @Override
    public Optional<Trainee> findById(long id) {
        log.info("Request to find trainee by ID: {}", id);
        return traineeDao.findById(id);
    }

    @Override
    public Collection<Trainee> findAll() {
        log.info("Request to find all trainees");
        return traineeDao.findAll();
    }

    @Override
    public void delete(Trainee trainee) {
        log.info("Request to delete trainee with ID: {}", trainee.getId());
        if (traineeDao.findById(trainee.getId()).isEmpty()) {
            throw new IllegalArgumentException("Trainee with ID " + trainee.getId() + " not found");
        }
        traineeDao.delete(trainee);
    }


    @Override
    public Trainee update(Trainee trainee) {
        log.info("Request to update trainee with ID: {}", trainee.getId());
        if (traineeDao.findById(trainee.getId()).isEmpty()) {
            throw new IllegalArgumentException("Invalid trainee");
        }

        return traineeDao.update(trainee);
    }

    @Override
    public void deleteByUsername(String username) {
        log.info("Request to delete trainee with username: {}", username);
        if (traineeDao.findByUsername(username).isEmpty()) {
            throw new IllegalArgumentException("Invalid username");
        }
        traineeDao.delete(traineeDao.findByUsername(username).get());
    }

    @Override
    public Optional<Trainee> findByUsername(String username) {
        return traineeDao.findByUsername(username);
    }

    @Override
    public void changePassword(String username, String oldPassword, String newPassword) {
        if (traineeDao.findByUsername(username).isPresent()) {
            Trainee trainee = traineeDao.findByUsername(username).get();
            if (UserUtils.passwordMatch(oldPassword, trainee.getPassword())) {
                trainee.setPassword(UserUtils.hashPassword(newPassword));
                traineeDao.update(trainee);
                return;
            }
            throw new IllegalArgumentException("Invalid password");
        }
        throw new IllegalArgumentException("Invalid username");
    }

    @Override
    public void activate(Trainee entity) {
        if (traineeDao.findById(entity.getId()).isEmpty()) {
            throw new IllegalArgumentException("Invalid trainee");

        }
        entity.setActive(true);
        traineeDao.update(entity);

    }

    @Override
    public void deactivate(Trainee entity) {
        if (traineeDao.findById(entity.getId()).isEmpty()) {
            throw new IllegalArgumentException("Invalid trainee");

        }
        entity.setActive(false);
        traineeDao.update(entity);

    }

}