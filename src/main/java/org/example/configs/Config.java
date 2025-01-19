package org.example.configs;

import org.springframework.context.annotation.*;

@Configuration
@ComponentScan(basePackages = "org.example")
@Import({LogConfig.class, HibernateConfig.class})
public class Config {


}
