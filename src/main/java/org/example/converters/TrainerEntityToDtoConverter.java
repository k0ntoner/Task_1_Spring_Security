package org.example.converters;

import org.example.models.TrainerDto;
import org.example.repositories.entities.Trainer;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class TrainerEntityToDtoConverter implements Converter<Trainer, TrainerDto> {

    @Override
    public TrainerDto convert(Trainer entity) {
        return TrainerDto.builder()
                .id(entity.getId())
                .firstName(entity.getFirstName())
                .lastName(entity.getLastName())
                .username(entity.getUsername())
                .password(entity.getPassword())
                .isActive(entity.isActive())
                .specialization(entity.getSpecialization())
                .build();
    }
}
