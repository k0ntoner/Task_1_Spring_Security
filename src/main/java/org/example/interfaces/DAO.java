package org.example.interfaces;

import java.util.List;
import java.util.Map;

public interface DAO<T>{
    void add(T entity);
    void update(Integer id, T entity);
    T findById(int id);
    Map<Integer,T> findAll();
    void delete(int id);
}
