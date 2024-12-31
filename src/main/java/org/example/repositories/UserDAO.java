package org.example.repositories;

import java.util.Map;

public interface UserDAO<T>{
    T add(T entity);
    T update(long id, T entity);
    T findById(long id);
    Map<Long,T> findAll();
    boolean delete(long id);
}
