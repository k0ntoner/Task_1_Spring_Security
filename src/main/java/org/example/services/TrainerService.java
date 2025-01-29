package org.example.services;

import org.example.enums.TrainingType;
import org.example.models.trainee.TraineeDto;
import org.example.models.trainer.TrainerDto;

import java.util.Collection;

public interface TrainerService extends UserService<TrainerDto> {
    public Collection<TrainerDto> findTrainersNotAssignedToTrainee(String traineeUsername);

    Collection<TraineeDto> findTraineesByTrainerUsername(String username);

    TrainerDto add(Long id, String firstName, String lastName, String username, String password, boolean isActive, TrainingType specialization);
}
