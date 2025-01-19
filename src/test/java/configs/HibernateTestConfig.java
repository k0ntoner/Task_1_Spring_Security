package configs;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Environment;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.Properties;

@Configuration
@EnableTransactionManagement
@PropertySource("classpath:application.properties")
public class HibernateTestConfig {
    @Value("${hibernate-test.connection.url}")
    private String url;

    @Value("${hibernate-test.connection.username}")
    private String username;

    @Value("${hibernate-test.connection.password}")
    private String password;

    @Value("${hibernate-test.dialect}")
    private String dialect;

    @Value("${hibernate-test.hbm2ddl.auto}")
    private String hbm2ddlAuto;

    @Value("${hibernate-test.show_sql}")
    private String showSql;

    @Value("${hibernate-test.format_sql}")
    private String formatSql;

    @Value("${hibernate-test.connection.driver_class}")
    private String driverClass;

    @Value("${hibernate-test.current_session_context_class}")
    private String sessionContextClass;

    @Bean
    public DataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(driverClass);
        dataSource.setUrl(url);
        dataSource.setUsername(username);
        dataSource.setPassword("");
        return dataSource;
    }

    @Bean
    public LocalSessionFactoryBean sessionFactory(DataSource dataSource) {
        LocalSessionFactoryBean sessionFactory = new LocalSessionFactoryBean();
        sessionFactory.setDataSource(dataSource());

        Properties hibernateProperties = new Properties();
        hibernateProperties.put(Environment.DIALECT, dialect);
        hibernateProperties.put(Environment.HBM2DDL_AUTO, hbm2ddlAuto);
        hibernateProperties.put(Environment.SHOW_SQL, showSql);
        hibernateProperties.put(Environment.FORMAT_SQL, formatSql);
        hibernateProperties.put(Environment.CURRENT_SESSION_CONTEXT_CLASS, sessionContextClass);

        sessionFactory.setHibernateProperties(hibernateProperties);
        sessionFactory.setPackagesToScan("org.example.repositories.entities");
        return sessionFactory;
    }

    @Bean
    public org.springframework.orm.hibernate5.HibernateTransactionManager transactionManager(SessionFactory sessionFactory) {
        org.springframework.orm.hibernate5.HibernateTransactionManager transactionManager = new org.springframework.orm.hibernate5.HibernateTransactionManager();
        transactionManager.setSessionFactory(sessionFactory);
        return transactionManager;
    }


}
