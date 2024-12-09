package com.threadmanager.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
    
    @Bean
    public OpenAPI threadManagementOpenAPI() {
        return new OpenAPI()
            .info(new Info()
                .title("Thread Management API")
                .description("Rest API documentation for thread and queue management")
                .version("OPENAPI_3_0")
                .contact(new Contact()
                    .name("Zulal Karin")
                    .email("zulal.karin19@gmail.com")));
    }
} 
