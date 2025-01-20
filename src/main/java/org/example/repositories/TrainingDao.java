package org.example.repositories;


import org.example.repositories.entities.Training;
import org.example.enums.TrainingType;

import java.time.LocalDateTime;
import java.util.Collection;

public interface TrainingDao extends BasicDao<Training> {
    Collection<Training> findByTrainer(String trainerUsername, LocalDateTime startDateTime, LocalDateTime endDateTime, String traineeUsername);

    Collection<Training> findByTrainee(String traineeUsername, LocalDateTime startDateTime, LocalDateTime endDateTime, String trainerUsername, TrainingType trainingType);
}
