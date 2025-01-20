package org.example.services;

import org.example.models.TraineeDto;
import org.example.repositories.entities.Trainee;

import java.util.Collection;
import java.util.Optional;

public interface TraineeService extends UserService<TraineeDto> {
    void delete(TraineeDto entity);

    void deleteByUsername(String username);
}
