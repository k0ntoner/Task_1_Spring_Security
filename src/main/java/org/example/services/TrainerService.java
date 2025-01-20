package org.example.services;

import org.example.models.TrainerDto;
import org.example.repositories.entities.Trainee;
import org.example.repositories.entities.Trainer;
import org.hibernate.Session;

import java.util.Collection;

import java.util.Optional;

public interface TrainerService extends UserService<TrainerDto> {
    public Collection<TrainerDto> findTrainersNotAssignedToTrainee(String traineeUsername);
}
