package org.example.converters;

import org.example.models.TraineeDto;
import org.example.repositories.entities.Trainee;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class TraineeEntityToDtoConverter implements Converter<Trainee, TraineeDto> {

    @Override
    public TraineeDto convert(Trainee entity) {
        return TraineeDto.builder()
                .id(entity.getId())
                .firstName(entity.getFirstName())
                .lastName(entity.getLastName())
                .username(entity.getUsername())
                .password(entity.getPassword())
                .isActive(entity.isActive())
                .dateOfBirth(entity.getDateOfBirth())
                .address(entity.getAddress())
                .build();
    }
}