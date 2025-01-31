package org.example.repositories.impl;


import jakarta.persistence.PersistenceException;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

import org.example.repositories.TraineeDao;
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
import java.util.stream.Collectors;

@Repository
@Slf4j
public class TraineeDaoImpl implements TraineeDao {
    private SessionFactory sessionFactory;

    @Autowired
    public TraineeDaoImpl(@Qualifier("sessionFactory") SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    @Transactional
    public Trainee save(Trainee entity) {
        try {
            if (entity.getId() != null) {
                throw new IllegalArgumentException("Trainee must not have an id");
            }

            sessionFactory.getCurrentSession().persist(entity);
            log.info("Saved new Trainee: {}", entity);
            return entity;
        } catch (Exception e) {
            log.error(e.getMessage());
            throw e;
        }
    }

    @Override
    @Transactional
    public Trainee update(Trainee entity) {
        try {
            if (findById(entity.getId()).isEmpty()) {
                throw new IllegalArgumentException("Trainee with id " + entity.getId() + " not found");
            }

            entity = sessionFactory.getCurrentSession().merge(entity);
            log.info("Updated Trainee: {}", entity);
            return entity;
        } catch (Exception e) {
            log.error(e.getMessage());
            throw e;
        }
    }

    @Override
    public Optional<Trainee> findById(long id) {
        try (Session session = sessionFactory.openSession()) {
            Trainee entity = session.get(Trainee.class, id);
            log.info("Found Trainee: {}", entity);
            return Optional.of(entity);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return Optional.empty();
    }

    @Override
    public Collection<Trainee> findAll() {
        try (Session session = sessionFactory.openSession()) {
            Collection<Trainee> entities = session.createQuery("select t from Trainee t", Trainee.class).list();
            log.info("Found {} Trainees", entities.size());
            return entities;
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return new ArrayList<>();
    }

    @Override
    @Transactional
    public void delete(Trainee entity) {
        try {
            Session session = sessionFactory.getCurrentSession();

            if (!session.contains(entity)) {
                entity = session.merge(entity);
            }

            session.remove(entity);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    @Override
    @Transactional
    public Collection<Trainer> findTrainersByTraineeUsername(String username) {
        try {
            Session session = sessionFactory.getCurrentSession();

            Collection<Trainer> trainers = session.createQuery("Select distinct t.trainer from Training t where t.trainee.username=:username").setParameter("username", username).list();
            Collection<Trainer> uniqueTrainers = new HashSet<>();

            trainers.forEach(trainer -> uniqueTrainers.add(trainer));

            log.info("Found {} Trainers by Trainee username", uniqueTrainers.size());
            return uniqueTrainers;

        } catch (PersistenceException e) {
            log.error(e.getMessage());
        }
        return new HashSet<>();
    }


    @Override
    public boolean isUsernameExist(String username) {
        try (Session session = sessionFactory.openSession()) {
            User user = session.createQuery("from User u where u.username=:username", User.class).setParameter("username", username).uniqueResult();
            return user != null;
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return false;
    }

    @Override
    public Optional<Trainee> findByUsername(String username) {
        try (Session session = sessionFactory.openSession()) {
            Trainee entity = session.createQuery(
                            "FROM Trainee t where t.username = :username", Trainee.class)
                    .setParameter("username", username)
                    .uniqueResult();

            return Optional.of(entity);

        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return Optional.empty();
    }

}
