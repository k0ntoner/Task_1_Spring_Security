package org.example.converters.trainer;

import org.example.models.trainer.TrainerDto;
import org.example.models.trainer.TrainerViewDto;
import org.example.models.training.TrainingListViewDto;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class TrainerDtoToTrainerViewDtoConverter implements Converter<TrainerDto, TrainerViewDto> {
    @Override
    public TrainerViewDto convert(TrainerDto dto) {
        return TrainerViewDto.builder()
                .firstName(dto.getFirstName())
                .lastName(dto.getLastName())
                .isActive(dto.isActive())
                .specialization(dto.getSpecialization())
                .trainees(dto.getTrainees().stream().map(traineeDto -> TrainerViewDto.TraineeViewDto.builder()
                        .username(traineeDto.getUsername())
                        .firstName(traineeDto.getFirstName())
                        .lastName(traineeDto.getLastName())
                        .dateOfBirth(traineeDto.getDateOfBirth())
                        .address(traineeDto.getAddress())
                        .build()
                ).collect(Collectors.toList()))
                .trainings(dto.getTrainings().stream().map(trainingDto -> TrainingListViewDto.builder()
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
