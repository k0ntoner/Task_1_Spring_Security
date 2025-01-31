package org.example.converters.training;

import org.example.models.training.TrainingDto;
import org.example.models.training.TrainingViewDto;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class TrainingDtoToTrainingViewDtoConverter implements Converter<TrainingDto, TrainingViewDto> {
    @Override
    public TrainingViewDto convert(TrainingDto trainingDto) {
        return TrainingViewDto.builder()
                .trainingType(trainingDto.getTrainingType())
                .trainingDateTime(trainingDto.getTrainingDate())
                .trainingDuration(trainingDto.getTrainingDuration())
                .trainingName(trainingDto.getTrainingName())
                .traineeUsername(trainingDto.getTraineeDto().getUsername())
                .trainerUsername(trainingDto.getTrainerDto().getUsername())
                .build();
    }
}
