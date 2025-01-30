package org.example.converters.trainer;

import org.example.models.trainee.TraineeRegistrationDto;
import org.example.models.trainer.TrainerDto;
import org.example.models.trainer.TrainerRegistrationDto;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class TrainerDtoToTrainerRegistrationDtoConverter implements Converter<TrainerDto, TrainerRegistrationDto> {
    @Override
    public TrainerRegistrationDto convert(TrainerDto trainerDto) {
        return TrainerRegistrationDto.builder()
                .firstName(trainerDto.getFirstName())
                .lastName(trainerDto.getLastName())
                .password(trainerDto.getPassword())
                .specialization(trainerDto.getSpecialization())
                .build();
    }
}
