package com.connectfood.infrastructure.persistence.mappers;

import java.util.UUID;

import com.connectfood.core.domain.model.RestaurantItemsImages;
import com.connectfood.infrastructure.persistence.entity.RestaurantItemsEntity;
import com.connectfood.infrastructure.persistence.entity.RestaurantItemsImagesEntity;
import com.connectfood.infrastructure.persistence.mappers.RestaurantItemsImageInfraMapper;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class RestaurantItemsImageInfraMapperTest {

  private final RestaurantItemsImageInfraMapper mapper = new RestaurantItemsImageInfraMapper();

  @Test
  @DisplayName("Deve criar o mapper pelo construtor padrão")
  void shouldCreateMapperUsingDefaultConstructor() {
    final var instance = new RestaurantItemsImageInfraMapper();

    Assertions.assertNotNull(instance);
  }

  @Test
  @DisplayName("Deve retornar null ao converter entity null para domínio")
  void shouldReturnNullWhenToDomainReceivesNull() {
    final RestaurantItemsImages result = mapper.toDomain(null);

    Assertions.assertNull(result);
  }

  @Test
  @DisplayName("Deve converter entity para domínio corretamente")
  void shouldConvertEntityToDomainCorrectly() {
    final var uuid = UUID.randomUUID();

    final var entity = new RestaurantItemsImagesEntity();
    entity.setUuid(uuid);
    entity.setName("IMAGE_01");
    entity.setDescription("Image description");
    entity.setPath("/images/image-01.png");

    final RestaurantItemsImages result = mapper.toDomain(entity);

    Assertions.assertNotNull(result);
    Assertions.assertEquals(uuid, result.getUuid());
    Assertions.assertEquals("IMAGE_01", result.getName());
    Assertions.assertEquals("Image description", result.getDescription());
    Assertions.assertEquals("/images/image-01.png", result.getPath());
  }

  @Test
  @DisplayName("Deve retornar null ao converter model null para entity no overload com RestaurantItemsEntity")
  void shouldReturnNullWhenToEntityWithRestaurantItemsEntityReceivesNullArguments() {
    final RestaurantItemsEntity restaurantItemsEntity = Mockito.mock(RestaurantItemsEntity.class);
    final RestaurantItemsImages model = Mockito.mock(RestaurantItemsImages.class);

    Assertions.assertNull(mapper.toEntity(null, restaurantItemsEntity));
    Assertions.assertNull(mapper.toEntity(model, (RestaurantItemsEntity) null));
    Assertions.assertNull(mapper.toEntity(null, (RestaurantItemsEntity) null));
  }

  @Test
  @DisplayName("Deve retornar null ao converter model null para entity no overload com RestaurantItemsImagesEntity")
  void shouldReturnNullWhenToEntityWithRestaurantItemsImagesEntityReceivesNullArguments() {
    final RestaurantItemsImagesEntity entity = Mockito.mock(RestaurantItemsImagesEntity.class);
    final RestaurantItemsImages model = Mockito.mock(RestaurantItemsImages.class);

    Assertions.assertThrows(
        NullPointerException.class,
        () -> mapper.toEntity(model, (RestaurantItemsImagesEntity) null)
    );

    Assertions.assertThrows(
        NullPointerException.class,
        () -> mapper.toEntity(null, (RestaurantItemsImagesEntity) null)
    );

    final var result = mapper.toEntity(model, entity);
    Assertions.assertNotNull(result);
  }

  @Test
  @DisplayName("Deve converter model para entity corretamente")
  void shouldConvertModelToEntityCorrectly() {
    final var uuid = UUID.randomUUID();

    final var model = new RestaurantItemsImages(
        uuid,
        "IMAGE_01",
        "Image description",
        "/images/image-01.png"
    );

    final RestaurantItemsEntity restaurantItemsEntity = Mockito.mock(RestaurantItemsEntity.class);

    final RestaurantItemsImagesEntity result = mapper.toEntity(model, restaurantItemsEntity);

    Assertions.assertNotNull(result);
    Assertions.assertEquals(uuid, result.getUuid());
    Assertions.assertEquals("IMAGE_01", result.getName());
    Assertions.assertEquals("Image description", result.getDescription());
    Assertions.assertEquals("/images/image-01.png", result.getPath());
    Assertions.assertEquals(restaurantItemsEntity, result.getRestaurantItems());
  }

  @Test
  @DisplayName("Deve atualizar a entity existente com base no model")
  void shouldUpdateExistingEntityUsingModel() {
    final var entity = new RestaurantItemsImagesEntity();
    entity.setUuid(UUID.randomUUID());
    entity.setName("OLD_NAME");
    entity.setDescription("Old description");
    entity.setPath("/images/old.png");

    final var model = new RestaurantItemsImages(
        UUID.randomUUID(),
        "NEW_NAME",
        "New description",
        "/images/new.png"
    );

    final RestaurantItemsImagesEntity result = mapper.toEntity(model, entity);

    Assertions.assertSame(entity, result);
    Assertions.assertEquals("NEW_NAME", result.getName());
    Assertions.assertEquals("New description", result.getDescription());

    Assertions.assertEquals("/images/old.png", result.getPath());
  }
}
