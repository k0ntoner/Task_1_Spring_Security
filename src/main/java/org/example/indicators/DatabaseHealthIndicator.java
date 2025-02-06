package org.example.indicators;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.validation.Valid;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

@Component
public class DatabaseHealthIndicator implements HealthIndicator {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Health health() {
        try{
            entityManager.createNativeQuery("select 1").getSingleResult();
            return Health.up().withDetail("Database", "Connected").build();
        }
        catch(Exception e){
            return Health.down().withDetail("Database", "Not Connected").withException(e).build();
        }
    }
}
