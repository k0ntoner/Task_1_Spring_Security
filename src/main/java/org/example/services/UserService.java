package org.example.services;

import java.util.Map;

public interface UserService<T> extends BasicService<T> {
    boolean delete(T entity);

    T update(T entity);
}
