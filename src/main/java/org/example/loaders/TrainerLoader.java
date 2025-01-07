package org.example.loaders;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.slf4j.Slf4j;
import org.example.models.Trainer;
import org.example.repositories.TrainerDaoImpl;
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
public class TrainerLoader implements Loader<Trainer> {
    @Value("${trainers.file.path}")
    private String filePath;

    private TrainerDaoImpl trainerDaoImpl;

    @Autowired
    public TrainerLoader(@Qualifier("trainerDaoImpl") TrainerDaoImpl trainerDaoImpl) {
        this.trainerDaoImpl = trainerDaoImpl;
        log.info("TrainerLoader initialized with file {}", filePath);
    }

    @PostConstruct
    public void init() {
        load();
    }

    @Override
    public void load() {
        log.info("TrainerLoader: Loading data from file {}", filePath);
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());
            List<Trainer> trainersList = objectMapper.readValue(new File(filePath), new TypeReference<List<Trainer>>() {
            });
            trainersList.forEach(trainee -> trainerDaoImpl.add(trainee));
        } catch (IOException e) {
            log.error("TrainerLoader: Failed to load data from file {}", filePath, e);
        }
    }

}
