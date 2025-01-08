package org.example.repositories;

import org.example.models.Training;

import java.time.LocalDateTime;

public interface TrainingDao extends BasicDao<Training> {
    Training findByTrainer(long trainerId, LocalDateTime dateTime);

    Training findByTrainee(long traineeId, LocalDateTime dateTime);

}
