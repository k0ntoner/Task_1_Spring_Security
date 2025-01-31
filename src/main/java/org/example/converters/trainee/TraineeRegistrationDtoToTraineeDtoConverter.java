package org.example.converters.trainee;

import org.example.models.trainee.TraineeDto;
import org.example.models.trainee.TraineeRegistrationDto;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;


@Component
public class TraineeRegistrationDtoToTraineeDtoConverter implements Converter<TraineeRegistrationDto, TraineeDto> {
    @Override
    public TraineeDto convert(TraineeRegistrationDto traineeRegistrationDto) {
        return TraineeDto.builder()
                .firstName(traineeRegistrationDto.getFirstName())
                .lastName(traineeRegistrationDto.getLastName())
                .password(traineeRegistrationDto.getPassword())
                .dateOfBirth(traineeRegistrationDto.getDateOfBirth())
                .address(traineeRegistrationDto.getAddress())
                .build();

    }
}
