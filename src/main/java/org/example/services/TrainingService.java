package org.example.services;

import org.example.models.TraineeDto;
import org.example.models.TrainerDto;
import org.example.models.TrainingDto;
import org.example.repositories.entities.Training;
import org.example.enums.TrainingType;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Collection;

public interface TrainingService extends BasicService<TrainingDto> {
    Collection<TrainingDto> findByTrainer(String trainerUsername, LocalDateTime startDateTime, LocalDateTime endDateTime, String traineeUsername);

    Collection<TrainingDto> findByTrainee(String traineeUsername, LocalDateTime startDateTime, LocalDateTime endDateTime, String trainerUsername, TrainingType trainingType);

    TrainingDto add(Long id, TraineeDto traineeDto, TrainerDto trainerDto, TrainingType trainingType, String trainingName, Duration duration, LocalDateTime dateTime);
}
