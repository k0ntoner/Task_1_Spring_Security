package org.example.services;

import org.example.models.trainee.TraineeDto;
import org.example.models.trainer.TrainerDto;
import org.example.models.training.TrainingDto;
import org.example.enums.TrainingType;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Collection;

public interface TrainingService extends BasicService<TrainingDto> {
    Collection<TrainingDto> findByTrainer(String trainerUsername, LocalDateTime startDateTime, LocalDateTime endDateTime, String traineeUsername);

    Collection<TrainingDto> findByTrainee(String traineeUsername, LocalDateTime startDateTime, LocalDateTime endDateTime, String trainerUsername, TrainingType trainingType);

    TrainingDto add(String traineeUsername, String trainerUsername, TrainingType trainingType, String trainingName, Duration duration, LocalDateTime dateTime);


}
