package org.example.services;

import lombok.extern.slf4j.Slf4j;
import org.example.models.Trainee;
import org.example.models.Trainer;
import org.example.models.Training;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Map;

@Component
public class ServiceFacade {

    private final TrainingService<Training> trainingService;
    private final UserService<Trainee> traineeService;
    private final UserService<Trainer> trainerService;

    @Autowired
    public ServiceFacade(@Qualifier("trainingServiceImpl") TrainingService<Training> trainingService, @Qualifier("traineeServiceImpl") UserService<Trainee> traineeService, @Qualifier("trainerServiceImpl") UserService<Trainer> trainerService) {
        this.trainingService = trainingService;
        this.traineeService = traineeService;
        this.trainerService = trainerService;
    }

    public Trainee addTrainee(Trainee trainee) {
        return traineeService.add(trainee);
    }

    public Trainee findTraineeById(long id) {
        return traineeService.findById(id);
    }

    public Map<Long, Trainee> findAllTrainees() {
        return traineeService.findAll();
    }

    public boolean deleteTrainee(long id) {
        return traineeService.delete(id);
    }

    public Trainee updateTrainee(long id, Trainee trainee) {
        return traineeService.update(id, trainee);
    }

    public Trainer addTrainer(Trainer trainer) {
        return trainerService.add(trainer);
    }

    public Trainer findTrainerById(long id) {
        return trainerService.findById(id);
    }

    public Map<Long, Trainer> findAllTrainers() {
        return trainerService.findAll();
    }

    public boolean deleteTrainer(long id) {
        return trainerService.delete(id);
    }

    public Trainer updateTrainer(long id, Trainer trainer) {
        return trainerService.update(id, trainer);
    }

    public Training addTraining(Training training) {
        return trainingService.add(training);
    }

    public Training findTrainingById(long id) {
        return trainingService.findById(id);
    }

    public Map<Long, Training> findAllTrainings() {
        return trainingService.findAll();
    }

    public Training findTrainingByTrainer(long trainerId, LocalDateTime dateTime) {
        return trainingService.findByTrainer(trainerId, dateTime);
    }

    public Training findTrainingByTrainee(long traineeId, LocalDateTime dateTime) {
        return findTrainingByTrainee(traineeId,dateTime);
    }
}

