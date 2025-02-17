package org.example.repositories;

import org.example.repositories.entities.Trainee;

import java.util.Collection;
import java.util.Optional;

public interface UserDao<T>{
    T update(T entity);

    boolean isUsernameExist(String username);

    Optional<T> findByUsername(String username);
}
