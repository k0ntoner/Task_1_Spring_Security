package org.example.repositories;

import java.time.LocalDateTime;
import java.util.Map;

public interface TrainingDAO<T> {
    T add(T entity);
    T update(long id, T entity);
    T findByTrainer(long trainerId, LocalDateTime dateTime);
    T findByTrainee(long traineeId, LocalDateTime dateTime);
    T findById(long id);
    Map<Long,T> findAll();
    boolean delete(long id);

}
