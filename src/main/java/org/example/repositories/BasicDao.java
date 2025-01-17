package org.example.repositories;

import java.util.Collection;
import java.util.Optional;

public interface BasicDao<T> {
    Optional<T> save(T entity);

    Optional<T> findById(long id);

    Optional<Collection<T>> findAll();

}
