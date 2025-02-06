package org.example.converters.trainee;

import org.example.models.trainee.TraineeDto;
import org.example.models.trainee.TraineeViewDto;
import org.example.models.trainer.TrainerListViewDto;
import org.example.models.training.TrainingViewDto;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class TraineeDtoToTraineeViewDtoConverter implements Converter<TraineeDto, TraineeViewDto> {
    @Override
    public TraineeViewDto convert(TraineeDto dto) {
        return TraineeViewDto.builder()
                .firstName(dto.getFirstName())
                .lastName(dto.getLastName())
                .isActive(dto.isActive())
                .dateOfBirth(dto.getDateOfBirth())
                .address(dto.getAddress())
                .trainers(dto.getTrainers().stream().map(trainerDto -> TrainerListViewDto.builder()
                        .username(trainerDto.getUsername())
                        .specialization(trainerDto.getSpecialization())
                        .firstName(trainerDto.getFirstName())
                        .lastName(trainerDto.getLastName())
                        .build()
                ).collect(Collectors.toList()))
                .trainings(dto.getTrainings().stream().map(trainingDto -> TrainingViewDto.builder()
                        .traineeUsername(trainingDto.getTraineeDto().getUsername())
                        .trainerUsername(trainingDto.getTrainerDto().getUsername())
                        .trainingType(trainingDto.getTrainingType())
                        .trainingDuration(trainingDto.getTrainingDuration())
                        .trainingDateTime(trainingDto.getTrainingDate())
                        .trainingName(trainingDto.getTrainingName()).build()
                ).collect(Collectors.toList()))
                .build();
    }

}
