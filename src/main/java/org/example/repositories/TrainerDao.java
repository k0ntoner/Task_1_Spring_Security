package org.example.repositories;


import org.example.repositories.entities.Trainer;

import java.util.Collection;

public interface TrainerDao extends UserDao<Trainer> {
    Collection<Trainer> findTrainersNotAssignedToTrainee(String traineeUsername);
}
