package org.example.loaders;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.slf4j.Slf4j;
import org.example.models.Training;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class TrainingLoader implements Loader<Training> {
    private String filePath;
    private Map<Long, Training> trainings;

    @Autowired
    public TrainingLoader(@Qualifier("trainingsFilePath") String filePath, @Qualifier("trainingStorage") Map<Long, Training> trainings) {
        this.filePath = filePath;
        this.trainings = trainings;
        log.info("TrainingLoader initialized with file {}", filePath);
    }

    @PostConstruct
    public void init() {
        log.info("TrainingLoader: PostConstruct initialization started . . .");
        load();
        log.info("TrainingLoader: PostConstruct initialization completed");
    }

    @PreDestroy
    public void destroy() {
        log.info("TrainingLoader: PreDestroy method started . . .");
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new JavaTimeModule());
            List<Training> trainingList = trainings.values().stream().toList();
            mapper.writeValue(new File(filePath), trainingList);
            log.info("TrainingLoader: Data successfully saved to file {}", filePath);
        } catch (Exception e) {
            log.error("TrainingLoader: Failed to save data to file {}", filePath, e);
        }
    }

    @Override
    public Map<Long, Training> load() {
        log.info("TrainingLoader: Loading data from file {}", filePath);
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());
            List<Training> trainingsList = objectMapper.readValue(new File(filePath), new TypeReference<List<Training>>() {});
            Map<Long, Training> map = this.trainings;
            long id = 0;
            for (Training training : trainingsList) {
                id++;
                map.put(id, training);
            }
            log.info("TrainingLoader: Data loaded successfully");
            return map;
        } catch (IOException e) {
            log.error("TrainingLoader: Failed to load data from file {}", filePath, e);
        }
        return new HashMap<>();
    }

    @Override
    public void clear() {
        log.info("TrainingLoader: Clearing data in memory and file {}", filePath);
        try (FileWriter writer = new FileWriter(new File(filePath))) {
            this.trainings.clear();
            log.info("TrainingLoader: Data cleared successfully");
        } catch (IOException e) {
            log.error("TrainingLoader: Failed to clear data in file {}", filePath, e);
        }
    }
}
