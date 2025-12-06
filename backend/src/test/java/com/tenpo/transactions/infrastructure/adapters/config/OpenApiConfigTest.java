package com.tenpo.transactions.infrastructure.adapters.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("OpenApiConfig Tests")
class OpenApiConfigTest {

    private final OpenApiConfig openApiConfig = new OpenApiConfig();

    @Test
    @DisplayName("Should create OpenAPI bean with correct title")
    void testApiInfoTitle() {
        OpenAPI openAPI = openApiConfig.apiInfo();
        assertEquals("Tenpo Transactions API", openAPI.getInfo().getTitle());
    }

    @Test
    @DisplayName("Should create OpenAPI bean with correct version")
    void testApiInfoVersion() {
        OpenAPI openAPI = openApiConfig.apiInfo();
        assertEquals("1.0", openAPI.getInfo().getVersion());
    }

    @Test
    @DisplayName("Should create OpenAPI bean with correct description")
    void testApiInfoDescription() {
        OpenAPI openAPI = openApiConfig.apiInfo();
        assertEquals("API para gestionar transacciones del Tenpista en arquitectura hexagonal.", 
                     openAPI.getInfo().getDescription());
    }

    @Test
    @DisplayName("Should create non-null OpenAPI bean")
    void testApiInfoNotNull() {
        assertNotNull(openApiConfig.apiInfo());
    }

    @Test
    @DisplayName("Should create OpenAPI bean with non-null info")
    void testApiInfoInfoNotNull() {
        assertNotNull(openApiConfig.apiInfo().getInfo());
    }
}