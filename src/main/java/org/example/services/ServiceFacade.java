package org.example.services;

import lombok.extern.slf4j.Slf4j;
import org.example.repositories.entities.Trainee;
import org.example.repositories.entities.Trainer;
import org.example.repositories.entities.Training;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Optional;

@Component
public class ServiceFacade {

    private final TrainingService trainingService;
    private final TraineeService traineeService;
    private final TrainerService trainerService;

    @Autowired
    public ServiceFacade(@Qualifier("trainingServiceImpl") TrainingService trainingService, @Qualifier("traineeServiceImpl") TraineeService traineeService, @Qualifier("trainerServiceImpl") TrainerService trainerService) {
        this.trainingService = trainingService;
        this.traineeService = traineeService;
        this.trainerService = trainerService;
    }

    public Trainee addTrainee(Trainee trainee) {
        return traineeService.add(trainee).get();
    }

    public Trainee findTraineeById(long id) {
        return traineeService.findById(id).get();
    }

    public Collection<Trainee> findAllTrainees() {
        return traineeService.findAll().get();
    }

    public boolean deleteTrainee(Trainee trainee) {
        return traineeService.delete(trainee);
    }

    public Trainee updateTrainee(Trainee trainee) {
        return traineeService.update(trainee).get();
    }

    public Trainer addTrainer(Trainer trainer) {
        return trainerService.add(trainer).get();
    }

    public Trainer findTrainerById(long id) {
        return trainerService.findById(id).get();
    }

    public Collection<Trainer> findAllTrainers() {
        return trainerService.findAll().get();
    }

    public Trainer updateTrainer(Trainer trainer) {
        return trainerService.update(trainer).get();
    }

    public Training addTraining(Training training) {
        return trainingService.add(training).get();
    }

    public Training findTrainingById(long id) {
        return trainingService.findById(id).get();
    }

    public Collection<Training> findAllTrainings() {
        return trainingService.findAll().get();
    }
}

