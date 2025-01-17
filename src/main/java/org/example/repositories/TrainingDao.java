package org.example.repositories;



import org.example.repositories.entities.Training;
import org.example.repositories.entities.TrainingType;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Optional;

public interface TrainingDao extends BasicDao<Training> {
    Optional<Collection<Training>> findByTrainer(String trainerUsername, LocalDateTime startDateTime, LocalDateTime endDateTime, String traineeUsername);

    Optional<Collection<Training>> findByTrainee(String traineeUsername, LocalDateTime startDateTime, LocalDateTime endDateTime, String trainerUsername, TrainingType trainingType);


}
