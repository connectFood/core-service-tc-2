package com.connectfood.core.infrastructure.persistence.mappers;

import java.math.BigDecimal;
import java.util.UUID;

import com.connectfood.core.domain.model.RestaurantItems;
import com.connectfood.core.domain.model.Restaurants;
import com.connectfood.core.infrastructure.persistence.entity.RestaurantItemsEntity;
import com.connectfood.core.infrastructure.persistence.entity.RestaurantsEntity;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class RestaurantItemsInfraMapperTest {

  @Mock
  private RestaurantsInfraMapper restaurantsMapper;

  @InjectMocks
  private RestaurantItemsInfraMapper mapper;

  @Test
  @DisplayName("toDomain: deve retornar null quando entity for null")
  void toDomainShouldReturnNullWhenEntityIsNull() {
    final var result = mapper.toDomain(null);
    Assertions.assertNull(result);
  }

  @Test
  @DisplayName("toDomain: deve mapear entity para domain quando restaurant estiver presente")
  void toDomainShouldMapEntityToDomainWhenRestaurantIsPresent() {
    final var uuid = UUID.randomUUID();
    final var name = "ITEM";
    final var description = "Desc";
    final var value = BigDecimal.valueOf(9.90);
    final var requestType = "DELIVERY";

    final var restaurantEntity = Mockito.mock(RestaurantsEntity.class);

    final var entity = new RestaurantItemsEntity();
    entity.setUuid(uuid);
    entity.setName(name);
    entity.setDescription(description);
    entity.setValue(value);
    entity.setRequestType(requestType);
    entity.setRestaurant(restaurantEntity);

    final Restaurants restaurantDomain = Mockito.mock(Restaurants.class);
    Mockito.when(restaurantsMapper.toDomain(restaurantEntity)).thenReturn(restaurantDomain);

    final var result = mapper.toDomain(entity);

    Assertions.assertNotNull(result);
    Assertions.assertEquals(uuid, result.getUuid());
    Assertions.assertEquals(name, result.getName());
    Assertions.assertEquals(description, result.getDescription());
    Assertions.assertEquals(value, result.getValue());
    Assertions.assertEquals(requestType, result.getRequestType());
    Assertions.assertEquals(restaurantDomain, result.getRestaurant());

    Mockito.verify(restaurantsMapper, Mockito.times(1)).toDomain(restaurantEntity);
  }

  @Test
  @DisplayName("toDomain: deve mapear entity para domain quando restaurant for null (sem chamar mapper de restaurant)")
  void toDomainShouldMapEntityToDomainWhenRestaurantIsNull() {
    final var uuid = UUID.randomUUID();
    final var name = "ITEM";
    final var description = "Desc";
    final var value = BigDecimal.valueOf(9.90);
    final var requestType = "DELIVERY";

    final var entity = new RestaurantItemsEntity();
    entity.setUuid(uuid);
    entity.setName(name);
    entity.setDescription(description);
    entity.setValue(value);
    entity.setRequestType(requestType);
    entity.setRestaurant(null);

    final var result = mapper.toDomain(entity);

    Assertions.assertNotNull(result);
    Assertions.assertEquals(uuid, result.getUuid());
    Assertions.assertEquals(name, result.getName());
    Assertions.assertEquals(description, result.getDescription());
    Assertions.assertEquals(value, result.getValue());
    Assertions.assertEquals(requestType, result.getRequestType());
    Assertions.assertNull(result.getRestaurant());

    Mockito.verifyNoInteractions(restaurantsMapper);
  }

  @Test
  @DisplayName("toEntity(model, restaurantsEntity): deve retornar null quando model for null")
  void toEntityWithRestaurantsEntityShouldReturnNullWhenModelIsNull() {
    final var restaurantsEntity = Mockito.mock(RestaurantsEntity.class);

    final var result = mapper.toEntity(null, restaurantsEntity);

    Assertions.assertNull(result);
    Mockito.verifyNoInteractions(restaurantsMapper);
  }

  @Test
  @DisplayName("toEntity(model, restaurantsEntity): deve retornar null quando restaurantsEntity for null")
  void toEntityWithRestaurantsEntityShouldReturnNullWhenRestaurantsEntityIsNull() {
    final RestaurantItems model = Mockito.mock(RestaurantItems.class);

    final var result = mapper.toEntity(model, (RestaurantsEntity) null);

    Assertions.assertNull(result);
    Mockito.verifyNoInteractions(restaurantsMapper);
  }

  @Test
  @DisplayName("toEntity(model, restaurantsEntity): deve mapear domain para entity com restaurant informado")
  void toEntityWithRestaurantsEntityShouldMapDomainToEntity() {
    final var uuid = UUID.randomUUID();
    final var name = "ITEM";
    final var description = "Desc";
    final var value = BigDecimal.valueOf(9.90);
    final var requestType = "DELIVERY";

    final Restaurants restaurantDomain = Mockito.mock(Restaurants.class);
    final var model = new RestaurantItems(uuid, name, description, value, requestType, restaurantDomain);

    final var restaurantsEntity = new RestaurantsEntity();

    final var result = mapper.toEntity(model, restaurantsEntity);

    Assertions.assertNotNull(result);
    Assertions.assertEquals(uuid, result.getUuid());
    Assertions.assertEquals(name, result.getName());
    Assertions.assertEquals(description, result.getDescription());
    Assertions.assertEquals(value, result.getValue());
    Assertions.assertEquals(requestType, result.getRequestType());
    Assertions.assertEquals(restaurantsEntity, result.getRestaurant());

    Mockito.verifyNoInteractions(restaurantsMapper);
  }

  @Test
  @DisplayName("toEntity(model, entity): deve atualizar entity com dados do model e retornar a mesma instância")
  void toEntityWithExistingEntityShouldUpdateAndReturnSameInstance() {
    final var uuid = UUID.randomUUID();
    final var initialEntity = new RestaurantItemsEntity();
    initialEntity.setUuid(uuid);
    initialEntity.setName("OLD");
    initialEntity.setDescription("OLD");
    initialEntity.setValue(BigDecimal.valueOf(1.00));
    initialEntity.setRequestType("OLD");

    final Restaurants restaurantDomain = Mockito.mock(Restaurants.class);
    final var model = new RestaurantItems(
        uuid,
        "NEW",
        "NEW DESC",
        BigDecimal.valueOf(12.34),
        "DELIVERY",
        restaurantDomain
    );

    final var result = mapper.toEntity(model, initialEntity);

    Assertions.assertSame(initialEntity, result);
    Assertions.assertEquals(uuid, result.getUuid(), "UUID não deve ser alterado por este método");
    Assertions.assertEquals("NEW", result.getName());
    Assertions.assertEquals("NEW DESC", result.getDescription());
    Assertions.assertEquals(BigDecimal.valueOf(12.34), result.getValue());
    Assertions.assertEquals("DELIVERY", result.getRequestType());

    Mockito.verifyNoInteractions(restaurantsMapper);
  }
}
