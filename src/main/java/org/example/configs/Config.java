package org.example.configs;

import org.springframework.context.annotation.*;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.support.DefaultConversionService;
import org.springframework.core.convert.support.GenericConversionService;

import java.util.Set;

@Configuration
@ComponentScan(basePackages = "org.example")
@Import({LogConfig.class, HibernateConfig.class})
public class Config {
    @Bean
    public ConversionService conversionService(Set<Converter<?, ?>> converters) {
        GenericConversionService conversionService = new DefaultConversionService();
        converters.forEach(conversionService::addConverter);
        return conversionService;
    }

}
