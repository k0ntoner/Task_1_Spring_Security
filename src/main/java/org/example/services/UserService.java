package org.example.services;

import java.util.Map;
import java.util.Optional;

public interface UserService<T> extends BasicService<T> {


    Optional<T> update(T entity);

    Optional<T> findByUsername(String username);

    Optional<T> changePassword(String username, String oldPassword, String newPassword);

    Optional<T> activate(T entity);

    Optional<T> deactivate(T entity);
}
