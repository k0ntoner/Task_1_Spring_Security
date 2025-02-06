package org.example.indicators;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class SwaggerHealthIndicator implements HealthIndicator {
    @Override
    public Health health() {
        try{
            RestTemplate restTemplate = new RestTemplate();
            restTemplate.getForObject("http://localhost:8080/swagger-ui/index.html", String.class);
            return Health.up().withDetail("Swagger UI", "Available").build();
        }
        catch (Exception e){
            return Health.down().withDetail("Swagger UI", "Not Available").withException(e).build();
        }
    }
}
