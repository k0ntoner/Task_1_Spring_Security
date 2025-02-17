package configs;

import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.*;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@TestConfiguration
@ComponentScan(basePackages = "org.example")
@Profile("test")
public class TestWebConfig implements WebMvcConfigurer {
    @JsonFilter("linksFilter")
    private static class IgnoreLinksMixin {
    }

    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();

        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        objectMapper.addMixIn(Object.class, IgnoreLinksMixin.class);
        objectMapper.setFilterProvider(new SimpleFilterProvider()
                .addFilter("linksFilter", SimpleBeanPropertyFilter.serializeAllExcept("links")));

        return objectMapper;

    }
}
