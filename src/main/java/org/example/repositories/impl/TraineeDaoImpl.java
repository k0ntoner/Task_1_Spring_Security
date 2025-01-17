package org.example.repositories.impl;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

import org.example.repositories.TraineeDao;
import org.example.repositories.entities.Trainee;
import org.example.repositories.entities.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

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

        try (Session session = sessionFactory.openSession()) {

            session.persist(entity);
            log.info("Saved new Trainee: {}", entity);

            return entity;
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return null;
    }

    @Override
    @Transactional
    public Trainee update(Trainee entity) {
        try (Session session = sessionFactory.openSession()) {
            entity = session.merge(entity);
            log.info("Updated Trainee: {}", entity);
            return entity;
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return null;
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
        try (Session session = sessionFactory.openSession()) {
            if (!session.contains(entity)) {
                entity = session.merge(entity);
            }
            session.remove(entity);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
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
