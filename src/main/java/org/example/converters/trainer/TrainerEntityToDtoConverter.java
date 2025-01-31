package org.example.converters.trainer;

import org.example.converters.training.TrainingEntityToDtoConverter;
import org.example.models.trainer.TrainerDto;
import org.example.models.training.TrainingDto;
import org.example.repositories.entities.Trainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.stream.Collectors;

@Component
public class TrainerEntityToDtoConverter implements Converter<Trainer, TrainerDto> {
    @Autowired
    private TrainingEntityToDtoConverter trainingEntityToDtoConverter;

    @Override
    public TrainerDto convert(Trainer entity) {
        TrainerDto trainerDto = TrainerDto.builder()
                .id(entity.getId())
                .firstName(entity.getFirstName())
                .lastName(entity.getLastName())
                .username(entity.getUsername())
                .password(entity.getPassword())
                .isActive(entity.isActive())
                .specialization(entity.getSpecialization())
                .build();

        Collection<TrainingDto> trainingDtos = entity.getTrainings().stream().map(training -> trainingEntityToDtoConverter.convert(training)).collect(Collectors.toList());
        trainerDto.setTrainings(trainingDtos);

        return trainerDto;
    }

}
