package org.example.services;

import org.example.models.trainee.TraineeDto;
import org.example.models.trainer.TrainerDto;
import org.example.repositories.BasicDao;

import java.time.LocalDate;
import java.util.Collection;

public interface TraineeService extends UserService<TraineeDto>, BasicService<TraineeDto> {
    TraineeDto update(TraineeDto dto);

    void delete(TraineeDto entity);

    void deleteByUsername(String username);

    TraineeDto add(Long id, String firstName, String lastName, String username, String password, boolean isActive, LocalDate dateOfBirth, String address);

    Collection<TrainerDto> findTrainersByTraineeUsername(String username);
}
