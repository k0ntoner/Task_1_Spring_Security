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
}
