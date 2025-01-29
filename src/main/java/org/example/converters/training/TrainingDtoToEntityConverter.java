package org.example.converters.training;

import org.example.models.trainee.TraineeDto;
import org.example.models.trainer.TrainerDto;
import org.example.models.training.TrainingDto;
import org.example.repositories.entities.Trainee;
import org.example.repositories.entities.Trainer;
import org.example.repositories.entities.Training;
import org.springframework.stereotype.Component;
import org.springframework.core.convert.converter.Converter;

@Component
public class TrainingDtoToEntityConverter implements Converter<TrainingDto, Training> {

    @Override
    public Training convert(TrainingDto dto) {
        return Training.builder()
                .id(dto.getId())
                .trainer(convertWithoutDependencies(dto.getTrainerDto()))
                .trainee(convertWithoutDependencies(dto.getTraineeDto()))
                .trainingDate(dto.getTrainingDate())
                .trainingDuration(dto.getTrainingDuration())
                .trainingName(dto.getTrainingName())
                .trainingType(dto.getTrainingType())
                .build();
    }
    public Trainee convertWithoutDependencies(TraineeDto dto) {
        return Trainee.builder()
                .id(dto.getId())
                .firstName(dto.getFirstName())
                .lastName(dto.getLastName())
                .username(dto.getUsername())
                .password(dto.getPassword())
                .isActive(dto.isActive())
                .dateOfBirth(dto.getDateOfBirth())
                .address(dto.getAddress())
                .build();
    }

    public Trainer convertWithoutDependencies(TrainerDto dto) {
        return Trainer.builder()
                .id(dto.getId())
                .firstName(dto.getFirstName())
                .lastName(dto.getLastName())
                .username(dto.getUsername())
                .password(dto.getPassword())
                .isActive(dto.isActive())
                .specialization(dto.getSpecialization())
                .build();
    }
}
