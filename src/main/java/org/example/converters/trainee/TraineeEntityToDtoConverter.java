package org.example.converters.trainee;

import org.example.converters.training.TrainingEntityToDtoConverter;
import org.example.models.trainee.TraineeDto;
import org.example.models.training.TrainingDto;
import org.example.repositories.entities.Trainee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.stream.Collectors;

@Component
public class TraineeEntityToDtoConverter implements Converter<Trainee, TraineeDto> {
    @Autowired
    private TrainingEntityToDtoConverter trainingEntityToDtoConverter;


    @Override
    public TraineeDto convert(Trainee entity) {
        TraineeDto traineeDto = TraineeDto.builder()
                .id(entity.getId())
                .firstName(entity.getFirstName())
                .lastName(entity.getLastName())
                .username(entity.getUsername())
                .password(entity.getPassword())
                .isActive(entity.isActive())
                .dateOfBirth(entity.getDateOfBirth())
                .address(entity.getAddress())
                .build();
        Collection<TrainingDto> trainingDtos = entity.getTrainings().stream().map(trainingEntityToDtoConverter::convert).collect(Collectors.toList());
        traineeDto.setTrainings(trainingDtos);
        return traineeDto;
    }
}