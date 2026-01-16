package com.connectfood.infrastructure.persistence.mappers;

import java.util.UUID;

import com.connectfood.core.domain.model.RestaurantItemImage;
import com.connectfood.infrastructure.persistence.entity.RestaurantItemEntity;
import com.connectfood.infrastructure.persistence.entity.RestaurantItemImageEntity;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class RestaurantItemImageInfraMapperTest {

  private final RestaurantItemImageInfraMapper mapper = new RestaurantItemImageInfraMapper();

  @Test
  @DisplayName("Deve criar o mapper pelo construtor padrão")
  void shouldCreateMapperUsingDefaultConstructor() {
    final var instance = new RestaurantItemImageInfraMapper();

    Assertions.assertNotNull(instance);
  }

  @Test
  @DisplayName("Deve retornar null ao converter entity null para domínio")
  void shouldReturnNullWhenToDomainReceivesNull() {
    final RestaurantItemImage result = mapper.toDomain(null);

    Assertions.assertNull(result);
  }

  @Test
  @DisplayName("Deve converter entity para domínio corretamente")
  void shouldConvertEntityToDomainCorrectly() {
    final var uuid = UUID.randomUUID();

    final var entity = new RestaurantItemImageEntity();
    entity.setUuid(uuid);
    entity.setName("IMAGE_01");
    entity.setDescription("Image description");
    entity.setPath("/images/image-01.png");

    final RestaurantItemImage result = mapper.toDomain(entity);

    Assertions.assertNotNull(result);
    Assertions.assertEquals(uuid, result.getUuid());
    Assertions.assertEquals("IMAGE_01", result.getName());
    Assertions.assertEquals("Image description", result.getDescription());
    Assertions.assertEquals("/images/image-01.png", result.getPath());
  }

  @Test
  @DisplayName("Deve retornar null ao converter model null para entity no overload com RestaurantItemsEntity")
  void shouldReturnNullWhenToEntityWithRestaurantItemsEntityReceivesNullArguments() {
    final RestaurantItemEntity restaurantItemEntity = Mockito.mock(RestaurantItemEntity.class);
    final RestaurantItemImage model = Mockito.mock(RestaurantItemImage.class);

    Assertions.assertNull(mapper.toEntity(null, restaurantItemEntity));
    Assertions.assertNull(mapper.toEntity(model, (RestaurantItemEntity) null));
    Assertions.assertNull(mapper.toEntity(null, (RestaurantItemEntity) null));
  }

  @Test
  @DisplayName("Deve retornar null ao converter model null para entity no overload com RestaurantItemsImagesEntity")
  void shouldReturnNullWhenToEntityWithRestaurantItemsImagesEntityReceivesNullArguments() {
    final RestaurantItemImageEntity entity = Mockito.mock(RestaurantItemImageEntity.class);
    final RestaurantItemImage model = Mockito.mock(RestaurantItemImage.class);

    Assertions.assertThrows(
        NullPointerException.class,
        () -> mapper.toEntity(model, (RestaurantItemImageEntity) null)
    );

    Assertions.assertThrows(
        NullPointerException.class,
        () -> mapper.toEntity(null, (RestaurantItemImageEntity) null)
    );

    final var result = mapper.toEntity(model, entity);
    Assertions.assertNotNull(result);
  }

  @Test
  @DisplayName("Deve converter model para entity corretamente")
  void shouldConvertModelToEntityCorrectly() {
    final var uuid = UUID.randomUUID();

    final var model = new RestaurantItemImage(
        uuid,
        "IMAGE_01",
        "Image description",
        "/images/image-01.png"
    );

    final RestaurantItemEntity restaurantItemEntity = Mockito.mock(RestaurantItemEntity.class);

    final RestaurantItemImageEntity result = mapper.toEntity(model, restaurantItemEntity);

    Assertions.assertNotNull(result);
    Assertions.assertEquals(uuid, result.getUuid());
    Assertions.assertEquals("IMAGE_01", result.getName());
    Assertions.assertEquals("Image description", result.getDescription());
    Assertions.assertEquals("/images/image-01.png", result.getPath());
    Assertions.assertEquals(restaurantItemEntity, result.getRestaurantItems());
  }

  @Test
  @DisplayName("Deve atualizar a entity existente com base no model")
  void shouldUpdateExistingEntityUsingModel() {
    final var entity = new RestaurantItemImageEntity();
    entity.setUuid(UUID.randomUUID());
    entity.setName("OLD_NAME");
    entity.setDescription("Old description");
    entity.setPath("/images/old.png");

    final var model = new RestaurantItemImage(
        UUID.randomUUID(),
        "NEW_NAME",
        "New description",
        "/images/new.png"
    );

    final RestaurantItemImageEntity result = mapper.toEntity(model, entity);

    Assertions.assertSame(entity, result);
    Assertions.assertEquals("NEW_NAME", result.getName());
    Assertions.assertEquals("New description", result.getDescription());

    Assertions.assertEquals("/images/old.png", result.getPath());
  }
}
