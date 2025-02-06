package org.example.repositories.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.PersistenceException;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.example.repositories.TrainerDao;
import org.example.repositories.entities.Trainee;
import org.example.repositories.entities.Trainer;
import org.example.repositories.entities.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
@Slf4j
public class TrainerDaoImpl implements TrainerDao {
    @PersistenceContext
    private EntityManager entityManager;


    @Override
    @Transactional
    public Trainer save(Trainer entity) {
        try {
            if (entity.getId() != null) {
                throw new IllegalArgumentException("Trainee must not have an id");
            }

            entityManager.persist(entity);
            log.info("Saved new Trainer: {}", entity);

            return entity;
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new PersistenceException("Error while saving Trainer",e);
        }
    }

    @Override
    @Transactional
    public Trainer update(Trainer entity) {
        try {
            if (findById(entity.getId()).isEmpty()) {
                throw new IllegalArgumentException("Trainer with id " + entity.getId() + " not found");
            }

            entity = entityManager.merge(entity);
            log.info("Updated Trainer: {}", entity);

            return entity;
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new PersistenceException("Error while updating Trainer",e);
        }
    }

    @Override
    public Optional<Trainer> findById(long id) {
        try {
            Trainer entity = entityManager.find(Trainer.class, id);
            log.info("Found Trainer: {}", entity);
            return Optional.of(entity);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return Optional.empty();

    }

    @Override
    public Collection<Trainer> findAll() {
        try {
            Collection<Trainer> entities = entityManager.createQuery("select t from Trainer t", Trainer.class).getResultList();
            log.info("Found {} Trainers", entities.size());
            return entities;
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return new ArrayList<>();
    }


    @Override
    public boolean isUsernameExist(String username) {
        try{
            User user = entityManager.createQuery("from User u where u.username=:username", User.class)
                    .setParameter("username", username).getSingleResult();
            return user != null;
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return false;
    }

    @Override
    public Optional<Trainer> findByUsername(String username) {
        try {
            Trainer entity = entityManager.createQuery(
                            "FROM Trainer t where t.username = :username", Trainer.class)
                    .setParameter("username", username)
                    .getSingleResult();

            return Optional.of(entity);

        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return Optional.empty();
    }

    @Override
    public Collection<Trainer> findTrainersNotAssignedToTrainee(String traineeUsername) {
        try{
            List<Trainer> trainers = entityManager.createQuery("from Trainer t " +
                            "where t.isActive=true and t.id not in (" +
                            "Select trainer.id " +
                            "from Training training " +
                            "join training.trainer trainer " +
                            "join training.trainee trainee " +
                            "where trainee.username!=:username )", Trainer.class)
                    .setParameter("username", traineeUsername)
                    .getResultList();

            return trainers;
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return new ArrayList<>();
    }

    @Override
    public Collection<Trainee> findTraineesByTrainerUsername(String username) {
        try {

            Collection<Trainee> trainees = entityManager.createQuery("Select distinct t.trainee from Training t where t.trainer.username=:username").setParameter("username", username).getResultList();
            Collection<Trainee> uniqueTrainees = new HashSet<>();

            trainees.forEach(trainee -> uniqueTrainees.add(trainee));

            log.info("Found {} Trainers by Trainee username", uniqueTrainees.size());
            return uniqueTrainees;

        } catch (PersistenceException e) {
            log.error(e.getMessage());
        }
        return new HashSet<>();
    }

}
