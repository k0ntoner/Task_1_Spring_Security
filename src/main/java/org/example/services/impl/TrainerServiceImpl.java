package org.example.services.impl;

import lombok.extern.slf4j.Slf4j;
import org.example.repositories.TrainerDao;
import org.example.repositories.entities.Trainer;
import org.example.services.TrainerService;
import org.example.utils.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;

@Service
@Slf4j
public class TrainerServiceImpl implements TrainerService {
    @Autowired
    @Qualifier("trainerDaoImpl")
    private TrainerDao trainerDao;

    @Override
    public Trainer add(Trainer entity) {
        log.info("Request to save trainer");
        if (!UserUtils.verifyPassword(entity.getPassword())) {
            throw new IllegalArgumentException("Invalid password");
        }
        entity.setUsername(UserUtils.generateUserName(entity, trainerDao::isUsernameExist));
        entity.setPassword(UserUtils.hashPassword(entity.getPassword()));
        return trainerDao.save(entity);
    }

    @Override
    public Optional<Trainer> findById(long id) {
        log.info("Request to find trainer by ID: {}", id);
        return trainerDao.findById(id);
    }

    @Override
    public Collection<Trainer> findAll() {
        log.info("Request to find all trainers");
        return trainerDao.findAll();
    }

    @Override
    public Trainer update(Trainer trainer) {
        log.info("Request to update trainer with ID: {}", trainer.getId());
        if (trainerDao.findById(trainer.getId()).isEmpty()) {
            throw new IllegalArgumentException("Trainer not found");
        }

        return trainerDao.update(trainer);
    }

    @Override
    public Optional<Trainer> findByUsername(String username) {
        return trainerDao.findByUsername(username);
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
    public void activate(Trainer entity) {
        if (trainerDao.findById(entity.getId()).isEmpty()) {
            throw new IllegalArgumentException("Invalid trainer");

        }
        entity.setActive(true);
        trainerDao.update(entity);
    }

    @Override
    public void deactivate(Trainer entity) {
        if (trainerDao.findById(entity.getId()).isEmpty()) {
            throw new IllegalArgumentException("Invalid trainer");

        }
        entity.setActive(false);
        trainerDao.update(entity);
    }

    @Override
    public Collection<Trainer> findTrainersNotAssignedToTrainee(String traineeUsername) {
        return trainerDao.findTrainersNotAssignedToTrainee(traineeUsername);
    }
}
