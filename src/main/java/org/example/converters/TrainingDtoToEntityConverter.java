package org.example.converters;

import org.example.models.TrainingDto;
import org.example.repositories.entities.Training;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.core.convert.converter.Converter;

@Component
public class TrainingDtoToEntityConverter implements Converter<TrainingDto, Training> {

    private final TrainerDtoToEntityConverter trainerConverter;
    private final TraineeDtoToEntityConverter traineeConverter;

    @Autowired
    public TrainingDtoToEntityConverter(TrainerDtoToEntityConverter trainerConverter, TraineeDtoToEntityConverter traineeConverter) {
        this.trainerConverter = trainerConverter;
        this.traineeConverter = traineeConverter;
    }

    @Override
    public Training convert(TrainingDto dto) {
        return Training.builder()
                .id(dto.getId())
                .trainer(trainerConverter.convert(dto.getTrainer()))
                .trainee(traineeConverter.convert(dto.getTrainee()))
                .trainingDate(dto.getTrainingDate())
                .trainingDuration(dto.getTrainingDuration())
                .trainingName(dto.getTrainingName())
                .trainingType(dto.getTrainingType())
                .build();
    }
}
