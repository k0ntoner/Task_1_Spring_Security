package org.example.services;

import java.util.Map;
import java.util.Optional;

public interface UserService<T> extends BasicService<T> {

    T update(T entity);

    Optional<T> findByUsername(String username);

    void changePassword(String username, String oldPassword, String newPassword);

    void activate(T entity);

    void deactivate(T entity);
}
