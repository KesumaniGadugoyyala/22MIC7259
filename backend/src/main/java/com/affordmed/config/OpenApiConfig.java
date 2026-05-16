package com.affordmed.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {
    @Bean
    public OpenAPI campusOpenApi() {
        return new OpenAPI()
            .info(new Info()
                .title("Campus Notification API")
                .description("Real-time campus notification platform")
                .version("1.0.0")
                .license(new License().name("MIT")));
    }
}
