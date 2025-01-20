package org.example.utils;

import lombok.extern.slf4j.Slf4j;

import org.example.models.TraineeDto;
import org.example.models.TrainerDto;
import org.example.models.TrainingDto;
import org.example.repositories.entities.Trainee;
import org.example.repositories.entities.Trainer;
import org.example.repositories.entities.Training;
import org.example.repositories.entities.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
@Slf4j
public class UserUtils {
    private static BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
    private static final int MIN_LENGTH = 4;
    private static final int MAX_LENGTH = 10;

    public static String generateUserName(User entity, Function<String, Boolean> isUserNameExist) {
        String userName = entity.getFirstName() + "." + entity.getLastName();
        String uniqueUserName = userName;
        long counter = 1;

        while (isUserNameExist.apply(uniqueUserName)) {
            uniqueUserName = userName + counter;
            counter++;
        }

        return uniqueUserName;
    }

    public static String hashPassword(String password) {
        return encoder.encode(password);
    }

    public static boolean passwordMatch(String password, String hashedPassword) {
        return encoder.matches(password, hashedPassword);
    }

    public static boolean verifyPassword(String password) {
        if (password != null) {
            if (password.length() >= MIN_LENGTH && password.length() <= MAX_LENGTH) {
                return true;
            }
        }
        log.error("Invalid password");
        return false;
    }

    public static Trainee convertTraineeDtoToEntity(TraineeDto dto) {
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

    public static Trainer convertTrainerDtoToEntity(TrainerDto dto) {
        return Trainer.builder()
                .id(dto.getId())
                .firstName(dto.getFirstName())
                .lastName(dto.getLastName())
                .username(dto.getUsername())
                .password(dto.getPassword())
                .isActive(dto.isActive())
                .specialization(dto.getSpecialization())
                .trainingType(dto.getTrainingType())
                .build();
    }

    public static Training convertTrainingDtoToEntity(TrainingDto dto) {
        return Training.builder()
                .id(dto.getId())
                .trainer(convertTrainerDtoToEntity(dto.getTrainer()))
                .trainee(convertTraineeDtoToEntity(dto.getTrainee()))
                .trainingDate(dto.getTrainingDate())
                .trainingDuration(dto.getTrainingDuration())
                .trainingName(dto.getTrainingName())
                .trainingType(dto.getTrainingType())
                .build();
    }

    public static TraineeDto convertTraineeEntityToDto(Trainee entity) {
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

    public static TrainerDto convertTrainerEntityToDto(Trainer entity) {
        return TrainerDto.builder()
                .id(entity.getId())
                .firstName(entity.getFirstName())
                .lastName(entity.getLastName())
                .username(entity.getUsername())
                .password(entity.getPassword())
                .isActive(entity.isActive())
                .specialization(entity.getSpecialization())
                .trainingType(entity.getTrainingType())
                .build();
    }

    public static TrainingDto convertTrainingEntityToDto(Training entity) {
        return TrainingDto.builder()
                .id(entity.getId())
                .trainer(convertTrainerEntityToDto(entity.getTrainer()))
                .trainee(convertTraineeEntityToDto(entity.getTrainee()))
                .trainingDate(entity.getTrainingDate())
                .trainingDuration(entity.getTrainingDuration())
                .trainingName(entity.getTrainingName())
                .trainingType(entity.getTrainingType())
                .build();
    }
}
