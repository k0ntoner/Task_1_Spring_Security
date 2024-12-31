package org.example.configs;

import org.example.models.Trainee;
import org.example.models.Trainer;
import org.example.models.Training;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Configuration
@PropertySource("classpath:application.properties")
@ComponentScan(basePackages = "org.example")
@Import({LogConfig.class})
public class Config {
    @Bean
    public Map<Long, Trainee> traineeStorage() {
        return new HashMap<>();
    }
    @Bean
    public Map<Long, Trainer> trainerStorage() {
        return new HashMap<>();
    }
    @Bean
    public Map<Long, Training> trainingStorage() {
        return new HashMap<>();
    }
    @Bean
    public String traineesFilePath(@Value("${trainees.file.path}") String filePath) {
        return filePath;
    }
    @Bean
    public String trainersFilePath(@Value("${trainers.file.path}") String filePath) {
        return filePath;
    }
    @Bean
    public String trainingsFilePath(@Value("${trainings.file.path}") String filePath) {
        return filePath;
    }

}
