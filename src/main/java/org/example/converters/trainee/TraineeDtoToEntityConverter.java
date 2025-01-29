package org.example.converters.trainee;

import org.example.converters.training.TrainingDtoToEntityConverter;
import org.example.models.trainee.TraineeDto;
import org.example.repositories.entities.Trainee;
import org.example.repositories.entities.Training;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.stream.Collectors;

@Component
public class TraineeDtoToEntityConverter implements Converter<TraineeDto, Trainee> {
    @Autowired
    private TrainingDtoToEntityConverter trainingDtoToEntityConverter;
    @Override
    public Trainee convert(TraineeDto dto) {
        Trainee trainee= Trainee.builder()
                .id(dto.getId())
                .firstName(dto.getFirstName())
                .lastName(dto.getLastName())
                .username(dto.getUsername())
                .password(dto.getPassword())
                .isActive(dto.isActive())
                .dateOfBirth(dto.getDateOfBirth())
                .address(dto.getAddress())
                .build();
        Collection<Training> trainings = dto.getTrainings().stream().map(trainingDtoToEntityConverter::convert).collect(Collectors.toList());
        trainee.setTrainings(trainings);
        return trainee;
    }

}