package org.example.services;

import org.example.models.TraineeDto;
import org.example.repositories.entities.Trainee;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Optional;

public interface TraineeService extends UserService<TraineeDto> {
    void delete(TraineeDto entity);

    void deleteByUsername(String username);

    TraineeDto add(Long id, String firstName, String lastName, String username, String password, boolean isActive, LocalDate dateOfBirth, String address);
}
