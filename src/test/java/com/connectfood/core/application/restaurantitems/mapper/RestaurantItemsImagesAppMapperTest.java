package com.connectfood.core.application.restaurantitems.mapper;

import java.util.UUID;

import com.connectfood.core.application.restaurantitems.dto.RestaurantItemsImagesInput;
import com.connectfood.core.application.restaurantitems.dto.RestaurantItemsImagesOutput;
import com.connectfood.core.domain.model.RestaurantItemImage;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class RestaurantItemsImagesAppMapperTest {

  @InjectMocks
  private RestaurantItemsImagesAppMapper mapper;

  @Test
  @DisplayName("Não deve criar domínio quando input for nulo")
  void shouldReturnNullWhenInputIsNull() {
    final var result = mapper.toDomain((RestaurantItemsImagesInput) null);

    Assertions.assertNull(result);
  }

  @Test
  @DisplayName("Deve criar domínio a partir do input informado")
  void shouldCreateDomainFromInput() {
    final var input = Mockito.mock(RestaurantItemsImagesInput.class);
    Mockito.when(input.getName())
        .thenReturn("image.png");
    Mockito.when(input.getDescription())
        .thenReturn("Main image");
    Mockito.when(input.getPath())
        .thenReturn("/images/image.png");

    final var result = mapper.toDomain(input);

    Assertions.assertNotNull(result);
    Assertions.assertNotNull(result.getUuid(), "UUID deve ser gerado pelo domínio quando não informado");
    Assertions.assertEquals("image.png", result.getName());
    Assertions.assertEquals("Main image", result.getDescription());
    Assertions.assertEquals("/images/image.png", result.getPath());

    Mockito.verify(input, Mockito.times(1))
        .getName();
    Mockito.verify(input, Mockito.times(1))
        .getDescription();
    Mockito.verify(input, Mockito.times(1))
        .getPath();
    Mockito.verifyNoMoreInteractions(input);
  }

  @Test
  @DisplayName("Não deve criar domínio quando input for nulo mesmo com uuid informado")
  void shouldReturnNullWhenInputIsNullEvenWithUuid() {
    final var uuid = UUID.randomUUID();

    final var result = mapper.toDomain(uuid, null);

    Assertions.assertNull(result);
  }

  @Test
  @DisplayName("Deve criar domínio com uuid quando input estiver preenchido")
  void shouldCreateDomainWithUuidFromInput() {
    final var uuid = UUID.randomUUID();

    final var input = Mockito.mock(RestaurantItemsImagesInput.class);
    Mockito.when(input.getName())
        .thenReturn("banner.jpg");
    Mockito.when(input.getDescription())
        .thenReturn("Banner");
    Mockito.when(input.getPath())
        .thenReturn("/images/banner.jpg");

    final var result = mapper.toDomain(uuid, input);

    Assertions.assertNotNull(result);
    Assertions.assertEquals(uuid, result.getUuid());
    Assertions.assertEquals("banner.jpg", result.getName());
    Assertions.assertEquals("Banner", result.getDescription());
    Assertions.assertEquals("/images/banner.jpg", result.getPath());

    Mockito.verify(input, Mockito.times(1))
        .getName();
    Mockito.verify(input, Mockito.times(1))
        .getDescription();
    Mockito.verify(input, Mockito.times(1))
        .getPath();
    Mockito.verifyNoMoreInteractions(input);
  }

  @Test
  @DisplayName("Não deve criar output quando domínio for nulo")
  void shouldReturnNullWhenDomainIsNull() {
    final var result = mapper.toOutput(null);

    Assertions.assertNull(result);
  }

  @Test
  @DisplayName("Deve criar output a partir do domínio informado")
  void shouldCreateOutputFromDomain() {
    final var uuid = UUID.randomUUID();

    final var model = Mockito.mock(RestaurantItemImage.class);
    Mockito.when(model.getUuid())
        .thenReturn(uuid);
    Mockito.when(model.getName())
        .thenReturn("photo.webp");
    Mockito.when(model.getDescription())
        .thenReturn("Thumbnail");
    Mockito.when(model.getPath())
        .thenReturn("/images/photo.webp");

    final RestaurantItemsImagesOutput result = mapper.toOutput(model);

    Assertions.assertNotNull(result);
    Assertions.assertEquals(uuid, result.getUuid());
    Assertions.assertEquals("photo.webp", result.getName());
    Assertions.assertEquals("Thumbnail", result.getDescription());
    Assertions.assertEquals("/images/photo.webp", result.getPath());

    Mockito.verify(model, Mockito.times(1))
        .getUuid();
    Mockito.verify(model, Mockito.times(1))
        .getName();
    Mockito.verify(model, Mockito.times(1))
        .getDescription();
    Mockito.verify(model, Mockito.times(1))
        .getPath();
    Mockito.verifyNoMoreInteractions(model);
  }
}
