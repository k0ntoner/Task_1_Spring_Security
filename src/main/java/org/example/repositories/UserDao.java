package org.example.repositories;

import java.util.Collection;
import java.util.Optional;

public interface UserDao<T> extends BasicDao<T> {


    boolean isUsernameExist(String username);
    boolean isPasswordMatch(T entity, String password);
    Optional<T> findByUsername(String username);
}
