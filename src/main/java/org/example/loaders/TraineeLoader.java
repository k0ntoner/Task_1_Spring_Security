package org.example.loaders;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.interfaces.Loader;
import org.example.interfaces.Model;
import org.example.models.Trainee;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TraineeLoader implements Loader {
    private String filePath;
    public TraineeLoader(String filePath) {
        this.filePath = filePath;
    }
    @Override
    public Map<Integer, Model> load() {
        try{
            ObjectMapper objectMapper = new ObjectMapper();
            List<Trainee> trainees = objectMapper.readValue(new File(filePath), new TypeReference<List<Trainee>>() {});
            Map<Integer, Model> map = new HashMap<>();
            for(Trainee trainee : trainees){
                map.put(trainee.getUserId(), trainee);
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
        } catch (IOException e) {

        }
    }
}
