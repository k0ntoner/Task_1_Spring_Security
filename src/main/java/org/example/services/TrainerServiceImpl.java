package org.example.services;

import lombok.extern.slf4j.Slf4j;
import org.example.models.Trainer;
import org.example.repositories.UserDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class TrainerServiceImpl implements UserService<Trainer> {
    private UserDAO<Trainer> trainerDAO;
    @Autowired
    public TrainerServiceImpl(@Qualifier("trainerDAOImpl") UserDAO<Trainer> trainerDAO) {
        this.trainerDAO = trainerDAO;
    }
    @Override
    public Trainer add(Trainer entity) {
        return trainerDAO.add(entity);
    }

    @Override
    public Trainer findById(long id) {
        return trainerDAO.findById(id);
    }

    @Override
    public Map<Long, Trainer> findAll() {
        return trainerDAO.findAll();
    }

    @Override
    public boolean delete(long id) {
        return trainerDAO.delete(id);
    }

    @Override
    public Trainer update(long id, Trainer trainee) {
        return trainerDAO.update(id, trainee);
    }
}
