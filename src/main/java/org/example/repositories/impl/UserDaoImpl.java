package org.example.repositories.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.PersistenceException;
import jakarta.persistence.Query;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.example.repositories.UserDao;
import org.example.repositories.entities.User;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@Slf4j
public class UserDaoImpl implements UserDao<User> {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @Transactional
    public User update(User entity) {
        try {
            if (findByUsername(entity.getUsername()).isEmpty()) {
                throw new IllegalArgumentException("User with username " + entity.getUsername() + " not found");
            }

            entity = entityManager.merge(entity);
            log.info("Updated Trainee: {}", entity);
            return entity;
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new PersistenceException("Error while updating Trainee", e);
        }
    }

    @Override
    public boolean isUsernameExist(String username) {
        try {
            log.info("Checking if username exists for {}", username);
            User user = entityManager.createQuery("from User u where u.username = :username", User.class).setParameter("username", username).getSingleResult();
            return user != null;
        } catch (Exception e) {
            log.error(e.getMessage());
            return false;
        }
    }

    @Override
    public Optional<User> findByUsername(String username) {
        try {
            log.info("Trying to find user by username {}", username);
            User user = entityManager.createQuery("From User u where u.username = :username", User.class).setParameter("username", username).getSingleResult();
            return Optional.of(user);
        } catch (Exception e) {
            log.error(e.getMessage());

            return Optional.empty();
        }
    }
}
