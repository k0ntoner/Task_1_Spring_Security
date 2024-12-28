package org.example.services;

import org.example.interfaces.DAO;
import org.example.interfaces.Model;
import org.example.models.Trainee;
import org.example.DAOs.TraineeDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.Map;

@Component
public class TraineeService {
    private final DAO traineeDAO;
    @Autowired
    public TraineeService(DAO traineeDAO) {
        this.traineeDAO = traineeDAO;
    }
    public void addTrainee(Trainee trainee) {
        traineeDAO.add(trainee);
    }
    public Trainee getTraineeById(int id) {
        return (Trainee) traineeDAO.findById(id);
    }
    public Map<Integer, Model> getAllTrainees() {
        return traineeDAO.findAll();
    }
    public void deleteTrainee(int id) {
        traineeDAO.delete(id);
    }
    public void updateTrainee(Integer id, Trainee trainee) {
        traineeDAO.update(id, trainee);
    }
}