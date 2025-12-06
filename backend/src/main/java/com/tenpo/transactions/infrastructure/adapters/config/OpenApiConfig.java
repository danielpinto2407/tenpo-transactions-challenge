package com.tenpo.transactions.infrastructure.adapters.config;

import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.OpenAPI;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI apiInfo() {
        return new OpenAPI()
                .info(new Info()
                        .title("Tenpo Transactions API")
                        .version("1.0")
                        .description("API para gestionar transacciones del Tenpista en arquitectura hexagonal.")
                );
    }
}

