package com.connectfood.infrastructure.persistence.mappers;

import java.util.UUID;

import com.connectfood.core.domain.model.User;
import com.connectfood.core.domain.model.UserType;
import com.connectfood.infrastructure.persistence.entity.UsersEntity;
import com.connectfood.infrastructure.persistence.entity.UsersTypeEntity;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class UsersInfraMapperTest {

  @Mock
  private UsersTypeInfraMapper usersTypeMapper;

  @InjectMocks
  private UsersInfraMapper mapper;

  @Test
  @DisplayName("Deve retornar null quando entity for null")
  void toDomainShouldReturnNullWhenEntityIsNull() {
    final User result = mapper.toDomain(null);

    Assertions.assertNull(result);
    Mockito.verifyNoInteractions(usersTypeMapper);
  }

  @Test
  @DisplayName("Deve mapear entity para domain quando usersType estiver presente")
  void toDomainShouldMapEntityToDomainWhenUsersTypeIsPresent() {
    final var uuid = UUID.randomUUID();

    final UsersTypeEntity usersTypeEntity = Mockito.mock(UsersTypeEntity.class);
    final UserType userTypeDomain = Mockito.mock(UserType.class);

    Mockito.when(usersTypeMapper.toDomain(usersTypeEntity))
        .thenReturn(userTypeDomain);

    final UsersEntity entity = Mockito.mock(UsersEntity.class);
    Mockito.when(entity.getUuid())
        .thenReturn(uuid);
    Mockito.when(entity.getFullName())
        .thenReturn("Lucas Santos");
    Mockito.when(entity.getEmail())
        .thenReturn("lucas@email.com");
    Mockito.when(entity.getPassword())
        .thenReturn("hashed-password");
    Mockito.when(entity.getUsersType())
        .thenReturn(usersTypeEntity);

    final User result = mapper.toDomain(entity);

    Assertions.assertNotNull(result);
    Assertions.assertEquals(uuid, result.getUuid());
    Assertions.assertEquals("Lucas Santos", result.getFullName());
    Assertions.assertEquals("lucas@email.com", result.getEmail());
    Assertions.assertEquals("hashed-password", result.getPasswordHash());
    Assertions.assertSame(userTypeDomain, result.getUserType());

    Mockito.verify(usersTypeMapper, Mockito.times(1))
        .toDomain(usersTypeEntity);
  }

  @Test
  @DisplayName("Deve lançar BadRequestException quando usersType for null")
  void toDomainShouldThrowBadRequestExceptionWhenUsersTypeIsNull() {
    final UsersEntity entity = Mockito.mock(UsersEntity.class);
    Mockito.when(entity.getUuid())
        .thenReturn(UUID.randomUUID());
    Mockito.when(entity.getFullName())
        .thenReturn("Lucas Santos");
    Mockito.when(entity.getEmail())
        .thenReturn("lucas@email.com");
    Mockito.when(entity.getPassword())
        .thenReturn("hashed-password");
    Mockito.when(entity.getUsersType())
        .thenReturn(null);

    final var ex = Assertions.assertThrows(
        com.connectfood.core.domain.exception.BadRequestException.class,
        () -> mapper.toDomain(entity)
    );

    Assertions.assertEquals("Users type is required", ex.getMessage());
    Mockito.verifyNoInteractions(usersTypeMapper);
  }

  @Test
  @DisplayName("Deve retornar null quando model for null no toEntity (novo)")
  void toEntityShouldReturnNullWhenModelIsNull() {
    final UsersEntity result = mapper.toEntity(null, Mockito.mock(UsersTypeEntity.class));

    Assertions.assertNull(result);
    Mockito.verifyNoInteractions(usersTypeMapper);
  }

  @Test
  @DisplayName("Deve mapear model para entity com usersType informado (novo)")
  void toEntityShouldMapModelToEntityCorrectly() {
    final var uuid = UUID.randomUUID();

    final User model = Mockito.mock(User.class);
    Mockito.when(model.getUuid())
        .thenReturn(uuid);
    Mockito.when(model.getFullName())
        .thenReturn("Lucas Santos");
    Mockito.when(model.getEmail())
        .thenReturn("lucas@email.com");
    Mockito.when(model.getPasswordHash())
        .thenReturn("hashed-password");

    final UsersTypeEntity usersTypeEntity = Mockito.mock(UsersTypeEntity.class);

    final UsersEntity result = mapper.toEntity(model, usersTypeEntity);

    Assertions.assertNotNull(result);
    Assertions.assertEquals(uuid, result.getUuid());
    Assertions.assertEquals("Lucas Santos", result.getFullName());
    Assertions.assertEquals("lucas@email.com", result.getEmail());
    Assertions.assertEquals("hashed-password", result.getPassword());
    Assertions.assertSame(usersTypeEntity, result.getUsersType());

    Mockito.verifyNoInteractions(usersTypeMapper);
  }

  @Test
  @DisplayName("Deve atualizar entity existente com base no model e preservar o UUID da entity")
  void toEntityWithExistingEntityShouldUpdateFields() {
    final var uuidOriginal = UUID.randomUUID();

    final var entity = new UsersEntity();
    entity.setUuid(uuidOriginal);
    entity.setFullName("Old Name");
    entity.setEmail("old@email.com");
    entity.setPassword("old-password");
    entity.setUsersType(Mockito.mock(UsersTypeEntity.class));

    final User model = Mockito.mock(User.class);
    Mockito.when(model.getFullName())
        .thenReturn("New Name");
    Mockito.when(model.getEmail())
        .thenReturn("new@email.com");
    Mockito.when(model.getPasswordHash())
        .thenReturn("new-password");

    final UsersTypeEntity newUsersTypeEntity = Mockito.mock(UsersTypeEntity.class);

    final UsersEntity result = mapper.toEntity(model, entity, newUsersTypeEntity);

    Assertions.assertSame(entity, result);
    Assertions.assertEquals(uuidOriginal, result.getUuid());

    Assertions.assertEquals("New Name", result.getFullName());
    Assertions.assertEquals("new@email.com", result.getEmail());
    Assertions.assertEquals("new-password", result.getPassword());
    Assertions.assertSame(newUsersTypeEntity, result.getUsersType());

    Mockito.verifyNoInteractions(usersTypeMapper);
  }
}
