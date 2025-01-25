package org.example.services;

import org.example.enums.TrainingType;
import org.example.models.TraineeDto;
import org.example.models.TrainerDto;
import org.example.repositories.entities.Trainee;
import org.example.repositories.entities.Trainer;
import org.hibernate.Session;

import java.time.LocalDate;
import java.util.Collection;

import java.util.Optional;

public interface TrainerService extends UserService<TrainerDto> {
    public Collection<TrainerDto> findTrainersNotAssignedToTrainee(String traineeUsername);
    TrainerDto add(Long id, String firstName, String lastName, String username, String password, boolean isActive, TrainingType specialization);
}
