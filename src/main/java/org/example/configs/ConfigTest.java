package org.example.configs;

import org.example.DAOs.TraineeDAO;
import org.example.interfaces.DAO;
import org.example.interfaces.Loader;
import org.example.interfaces.Model;
import org.example.loaders.TraineeLoader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import java.util.Map;

@Configuration
@ComponentScan(basePackages = "org.example")
@PropertySource("classpath:applicationTest.properties")
public class ConfigTest {
    @Value("${traineesTest.file.path}")
    private String traineesFilePath;
    @Bean
    public Map<Integer, Model> traineeStorage() {
        return traineeLoader().load();
    }
    @Bean
    public Loader traineeLoader() {
        return new TraineeLoader(traineesFilePath);
    }
    @Bean
    public DAO traineeDAO() {
        return new TraineeDAO(traineeStorage(), traineesFilePath);
    }
}
