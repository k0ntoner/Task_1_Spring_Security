package org.example.configs;


import jakarta.annotation.PreDestroy;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Environment;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.Properties;

@Configuration
@EnableTransactionManagement
@PropertySource("classpath:application.properties")
public class HibernateConfig {
    @Value("${hibernate.connection.url}")
    private String url;

    @Value("${hibernate.connection.username}")
    private String username;

    @Value("${hibernate.connection.password}")
    private String password;

    @Value("${hibernate.dialect}")
    private String dialect;

    @Value("${hibernate.hbm2ddl.auto}")
    private String hbm2ddlAuto;

    @Value("${hibernate.show_sql}")
    private String showSql;

    @Value("${hibernate.format_sql}")
    private String formatSql;

    @Value("${hibernate.connection.driver_class}")
    private String driverClass;

    @Value("${hibernate.current_session_context_class}")
    private String sessionContextClass;

    @Bean
    public DataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(driverClass);
        dataSource.setUrl(url);
        dataSource.setUsername(username);
        dataSource.setPassword(password);
        return dataSource;
    }

    @Bean
    public SessionFactory sessionFactory(DataSource dataSource) {
        org.hibernate.cfg.Configuration configuration = new org.hibernate.cfg.Configuration();

        Properties hibernateProperties = new Properties();
        hibernateProperties.put(Environment.DIALECT, dialect);
        hibernateProperties.put(Environment.HBM2DDL_AUTO, hbm2ddlAuto);
        hibernateProperties.put(Environment.SHOW_SQL, showSql);
        hibernateProperties.put(Environment.FORMAT_SQL, formatSql);
        hibernateProperties.put(Environment.DATASOURCE, dataSource);
        hibernateProperties.put(Environment.CURRENT_SESSION_CONTEXT_CLASS, sessionContextClass);


        configuration.setProperties(hibernateProperties);

        configuration.addAnnotatedClass(org.example.repositories.entities.Trainee.class);
        configuration.addAnnotatedClass(org.example.repositories.entities.Trainer.class);
        configuration.addAnnotatedClass(org.example.repositories.entities.Training.class);
        configuration.addAnnotatedClass(org.example.repositories.entities.User.class);

        return configuration.buildSessionFactory();
    }

    @Bean
    public PlatformTransactionManager transactionManager(SessionFactory sessionFactory) {
        return new HibernateTransactionManager(sessionFactory);
    }
}

