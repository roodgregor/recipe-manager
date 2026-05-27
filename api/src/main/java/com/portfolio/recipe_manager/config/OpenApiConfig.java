package com.portfolio.recipe_manager.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {
    @Bean
    public OpenAPI myApiDefinition() {
        return new OpenAPI()
                .info(new Info()
                        .title("Recipe Management API - Joshua Medina, 2026")
                        .description("API for managing recipes as part of a fullstack portfolio project hosted on GitHub.")
                        .version("1.0.0"));
    }
}
