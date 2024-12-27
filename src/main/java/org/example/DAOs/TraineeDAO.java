package org.example.DAOs;

import org.example.interfaces.DAO;
import org.example.models.Trainee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Map;

@Repository
public class TraineeDAO implements DAO<Trainee> {
    private final Map<Integer, Trainee> storage;
    @Autowired
    public TraineeDAO(Map<Integer,Trainee> storage) {
        this.storage = storage;
    }

    @Override
    public void add(Trainee entity) {
        storage.put(entity.getUserId(), entity);
    }

    @Override
    public void update(Integer id, Trainee entity) {
        storage.put(entity.hashCode(), entity);
    }

    @Override
    public Trainee findById(int id) {
        return storage.get(id);
    }

    @Override
    public Map<Integer, Trainee> findAll() {
        return storage;
    }

    @Override
    public void delete(int id) {
        storage.remove(id);
    }
}
