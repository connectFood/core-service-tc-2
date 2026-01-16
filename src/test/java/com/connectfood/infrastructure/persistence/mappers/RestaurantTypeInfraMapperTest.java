package com.connectfood.infrastructure.persistence.mappers;

import java.util.UUID;

import com.connectfood.core.domain.model.RestaurantType;
import com.connectfood.infrastructure.persistence.entity.RestaurantTypeEntity;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class RestaurantTypeInfraMapperTest {

  private final RestaurantTypeInfraMapper mapper = new RestaurantTypeInfraMapper();

  @Test
  @DisplayName("Deve criar o mapper pelo construtor padrão")
  void shouldCreateMapperUsingDefaultConstructor() {
    final var instance = new RestaurantTypeInfraMapper();

    Assertions.assertNotNull(instance);
  }

  @Test
  @DisplayName("Deve retornar null quando entity for null")
  void toDomainShouldReturnNullWhenEntityIsNull() {
    final RestaurantType result = mapper.toDomain(null);

    Assertions.assertNull(result);
  }

  @Test
  @DisplayName("Deve mapear entity para domain corretamente")
  void toDomainShouldMapEntityToDomainCorrectly() {
    final var uuid = UUID.randomUUID();

    final var entity = new RestaurantTypeEntity();
    entity.setUuid(uuid);
    entity.setName("TYPE");
    entity.setDescription("Type description");

    final RestaurantType result = mapper.toDomain(entity);

    Assertions.assertNotNull(result);
    Assertions.assertEquals(uuid, result.getUuid());
    Assertions.assertEquals("TYPE", result.getName());
    Assertions.assertEquals("Type description", result.getDescription());
  }

  @Test
  @DisplayName("Deve retornar null quando model for null")
  void toEntityShouldReturnNullWhenModelIsNull() {
    final RestaurantTypeEntity result = mapper.toEntity(null);

    Assertions.assertNull(result);
  }

  @Test
  @DisplayName("Deve mapear model para entity corretamente")
  void toEntityShouldMapModelToEntityCorrectly() {
    final var uuid = UUID.randomUUID();

    final var model = new RestaurantType(
        uuid,
        "TYPE",
        "Type description"
    );

    final RestaurantTypeEntity result = mapper.toEntity(model);

    Assertions.assertNotNull(result);
    Assertions.assertEquals(uuid, result.getUuid());
    Assertions.assertEquals("TYPE", result.getName());
    Assertions.assertEquals("Type description", result.getDescription());
  }

  @Test
  @DisplayName("Deve atualizar entity existente com base no model")
  void toEntityWithExistingEntityShouldUpdateFields() {
    final var entity = new RestaurantTypeEntity();
    entity.setUuid(UUID.randomUUID());
    entity.setName("OLD_NAME");
    entity.setDescription("Old description");

    final var model = new RestaurantType(
        UUID.randomUUID(),
        "NEW_NAME",
        "New description"
    );

    final RestaurantTypeEntity result = mapper.toEntity(model, entity);

    Assertions.assertSame(entity, result);
    Assertions.assertEquals("NEW_NAME", result.getName());
    Assertions.assertEquals("New description", result.getDescription());
  }
}
