package org.example.services;

import lombok.extern.slf4j.Slf4j;
import org.example.repositories.UserDao;
import org.example.models.Trainee;
import org.example.utils.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.Collection;


@Component
@Slf4j
public class TraineeServiceImpl implements UserService<Trainee> {
    private UserDao<Trainee> traineeDao;

    @Autowired
    public TraineeServiceImpl(@Qualifier("traineeDaoImpl") UserDao<Trainee> traineeDao) {
        this.traineeDao = traineeDao;
        log.info("TraineeServiceImpl initialized");
    }

    @Override
    public Trainee add(Trainee trainee) {
        log.info("Request to add trainee");
        trainee.setPassword(UserUtils.generatePassword());
        trainee.setUsername(UserUtils.generateUserName(trainee, traineeDao::isUsernameExist));
        return traineeDao.add(trainee);
    }

    @Override
    public Trainee findById(long id) {
        log.info("Request to find trainee by ID: {}", id);
        return traineeDao.findById(id);
    }

    @Override
    public Collection<Trainee> findAll() {
        log.info("Request to find all trainees");
        return traineeDao.findAll();
    }

    @Override
    public boolean delete(Trainee trainee) {
        log.info("Request to delete trainee with ID: {}", trainee.getUserId());
        return traineeDao.delete(trainee);
    }

    @Override
    public Trainee update(Trainee trainee) {
        log.info("Request to update trainee with ID: {}", trainee.getUserId());
        if (traineeDao.findById(trainee.getUserId()) == null)
            throw new IllegalArgumentException("Trainee with id " + trainee.getUserId() + " not found");

        return traineeDao.update(trainee);
    }

}