package com.connectfood.core.application.restaurantitems.usecase;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.connectfood.core.application.restaurantitems.dto.RestaurantItemsImagesInput;
import com.connectfood.core.application.restaurantitems.dto.RestaurantItemsInput;
import com.connectfood.core.application.restaurantitems.dto.RestaurantItemsOutput;
import com.connectfood.core.application.restaurantitems.mapper.RestaurantItemsAppMapper;
import com.connectfood.core.application.restaurantitems.mapper.RestaurantItemsImagesAppMapper;
import com.connectfood.core.application.security.RequestUser;
import com.connectfood.core.application.security.RequestUserGuard;
import com.connectfood.core.domain.exception.NotFoundException;
import com.connectfood.core.domain.model.RestaurantItems;
import com.connectfood.core.domain.model.RestaurantItemsImages;
import com.connectfood.core.domain.model.Restaurants;
import com.connectfood.core.domain.repository.RestaurantItemsImagesGateway;
import com.connectfood.core.domain.repository.RestaurantItemsGateway;
import com.connectfood.core.domain.repository.RestaurantsGateway;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CreateRestaurantItemsUseCaseTest {

  @Mock
  private RestaurantItemsGateway repository;

  @Mock
  private RestaurantItemsAppMapper mapper;

  @Mock
  private RequestUserGuard guard;

  @Mock
  private RestaurantsGateway restaurantsGateway;

  @Mock
  private RestaurantItemsImagesAppMapper restaurantItemsImagesMapper;

  @Mock
  private RestaurantItemsImagesGateway restaurantItemsImagesGateway;

  @InjectMocks
  private CreateRestaurantItemsUseCase useCase;

  @Test
  @DisplayName("Deve criar item de restaurante com imagens quando restaurant existir e retornar output")
  void shouldCreateRestaurantItemsWithImagesWhenRestaurantExistsAndReturnOutput() {
    final var requestUser = new RequestUser(UUID.randomUUID());
    final var restaurantUuid = UUID.randomUUID();

    final var imageInput1 = Mockito.mock(RestaurantItemsImagesInput.class);
    final var imageInput2 = Mockito.mock(RestaurantItemsImagesInput.class);

    final var input = Mockito.mock(RestaurantItemsInput.class);
    Mockito.when(input.getRestaurantUuid())
        .thenReturn(restaurantUuid);
    Mockito.when(input.getImages())
        .thenReturn(List.of(imageInput1, imageInput2));

    final Restaurants restaurants = Mockito.mock(Restaurants.class);
    Mockito.when(restaurantsGateway.findByUuid(restaurantUuid))
        .thenReturn(Optional.of(restaurants));

    final RestaurantItems domainToSave = Mockito.mock(RestaurantItems.class);
    Mockito.when(mapper.toDomain(input, restaurants))
        .thenReturn(domainToSave);

    final var itemUuid = UUID.randomUUID();
    final RestaurantItems savedModel = Mockito.mock(RestaurantItems.class);
    Mockito.when(savedModel.getUuid())
        .thenReturn(itemUuid);
    Mockito.when(repository.save(domainToSave))
        .thenReturn(savedModel);

    final RestaurantItemsImages imgDomain1 = Mockito.mock(RestaurantItemsImages.class);
    final RestaurantItemsImages imgDomain2 = Mockito.mock(RestaurantItemsImages.class);
    Mockito.when(restaurantItemsImagesMapper.toDomain(imageInput1))
        .thenReturn(imgDomain1);
    Mockito.when(restaurantItemsImagesMapper.toDomain(imageInput2))
        .thenReturn(imgDomain2);

    final RestaurantItemsImages persisted1 = Mockito.mock(RestaurantItemsImages.class);
    final RestaurantItemsImages persisted2 = Mockito.mock(RestaurantItemsImages.class);
    Mockito.when(restaurantItemsImagesGateway.save(itemUuid, imgDomain1))
        .thenReturn(persisted1);
    Mockito.when(restaurantItemsImagesGateway.save(itemUuid, imgDomain2))
        .thenReturn(persisted2);

    final RestaurantItemsOutput expectedOutput = Mockito.mock(RestaurantItemsOutput.class);
    Mockito.when(mapper.toOutput(savedModel, List.of(persisted1, persisted2)))
        .thenReturn(expectedOutput);

    final var result = useCase.execute(requestUser, input);

    Assertions.assertSame(expectedOutput, result);

    Mockito.verify(guard, Mockito.times(1))
        .requireRole(requestUser, "OWNER");

    Mockito.verify(restaurantsGateway, Mockito.times(1))
        .findByUuid(restaurantUuid);
    Mockito.verify(mapper, Mockito.times(1))
        .toDomain(input, restaurants);
    Mockito.verify(repository, Mockito.times(1))
        .save(domainToSave);

    Mockito.verify(restaurantItemsImagesMapper, Mockito.times(1))
        .toDomain(imageInput1);
    Mockito.verify(restaurantItemsImagesMapper, Mockito.times(1))
        .toDomain(imageInput2);

    Mockito.verify(restaurantItemsImagesGateway, Mockito.times(1))
        .save(itemUuid, imgDomain1);
    Mockito.verify(restaurantItemsImagesGateway, Mockito.times(1))
        .save(itemUuid, imgDomain2);

    Mockito.verify(mapper, Mockito.times(1))
        .toOutput(savedModel, List.of(persisted1, persisted2));

    Mockito.verifyNoMoreInteractions(
        guard,
        restaurantsGateway,
        mapper,
        repository,
        restaurantItemsImagesMapper,
        restaurantItemsImagesGateway
    );
  }

  @Test
  @DisplayName("Deve criar item de restaurante quando images for lista vazia e retornar output")
  void shouldCreateRestaurantItemsWithEmptyImagesListAndReturnOutput() {
    final var requestUser = new RequestUser(UUID.randomUUID());
    final var restaurantUuid = UUID.randomUUID();

    final var input = Mockito.mock(RestaurantItemsInput.class);
    Mockito.when(input.getRestaurantUuid())
        .thenReturn(restaurantUuid);
    Mockito.when(input.getImages())
        .thenReturn(List.of());

    final Restaurants restaurants = Mockito.mock(Restaurants.class);
    Mockito.when(restaurantsGateway.findByUuid(restaurantUuid))
        .thenReturn(Optional.of(restaurants));

    final RestaurantItems domainToSave = Mockito.mock(RestaurantItems.class);
    Mockito.when(mapper.toDomain(input, restaurants))
        .thenReturn(domainToSave);

    final RestaurantItems savedModel = Mockito.mock(RestaurantItems.class);
    Mockito.when(repository.save(domainToSave))
        .thenReturn(savedModel);

    final RestaurantItemsOutput expectedOutput = Mockito.mock(RestaurantItemsOutput.class);
    Mockito.when(mapper.toOutput(savedModel, List.of()))
        .thenReturn(expectedOutput);

    final var result = useCase.execute(requestUser, input);

    Assertions.assertSame(expectedOutput, result);

    Mockito.verify(guard, Mockito.times(1))
        .requireRole(requestUser, "OWNER");

    Mockito.verify(restaurantsGateway, Mockito.times(1))
        .findByUuid(restaurantUuid);
    Mockito.verify(mapper, Mockito.times(1))
        .toDomain(input, restaurants);
    Mockito.verify(repository, Mockito.times(1))
        .save(domainToSave);
    Mockito.verify(mapper, Mockito.times(1))
        .toOutput(savedModel, List.of());

    Mockito.verifyNoInteractions(restaurantItemsImagesMapper, restaurantItemsImagesGateway);

    Mockito.verifyNoMoreInteractions(guard, restaurantsGateway, mapper, repository);
  }

  @Test
  @DisplayName("Deve criar item de restaurante quando images for null e retornar output sem salvar imagens")
  void shouldCreateRestaurantItemsWithNullImagesAndReturnOutput() {
    final var requestUser = new RequestUser(UUID.randomUUID());
    final var restaurantUuid = UUID.randomUUID();

    final var input = Mockito.mock(RestaurantItemsInput.class);
    Mockito.when(input.getRestaurantUuid())
        .thenReturn(restaurantUuid);
    Mockito.when(input.getImages())
        .thenReturn(null);

    final Restaurants restaurants = Mockito.mock(Restaurants.class);
    Mockito.when(restaurantsGateway.findByUuid(restaurantUuid))
        .thenReturn(Optional.of(restaurants));

    final RestaurantItems domainToSave = Mockito.mock(RestaurantItems.class);
    Mockito.when(mapper.toDomain(input, restaurants))
        .thenReturn(domainToSave);

    final RestaurantItems savedModel = Mockito.mock(RestaurantItems.class);
    Mockito.when(repository.save(domainToSave))
        .thenReturn(savedModel);

    final RestaurantItemsOutput expectedOutput = Mockito.mock(RestaurantItemsOutput.class);
    Mockito.when(mapper.toOutput(savedModel, List.of()))
        .thenReturn(expectedOutput);

    final var result = useCase.execute(requestUser, input);

    Assertions.assertSame(expectedOutput, result);

    Mockito.verify(guard, Mockito.times(1))
        .requireRole(requestUser, "OWNER");

    Mockito.verify(restaurantsGateway, Mockito.times(1))
        .findByUuid(restaurantUuid);
    Mockito.verify(mapper, Mockito.times(1))
        .toDomain(input, restaurants);
    Mockito.verify(repository, Mockito.times(1))
        .save(domainToSave);
    Mockito.verify(mapper, Mockito.times(1))
        .toOutput(savedModel, List.of());

    Mockito.verifyNoInteractions(restaurantItemsImagesMapper, restaurantItemsImagesGateway);

    Mockito.verifyNoMoreInteractions(guard, restaurantsGateway, mapper, repository);
  }

  @Test
  @DisplayName("Não deve criar item de restaurante quando restaurant não existir e deve lançar NotFoundException")
  void shouldThrowNotFoundExceptionWhenRestaurantDoesNotExist() {
    final var requestUser = new RequestUser(UUID.randomUUID());
    final var restaurantUuid = UUID.randomUUID();

    final var input = Mockito.mock(RestaurantItemsInput.class);
    Mockito.when(input.getRestaurantUuid())
        .thenReturn(restaurantUuid);

    Mockito.when(restaurantsGateway.findByUuid(restaurantUuid))
        .thenReturn(Optional.empty());

    final var exception = Assertions.assertThrows(
        NotFoundException.class,
        () -> useCase.execute(requestUser, input)
    );

    Assertions.assertEquals("Restaurant not found", exception.getMessage());

    Mockito.verify(guard, Mockito.times(1))
        .requireRole(requestUser, "OWNER");

    Mockito.verify(restaurantsGateway, Mockito.times(1))
        .findByUuid(restaurantUuid);

    Mockito.verifyNoInteractions(mapper, repository, restaurantItemsImagesMapper, restaurantItemsImagesGateway);

    Mockito.verifyNoMoreInteractions(guard, restaurantsGateway);
  }
}
