package org.example.services;

import java.util.Collection;
import java.util.Map;

public interface BasicService<T> {
    T add(T entity);

    T findById(long id);

    Collection<T> findAll();
}
