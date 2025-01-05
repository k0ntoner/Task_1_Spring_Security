package configs;

import org.example.configs.LogConfig;
import org.example.models.Trainee;
import org.example.models.Trainer;
import org.example.models.Training;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Configuration
@ComponentScan(basePackages = "org.example")
@PropertySource("classpath:applicationTest.properties")
@Import({LogConfig.class})
public class ConfigTest {

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
    public String traineesFilePath(@Value("${traineesTest.file.path}") String filePath) {
        return filePath;
    }
    @Bean
    public String trainersFilePath(@Value("${trainersTest.file.path}") String filePath) {
        return filePath;
    }
    @Bean
    public String trainingsFilePath(@Value("${trainingsTest.file.path}") String filePath) {
        return filePath;
    }

}
