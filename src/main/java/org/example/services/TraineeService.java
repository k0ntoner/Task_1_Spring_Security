package org.example.services;

import org.example.models.Trainee;
import org.example.DAOs.TraineeDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class TraineeService {
    private final TraineeDAO traineeDAO;
    @Autowired
    public TraineeService(TraineeDAO traineeDAO) {
        this.traineeDAO = traineeDAO;
    }
    public void addTrainee(Trainee trainee) {
        traineeDAO.add(trainee);
    }
    public Trainee getTraineeById(int id) {
        return traineeDAO.findById(id);
    }
    public Map<Integer,Trainee> getAllTrainees() {
        return  traineeDAO.findAll();
    }
    public void deleteTrainee(int id) {
        traineeDAO.delete(id);
    }
    public void updateTrainee(Integer id, Trainee trainee) {
        traineeDAO.update(id, trainee);
    }
}