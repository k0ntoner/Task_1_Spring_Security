package org.example.converters;

import org.example.models.TrainingDto;
import org.example.repositories.entities.Training;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class TrainingEntityToDtoConverter implements Converter<Training, TrainingDto> {

    private final TrainerEntityToDtoConverter trainerConverter;
    private final TraineeEntityToDtoConverter traineeConverter;

    @Autowired
    public TrainingEntityToDtoConverter(TrainerEntityToDtoConverter trainerConverter, TraineeEntityToDtoConverter traineeConverter) {
        this.trainerConverter = trainerConverter;
        this.traineeConverter = traineeConverter;
    }

    @Override
    public TrainingDto convert(Training entity) {
        return TrainingDto.builder()
                .id(entity.getId())
                .trainer(trainerConverter.convert(entity.getTrainer()))
                .trainee(traineeConverter.convert(entity.getTrainee()))
                .trainingDate(entity.getTrainingDate())
                .trainingDuration(entity.getTrainingDuration())
                .trainingName(entity.getTrainingName())
                .trainingType(entity.getTrainingType())
                .build();
    }
}
