package com.musalasoft.drones.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
@RequiredArgsConstructor
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        final String apiTitle = "Drones API";
        final String apiDescription = "API for Drone Service. Contains public end points.";
        return new OpenAPI()
                .components(
                        new Components()
                )
                .info(new Info()
                        .title(apiTitle)
                        .description(apiDescription)
                        .termsOfService("Terms of service")
                        .version("1.0"));
    }

}
