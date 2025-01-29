package org.example.services.impl;

import lombok.extern.slf4j.Slf4j;
import org.example.enums.TrainingType;
import org.example.models.trainee.TraineeDto;
import org.example.models.trainer.TrainerDto;
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

        TrainerDto savedTrainerDto= conversionService.convert(savedTrainer, TrainerDto.class);
        savedTrainerDto.setTrainees(findTraineesByTrainerUsername(savedTrainer.getUsername()));
        return savedTrainerDto;

    }

    @Override
    public Optional<TrainerDto> findById(long id) {
        log.info("Request to find trainer by ID: {}", id);
        return trainerDao.findById(id)
                .map(trainer -> conversionService.convert(trainer, TrainerDto.class))
                .map(trainerDto -> {
                    trainerDto.setTrainees(findTraineesByTrainerUsername(trainerDto.getUsername()));
                    return trainerDto;
                });
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

        TrainerDto updatedTrainerDto= conversionService.convert(updatedTrainer, TrainerDto.class);

        updatedTrainerDto.setTrainees(findTraineesByTrainerUsername(updatedTrainer.getUsername()));

        return updatedTrainerDto;
    }

    @Override
    public Optional<TrainerDto> findByUsername(String username) {
        return trainerDao.findByUsername(username).map(trainer -> conversionService.convert(trainer, TrainerDto.class))
                .map(trainerDto -> {
                    trainerDto.setTrainees(findTraineesByTrainerUsername(trainerDto.getUsername()));
                    return trainerDto;
                });
    }

    @Override
    public void changePassword(String username, String oldPassword, String newPassword) {
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
        Trainer entity = conversionService.convert(trainerDto, Trainer.class);
        entity.setActive(true);
        trainerDao.update(entity);
    }

    @Override
    public void deactivate(TrainerDto entityDto) {
        Trainer entity = conversionService.convert(entityDto, Trainer.class);
        entity.setActive(false);
        trainerDao.update(entity);
    }

    @Override
    public Collection<TrainerDto> findTrainersNotAssignedToTrainee(String traineeUsername) {
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

        TrainerDto savedTrainerDto= conversionService.convert(savedTrainer, TrainerDto.class);
        savedTrainerDto.setTrainees(findTraineesByTrainerUsername(savedTrainer.getUsername()));
        return savedTrainerDto;
    }

    @Override
    public TrainerDto matchPassword(String username, String password){
        log.info("Request to match password");
        Optional<Trainer> trainer = trainerDao.findByUsername(username);
        if(trainer.isPresent()){
            boolean result = UserUtils.passwordMatch(password, trainer.get().getPassword());
            if(result){
                TrainerDto trainerDto= conversionService.convert(trainer.get(), TrainerDto.class);
                trainerDto.setTrainees(findTraineesByTrainerUsername(trainer.get().getUsername()));
                return trainerDto;
            }
            throw new IllegalArgumentException("Invalid password");
        }
        throw new IllegalArgumentException("Invalid username");
    }

    @Override
    public Collection<TraineeDto> findTraineesByTrainerUsername(String username) {
        log.info("Request to find trainees by Trainer username: {}", username);
        return trainerDao.findTraineesByTrainerUsername(username).stream().map(trainee -> conversionService.convert(trainee, TraineeDto.class)).collect(Collectors.toList());
    }
}
