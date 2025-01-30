package org.example.repositories;

import org.example.repositories.entities.Trainee;
import org.example.repositories.entities.Trainer;

import java.util.Collection;
import java.util.Optional;

public interface TraineeDao extends UserDao<Trainee> {
    void delete(Trainee entity);

    Collection<Trainer> findTrainersByTraineeUsername(String username);
}
