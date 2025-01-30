package configs;

import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.apache.tools.ant.taskdefs.Java;
import org.example.configs.LogConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.support.DefaultConversionService;
import org.springframework.core.convert.support.GenericConversionService;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Set;

@Configuration
@ComponentScan(basePackages = "org.example")
@Import({LogConfig.class, HibernateTestConfig.class})
public class TestWebConfig implements WebMvcConfigurer {
    @Bean
    public ConversionService conversionService(Set<Converter<?, ?>> converters) {
        GenericConversionService conversionService = new DefaultConversionService();
        converters.forEach(conversionService::addConverter);
        return conversionService;
    }

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
