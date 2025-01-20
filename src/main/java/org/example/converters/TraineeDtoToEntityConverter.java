package org.example.converters;

import org.example.models.TraineeDto;
import org.example.repositories.entities.Trainee;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class TraineeDtoToEntityConverter implements Converter<TraineeDto, Trainee> {

    @Override
    public Trainee convert(TraineeDto dto) {
        return Trainee.builder()
                .id(dto.getId())
                .firstName(dto.getFirstName())
                .lastName(dto.getLastName())
                .username(dto.getUsername())
                .password(dto.getPassword())
                .isActive(dto.isActive())
                .dateOfBirth(dto.getDateOfBirth())
                .address(dto.getAddress())
                .build();
    }
}