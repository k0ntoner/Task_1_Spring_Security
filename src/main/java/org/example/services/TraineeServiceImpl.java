package org.example.services;

import lombok.extern.slf4j.Slf4j;
import org.example.repositories.UserDAO;
import org.example.models.Trainee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.Map;


@Component
public class TraineeServiceImpl implements UserService<Trainee> {
    private UserDAO<Trainee> traineeDAO;
    @Autowired
    public TraineeServiceImpl(@Qualifier("traineeDAOImpl") UserDAO<Trainee> traineeDAO) {
        this.traineeDAO = traineeDAO;
    }

    @Override
    public Trainee add(Trainee trainee) {
        return traineeDAO.add(trainee);
    }

    @Override
    public Trainee findById(long id) {
        return traineeDAO.findById(id);
    }

    @Override
    public Map<Long, Trainee> findAll() {
        return traineeDAO.findAll();
    }

    @Override
    public boolean delete(long id) {
        return traineeDAO.delete(id);
    }

    @Override
    public Trainee update(long id, Trainee trainee) {
        return traineeDAO.update(id, trainee);
    }
}