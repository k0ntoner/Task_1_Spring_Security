package org.example.services.impl;

import lombok.extern.slf4j.Slf4j;

import org.example.models.trainee.TraineeDto;
import org.example.models.trainer.TrainerDto;
import org.example.repositories.TraineeDao;

import org.example.repositories.entities.Trainee;
import org.example.services.TraineeService;
import org.example.utils.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Collection;
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

        TraineeDto savedTraineeDto =conversionService.convert(savedTrainee, TraineeDto.class);
        savedTraineeDto.setTrainers(findTrainersByTraineeUsername(traineeDto.getUsername()));
        return savedTraineeDto;
    }

    @Override
    public Optional<TraineeDto> findById(long id) {
        log.info("Request to find trainee by ID: {}", id);
        Optional<Trainee> trainee = traineeDao.findById(id);
        if (trainee.isPresent()) {
            TraineeDto traineeDto = conversionService.convert(trainee.get(), TraineeDto.class);
            traineeDto.setTrainers(findTrainersByTraineeUsername(traineeDto.getUsername()));
            return Optional.of(traineeDto);
        }
        return Optional.empty();
    }

    @Override
    public Collection<TraineeDto> findAll() {
        log.info("Request to find all trainees");
        return traineeDao.findAll()
                .stream()
                .map(trainee -> {
                    TraineeDto traineeDto= conversionService.convert(trainee, TraineeDto.class);
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
    }


    @Override
    public TraineeDto update(TraineeDto traineeDto) {
        Trainee trainee = conversionService.convert(traineeDto, Trainee.class);
        log.info("Request to update trainee with ID: {}", trainee.getId());

        Trainee updatedTrainee = traineeDao.update(trainee);

        TraineeDto updatedTraineeDto =conversionService.convert(updatedTrainee, TraineeDto.class);
        updatedTraineeDto.setTrainers(findTrainersByTraineeUsername(traineeDto.getUsername()));
        return updatedTraineeDto;
    }

    @Override
    public void deleteByUsername(String username) {
        log.info("Request to delete trainee with username: {}", username);
        traineeDao.delete(traineeDao.findByUsername(username).get());
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

        TraineeDto savedTraineeDto =conversionService.convert(savedTrainee, TraineeDto.class);
        savedTraineeDto.setTrainers(findTrainersByTraineeUsername(traineeDto.getUsername()));
        return savedTraineeDto;
    }

    @Override
    public Optional<TraineeDto> findByUsername(String username) {
        log.info("Request to find trainee with username: {}", username);
        Optional<Trainee> trainee = traineeDao.findByUsername(username);
        if (trainee.isPresent()) {
            TraineeDto traineeDto = conversionService.convert(trainee.get(), TraineeDto.class);
            traineeDto.setTrainers(findTrainersByTraineeUsername(traineeDto.getUsername()));
            return Optional.of(traineeDto);
        }
        return Optional.empty();
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
    public TraineeDto matchPassword(String username, String password) {
        log.info("Request to match password");
        Optional<Trainee> trainee = traineeDao.findByUsername(username);
        if(trainee.isPresent()){
            boolean result = UserUtils.passwordMatch(password, trainee.get().getPassword());
            if(result){
                TraineeDto traineeDto= conversionService.convert(trainee.get(), TraineeDto.class);
                traineeDto.setTrainers(findTrainersByTraineeUsername(traineeDto.getUsername()));
                return traineeDto;
            }
            throw new IllegalArgumentException("Invalid password");
        }
        throw new IllegalArgumentException("Invalid username");
    }
    @Override
    public Collection<TrainerDto> findTrainersByTraineeUsername(String username) {
        log.info("Request to find trainers by Trainee username: {}", username);
        return traineeDao.findTrainersByTraineeUsername(username).stream().map(trainer -> conversionService.convert(trainer, TrainerDto.class)).collect(Collectors.toList());
    }

}