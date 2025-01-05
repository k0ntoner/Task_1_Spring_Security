package org.example.services;

import lombok.extern.slf4j.Slf4j;
import org.example.models.Trainer;
import org.example.repositories.UserDao;
import org.example.utils.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.Collection;

@Component
@Slf4j
public class TrainerServiceImpl implements UserService<Trainer> {
    private final UserDao<Trainer> trainerDAO;

    @Autowired
    public TrainerServiceImpl(@Qualifier("trainerDaoImpl") UserDao<Trainer> trainerDAO) {
        this.trainerDAO = trainerDAO;
        log.info("TrainerServiceImpl initialized");
    }

    @Override
    public Trainer add(Trainer entity) {
        log.info("Request to add trainer");
        entity.setUsername(UserUtils.generateUserName(entity, trainerDAO::isUsernameExist));
        entity.setPassword(UserUtils.generatePassword());
        return trainerDAO.add(entity);
    }

    @Override
    public Trainer findById(long id) {
        log.info("Request to find trainer by ID: {}", id);
        return trainerDAO.findById(id);
    }

    @Override
    public Collection<Trainer> findAll() {
        log.info("Request to find all trainers");
        return trainerDAO.findAll();
    }

    @Override
    public boolean delete(Trainer trainer) {
        log.info("Request to delete trainer with ID: {}", trainer.getUserId());
        if(trainerDAO.findById(trainer.getUserId()) == null)
            throw new IllegalArgumentException("Trainer with id " + trainer.getUserId() + " not found");

        return trainerDAO.delete(trainer);

    }

    @Override
    public Trainer update(Trainer trainer) {
        log.info("Request to update trainer with ID: {}", trainer.getUserId());
        if(trainerDAO.findById(trainer.getUserId()) == null)
            throw new IllegalArgumentException("Trainer with id " + trainer.getUserId() + " not found");

        return trainerDAO.update(trainer);
    }
}
