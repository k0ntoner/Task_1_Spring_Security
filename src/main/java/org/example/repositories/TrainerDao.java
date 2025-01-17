package org.example.repositories;


import org.example.repositories.entities.Trainer;

import java.util.Collection;
import java.util.Optional;

public interface TrainerDao extends UserDao<Trainer> {
    Optional<Collection<Trainer>> findTrainersNotAssignedToTrainee(String traineeUsername);
}
