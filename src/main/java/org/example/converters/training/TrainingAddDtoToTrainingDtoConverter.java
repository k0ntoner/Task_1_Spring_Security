package org.example.converters.training;

import org.example.models.training.TrainingDto;
import org.example.models.training.TrainingAddDto;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class TrainingAddDtoToTrainingDtoConverter implements Converter<TrainingAddDto, TrainingDto> {
    @Override
    public TrainingDto convert(TrainingAddDto trainingAddDto) {
        return TrainingDto.builder()
                .trainerDto(trainingAddDto.getTrainerDto())
                .traineeDto(trainingAddDto.getTraineeDto())
                .trainingName(trainingAddDto.getTrainingName())
                .trainingType(trainingAddDto.getTrainingType())
                .trainingDate(trainingAddDto.getTrainingDate())
                .trainingDuration(trainingAddDto.getTrainingDuration())
                .build();
    }
}
