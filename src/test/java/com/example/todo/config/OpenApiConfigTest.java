package com.example.todo.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class OpenApiConfigTest {

    @Test
    void createsOpenApiWithExpectedInfo() {
        OpenApiConfig cfg = new OpenApiConfig();
        OpenAPI api = cfg.todoOpenAPI();
        assertNotNull(api);
        Info info = api.getInfo();
        assertNotNull(info);
        assertEquals("Todo API", info.getTitle());
        assertEquals("Spring Boot REST API for Todo Management", info.getDescription());
        assertEquals("1.0", info.getVersion());
    }
}
