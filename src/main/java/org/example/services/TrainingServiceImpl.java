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
public class TrainingServiceImpl implements TrainingService<Training> {

    private TrainingDAO<Training> trainingDAO;
    @Autowired
    public TrainingServiceImpl(@Qualifier("trainingDAOImpl") TrainingDAO<Training> trainingDAO) {
        this.trainingDAO = trainingDAO;
    }

    @Override
    public Training add(Training entity) {
        return trainingDAO.add(entity);
    }

    @Override
    public Training findById(long id) {
        return trainingDAO.findById(id);
    }

    @Override
    public Map<Long, Training> findAll() {
        return trainingDAO.findAll();
    }

    @Override
    public boolean delete(long id) {
        return trainingDAO.delete(id);
    }

    @Override
    public Training update(long id, Training trainee) {
        return trainingDAO.update(id, trainee);
    }

    @Override
    public Training findByTrainer(long trainerId, LocalDateTime dateTime) {
        return trainingDAO.findByTrainer(trainerId, dateTime);
    }

    @Override
    public Training findByTrainee(long traineeId, LocalDateTime dateTime) {
        return trainingDAO.findByTrainee(traineeId, dateTime);
    }
}
