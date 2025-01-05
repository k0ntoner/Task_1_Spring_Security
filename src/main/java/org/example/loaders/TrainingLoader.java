package org.example.loaders;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.slf4j.Slf4j;
import org.example.models.Training;
import org.example.repositories.TrainingDaoImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.util.List;

@Component
@Slf4j
public class TrainingLoader implements Loader<Training> {
    private String filePath;
    private TrainingDaoImpl trainingDAOImpl;

    @Autowired
    public TrainingLoader(@Qualifier("trainingsFilePath") String filePath, @Qualifier("trainingDaoImpl") TrainingDaoImpl trainingDAOImpl) {
        this.filePath = filePath;
        this.trainingDAOImpl = trainingDAOImpl;
        log.info("TrainingLoader initialized with file {}", filePath);
    }

    @PostConstruct
    public void init() {
        log.info("TrainingLoader: PostConstruct initialization started . . .");
        load();
        log.info("TrainingLoader: PostConstruct initialization completed");
    }


    @Override
    public void load() {
        log.info("TrainingLoader: Loading data from file {}", filePath);
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());
            List<Training> trainingsList = objectMapper.readValue(new File(filePath), new TypeReference<List<Training>>() {});
            trainingsList.forEach(training -> {trainingDAOImpl.add(training);});
            log.info("TrainingLoader: Data loaded successfully");
        } catch (IOException e) {
            log.error("TrainingLoader: Failed to load data from file {}", filePath, e);
        }
    }

}
