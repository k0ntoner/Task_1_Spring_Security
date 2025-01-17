package org.example.configs;

import org.example.utils.HibernateUtil;

import org.hibernate.SessionFactory;
import org.springframework.context.annotation.*;

@Configuration
@ComponentScan(basePackages = "org.example")
@Import({LogConfig.class})
public class Config {
    @Bean
    public static SessionFactory sessionFactory() {
        return HibernateUtil.getSessionFactory();
    }

}
