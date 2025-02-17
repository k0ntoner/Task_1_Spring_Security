package org.example.services;

import java.util.Map;
import java.util.Optional;

public interface UserService<T> {

    T findByUsername(String username);

    void changePassword(String username, String oldPassword, String newPassword);

    void activate(T dto);

    void deactivate(T dto);

    boolean matchPassword(String username, String password);
}
