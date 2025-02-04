package org.example.repositories.impl;


import jakarta.persistence.PersistenceException;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.example.repositories.TrainingDao;
import org.example.repositories.entities.Training;
import org.example.enums.TrainingType;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.criteria.HibernateCriteriaBuilder;
import org.hibernate.query.criteria.JpaCriteriaQuery;
import org.hibernate.query.criteria.JpaPredicate;
import org.hibernate.query.criteria.JpaRoot;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.*;

@Repository
@Slf4j
public class TrainingDaoImpl implements TrainingDao {
    private SessionFactory sessionFactory;

    @Autowired
    public TrainingDaoImpl(@Qualifier("sessionFactory") SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    @Transactional
    public Training save(Training entity) {
        try {
            if (entity.getId() != null) {
                throw new IllegalArgumentException("Training must not have an id");
            }

            sessionFactory.getCurrentSession().persist(entity);
            log.info("Saved new Training: {}", entity);

            return entity;
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new PersistenceException("Error while saving Training", e);
        }
    }

    @Override
    public Optional<Training> findById(long id) {
        try (Session session = sessionFactory.openSession()) {
            Training entity = session.get(Training.class, id);
            log.info("Found Training: {}", entity);
            return Optional.of(entity);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return Optional.empty();
    }

    @Override
    public Collection<Training> findAll() {
        try (Session session = sessionFactory.openSession()) {
            Collection<Training> entities = session.createQuery("select t from Training t", Training.class).list();
            log.info("Found {} Trainings", entities.size());
            return entities;
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return new ArrayList<>();
    }

    @Override
    public Collection<Training> findByTrainer(String trainerUsername, LocalDateTime startDateTime, LocalDateTime endDateTime, String traineeUsername) {
        try (Session session = sessionFactory.openSession()) {

            HibernateCriteriaBuilder builder = session.getCriteriaBuilder();
            JpaCriteriaQuery<Training> criteriaQuery = builder.createQuery(Training.class);
            JpaRoot<Training> trainingRoot = criteriaQuery.from(Training.class);

            List<JpaPredicate> predicates = new ArrayList<>();

            if (trainerUsername == null) {
                log.info("Trainer username must be indicated");
                return new ArrayList<>();
            }

            predicates.add(builder.equal(trainingRoot.get("trainer").get("username"), trainerUsername));

            if (startDateTime != null) {
                predicates.add(builder.greaterThanOrEqualTo(trainingRoot.get("trainingDate"), startDateTime));
            }

            if (endDateTime != null) {
                predicates.add(builder.lessThanOrEqualTo(trainingRoot.get("trainingDate"), endDateTime));
            }

            if (traineeUsername != null) {
                predicates.add(builder.equal(trainingRoot.get("trainee").get("username"), traineeUsername));
            }

            criteriaQuery.select(trainingRoot).where(predicates.toArray(new JpaPredicate[0]));

            return session.createQuery(criteriaQuery).getResultList();

        }
    }

    @Override
    public Collection<Training> findByTrainee(String traineeUsername, LocalDateTime startDateTime, LocalDateTime endDateTime, String trainerUsername, TrainingType trainingType) {
        try (Session session = sessionFactory.openSession()) {

            HibernateCriteriaBuilder builder = session.getCriteriaBuilder();
            JpaCriteriaQuery<Training> criteriaQuery = builder.createQuery(Training.class);
            JpaRoot<Training> trainingRoot = criteriaQuery.from(Training.class);

            List<JpaPredicate> predicates = new ArrayList<>();

            if (traineeUsername == null) {
                log.info("Trainee username must be indicated");
                return new ArrayList<>();
            }

            predicates.add(builder.equal(trainingRoot.get("trainee").get("username"), traineeUsername));

            if (startDateTime != null) {
                predicates.add(builder.greaterThanOrEqualTo(trainingRoot.get("trainingDate"), startDateTime));
            }

            if (endDateTime != null) {
                predicates.add(builder.lessThanOrEqualTo(trainingRoot.get("trainingDate"), endDateTime));
            }

            if (trainerUsername != null) {
                predicates.add(builder.equal(trainingRoot.get("trainer").get("username"), trainerUsername));
            }

            if (trainingType != null) {
                predicates.add(builder.equal(trainingRoot.get("trainingType"), trainingType));
            }

            criteriaQuery.select(trainingRoot).where(predicates.toArray(new JpaPredicate[0]));

            return session.createQuery(criteriaQuery).getResultList();

        }
    }
}
