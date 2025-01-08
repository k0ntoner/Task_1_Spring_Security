package org.example.repositories;

public interface UserDao<T> extends BasicDao<T> {
    T update(T entity);

    boolean delete(T entity);

    boolean isUsernameExist(String username);
}
