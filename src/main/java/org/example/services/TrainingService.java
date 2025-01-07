package org.example.services;

import org.example.models.Training;

import java.time.LocalDateTime;
import java.util.Map;

public interface TrainingService extends BasicService<Training> {
    Training findByTrainer(long trainerId, LocalDateTime dateTime);

    Training findByTrainee(long traineeId, LocalDateTime dateTime);
}
