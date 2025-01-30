package org.example.converters.trainer;

import org.example.converters.training.TrainingDtoToEntityConverter;
import org.example.models.trainer.TrainerDto;
import org.example.repositories.entities.Trainer;
import org.example.repositories.entities.Training;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.stream.Collectors;

@Component
public class TrainerDtoToEntityConverter implements Converter<TrainerDto, Trainer> {
    @Autowired
    private TrainingDtoToEntityConverter trainingDtoToEntityConverter;

    @Override
    public Trainer convert(TrainerDto dto) {
        Trainer trainer = Trainer.builder()
                .id(dto.getId())
                .firstName(dto.getFirstName())
                .lastName(dto.getLastName())
                .username(dto.getUsername())
                .password(dto.getPassword())
                .isActive(dto.isActive())
                .specialization(dto.getSpecialization())
                .build();
        Collection<Training> trainings = dto.getTrainings().stream().map(trainingDtoToEntityConverter::convert).collect(Collectors.toList());
        trainer.setTrainings(trainings);
        return trainer;
    }

}