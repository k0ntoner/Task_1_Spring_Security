package org.example.services;

import org.example.models.TraineeDto;
import org.example.models.TrainerDto;
import org.example.models.TrainingDto;
import org.example.repositories.entities.Trainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.Collection;

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

    public TraineeDto addTrainee(TraineeDto traineeDto) {
        return traineeService.add(traineeDto);
    }

    public TraineeDto findTraineeById(long id) {
        return traineeService.findById(id).get();
    }

    public Collection<TraineeDto> findAllTrainees() {
        return traineeService.findAll();
    }

    public void deleteTrainee(TraineeDto traineeDto) {
        traineeService.delete(traineeDto);
    }

    public TraineeDto updateTrainee(TraineeDto traineeDto) {
        return traineeService.update(traineeDto);
    }

    public TrainerDto addTrainer(TrainerDto trainerDto) {
        return trainerService.add(trainerDto);
    }

    public TrainerDto findTrainerById(long id) {
        return trainerService.findById(id).get();
    }

    public Collection<TrainerDto> findAllTrainers() {
        return trainerService.findAll();
    }

    public TrainerDto updateTrainer(TrainerDto trainerDto) {
        return trainerService.update(trainerDto);
    }

    public TrainingDto addTraining(TrainingDto trainingDto) {
        return trainingService.add(trainingDto);
    }

    public TrainingDto findTrainingById(long id) {
        return trainingService.findById(id).get();
    }

    public Collection<TrainingDto> findAllTrainings() {
        return trainingService.findAll();
    }
}

