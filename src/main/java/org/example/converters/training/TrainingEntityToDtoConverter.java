package org.example.converters.training;

import org.example.models.trainee.TraineeDto;
import org.example.models.trainer.TrainerDto;
import org.example.models.training.TrainingDto;
import org.example.repositories.entities.Trainee;
import org.example.repositories.entities.Trainer;
import org.example.repositories.entities.Training;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class TrainingEntityToDtoConverter implements Converter<Training, TrainingDto> {
    @Override
    public TrainingDto convert(Training entity) {
        return TrainingDto.builder()
                .id(entity.getId())
                .trainerDto(convertWithoutDependencies(entity.getTrainer()))
                .traineeDto(convertWithoutDependencies(entity.getTrainee()))
                .trainingDate(entity.getTrainingDate())
                .trainingDuration(entity.getTrainingDuration())
                .trainingName(entity.getTrainingName())
                .trainingType(entity.getTrainingType())
                .build();
    }

    public TraineeDto convertWithoutDependencies(Trainee entity) {
        return TraineeDto.builder()
                .id(entity.getId())
                .firstName(entity.getFirstName())
                .lastName(entity.getLastName())
                .username(entity.getUsername())
                .password(entity.getPassword())
                .isActive(entity.isActive())
                .dateOfBirth(entity.getDateOfBirth())
                .address(entity.getAddress())
                .build();
    }

    public TrainerDto convertWithoutDependencies(Trainer entity) {
        return TrainerDto.builder()
                .id(entity.getId())
                .firstName(entity.getFirstName())
                .lastName(entity.getLastName())
                .username(entity.getUsername())
                .password(entity.getPassword())
                .isActive(entity.isActive())
                .specialization(entity.getSpecialization())
                .build();
    }
}
