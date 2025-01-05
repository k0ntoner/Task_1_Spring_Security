package org.example.loaders;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.slf4j.Slf4j;
import org.example.models.Trainer;
import org.example.repositories.TrainerDaoImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.util.List;

@Component
@Slf4j
public class TrainerLoader implements Loader<Trainer> {
    private String filePath;
    private TrainerDaoImpl trainerDAOImpl;

    @Autowired
    public TrainerLoader(@Qualifier("trainersFilePath") String filePath,
                         @Qualifier("trainerDaoImpl") TrainerDaoImpl trainerDAOImpl){
        this.filePath = filePath;
        this.trainerDAOImpl = trainerDAOImpl;
        log.info("TrainerLoader initialized with file {}", filePath);
    }

    @PostConstruct
    public void init() {
        log.info("TrainerLoader: PostConstruct initialization started...");
        load();
        log.info("TrainerLoader: PostConstruct initialization completed");
    }

    @Override
    public void load() {
        log.info("TrainerLoader: Loading data from file {}", filePath);
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());
            List<Trainer> trainersList = objectMapper.readValue(new File(filePath), new TypeReference<List<Trainer>>() {});
            trainersList.forEach(trainee -> trainerDAOImpl.add(trainee));
            log.info("TrainerLoader: Data loaded successfully from file {}", filePath);
        } catch (IOException e) {
            log.error("TrainerLoader: Failed to load data from file {}", filePath, e);
        }
    }

}
