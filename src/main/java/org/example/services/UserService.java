package org.example.services;

import java.util.Map;

public interface UserService<T> {
    T add(T entity);
    T findById(long id);
    Map<Long, T> findAll();
    boolean delete(long id);
    T update(long id, T trainee);
}
