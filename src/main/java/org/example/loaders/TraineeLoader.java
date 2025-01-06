package org.example.loaders;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.slf4j.Slf4j;
import org.example.models.Trainee;
import org.example.repositories.TraineeDaoImpl;
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
public class TraineeLoader implements Loader<Trainee> {
    @Value("${trainees.file.path}")
    private String filePath;

    private TraineeDaoImpl traineeDaoImpl;
    @Autowired
    public TraineeLoader(@Qualifier("traineeDaoImpl") TraineeDaoImpl traineeDAOImpl) {
        this.traineeDaoImpl = traineeDAOImpl;
        log.info("TraineeLoader initialized with file {}", filePath);
    }
    @PostConstruct
    public void init() {
        load();
    }
    @Override
    public void load() {
        log.info("TraineeLoader: Loading data from file {}", filePath);
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());
            List<Trainee> traineesList = objectMapper.readValue(new File(filePath), new TypeReference<List<Trainee>>() {});
            traineesList.forEach(trainee -> traineeDaoImpl.add(trainee));
        } catch (IOException e) {
            log.error("TraineeLoader: Failed to load data from file {}", filePath, e);
        }
    }

}
