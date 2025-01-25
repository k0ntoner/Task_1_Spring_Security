package org.example.services;

import java.util.Map;
import java.util.Optional;

public interface UserService<T> extends BasicService<T> {
    T update(T dto);

    Optional<T> findByUsername(String username);

    void changePassword(String username, String oldPassword, String newPassword);

    void activate(T dto);

    void deactivate(T dto);

    T matchPassword(String username, String password);
}
