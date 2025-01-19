package configs;

import org.example.configs.HibernateConfig;
import org.example.configs.LogConfig;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@ComponentScan(basePackages = "org.example")
@Import({LogConfig.class, HibernateTestConfig.class})
public class TestConfig {
}
