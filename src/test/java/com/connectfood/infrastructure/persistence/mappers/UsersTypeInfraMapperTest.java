package com.connectfood.infrastructure.persistence.mappers;

import java.util.UUID;

import com.connectfood.core.domain.model.UsersType;
import com.connectfood.infrastructure.persistence.entity.UsersTypeEntity;
import com.connectfood.infrastructure.persistence.mappers.UsersTypeInfraMapper;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class UsersTypeInfraMapperTest {

  private final UsersTypeInfraMapper mapper = new UsersTypeInfraMapper();

  @Test
  @DisplayName("Deve criar o mapper pelo construtor padrão")
  void shouldCreateMapperUsingDefaultConstructor() {
    final var instance = new UsersTypeInfraMapper();
    Assertions.assertNotNull(instance);
  }

  @Test
  @DisplayName("Deve retornar null ao converter entity null para domínio")
  void shouldReturnNullWhenToDomainReceivesNull() {
    final UsersType result = mapper.toDomain(null);
    Assertions.assertNull(result);
  }

  @Test
  @DisplayName("Deve converter entity para domínio corretamente")
  void shouldConvertEntityToDomainCorrectly() {
    final var uuid = UUID.randomUUID();

    final var entity = new UsersTypeEntity();
    entity.setUuid(uuid);
    entity.setName("ADMIN");
    entity.setDescription("Administrator role");

    final UsersType result = mapper.toDomain(entity);

    Assertions.assertNotNull(result);
    Assertions.assertEquals(uuid, result.getUuid());
    Assertions.assertEquals("ADMIN", result.getName());
    Assertions.assertEquals("Administrator role", result.getDescription());
  }

  @Test
  @DisplayName("Deve retornar null ao converter model null para entity")
  void shouldReturnNullWhenToEntityReceivesNull() {
    final UsersTypeEntity result = mapper.toEntity((UsersType) null);
    Assertions.assertNull(result);
  }

  @Test
  @DisplayName("Deve converter model para entity corretamente")
  void shouldConvertModelToEntityCorrectly() {
    final var uuid = UUID.randomUUID();

    final var model = new UsersType(uuid, "OWNER", "Owner role");

    final UsersTypeEntity result = mapper.toEntity(model);

    Assertions.assertNotNull(result);
    Assertions.assertEquals(uuid, result.getUuid());
    Assertions.assertEquals("OWNER", result.getName());
    Assertions.assertEquals("Owner role", result.getDescription());
  }

  @Test
  @DisplayName("Deve atualizar a entity existente com base no model e preservar o UUID da entity")
  void shouldUpdateExistingEntityUsingModel() {
    final var uuidOriginal = UUID.randomUUID();

    final var entity = new UsersTypeEntity();
    entity.setUuid(uuidOriginal);
    entity.setName("OLD_NAME");
    entity.setDescription("Old description");

    final var model = new UsersType(UUID.randomUUID(), "NEW_NAME", "New description");

    final UsersTypeEntity result = mapper.toEntity(model, entity);

    Assertions.assertSame(entity, result);

    Assertions.assertEquals(uuidOriginal, result.getUuid());

    Assertions.assertEquals("NEW_NAME", result.getName());
    Assertions.assertEquals("New description", result.getDescription());
  }
}
