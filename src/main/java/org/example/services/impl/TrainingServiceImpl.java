package org.example.services.impl;

import lombok.extern.slf4j.Slf4j;
import org.example.repositories.entities.Training;
import org.example.repositories.TrainingDao;
import org.example.repositories.entities.TrainingType;
import org.example.services.TrainingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Optional;

@Service
@Slf4j
public class TrainingServiceImpl implements TrainingService {
    @Autowired
    @Qualifier("trainingDaoImpl")
    private TrainingDao trainingDao;

    @Override
    public Training add(Training entity) {
        log.info("Request to save training: {}", entity.getTrainingName());
        return trainingDao.save(entity);

    }

    @Override
    public Optional<Training> findById(long id) {
        log.info("Request to find training by ID: {}", id);
        return trainingDao.findById(id);
    }

    @Override
    public Collection<Training> findAll() {
        log.info("Request to find all trainings");
        return trainingDao.findAll();
    }

    @Override
    public Collection<Training> findByTrainer(String trainerUsername, LocalDateTime startDateTime, LocalDateTime endDateTime, String traineeUsername) {
        log.info("Request to find training by trainer username: {}, and criteria: startDateTime: {}, endDateTime: {}, traineeUsername: {}", trainerUsername, startDateTime, endDateTime, traineeUsername);
        return trainingDao.findByTrainer(trainerUsername, startDateTime, endDateTime, traineeUsername);
    }

    @Override
    public Collection<Training> findByTrainee(String traineeUsername, LocalDateTime startDateTime, LocalDateTime endDateTime, String trainerUsername, TrainingType trainingType) {
        log.info("Request to find training by trainee username: {}, and criteria: startDateTime: {}, endDateTime: {}, trainerUsername: {}, trainingType: {}", trainerUsername, startDateTime, endDateTime, traineeUsername, trainingType);
        return trainingDao.findByTrainee(traineeUsername, startDateTime, endDateTime, trainerUsername, trainingType);
    }
}
