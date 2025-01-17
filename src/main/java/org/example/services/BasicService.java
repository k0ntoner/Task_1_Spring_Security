package org.example.services;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;

public interface BasicService<T> {
    T add(T entity);

    Optional<T> findById(long id);

    Collection<T> findAll();
}
