package org.example.configs;

import org.example.loaders.TraineeLoader;
import org.example.models.Trainee;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Map;

@Configuration
public class Config {
    @Bean
    public Map<Integer, Trainee> traineeStorage() {
        return new TraineeLoader().load("/Users/andriikot/IdeaProjects/Epum/Task_1_Spring_Core/src/main/java/org/example/resources/trainees.json");
    }

}
