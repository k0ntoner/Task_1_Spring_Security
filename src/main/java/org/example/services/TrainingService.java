package org.example.services;

import org.example.repositories.entities.Training;
import org.example.repositories.entities.TrainingType;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Optional;

public interface TrainingService extends BasicService<Training> {
    Collection<Training> findByTrainer(String trainerUsername, LocalDateTime startDateTime, LocalDateTime endDateTime, String traineeUsername);

    Collection<Training> findByTrainee(String traineeUsername, LocalDateTime startDateTime, LocalDateTime endDateTime, String trainerUsername, TrainingType trainingType);

}
