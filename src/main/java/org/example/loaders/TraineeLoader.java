package org.example.loaders;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.slf4j.Slf4j;
import org.example.models.Trainee;
import org.example.repositories.TraineeDaoImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.util.List;

@Component
@Slf4j
public class TraineeLoader implements Loader<Trainee> {
    private String filePath;
    private TraineeDaoImpl traineeDAOImpl;
    @Autowired
    public TraineeLoader(@Qualifier("traineesFilePath") String  filePath,
                         @Qualifier("traineeDaoImpl") TraineeDaoImpl traineeDAOImpl) {
        this.filePath = filePath;
        this.traineeDAOImpl = traineeDAOImpl;
        log.info("TraineeLoader initialized with file {}", filePath);
    }
    @PostConstruct
    public void init() {
        log.info("TraineeLoader: PostConstruct initialization started . . .");
        load();
        log.info("TraineeLoader: PostConstruct initialization completed");
    }
    @Override
    public void load() {
        log.info("TraineeLoader: Loading data from file {}", filePath);
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());
            List<Trainee> traineesList = objectMapper.readValue(new File(filePath), new TypeReference<List<Trainee>>() {});
            traineesList.forEach(trainee -> traineeDAOImpl.add(trainee));
            log.info("TraineeLoader: Data loaded successfully");
        } catch (IOException e) {
            log.error("TraineeLoader: Failed to load data from file {}", filePath, e);
        }
    }

}
