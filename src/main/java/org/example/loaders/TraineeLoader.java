package org.example.loaders;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.slf4j.Slf4j;
import org.example.models.Trainee;
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
public class TraineeLoader implements Loader<Trainee> {
    private String filePath;
    private Map<Long, Trainee> trainees;
    @Autowired
    public TraineeLoader(@Qualifier("traineesFilePath") String  filePath, @Qualifier("traineeStorage") Map<Long, Trainee> trainees) {
        this.filePath = filePath;
        this.trainees = trainees;
        log.info("TraineeLoader initialized with file {}", filePath);
    }
    @PostConstruct
    public void init() {
        log.info("TraineeLoader: PostConstruct initialization started . . .");
        load();
        log.info("TraineeLoader: PostConstruct initialization completed");
    }
    @PreDestroy
    public void destroy() {
        log.info("TraineeLoader: PreDestroy method started . . .");
        try{
            ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new JavaTimeModule());
            List<Trainee> traineeList=trainees.values().stream().toList();
            mapper.writeValue(new File(filePath), traineeList);
            log.info("TraineeLoader: Data successfully saved to file {}", filePath);
        }
        catch (Exception e){
            log.error("TraineeLoader: Data not saved to file {}", filePath);
        }
    }
    @Override
    public Map<Long, Trainee> load() {
        log.info("TraineeLoader: Loading data from file {}", filePath);
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());
            List<Trainee> traineesList = objectMapper.readValue(new File(filePath), new TypeReference<List<Trainee>>() {});
            Map<Long, Trainee> map = this.trainees;
            long id = 0;
            for (Trainee trainee : traineesList) {
                id++;
                map.put(id, trainee);
            }
            log.info("TraineeLoader: Data loaded successfully");
            return map;
        } catch (IOException e) {
            log.error("TraineeLoader: Failed to load data from file {}", filePath, e);
        }
        return new HashMap<>();
    }

    @Override
    public void clear() {
        log.info("TraineeLoader: Clearing data in memory and file {}", filePath);
        try (FileWriter writer = new FileWriter(new File(filePath))) {
            this.trainees.clear();
            log.info("TraineeLoader: Data cleared successfully");
        } catch (IOException e) {
            log.error("TraineeLoader: Failed to clear data in file {}", filePath, e);
        }
    }
}
