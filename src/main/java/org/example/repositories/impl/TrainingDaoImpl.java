package org.example.repositories.impl;


import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.PersistenceException;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
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

    @PersistenceContext
    private EntityManager entityManager;


    @Override
    @Transactional
    public Training save(Training entity) {
        try {
            if (entity.getId() != null) {
                throw new IllegalArgumentException("Training must not have an id");
            }

            entityManager.persist(entity);
            log.info("Saved new Training: {}", entity);

            return entity;
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new PersistenceException("Error while saving Training", e);
        }
    }

    @Override
    public Optional<Training> findById(long id) {
        try {
            Training entity = entityManager.find(Training.class, id);
            log.info("Found Training: {}", entity);
            return Optional.of(entity);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return Optional.empty();
    }

    @Override
    public Collection<Training> findAll() {
        try {
            Collection<Training> entities = entityManager.createQuery("select t from Training t", Training.class).getResultList();
            log.info("Found {} Trainings", entities.size());
            return entities;
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return new ArrayList<>();
    }

    @Override
    public Collection<Training> findByTrainer(String trainerUsername, LocalDateTime startDateTime, LocalDateTime endDateTime, String traineeUsername) {
        try {

            CriteriaBuilder builder = entityManager.getCriteriaBuilder();
            CriteriaQuery<Training> criteriaQuery = builder.createQuery(Training.class);
            Root<Training> trainingRoot = criteriaQuery.from(Training.class);

            List<Predicate> predicates = new ArrayList<>();

            if (trainerUsername == null) {
                throw new IllegalArgumentException("Trainer username must be indicated");
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

            return entityManager.createQuery(criteriaQuery).getResultList();

        }
        catch (Exception e) {
            log.error(e.getMessage());
            return new ArrayList<>();
        }
    }

    @Override
    public Collection<Training> findByTrainee(String traineeUsername, LocalDateTime startDateTime, LocalDateTime endDateTime, String trainerUsername, TrainingType trainingType) {
        try {

            CriteriaBuilder builder = entityManager.getCriteriaBuilder();
            CriteriaQuery<Training> criteriaQuery = builder.createQuery(Training.class);
            Root<Training> trainingRoot = criteriaQuery.from(Training.class);

            List<Predicate> predicates = new ArrayList<>();

            if (traineeUsername == null) {
                throw new IllegalArgumentException("Trainee username must be indicated");
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

            return entityManager.createQuery(criteriaQuery).getResultList();
        }
        catch (Exception e) {
            log.error(e.getMessage());
            return new ArrayList<>();
        }
    }
}
