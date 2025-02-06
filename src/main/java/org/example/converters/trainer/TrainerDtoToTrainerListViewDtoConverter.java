package org.example.converters.trainer;

import org.example.models.trainer.TrainerDto;
import org.example.models.trainer.TrainerListViewDto;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class TrainerDtoToTrainerListViewDtoConverter implements Converter<TrainerDto, TrainerListViewDto> {
    @Override
    public TrainerListViewDto convert(TrainerDto source) {
        return TrainerListViewDto.builder()
                .specialization(source.getSpecialization())
                .username(source.getUsername())
                .firstName(source.getFirstName())
                .lastName(source.getLastName())
                .build();
    }
}
