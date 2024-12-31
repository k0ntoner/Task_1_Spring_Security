package org.example.loaders;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.slf4j.Slf4j;
import org.example.models.Trainee;
import org.example.models.Trainer;
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
public class TrainerLoader implements Loader<Trainer> {
    private String filePath;
    private Map<Long, Trainer> trainers;
    @Autowired
    public TrainerLoader(@Qualifier("trainersFilePath") String  filePath, @Qualifier("trainerStorage") Map<Long, Trainer> trainers) {
        this.filePath = filePath;
        this.trainers = trainers;
    }
    @PostConstruct
    public void init() {
        load();
    }
    @PreDestroy
    public void destroy() {
        try{
            ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new JavaTimeModule());
            List<Trainer> trainerList=trainers.values().stream().toList();
            mapper.writeValue(new File(filePath), trainerList);
        }
        catch (Exception e){

        }
    }
    @Override
    public Map<Long, Trainer> load() {
        try{
            ObjectMapper objectMapper = new ObjectMapper();
            List<Trainer> trainersList = objectMapper.readValue(new File(filePath), new TypeReference<List<Trainer>>() {});
            Map<Long, Trainer> map = this.trainers;
            for(Trainer trainer : trainersList){
                map.put(trainer.getUserId(), trainer);
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
            trainers.clear();
        } catch (IOException e) {

        }
    }
}
