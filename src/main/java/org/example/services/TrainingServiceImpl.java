package org.example.services;

import lombok.extern.slf4j.Slf4j;
import org.example.models.Training;
import org.example.repositories.TrainingDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Collection;

@Component
@Slf4j
public class TrainingServiceImpl implements TrainingService {

    private final TrainingDao trainingDAO;

    @Autowired
    public TrainingServiceImpl(@Qualifier("trainingDaoImpl") TrainingDao trainingDAO) {
        this.trainingDAO = trainingDAO;
        log.info("TrainingServiceImpl initialized");
    }

    @Override
    public Training add(Training entity) {
        log.info("Request to add training: {}", entity.getTrainingName());
        return trainingDAO.add(entity);

    }

    @Override
    public Training findById(long id) {
        log.info("Request to find training by ID: {}", id);
        return trainingDAO.findById(id);
    }

    @Override
    public Collection<Training> findAll() {
        log.info("Request to find all trainings");
        return trainingDAO.findAll();
    }

    @Override
    public Training findByTrainer(long trainerId, LocalDateTime dateTime) {
        log.info("Request to find training by trainer ID: {} at time: {}", trainerId, dateTime);
        return trainingDAO.findByTrainer(trainerId, dateTime);

    }

    @Override
    public Training findByTrainee(long traineeId, LocalDateTime dateTime) {
        log.info("Request to find training by trainee ID: {} at time: {}", traineeId, dateTime);
        return trainingDAO.findByTrainee(traineeId, dateTime);

    }

}
