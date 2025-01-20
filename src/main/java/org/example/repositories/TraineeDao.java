package org.example.repositories;

import org.example.repositories.entities.Trainee;

import java.util.Collection;
import java.util.Optional;

public interface TraineeDao extends UserDao<Trainee> {
    void delete(Trainee entity);
}
