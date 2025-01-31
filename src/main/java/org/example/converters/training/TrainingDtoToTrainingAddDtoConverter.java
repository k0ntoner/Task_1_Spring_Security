package org.example.converters.training;

import org.example.models.training.TrainingAddDto;
import org.example.models.training.TrainingDto;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class TrainingDtoToTrainingAddDtoConverter implements Converter<TrainingDto, TrainingAddDto> {
    @Override
    public TrainingAddDto convert(TrainingDto trainingDto) {
        return TrainingAddDto.builder()
                .trainingDate(trainingDto.getTrainingDate())
                .trainingDuration(trainingDto.getTrainingDuration())
                .trainingName(trainingDto.getTrainingName())
                .trainingType(trainingDto.getTrainingType())
                .traineeUsername(trainingDto.getTraineeDto().getUsername())
                .trainerUsername(trainingDto.getTrainerDto().getUsername())
                .build();
    }
}
