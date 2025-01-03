package org.example.services;

import lombok.extern.slf4j.Slf4j;
import org.example.repositories.UserDAO;
import org.example.models.Trainee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import java.util.Map;


@Component
@Slf4j
public class TraineeServiceImpl implements UserService<Trainee> {
    private UserDAO<Trainee> traineeDAO;
    @Autowired
    public TraineeServiceImpl(@Qualifier("traineeDAOImpl") UserDAO<Trainee> traineeDAO) {
        this.traineeDAO = traineeDAO;
        log.info("TraineeServiceImpl initialized");
    }

    @Override
    public Trainee add(Trainee trainee) {
        log.info("Request to add trainee: {}", trainee.getUsername());
        Trainee addedTrainee = traineeDAO.add(trainee);
        log.info("Service completed action successfully");
        return addedTrainee;
    }

    @Override
    public Trainee findById(long id) {
        log.info("Request to find trainee by ID: {}", id);
        Trainee trainee = traineeDAO.findById(id);
        if (trainee != null) {
            log.info("Service completed action successfully");
        } else {
            log.warn("Service completed action unsuccessfully");
        }
        return trainee;
    }

    @Override
    public Map<Long, Trainee> findAll() {
        log.info("Request to find all trainees");
        Map<Long, Trainee> trainees = traineeDAO.findAll();
        log.info("Service completed action successfully");
        return trainees;
    }

    @Override
    public boolean delete(long id) {
        log.info("Request to delete trainee with ID: {}", id);
        boolean result = traineeDAO.delete(id);
        if (result) {
            log.info("Service completed action successfully");
        } else {
            log.warn("Service completed action unsuccessfully");
        }
        return result;
    }

    @Override
    public Trainee update(long id, Trainee trainee) {
        log.info("Request to update trainee with ID: {}", id);
        Trainee updatedTrainee = traineeDAO.update(id, trainee);
        log.info("Service completed action successfully");
        return updatedTrainee;
    }
}