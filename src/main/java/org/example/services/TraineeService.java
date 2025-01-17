package org.example.services;

import org.example.repositories.entities.Trainee;

import java.util.Collection;
import java.util.Optional;

public interface TraineeService extends UserService<Trainee> {
    void delete(Trainee entity);

    void deleteByUsername(String username);
}
