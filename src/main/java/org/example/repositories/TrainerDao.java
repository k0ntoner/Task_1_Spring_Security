package org.example.repositories;


import org.example.repositories.entities.Trainee;
import org.example.repositories.entities.Trainer;

import java.util.Collection;

public interface TrainerDao extends UserDao<Trainer>, BasicDao<Trainer> {
    Collection<Trainer> findTrainersNotAssignedToTrainee(String traineeUsername);

    Collection<Trainee> findTraineesByTrainerUsername(String username);
}
