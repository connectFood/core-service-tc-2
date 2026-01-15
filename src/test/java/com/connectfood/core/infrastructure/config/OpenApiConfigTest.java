package com.connectfood.core.infrastructure.config;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

class OpenApiConfigTest {

  private final OpenApiConfig config = new OpenApiConfig();

  @Test
  @DisplayName("Deve criar bean OpenAPI com informações básicas configuradas")
  void shouldCreateOpenApiWithBasicInfo() {
    final OpenAPI openAPI = config.connectFoodOpenAPI();

    Assertions.assertNotNull(openAPI);
    Assertions.assertNotNull(openAPI.getInfo());

    final Info info = openAPI.getInfo();
    Assertions.assertEquals("Restaurant Management System - ConnectFood", info.getTitle());
    Assertions.assertEquals("1.0.0", info.getVersion());
    Assertions.assertEquals(
        "API for user management (customers and restaurant owners).",
        info.getDescription()
    );
  }

  @Test
  @DisplayName("Deve configurar servidor localhost corretamente")
  void shouldConfigureLocalhostServer() {
    final OpenAPI openAPI = config.connectFoodOpenAPI();

    Assertions.assertNotNull(openAPI.getServers());
    Assertions.assertEquals(1, openAPI.getServers()
        .size()
    );

    final var server = openAPI.getServers()
        .get(0);
    Assertions.assertEquals("http://localhost:9090", server.getUrl());
    Assertions.assertEquals("Localhost environment", server.getDescription());
  }

  @Test
  @DisplayName("Não deve configurar esquemas de segurança no OpenAPI")
  void shouldNotConfigureSecuritySchemes() {
    final OpenAPI openAPI = config.connectFoodOpenAPI();

    Assertions.assertTrue(
        openAPI.getComponents() == null || openAPI.getComponents()
            .getSecuritySchemes() == null,
        "Security schemes should not be configured"
    );
  }

  @Test
  @DisplayName("Não deve adicionar requisito de segurança global no OpenAPI")
  void shouldNotAddGlobalSecurityRequirement() {
    final OpenAPI openAPI = config.connectFoodOpenAPI();

    Assertions.assertTrue(
        openAPI.getSecurity() == null || openAPI.getSecurity()
            .isEmpty(),
        "Global security requirements should not be configured"
    );
  }
}
