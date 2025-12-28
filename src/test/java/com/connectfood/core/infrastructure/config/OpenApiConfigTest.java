package com.connectfood.core.infrastructure.config;

import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.junit.jupiter.MockitoExtension;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityScheme;

@ExtendWith(MockitoExtension.class)
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
    Assertions.assertEquals(1, openAPI.getServers().size());

    final var server = openAPI.getServers().getFirst();
    Assertions.assertEquals("http://localhost:9090", server.getUrl());
    Assertions.assertEquals("Localhost environment", server.getDescription());
  }

  @Test
  @DisplayName("Deve configurar esquema de segurança Bearer JWT")
  void shouldConfigureBearerJwtSecurityScheme() {
    final OpenAPI openAPI = config.connectFoodOpenAPI();

    Assertions.assertNotNull(openAPI.getComponents());
    Assertions.assertNotNull(openAPI.getComponents().getSecuritySchemes());

    final var securitySchemes = openAPI.getComponents().getSecuritySchemes();
    Assertions.assertTrue(securitySchemes.containsKey("bearerAuth"));

    final SecurityScheme scheme = securitySchemes.get("bearerAuth");
    Assertions.assertEquals(SecurityScheme.Type.HTTP, scheme.getType());
    Assertions.assertEquals("bearer", scheme.getScheme());
    Assertions.assertEquals("JWT", scheme.getBearerFormat());
    Assertions.assertEquals(SecurityScheme.In.HEADER, scheme.getIn());
    Assertions.assertEquals("Authorization", scheme.getName());
  }

  @Test
  @DisplayName("Deve adicionar requisito de segurança global no OpenAPI")
  void shouldAddGlobalSecurityRequirement() {
    final OpenAPI openAPI = config.connectFoodOpenAPI();

    Assertions.assertNotNull(openAPI.getSecurity());
    Assertions.assertFalse(openAPI.getSecurity().isEmpty());

    final var securityRequirement = openAPI.getSecurity().getFirst();
    Assertions.assertTrue(securityRequirement.containsKey("bearerAuth"));
    Assertions.assertEquals(List.of(), securityRequirement.get("bearerAuth"));
  }
}
