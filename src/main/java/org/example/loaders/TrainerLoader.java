package org.example.loaders;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.slf4j.Slf4j;
import org.example.models.Trainer;
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
public class TrainerLoader implements Loader<Trainer> {
    private String filePath;
    private Map<Long, Trainer> trainers;

    @Autowired
    public TrainerLoader(@Qualifier("trainersFilePath") String filePath, @Qualifier("trainerStorage") Map<Long, Trainer> trainers) {
        this.filePath = filePath;
        this.trainers = trainers;
        log.info("TrainerLoader initialized with file {}", filePath);
    }

    @PostConstruct
    public void init() {
        log.info("TrainerLoader: PostConstruct initialization started...");
        load();
        log.info("TrainerLoader: PostConstruct initialization completed");
    }

    @PreDestroy
    public void destroy() {
        log.info("TrainerLoader: PreDestroy method started...");
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new JavaTimeModule());
            List<Trainer> trainerList = trainers.values().stream().toList();
            mapper.writeValue(new File(filePath), trainerList);
            log.info("TrainerLoader: Data successfully saved to file {}", filePath);
        } catch (Exception e) {
            log.error("TrainerLoader: Failed to save data to file {}", filePath, e);
        }
    }

    @Override
    public Map<Long, Trainer> load() {
        log.info("TrainerLoader: Loading data from file {}", filePath);
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());
            List<Trainer> trainersList = objectMapper.readValue(new File(filePath), new TypeReference<List<Trainer>>() {});
            for (Trainer trainer : trainersList) {
                trainers.put(trainer.getUserId(), trainer);
            }
            log.info("TrainerLoader: Data loaded successfully from file {}", filePath);
            return trainers;
        } catch (IOException e) {
            log.error("TrainerLoader: Failed to load data from file {}", filePath, e);
        }
        return new HashMap<>();
    }

    @Override
    public void clear() {
        log.info("TrainerLoader: Clearing data in memory and file {}", filePath);
        try (FileWriter writer = new FileWriter(new File(filePath))) {
            trainers.clear();
            log.info("TrainerLoader: Data cleared successfully");
        } catch (IOException e) {
            log.error("TrainerLoader: Failed to clear data in file {}", filePath, e);
        }
    }
}
