package org.example.repositories;

import java.util.Collection;
import java.util.Map;

public interface BasicDao<T> {
    T add(T entity);
    T findById(long id);
    Collection<T> findAll();

}
