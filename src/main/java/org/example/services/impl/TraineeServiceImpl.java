package org.example.services.impl;

import lombok.extern.slf4j.Slf4j;

import org.example.metrics.UserRegistrationsMetric;
import org.example.models.trainee.TraineeDto;
import org.example.models.trainee.TraineeUpdateDto;
import org.example.models.trainer.TrainerDto;
import org.example.models.training.TrainingDto;
import org.example.models.training.TrainingListToUpdateDto;
import org.example.repositories.TraineeDao;

import org.example.repositories.TrainerDao;
import org.example.repositories.entities.Trainee;
import org.example.repositories.entities.Training;
import org.example.services.TraineeService;
import org.example.utils.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.convert.ConversionService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
@Slf4j
public class TraineeServiceImpl implements TraineeService {
    @Autowired
    @Qualifier("traineeDaoImpl")
    private TraineeDao traineeDao;

    @Autowired
    private ConversionService conversionService;

    @Autowired
    private UserRegistrationsMetric userRegistrationsMetric;

    @Autowired
    private TrainerDao trainerDao;

    @Override
    public TraineeDto add(TraineeDto traineeDto) {
        log.info("Request to save trainee");

        Trainee trainee = conversionService.convert(traineeDto, Trainee.class);

        if (!UserUtils.verifyPassword(trainee.getPassword())) {
            throw new IllegalArgumentException("Invalid password");
        }

        trainee.setPassword(UserUtils.hashPassword(trainee.getPassword()));
        trainee.setUsername(UserUtils.generateUserName(trainee, traineeDao::isUsernameExist));

        Trainee savedTrainee = traineeDao.save(trainee);

        TraineeDto savedTraineeDto = conversionService.convert(savedTrainee, TraineeDto.class);
        savedTraineeDto.setTrainers(findTrainersByTraineeUsername(traineeDto.getUsername()));

        userRegistrationsMetric.incrementUserRegistrations();

        return savedTraineeDto;
    }

    @Override
    public TraineeDto findById(long id) {
        log.info("Request to find trainee by ID: {}", id);
        Optional<Trainee> trainee = traineeDao.findById(id);
        if (trainee.isPresent()) {
            TraineeDto traineeDto = conversionService.convert(trainee.get(), TraineeDto.class);
            traineeDto.setTrainers(findTrainersByTraineeUsername(traineeDto.getUsername()));
            return traineeDto;
        }
        throw new UsernameNotFoundException("Trainee not found");
    }

    @Override
    public Collection<TraineeDto> findAll() {
        log.info("Request to find all trainees");
        return traineeDao.findAll()
                .stream()
                .map(trainee -> {
                    TraineeDto traineeDto = conversionService.convert(trainee, TraineeDto.class);
                    traineeDto.setTrainers(findTrainersByTraineeUsername(trainee.getUsername()));
                    return traineeDto;
                })
                .collect(Collectors.toList());

    }

    @Override
    public void delete(TraineeDto traineeDto) {
        Trainee trainee = conversionService.convert(traineeDto, Trainee.class);
        log.info("Request to delete trainee with ID: {}", trainee.getId());
        traineeDao.delete(trainee);
        userRegistrationsMetric.decrementUserRegistrations();
    }


    @Override
    public TraineeDto update(TraineeDto traineeDto) {
        log.info("Request to update trainee with username: {}", traineeDto.getUsername());
        Trainee trainee = conversionService.convert(traineeDto, Trainee.class);


        Trainee updatedTrainee = traineeDao.update(trainee);

        TraineeDto updatedTraineeDto = conversionService.convert(updatedTrainee, TraineeDto.class);
        updatedTraineeDto.setTrainers(findTrainersByTraineeUsername(traineeDto.getUsername()));
        return updatedTraineeDto;
    }

    @Override
    public TraineeDto update(String username, TraineeUpdateDto traineeUpdateDto) {
        log.info("Request to update trainee with username: {}", username);
        TraineeDto traineeDtoFromDb = findByUsername(username);

        traineeDtoFromDb.setFirstName(traineeUpdateDto.getFirstName());
        traineeDtoFromDb.setLastName(traineeUpdateDto.getLastName());
        if (traineeUpdateDto.getAddress() != null) {
            traineeDtoFromDb.setAddress(traineeUpdateDto.getAddress());
        }
        if (traineeUpdateDto.getDateOfBirth() != null) {
            traineeDtoFromDb.setDateOfBirth(traineeUpdateDto.getDateOfBirth());
        }

        Trainee updatedTrainee = traineeDao.update(conversionService.convert(traineeDtoFromDb, Trainee.class));

        TraineeDto updatedTraineeDto = conversionService.convert(updatedTrainee, TraineeDto.class);
        updatedTraineeDto.setTrainers(findTrainersByTraineeUsername(traineeDtoFromDb.getUsername()));
        return updatedTraineeDto;
    }

    @Override
    public TraineeDto updateTraineeTrainings(String username, TrainingListToUpdateDto listToUpdateDto) {
        Optional<Trainee> optionalTrainee = traineeDao.findByUsername(username);
        if (optionalTrainee.isPresent()) {
            Trainee trainee = optionalTrainee.get();
            trainee.getTrainings().clear();

            for (TrainingListToUpdateDto.TrainingUpdateDto trainingUpdateDto : listToUpdateDto.getTrainings()) {
                if (!trainingUpdateDto.getTraineeUsername().equals(username)) {
                    throw new IllegalArgumentException("Trainee's username in listToUpdateDto does not match trainee's username");
                }
                trainee.getTrainings().add(
                        Training.builder()
                                .id(trainingUpdateDto.getId())
                                .trainee(traineeDao.findByUsername(trainingUpdateDto.getTraineeUsername()).get())
                                .trainer(trainerDao.findByUsername(trainingUpdateDto.getTrainerUsername()).get())
                                .trainingName(trainingUpdateDto.getTrainingName())
                                .trainingType(trainingUpdateDto.getTrainingType())
                                .trainingDate(trainingUpdateDto.getTrainingDate())
                                .trainingDuration(trainingUpdateDto.getTrainingDuration())
                                .build()
                );
            }
            trainee = traineeDao.update(trainee);
            return conversionService.convert(trainee, TraineeDto.class);
        }
        throw new UsernameNotFoundException("Trainee not found");
    }

    @Override
    public void deleteByUsername(String username) {
        log.info("Request to delete trainee with username: {}", username);
        traineeDao.delete(traineeDao.findByUsername(username).get());
        userRegistrationsMetric.decrementUserRegistrations();
    }

    @Override
    public TraineeDto add(Long id, String firstName, String lastName, String username, String password, boolean isActive, LocalDate dateOfBirth, String address) {
        log.info("Request to save trainee");
        TraineeDto traineeDto = TraineeDto.builder()
                .id(id)
                .firstName(firstName)
                .lastName(lastName)
                .username(username)
                .password(password)
                .isActive(isActive)
                .dateOfBirth(dateOfBirth)
                .address(address)
                .build();

        Trainee trainee = conversionService.convert(traineeDto, Trainee.class);

        if (!UserUtils.verifyPassword(trainee.getPassword())) {
            throw new IllegalArgumentException("Invalid password");
        }

        trainee.setPassword(UserUtils.hashPassword(trainee.getPassword()));
        trainee.setUsername(UserUtils.generateUserName(trainee, traineeDao::isUsernameExist));

        Trainee savedTrainee = traineeDao.save(trainee);

        userRegistrationsMetric.incrementUserRegistrations();

        TraineeDto savedTraineeDto = conversionService.convert(savedTrainee, TraineeDto.class);
        savedTraineeDto.setTrainers(findTrainersByTraineeUsername(traineeDto.getUsername()));
        return savedTraineeDto;
    }

    @Override
    public TraineeDto findByUsername(String username) {
        log.info("Request to find trainee with username: {}", username);
        Optional<Trainee> trainee = traineeDao.findByUsername(username);
        if (trainee.isPresent()) {
            TraineeDto traineeDto = conversionService.convert(trainee.get(), TraineeDto.class);
            traineeDto.setTrainers(findTrainersByTraineeUsername(traineeDto.getUsername()));
            return traineeDto;
        }
        throw new UsernameNotFoundException("Trainee not found");
    }

    @Override
    public void changePassword(String username, String oldPassword, String newPassword) {
        log.info("Request to change password for trainee with username: {}", username);
        if (traineeDao.findByUsername(username).isPresent()) {
            Trainee trainee = traineeDao.findByUsername(username).get();

            if (UserUtils.passwordMatch(oldPassword, trainee.getPassword())) {
                trainee.setPassword(UserUtils.hashPassword(newPassword));
                traineeDao.update(trainee);
                return;
            }

            throw new IllegalArgumentException("Invalid password");
        }
        throw new IllegalArgumentException("Invalid username");
    }

    @Override
    public void activate(TraineeDto traineeDto) {
        log.info("Request to activate trainee with ID: {}", traineeDto.getId());
        Trainee entity = conversionService.convert(traineeDto, Trainee.class);
        entity.setActive(true);
        traineeDao.update(entity);
    }

    @Override
    public void deactivate(TraineeDto traineeDto) {
        log.info("Request to deactivate trainee with ID: {}", traineeDto.getId());
        Trainee entity = conversionService.convert(traineeDto, Trainee.class);
        entity.setActive(false);
        traineeDao.update(entity);
    }

    @Override
    public boolean matchPassword(String username, String password) {
        log.info("Request to match password");
        Optional<Trainee> trainee = traineeDao.findByUsername(username);
        if (trainee.isPresent()) {
            return UserUtils.passwordMatch(password, trainee.get().getPassword());
        }
        throw new IllegalArgumentException("Invalid username");
    }

    @Override
    public Collection<TrainerDto> findTrainersByTraineeUsername(String username) {
        log.info("Request to find trainers by Trainee username: {}", username);
        return traineeDao.findTrainersByTraineeUsername(username).stream().map(trainer -> conversionService.convert(trainer, TrainerDto.class)).collect(Collectors.toList());
    }

}