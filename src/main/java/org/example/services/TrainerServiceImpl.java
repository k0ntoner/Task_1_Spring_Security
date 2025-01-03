package org.example.services;

import lombok.extern.slf4j.Slf4j;
import org.example.models.Trainer;
import org.example.repositories.UserDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import java.util.Map;

@Component
@Slf4j
public class TrainerServiceImpl implements UserService<Trainer> {
    private final UserDAO<Trainer> trainerDAO;

    @Autowired
    public TrainerServiceImpl(@Qualifier("trainerDAOImpl") UserDAO<Trainer> trainerDAO) {
        this.trainerDAO = trainerDAO;
        log.info("TrainerServiceImpl initialized");
    }

    @Override
    public Trainer add(Trainer entity) {
        log.info("Request to add trainer: {}", entity.getUsername());
        Trainer addedTrainer = trainerDAO.add(entity);
        log.info("Service completed action successfully");
        return addedTrainer;
    }

    @Override
    public Trainer findById(long id) {
        log.info("Request to find trainer by ID: {}", id);
        Trainer trainer = trainerDAO.findById(id);
        if (trainer != null) {
            log.info("Service completed action successfully");
        } else {
            log.warn("Service completed action unsuccessfully");
        }
        return trainer;
    }

    @Override
    public Map<Long, Trainer> findAll() {
        log.info("Request to find all trainers");
        Map<Long, Trainer> trainers = trainerDAO.findAll();
        log.info("Service completed action successfully");
        return trainers;
    }

    @Override
    public boolean delete(long id) {
        log.info("Request to delete trainer with ID: {}", id);
        boolean result = trainerDAO.delete(id);
        if (result) {
            log.info("Service completed action successfully");
        } else {
            log.warn("Service completed action unsuccessfully");
        }
        return result;
    }

    @Override
    public Trainer update(long id, Trainer trainer) {
        log.info("Request to update trainer with ID: {}", id);
        Trainer updatedTrainer = trainerDAO.update(id, trainer);
        log.info("Service completed action successfully");
        return updatedTrainer;
    }
}
