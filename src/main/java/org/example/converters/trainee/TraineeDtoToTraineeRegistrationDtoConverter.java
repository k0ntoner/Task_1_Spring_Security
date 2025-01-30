package org.example.converters.trainee;

import org.example.models.trainee.TraineeDto;
import org.example.models.trainee.TraineeRegistrationDto;
import org.example.models.trainer.TrainerRegistrationDto;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class TraineeDtoToTraineeRegistrationDtoConverter implements Converter<TraineeDto, TraineeRegistrationDto> {

    @Override
    public TraineeRegistrationDto convert(TraineeDto traineeDto) {
        return TraineeRegistrationDto.builder()
                .firstName(traineeDto.getFirstName())
                .lastName(traineeDto.getLastName())
                .password(traineeDto.getPassword())
                .address(traineeDto.getAddress())
                .dateOfBirth(traineeDto.getDateOfBirth())
                .build();
    }
}
