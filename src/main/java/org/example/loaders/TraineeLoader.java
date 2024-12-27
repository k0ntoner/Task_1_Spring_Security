package org.example.loaders;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.interfaces.Loader;
import org.example.models.Trainee;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TraineeLoader implements Loader {
    @Override
    public Map<Integer, Trainee> load(String filePath) {
        try{
            ObjectMapper objectMapper = new ObjectMapper();
            List<Trainee> trainees = objectMapper.readValue(new File(filePath), new TypeReference<List<Trainee>>() {});
            Map<Integer, Trainee> map = new HashMap<>();
            for(Trainee trainee : trainees){
                map.put(trainee.getUserId(), trainee);
            }
            return map;
        }
        catch (IOException e){
            e.printStackTrace();
        }
        return null;
    }
}
