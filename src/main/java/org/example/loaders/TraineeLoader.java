package org.example.loaders;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.slf4j.Slf4j;
import org.example.models.Trainee;
import org.example.utils.LoggerManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
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
public class TraineeLoader implements Loader<Trainee> {
    private String filePath;
    private Map<Long, Trainee> trainees;
    private LoggerManager log;
    @Autowired
    public TraineeLoader(@Qualifier("traineesFilePath") String  filePath, @Qualifier("traineeStorage") Map<Long, Trainee> trainees,@Qualifier("loggerManager") LoggerManager loggerManager) {
        this.log = loggerManager;
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
            e.printStackTrace();
        }
    }
    @Override
    public Map<Long, Trainee> load() {
        try{
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());
            List<Trainee> traineesList = objectMapper.readValue(new File(filePath), new TypeReference<List<Trainee>>() {});
            Map<Long, Trainee> map = this.trainees;
            for(Trainee trainee : traineesList){
                map.put(trainee.getUserId(), trainee);
            }
            return map;
        }
        catch (IOException e){
            log.error("TraineeLoader: Failed to save data to file {}", filePath, e);
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
