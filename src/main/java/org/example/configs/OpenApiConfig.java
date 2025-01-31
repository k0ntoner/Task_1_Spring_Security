package org.example.configs;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = {"org.example", "org.springdoc"})
public class OpenApiConfig {

    @Bean
    public GroupedOpenApi init() {
        return GroupedOpenApi.builder()
                .group("all")
                .packagesToScan("org.example")
                .pathsToMatch("/**")
                .build();
    }

    @Bean
    public OpenAPI ApiInfo() {
        return new OpenAPI()
                .info(new Info()
                        .title("My API")
                        .version("1.0")
                        .description("Documentation API"));
    }
}
