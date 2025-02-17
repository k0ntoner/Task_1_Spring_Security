package org.example.services.impl;

import lombok.extern.slf4j.Slf4j;
import org.example.enums.TrainingType;
import org.example.metrics.UserRegistrationsMetric;
import org.example.models.trainee.TraineeDto;
import org.example.models.trainer.TrainerDto;
import org.example.models.trainer.TrainerUpdateDto;
import org.example.repositories.TraineeDao;
import org.example.repositories.TrainerDao;
import org.example.repositories.entities.Trainer;
import org.example.services.TrainerService;
import org.example.utils.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class TrainerServiceImpl implements TrainerService {
    @Autowired
    @Qualifier("trainerDaoImpl")
    private TrainerDao trainerDao;

    @Autowired
    private ConversionService conversionService;

    @Autowired
    private UserRegistrationsMetric userRegistrationsMetric;

    @Autowired
    private TraineeDao traineeDao;

    @Override
    public TrainerDto add(TrainerDto trainerDto) {
        Trainer entity = conversionService.convert(trainerDto, Trainer.class);
        log.info("Request to save trainer");

        if (!UserUtils.verifyPassword(entity.getPassword())) {
            throw new IllegalArgumentException("Invalid password");
        }

        entity.setUsername(UserUtils.generateUserName(entity, trainerDao::isUsernameExist));
        entity.setPassword(UserUtils.hashPassword(entity.getPassword()));

        Trainer savedTrainer = trainerDao.save(entity);

        userRegistrationsMetric.incrementUserRegistrations();

        TrainerDto savedTrainerDto = conversionService.convert(savedTrainer, TrainerDto.class);
        savedTrainerDto.setTrainees(findTraineesByTrainerUsername(savedTrainer.getUsername()));
        return savedTrainerDto;

    }

    @Override
    public TrainerDto findById(long id) {
        log.info("Request to find trainer by ID: {}", id);
        Optional<Trainer> trainer = trainerDao.findById(id);
        if(trainer.isPresent()) {
            return conversionService.convert(trainer.get(), TrainerDto.class);
        }
        throw new IllegalArgumentException("Trainer with ID " + id + " not found");
    }

    @Override
    public Collection<TrainerDto> findAll() {
        log.info("Request to find all trainers");
        return trainerDao.findAll()
                .stream()
                .map(trainer -> conversionService.convert(trainer, TrainerDto.class))
                .map(trainerDto -> {
                    trainerDto.setTrainees(findTraineesByTrainerUsername(trainerDto.getUsername()));
                    return trainerDto;
                })
                .collect(Collectors.toList());
    }

    @Override
    public TrainerDto update(TrainerDto trainerDto) {
        Trainer entity = conversionService.convert(trainerDto, Trainer.class);
        log.info("Request to update trainer with ID: {}", entity.getId());

        Trainer updatedTrainer = trainerDao.update(entity);

        TrainerDto updatedTrainerDto = conversionService.convert(updatedTrainer, TrainerDto.class);

        updatedTrainerDto.setTrainees(findTraineesByTrainerUsername(updatedTrainer.getUsername()));

        return updatedTrainerDto;
    }

    @Override
    public TrainerDto update(String username, TrainerUpdateDto trainerUpdateDto) {
        log.info("Request to update trainer with username: {}", username);
        TrainerDto trainerDtoFromDb = findByUsername(username);

        trainerDtoFromDb.setFirstName(trainerUpdateDto.getFirstName());
        trainerDtoFromDb.setLastName(trainerUpdateDto.getLastName());
        trainerDtoFromDb.setActive(trainerUpdateDto.isActive());

        Trainer updatedTrainer = trainerDao.update(conversionService.convert(trainerDtoFromDb, Trainer.class));

        TrainerDto updatedTrainerDto = conversionService.convert(updatedTrainer, TrainerDto.class);
        updatedTrainerDto.setTrainees(findTraineesByTrainerUsername(trainerDtoFromDb.getUsername()));
        return updatedTrainerDto;
    }

    @Override
    public TrainerDto findByUsername(String username) {
        log.info("Request to find trainer by username: {}", username);
        Optional<Trainer> trainer = trainerDao.findByUsername(username);
        if(trainer.isPresent()) {
            return conversionService.convert(trainer.get(), TrainerDto.class);
        }
        throw new IllegalArgumentException("Trainer with username " + username + " not found");
    }

    @Override
    public void changePassword(String username, String oldPassword, String newPassword) {
        log.info("Request to change password for trainer: {}", username);
        if (trainerDao.findByUsername(username).isPresent()) {
            Trainer trainer = trainerDao.findByUsername(username).get();

            if (UserUtils.passwordMatch(oldPassword, trainer.getPassword())) {
                trainer.setPassword(UserUtils.hashPassword(newPassword));
                trainerDao.update(trainer);
                return;
            }

            throw new IllegalArgumentException("Invalid password");
        }
        throw new IllegalArgumentException("Invalid username");
    }

    @Override
    public void activate(TrainerDto trainerDto) {
        log.info("Request to activate trainer: {}", trainerDto.getUsername());
        Trainer entity = conversionService.convert(trainerDto, Trainer.class);
        entity.setActive(true);
        trainerDao.update(entity);
    }

    @Override
    public void deactivate(TrainerDto entityDto) {
        log.info("Request to deactivate trainer: {}", entityDto.getUsername());
        Trainer entity = conversionService.convert(entityDto, Trainer.class);
        entity.setActive(false);
        trainerDao.update(entity);
    }

    @Override
    public Collection<TrainerDto> findTrainersNotAssignedToTrainee(String traineeUsername) {
        log.info("Request to find trainers not assigned to trainee: {}", traineeUsername);

        if(!traineeDao.findByUsername(traineeUsername).isPresent()) {
            throw new IllegalArgumentException("Trainee " + traineeUsername + " not found");
        }

        return trainerDao.findTrainersNotAssignedToTrainee(traineeUsername)
                .stream()
                .map(trainer -> conversionService.convert(trainer, TrainerDto.class))
                .map(trainerDto -> {
                    trainerDto.setTrainees(findTraineesByTrainerUsername(trainerDto.getUsername()));
                    return trainerDto;
                })
                .collect(Collectors.toList());
    }

    @Override
    public TrainerDto add(Long id, String firstName, String lastName, String username, String password, boolean isActive, TrainingType specialization) {
        TrainerDto trainerDto = TrainerDto.builder()
                .id(id)
                .firstName(firstName)
                .lastName(lastName)
                .username(username)
                .password(password)
                .isActive(isActive)
                .specialization(specialization)
                .build();

        Trainer entity = conversionService.convert(trainerDto, Trainer.class);
        log.info("Request to save trainer");

        if (!UserUtils.verifyPassword(entity.getPassword())) {
            throw new IllegalArgumentException("Invalid password");
        }

        entity.setUsername(UserUtils.generateUserName(entity, trainerDao::isUsernameExist));
        entity.setPassword(UserUtils.hashPassword(entity.getPassword()));

        Trainer savedTrainer = trainerDao.save(entity);

        userRegistrationsMetric.incrementUserRegistrations();

        TrainerDto savedTrainerDto = conversionService.convert(savedTrainer, TrainerDto.class);
        savedTrainerDto.setTrainees(findTraineesByTrainerUsername(savedTrainer.getUsername()));
        return savedTrainerDto;
    }

    @Override
    public boolean matchPassword(String username, String password) {
        log.info("Request to match password");
        Optional<Trainer> trainer = trainerDao.findByUsername(username);
        if (trainer.isPresent()) {
            return UserUtils.passwordMatch(password, trainer.get().getPassword());
        }
        throw new IllegalArgumentException("Invalid username");
    }

    @Override
    public Collection<TraineeDto> findTraineesByTrainerUsername(String username) {
        log.info("Request to find trainees by Trainer username: {}", username);
        return trainerDao.findTraineesByTrainerUsername(username).stream().map(trainee -> conversionService.convert(trainee, TraineeDto.class)).collect(Collectors.toList());
    }
}
