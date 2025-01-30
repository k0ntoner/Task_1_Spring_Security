package org.example.converters.trainer;

import org.example.models.trainee.TraineeDto;
import org.example.models.trainee.TraineeRegistrationDto;
import org.example.models.trainer.TrainerDto;
import org.example.models.trainer.TrainerRegistrationDto;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class TrainerRegistrationDtoToTrainerDtoConverter implements Converter<TrainerRegistrationDto, TrainerDto> {
    @Override
    public TrainerDto convert(TrainerRegistrationDto trainerRegistrationDto) {
        return TrainerDto.builder()
                .firstName(trainerRegistrationDto.getFirstName())
                .lastName(trainerRegistrationDto.getLastName())
                .password(trainerRegistrationDto.getPassword())
                .specialization(trainerRegistrationDto.getSpecialization())
                .build();
    }
}
