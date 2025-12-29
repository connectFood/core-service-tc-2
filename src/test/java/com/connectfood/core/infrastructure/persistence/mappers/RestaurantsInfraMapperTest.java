package com.connectfood.core.infrastructure.persistence.mappers;

import java.util.UUID;

import com.connectfood.core.domain.exception.BadRequestException;
import com.connectfood.core.domain.model.Restaurants;
import com.connectfood.core.domain.model.RestaurantsType;
import com.connectfood.core.infrastructure.persistence.entity.RestaurantsEntity;
import com.connectfood.core.infrastructure.persistence.entity.RestaurantsTypeEntity;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class RestaurantsInfraMapperTest {

  @Mock
  private RestaurantsTypeInfraMapper restaurantsTypeMapper;

  @InjectMocks
  private RestaurantsInfraMapper mapper;

  @Test
  @DisplayName("Deve retornar null quando entity for null")
  void toDomainShouldReturnNullWhenEntityIsNull() {
    final Restaurants result = mapper.toDomain(null);

    Assertions.assertNull(result);
    Mockito.verifyNoInteractions(restaurantsTypeMapper);
  }

  @Test
  @DisplayName("Deve mapear entity para domain quando restaurantsType estiver presente")
  void toDomainShouldMapEntityToDomainWhenRestaurantsTypeIsPresent() {
    final var uuid = UUID.randomUUID();
    final var name = "RESTAURANT";

    final RestaurantsTypeEntity typeEntity = Mockito.mock(RestaurantsTypeEntity.class);
    final RestaurantsType typeDomain = Mockito.mock(RestaurantsType.class);

    Mockito.when(restaurantsTypeMapper.toDomain(typeEntity))
        .thenReturn(typeDomain);

    final RestaurantsEntity entity = Mockito.mock(RestaurantsEntity.class);
    Mockito.when(entity.getUuid())
        .thenReturn(uuid);
    Mockito.when(entity.getName())
        .thenReturn(name);
    Mockito.when(entity.getRestaurantsType())
        .thenReturn(typeEntity);

    final Restaurants result = mapper.toDomain(entity);

    Assertions.assertNotNull(result);
    Assertions.assertEquals(uuid, result.getUuid());
    Assertions.assertEquals(name, result.getName());
    Assertions.assertSame(typeDomain, result.getRestaurantsType());

    Mockito.verify(restaurantsTypeMapper, Mockito.times(1))
        .toDomain(typeEntity);
  }

  @Test
  @DisplayName("Deve lançar BadRequestException quando restaurantsType for null")
  void toDomainShouldThrowBadRequestExceptionWhenRestaurantsTypeIsNull() {
    final RestaurantsEntity entity = Mockito.mock(RestaurantsEntity.class);
    Mockito.when(entity.getUuid())
        .thenReturn(UUID.randomUUID());
    Mockito.when(entity.getName())
        .thenReturn("RESTAURANT");
    Mockito.when(entity.getRestaurantsType())
        .thenReturn(null);

    final var exception = Assertions.assertThrows(
        BadRequestException.class,
        () -> mapper.toDomain(entity)
    );

    Assertions.assertEquals("Restaurant type is required", exception.getMessage());
    Mockito.verifyNoInteractions(restaurantsTypeMapper);
  }

  @Test
  @DisplayName("Deve retornar null quando model for null")
  void toEntityShouldReturnNullWhenModelIsNull() {
    final RestaurantsEntity result = mapper.toEntity(null, Mockito.mock(RestaurantsTypeEntity.class));

    Assertions.assertNull(result);
    Mockito.verifyNoInteractions(restaurantsTypeMapper);
  }

  @Test
  @DisplayName("Deve mapear model para entity com restaurantsType informado")
  void toEntityShouldMapModelToEntityWithRestaurantsType() {
    final var uuid = UUID.randomUUID();
    final var model = Mockito.mock(Restaurants.class);

    Mockito.when(model.getUuid())
        .thenReturn(uuid);
    Mockito.when(model.getName())
        .thenReturn("RESTAURANT");

    final RestaurantsTypeEntity typeEntity = Mockito.mock(RestaurantsTypeEntity.class);

    final RestaurantsEntity result = mapper.toEntity(model, typeEntity);

    Assertions.assertNotNull(result);
    Assertions.assertEquals(uuid, result.getUuid());
    Assertions.assertEquals("RESTAURANT", result.getName());
    Assertions.assertEquals(typeEntity, result.getRestaurantsType());

    Mockito.verifyNoInteractions(restaurantsTypeMapper);
  }

  @Test
  @DisplayName("Deve atualizar entity existente com base no model")
  void toEntityWithExistingEntityShouldUpdateFields() {
    final var entity = new RestaurantsEntity();
    entity.setUuid(UUID.randomUUID());
    entity.setName("OLD_NAME");
    entity.setRestaurantsType(Mockito.mock(RestaurantsTypeEntity.class));

    final Restaurants model = Mockito.mock(Restaurants.class);
    Mockito.when(model.getName())
        .thenReturn("NEW_NAME");

    final RestaurantsTypeEntity newTypeEntity = Mockito.mock(RestaurantsTypeEntity.class);

    final RestaurantsEntity result = mapper.toEntity(model, entity, newTypeEntity);

    Assertions.assertSame(entity, result);
    Assertions.assertEquals("NEW_NAME", result.getName());
    Assertions.assertEquals(newTypeEntity, result.getRestaurantsType());
  }
}
