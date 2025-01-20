package org.example.repositories;

import java.util.Collection;
import java.util.Optional;

public interface BasicDao<T> {
    T save(T entity);

    Optional<T> findById(long id);

    Collection<T> findAll();

}
