package org.example.interfaces;

import java.util.Map;

public interface DAO{
    void add(Model entity);
    void update(Integer id, Model entity);
    Model findById(int id);
    Map<Integer,Model> findAll();
    void delete(int id);
}
