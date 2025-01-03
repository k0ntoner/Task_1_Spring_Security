package org.example.services;

import lombok.extern.slf4j.Slf4j;
import org.example.models.Training;
import org.example.repositories.TrainingDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Map;

@Component
@Slf4j
public class TrainingServiceImpl implements TrainingService<Training> {

    private final TrainingDAO<Training> trainingDAO;

    @Autowired
    public TrainingServiceImpl(@Qualifier("trainingDAOImpl") TrainingDAO<Training> trainingDAO) {
        this.trainingDAO = trainingDAO;
        log.info("TrainingServiceImpl initialized");
    }

    @Override
    public Training add(Training entity) {
        log.info("Request to add training: {}", entity.getTrainingName());
        Training addedTraining = trainingDAO.add(entity);
        log.info("Service completed action successfully");
        return addedTraining;
    }

    @Override
    public Training findById(long id) {
        log.info("Request to find training by ID: {}", id);
        Training training = trainingDAO.findById(id);
        if (training != null) {
            log.info("Service completed action successfully");
        } else {
            log.warn("No training found with ID: {}", id);
        }
        return training;
    }

    @Override
    public Map<Long, Training> findAll() {
        log.info("Request to find all trainings");
        Map<Long, Training> trainings = trainingDAO.findAll();
        log.info("Service completed action successfully");
        return trainings;
    }

    @Override
    public Training findByTrainer(long trainerId, LocalDateTime dateTime) {
        log.info("Request to find training by trainer ID: {} at time: {}", trainerId, dateTime);
        Training training = trainingDAO.findByTrainer(trainerId, dateTime);
        if (training != null) {
            log.info("Service completed action successfully");
        } else {
            log.warn("No training found for trainer ID: {} at time: {}", trainerId, dateTime);
        }
        return training;
    }

    @Override
    public Training findByTrainee(long traineeId, LocalDateTime dateTime) {
        log.info("Request to find training by trainee ID: {} at time: {}", traineeId, dateTime);
        Training training = trainingDAO.findByTrainee(traineeId, dateTime);
        if (training != null) {
            log.info("Service completed action successfully");
        } else {
            log.warn("No training found for trainee ID: {} at time: {}", traineeId, dateTime);
        }
        return training;
    }

//    @Override
//    public boolean delete(long id) {
//        return trainingDAO.delete(id);
//    }
//
//    @Override
//    public Training update(long id, Training trainee) {
//        return trainingDAO.update(id, trainee);
//    }
}
