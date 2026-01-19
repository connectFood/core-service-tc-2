package com.connectfood.infrastructure.persistence.mappers;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import com.connectfood.core.domain.model.Restaurant;
import com.connectfood.core.domain.model.RestaurantItem;
import com.connectfood.core.domain.model.RestaurantItemImage;
import com.connectfood.core.domain.model.enums.RestaurantItemServiceType;
import com.connectfood.infrastructure.persistence.entity.RestaurantEntity;
import com.connectfood.infrastructure.persistence.entity.RestaurantItemEntity;
import com.connectfood.infrastructure.persistence.entity.RestaurantItemImageEntity;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class RestaurantItemInfraMapperTest {

  @Mock
  private RestaurantInfraMapper restaurantsMapper;

  @Mock
  private RestaurantItemImageInfraMapper restaurantItemsImageMapper;

  @InjectMocks
  private RestaurantItemInfraMapper mapper;

  @Test
  @DisplayName("Deve retornar null quando entity for null")
  void toDomainShouldReturnNullWhenEntityIsNull() {
    final var result = mapper.toDomain(null);

    Assertions.assertNull(result);
    Mockito.verifyNoInteractions(restaurantsMapper, restaurantItemsImageMapper);
  }

  @Test
  @DisplayName("Deve mapear entity para domain quando restaurant estiver presente")
  void toDomainShouldMapEntityToDomainWhenRestaurantIsPresent() {
    final var uuid = UUID.randomUUID();
    final var name = "ITEM";
    final var description = "Desc";
    final var value = BigDecimal.valueOf(9.90);

    final RestaurantEntity restaurantEntity = Mockito.mock(RestaurantEntity.class);
    final Restaurant restaurantDomain = Mockito.mock(Restaurant.class);

    Mockito.when(restaurantsMapper.toDomainSimple(restaurantEntity))
        .thenReturn(restaurantDomain);

    final RestaurantItemEntity entity = Mockito.mock(RestaurantItemEntity.class);
    Mockito.when(entity.getUuid())
        .thenReturn(uuid);
    Mockito.when(entity.getName())
        .thenReturn(name);
    Mockito.when(entity.getDescription())
        .thenReturn(description);
    Mockito.when(entity.getValue())
        .thenReturn(value);
    Mockito.when(entity.getRequestType())
        .thenReturn("DELIVERY");
    Mockito.when(entity.getRestaurant())
        .thenReturn(restaurantEntity);
    Mockito.when(entity.getImages())
        .thenReturn(null);

    final RestaurantItem result = mapper.toDomain(entity);

    Assertions.assertNotNull(result);
    Assertions.assertEquals(uuid, result.getUuid());
    Assertions.assertEquals(name, result.getName());
    Assertions.assertEquals(description, result.getDescription());
    Assertions.assertEquals(value, result.getValue());
    Assertions.assertEquals(RestaurantItemServiceType.DELIVERY, result.getRequestType());
    Assertions.assertSame(restaurantDomain, result.getRestaurant());
    Assertions.assertNotNull(result.getImages());
    Assertions.assertTrue(result.getImages()
        .isEmpty());

    Mockito.verify(restaurantsMapper, Mockito.times(1))
        .toDomainSimple(restaurantEntity);

    Mockito.verifyNoInteractions(restaurantItemsImageMapper);
    Mockito.verifyNoMoreInteractions(restaurantsMapper);
  }

  @Test
  @DisplayName("Deve mapear entity para domain quando restaurant for null")
  void toDomainShouldMapEntityToDomainWhenRestaurantIsNull() {
    final var uuid = UUID.randomUUID();

    final RestaurantItemEntity entity = Mockito.mock(RestaurantItemEntity.class);
    Mockito.when(entity.getUuid())
        .thenReturn(uuid);
    Mockito.when(entity.getName())
        .thenReturn("ITEM");
    Mockito.when(entity.getDescription())
        .thenReturn("Desc");
    Mockito.when(entity.getValue())
        .thenReturn(BigDecimal.valueOf(9.90));
    Mockito.when(entity.getRequestType())
        .thenReturn("DELIVERY");
    Mockito.when(entity.getRestaurant())
        .thenReturn(null);
    Mockito.when(entity.getImages())
        .thenReturn(List.of());

    final var result = mapper.toDomain(entity);

    Assertions.assertNotNull(result);
    Assertions.assertEquals(uuid, result.getUuid());
    Assertions.assertEquals(RestaurantItemServiceType.DELIVERY, result.getRequestType());
    Assertions.assertNull(result.getRestaurant());
    Assertions.assertNotNull(result.getImages());
    Assertions.assertTrue(result.getImages()
        .isEmpty());

    Mockito.verifyNoInteractions(restaurantsMapper, restaurantItemsImageMapper);
  }

  @Test
  @DisplayName("Deve mapear imagens utilizando o mapper de imagens")
  void toDomainShouldMapImages() {
    final RestaurantItemEntity entity = Mockito.mock(RestaurantItemEntity.class);
    Mockito.when(entity.getUuid())
        .thenReturn(UUID.randomUUID());
    Mockito.when(entity.getName())
        .thenReturn("ITEM");
    Mockito.when(entity.getDescription())
        .thenReturn("Desc");
    Mockito.when(entity.getValue())
        .thenReturn(BigDecimal.valueOf(9.90));
    Mockito.when(entity.getRequestType())
        .thenReturn("DELIVERY");
    Mockito.when(entity.getRestaurant())
        .thenReturn(null);

    final var imageEntity1 = Mockito.mock(
        RestaurantItemImageEntity.class
    );
    final var imageEntity2 = Mockito.mock(
        RestaurantItemImageEntity.class
    );

    Mockito.when(entity.getImages())
        .thenReturn(List.of(imageEntity1, imageEntity2));

    final RestaurantItemImage img1 = Mockito.mock(RestaurantItemImage.class);
    final RestaurantItemImage img2 = Mockito.mock(RestaurantItemImage.class);

    Mockito.when(restaurantItemsImageMapper.toDomain(imageEntity1))
        .thenReturn(img1);
    Mockito.when(restaurantItemsImageMapper.toDomain(imageEntity2))
        .thenReturn(img2);

    final var result = mapper.toDomain(entity);

    Assertions.assertNotNull(result);
    Assertions.assertEquals(2, result.getImages()
        .size()
    );
    Assertions.assertEquals(List.of(img1, img2), result.getImages());

    Mockito.verify(restaurantItemsImageMapper, Mockito.times(1))
        .toDomain(imageEntity1);
    Mockito.verify(restaurantItemsImageMapper, Mockito.times(1))
        .toDomain(imageEntity2);
    Mockito.verifyNoInteractions(restaurantsMapper);
  }

  @Test
  @DisplayName("Deve retornar null quando model for null")
  void toEntityWithRestaurantsEntityShouldReturnNullWhenModelIsNull() {
    final var restaurantsEntity = Mockito.mock(RestaurantEntity.class);

    final var result = mapper.toEntity(null, restaurantsEntity);

    Assertions.assertNull(result);
    Mockito.verifyNoInteractions(restaurantsMapper, restaurantItemsImageMapper);
  }

  @Test
  @DisplayName("Deve retornar null quando restaurantsEntity for null")
  void toEntityWithRestaurantsEntityShouldReturnNullWhenRestaurantsEntityIsNull() {
    final RestaurantItem model = Mockito.mock(RestaurantItem.class);

    final var result = mapper.toEntity(model, (RestaurantEntity) null);

    Assertions.assertNull(result);
    Mockito.verifyNoInteractions(restaurantsMapper, restaurantItemsImageMapper);
  }

  @Test
  @DisplayName("Deve mapear domain para entity com restaurant informado")
  void toEntityWithRestaurantsEntityShouldMapDomainToEntity() {
    final var uuid = UUID.randomUUID();
    final var name = "ITEM";
    final var description = "Desc";
    final var value = BigDecimal.valueOf(9.90);
    final var requestType = RestaurantItemServiceType.DELIVERY;

    final Restaurant restaurantDomain = Mockito.mock(Restaurant.class);
    final var model = new RestaurantItem(uuid, name, description, value, requestType, restaurantDomain);

    final var restaurantsEntity = new RestaurantEntity();

    final var result = mapper.toEntity(model, restaurantsEntity);

    Assertions.assertNotNull(result);
    Assertions.assertEquals(uuid, result.getUuid());
    Assertions.assertEquals(name, result.getName());
    Assertions.assertEquals(description, result.getDescription());
    Assertions.assertEquals(value, result.getValue());
    Assertions.assertEquals("DELIVERY", result.getRequestType());
    Assertions.assertEquals(restaurantsEntity, result.getRestaurant());

    Mockito.verifyNoInteractions(restaurantsMapper, restaurantItemsImageMapper);
  }

  @Test
  @DisplayName("Deve retornar null quando entity for null no toDomainAll")
  void toDomainAllShouldReturnNullWhenEntityIsNull() {
    final var result = mapper.toDomainAll(null);

    Assertions.assertNull(result);
    Mockito.verifyNoInteractions(restaurantsMapper, restaurantItemsImageMapper);
  }

  @Test
  @DisplayName("Deve mapear entity para domain corretamente no toDomainAll")
  void toDomainAllShouldMapEntityToDomainCorrectly() {
    final var uuid = UUID.randomUUID();
    final var entity = Mockito.mock(RestaurantItemEntity.class);

    Mockito.when(entity.getUuid())
        .thenReturn(uuid);
    Mockito.when(entity.getName())
        .thenReturn("ITEM");
    Mockito.when(entity.getDescription())
        .thenReturn("Desc");
    Mockito.when(entity.getValue())
        .thenReturn(BigDecimal.valueOf(9.90));
    Mockito.when(entity.getRequestType())
        .thenReturn("DELIVERY");

    final RestaurantItem result = mapper.toDomainAll(entity);

    Assertions.assertNotNull(result);
    Assertions.assertEquals(uuid, result.getUuid());
    Assertions.assertEquals("ITEM", result.getName());
    Assertions.assertEquals("Desc", result.getDescription());
    Assertions.assertEquals(BigDecimal.valueOf(9.90), result.getValue());
    Assertions.assertEquals(RestaurantItemServiceType.DELIVERY, result.getRequestType());
    Assertions.assertNull(result.getRestaurant());
    Assertions.assertNull(result.getImages());

    Mockito.verifyNoInteractions(restaurantsMapper, restaurantItemsImageMapper);
  }

  @Test
  @DisplayName("Deve atualizar entity existente com base no model")
  void toEntityWithExistingEntityShouldUpdateFields() {
    final var uuid = UUID.randomUUID();

    final RestaurantItem model = Mockito.mock(RestaurantItem.class);
    Mockito.when(model.getName())
        .thenReturn("NEW_NAME");
    Mockito.when(model.getDescription())
        .thenReturn("NEW_DESC");
    Mockito.when(model.getValue())
        .thenReturn(BigDecimal.valueOf(19.90));
    Mockito.when(model.getRequestType())
        .thenReturn(RestaurantItemServiceType.DELIVERY);

    final var entity = new RestaurantItemEntity();
    entity.setUuid(uuid);
    entity.setName("OLD_NAME");
    entity.setDescription("OLD_DESC");
    entity.setValue(BigDecimal.ZERO);
    entity.setRequestType("PICKUP");

    final var result = mapper.toEntity(model, entity);

    Assertions.assertSame(entity, result);
    Assertions.assertEquals("NEW_NAME", result.getName());
    Assertions.assertEquals("NEW_DESC", result.getDescription());
    Assertions.assertEquals(BigDecimal.valueOf(19.90), result.getValue());
    Assertions.assertEquals("DELIVERY", result.getRequestType());
    Assertions.assertEquals(uuid, result.getUuid());

    Mockito.verifyNoInteractions(restaurantsMapper, restaurantItemsImageMapper);
  }
}
