package com.connectfood.infrastructure;

import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
public abstract class AbstractIntegrationTest {

  @Container
  static final PostgreSQLContainer<?> POSTGRES = new PostgreSQLContainer<>("postgres:16-alpine")
      .withDatabaseName("connectfood")
      .withUsername("root")
      .withPassword("root");

  @DynamicPropertySource
  static void registerProperties(final DynamicPropertyRegistry registry) {
    registry.add("spring.datasource.url", POSTGRES::getJdbcUrl);
    registry.add("spring.datasource.username", POSTGRES::getUsername);
    registry.add("spring.datasource.password", POSTGRES::getPassword);

    // Garante que Flyway rode no schema correto no container
    registry.add("spring.flyway.enabled", () -> "true");
    registry.add("spring.flyway.default-schema", () -> "core");
    registry.add("spring.flyway.schemas", () -> "core");

    // Hibernate também apontando para o schema core
    registry.add("spring.jpa.properties.hibernate.default_schema", () -> "core");
  }
}
