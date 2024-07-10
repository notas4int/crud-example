package org.artem.projects.proteincrudexample.configs;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenAPIConfig {

    @Bean
    public OpenAPI openAPI() {
        Info info = new Info()
                .title("CRUD API for proteins")
                .version("1.0")
                .description("Service for managed proteins))");

        return new OpenAPI().info(info);
    }
}
