package org.example.DAOs;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.interfaces.DAO;
import org.example.interfaces.Model;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.File;
import java.io.IOException;
import java.util.Map;


public class TraineeDAO implements DAO {
    private final Map<Integer, Model> storage;
    private Integer head;
    private String filePath;
    private static final Logger logger = LoggerFactory.getLogger(TraineeDAO.class);
    public TraineeDAO(Map<Integer,Model> traineeStorage, String traineesFilePath) {
        this.storage = traineeStorage;
        this.filePath = traineesFilePath;
        logger.debug("Trainee DAO initialized: " + traineesFilePath);
    }
    @PostConstruct
    public void init() {
        head = storage.size();
        logger.debug("Trainee DAO post initialization");
    }
    @PreDestroy
    public void destroy() {
        try {
            logger.debug("Trainee DAO pre destroy started:");
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.writeValue(new File(filePath), storage);
            logger.debug("Trainee DAO pre destroy finished. File was successfully saved.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void add(Model entity) {
        storage.put(++head,entity);
        logger.info("Trainee DAO added new entity: {}", entity);
    }

    @Override
    public void update(Integer id, Model entity) {
        storage.put(id, entity);
        logger.info("Trainee DAO updated entity at {} to: {}",id, entity);
    }

    @Override
    public Model findById(int id) {
        return storage.get(id);
    }

    @Override
    public Map<Integer, Model> findAll() {
        return storage;
    }

    @Override
    public void delete(int id) {
        storage.remove(id);
        logger.warn("Trainee DAO deleted entity with id: {}", id);
    }
}
