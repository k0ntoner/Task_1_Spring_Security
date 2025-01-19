package org.example.services.impl;

import lombok.extern.slf4j.Slf4j;
import org.example.models.TrainerDto;
import org.example.repositories.TrainerDao;
import org.example.repositories.entities.Trainer;
import org.example.services.TrainerService;
import org.example.utils.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

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

    @Override
    public TrainerDto add(TrainerDto trainerDto) {
        Trainer entity = UserUtils.convertTrainerDtoToEntity(trainerDto);
        log.info("Request to save trainer");

        if (!UserUtils.verifyPassword(entity.getPassword())) {
            throw new IllegalArgumentException("Invalid password");
        }

        entity.setUsername(UserUtils.generateUserName(entity, trainerDao::isUsernameExist));
        entity.setPassword(UserUtils.hashPassword(entity.getPassword()));

        Trainer savedTrainer = trainerDao.save(entity);

        return savedTrainer == null ? null : UserUtils.convertTrainerEntityToDto(savedTrainer);
    }

    @Override
    public Optional<TrainerDto> findById(long id) {
        log.info("Request to find trainer by ID: {}", id);
        return trainerDao.findById(id).map(UserUtils::convertTrainerEntityToDto);
    }

    @Override
    public Collection<TrainerDto> findAll() {
        log.info("Request to find all trainers");
        return trainerDao.findAll()
                .stream()
                .map(UserUtils::convertTrainerEntityToDto)
                .collect(Collectors.toList());
    }

    @Override
    public TrainerDto update(TrainerDto trainerDto) {
        Trainer entity = UserUtils.convertTrainerDtoToEntity(trainerDto);
        log.info("Request to update trainer with ID: {}", entity.getId());

        Trainer updatedTrainer = trainerDao.update(entity);

        return updatedTrainer == null ? null : UserUtils.convertTrainerEntityToDto(updatedTrainer);
    }

    @Override
    public Optional<TrainerDto> findByUsername(String username) {
        return trainerDao.findByUsername(username).map(UserUtils::convertTrainerEntityToDto);
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
        Trainer entity = UserUtils.convertTrainerDtoToEntity(trainerDto);
        entity.setActive(true);
        trainerDao.update(entity);
    }

    @Override
    public void deactivate(TrainerDto entityDto) {
        Trainer entity = UserUtils.convertTrainerDtoToEntity(entityDto);
        entity.setActive(false);
        trainerDao.update(entity);
    }

    @Override
    public Collection<TrainerDto> findTrainersNotAssignedToTrainee(String traineeUsername) {
        return trainerDao.findTrainersNotAssignedToTrainee(traineeUsername)
                .stream()
                .map(UserUtils::convertTrainerEntityToDto)
                .collect(Collectors.toList());
    }
}
