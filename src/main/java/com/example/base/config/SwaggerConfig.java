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
    @Bean
    public OpenAPI customOpenAPI
            (
                    @Value("${application.desscription}") String appDescription,
                    @Value("${application.version}") String appVersion
            ) {
        OpenAPI openAPI = new OpenAPI()
                .info(new Info()
                        .title("Sample Swagger")
                        .version(appDescription)
                        .description(appDescription)
                        .license(new License().name("sample")
                                .url("http://localhost:8080")));

        return openAPI;
    }

    @Bean
    public GroupedOpenApi sampleGroupedApi() {
        return GroupedOpenApi.builder()
                .group("SampleCollection")
                .packagesToScan("com.example.base.controller")
                .build();
    }
}
