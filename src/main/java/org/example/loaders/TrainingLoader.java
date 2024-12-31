package org.example.loaders;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.slf4j.Slf4j;
import org.example.models.Trainer;
import org.example.models.Training;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
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
public class TrainingLoader implements Loader {
    private String filePath;
    private Map<Long, Training> trainings;
    @Autowired
    public TrainingLoader(@Qualifier("trainingsFilePath") String  filePath, @Qualifier("trainingStorage") Map<Long, Training> trainings) {
        this.filePath = filePath;
        this.trainings = trainings;
    }
    @PostConstruct
    public void init() {
        load();
    } @PreDestroy
    public void destroy() {
        try{
            ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new JavaTimeModule());
            List<Training> trainingList=trainings.values().stream().toList();
            mapper.writeValue(new File(filePath), trainingList);
        }
        catch (Exception e){

        }
    }
    @Override
    public Map<Long, Training> load() {
        try{
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());
            List<Training> trainingsList = objectMapper.readValue(new File(filePath), new TypeReference<List<Training>>() {});
            Map<Long, Training> map = this.trainings;
            long id=0;
            for(Training training : trainingsList){
                id++;
                map.put(id, training);
            }
            return map;
        }
        catch (IOException e){
            e.printStackTrace();
        }
        return new HashMap<>();
    }

    @Override
    public void clear() {
        try (FileWriter writer = new FileWriter(new File(filePath))) {
            this.trainings.clear();
        } catch (IOException e) {

        }
    }
}
