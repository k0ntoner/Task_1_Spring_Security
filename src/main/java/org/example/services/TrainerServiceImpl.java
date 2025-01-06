package org.example.services;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.NotImplementedException;
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
    private final UserDao<Trainer> trainerDao;

    @Autowired
    public TrainerServiceImpl(@Qualifier("trainerDaoImpl") UserDao<Trainer> trainerDao) {
        this.trainerDao = trainerDao;
        log.info("TrainerServiceImpl initialized");
    }

    @Override
    public Trainer add(Trainer entity) {
        log.info("Request to add trainer");
        entity.setUsername(UserUtils.generateUserName(entity, trainerDao::isUsernameExist));
        entity.setPassword(UserUtils.generatePassword());
        return trainerDao.add(entity);
    }

    @Override
    public Trainer findById(long id) {
        log.info("Request to find trainer by ID: {}", id);
        return trainerDao.findById(id);
    }

    @Override
    public Collection<Trainer> findAll() {
        log.info("Request to find all trainers");
        return trainerDao.findAll();
    }

    @Override
    public boolean delete(Trainer trainer) {
        throw new NotImplementedException();

    }

    @Override
    public Trainer update(Trainer trainer) {
        log.info("Request to update trainer with ID: {}", trainer.getUserId());
        if(trainerDao.findById(trainer.getUserId()) == null)
            throw new IllegalArgumentException("Trainer with id " + trainer.getUserId() + " not found");

        return trainerDao.update(trainer);
    }
}
