package org.example.loaders;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.slf4j.Slf4j;
import org.example.models.Training;
import org.example.repositories.TrainingDaoImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.util.List;

@Component
@PropertySource("classpath:application.properties")
@Slf4j
public class TrainingLoader implements Loader<Training> {
    @Value("${trainings.file.path}")
    private String filePath;

    private TrainingDaoImpl trainingDaoImpl;

    @Autowired
    public TrainingLoader(@Qualifier("trainingDaoImpl") TrainingDaoImpl trainingDAOImpl) {
        this.trainingDaoImpl = trainingDAOImpl;
        log.info("TrainingLoader initialized with file {}", filePath);
    }

    @PostConstruct
    public void init() {
        load();
    }

    @Override
    public void load() {
        log.info("TrainingLoader: Loading data from file {}", filePath);
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());
            List<Training> trainingsList = objectMapper.readValue(new File(filePath), new TypeReference<List<Training>>() {});
            trainingsList.forEach(training -> {
                trainingDaoImpl.add(training);});
        } catch (IOException e) {
            log.error("TrainingLoader: Failed to load data from file {}", filePath, e);
        }
    }

}
