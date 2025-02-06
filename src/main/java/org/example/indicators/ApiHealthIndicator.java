package org.example.indicators;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class ApiHealthIndicator implements HealthIndicator {
    @Override
    public Health health() {
        try{
            RestTemplate restTemplate = new RestTemplate();
            restTemplate.getForObject("http://localhost:8080/trainings/types", String.class);
            return Health.up().withDetail("Local API", "Available").build();
        }
        catch (Exception e){
            return Health.down().withDetail("Local API", "Not Available").withException(e).build();
        }
    }
}
