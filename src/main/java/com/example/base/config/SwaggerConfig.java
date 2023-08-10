package com.example.base.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springdoc.core.GroupedOpenApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
    @Value("${application.description}")
    private String appDescription;

    @Value("${application.version}")
    private String appVersion;

    @Bean
    public OpenAPI openAPI() {
        OpenAPI openAPI = new OpenAPI()
                .info(new Info()
                        .title("Sample Swagger")
                        .version("appDescription")
                        .description("appDescription")
                        .license(new License().name("sample")));

        return openAPI;
    }
}
