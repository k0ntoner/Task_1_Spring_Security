package org.example.converters;

import org.example.models.TrainerDto;
import org.example.repositories.entities.Trainer;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class TrainerDtoToEntityConverter implements Converter<TrainerDto, Trainer> {

    @Override
    public Trainer convert(TrainerDto dto) {
        return Trainer.builder()
                .id(dto.getId())
                .firstName(dto.getFirstName())
                .lastName(dto.getLastName())
                .username(dto.getUsername())
                .password(dto.getPassword())
                .isActive(dto.isActive())
                .specialization(dto.getSpecialization())
                .build();
    }
}