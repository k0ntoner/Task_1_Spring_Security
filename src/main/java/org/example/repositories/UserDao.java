package org.example.repositories;

import java.util.Collection;
import java.util.Optional;

public interface UserDao<T> extends BasicDao<T> {

    T update(T entity);

    boolean isUsernameExist(String username);

    Optional<T> findByUsername(String username);
}
