package configs;

import org.example.configs.HibernateConfig;
import org.example.configs.LogConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.support.DefaultConversionService;
import org.springframework.core.convert.support.GenericConversionService;

import java.util.Set;

@Configuration
@ComponentScan(basePackages = "org.example")
@Import({LogConfig.class, HibernateTestConfig.class})
public class TestConfig {
    @Bean
    public ConversionService conversionService(Set<Converter<?, ?>> converters) {
        GenericConversionService conversionService = new DefaultConversionService();
        converters.forEach(conversionService::addConverter);
        return conversionService;
    }
}
