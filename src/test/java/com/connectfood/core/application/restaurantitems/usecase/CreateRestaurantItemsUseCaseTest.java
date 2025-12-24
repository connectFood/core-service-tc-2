package com.connectfood.core.application.restaurantitems.usecase;

import java.util.Optional;
import java.util.UUID;

import com.connectfood.core.application.restaurantitems.dto.RestaurantItemsInput;
import com.connectfood.core.application.restaurantitems.dto.RestaurantItemsOutput;
import com.connectfood.core.application.restaurantitems.mapper.RestaurantItemsAppMapper;
import com.connectfood.core.domain.exception.NotFoundException;
import com.connectfood.core.domain.model.RestaurantItems;
import com.connectfood.core.domain.model.Restaurants;
import com.connectfood.core.domain.repository.RestaurantItemsRepository;
import com.connectfood.core.domain.repository.RestaurantsRepository;

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
  private RestaurantItemsRepository repository;

  @Mock
  private RestaurantItemsAppMapper mapper;

  @Mock
  private RestaurantsRepository restaurantsRepository;

  @InjectMocks
  private CreateRestaurantItemsUseCase useCase;

  @Test
  @DisplayName("Deve criar item de restaurante quando restaurant existir e retornar output")
  void shouldCreateRestaurantItemsWhenRestaurantExistsAndReturnOutput() {
    final var restaurantUuid = UUID.randomUUID();

    final var input = Mockito.mock(RestaurantItemsInput.class);
    Mockito.when(input.getRestaurantUuid())
        .thenReturn(restaurantUuid);

    final Restaurants restaurants = Mockito.mock(Restaurants.class);
    Mockito.when(restaurantsRepository.findByUuid(restaurantUuid))
        .thenReturn(Optional.of(restaurants));

    final RestaurantItems domainToSave = Mockito.mock(RestaurantItems.class);
    Mockito.when(mapper.toDomain(input, restaurants))
        .thenReturn(domainToSave);

    final RestaurantItems savedModel = Mockito.mock(RestaurantItems.class);
    Mockito.when(repository.save(domainToSave))
        .thenReturn(savedModel);

    final RestaurantItemsOutput expectedOutput = Mockito.mock(RestaurantItemsOutput.class);
    Mockito.when(mapper.toOutput(savedModel))
        .thenReturn(expectedOutput);

    final var result = useCase.execute(input);

    Assertions.assertSame(expectedOutput, result);

    Mockito.verify(restaurantsRepository, Mockito.times(1))
        .findByUuid(restaurantUuid);
    Mockito.verify(mapper, Mockito.times(1))
        .toDomain(input, restaurants);
    Mockito.verify(repository, Mockito.times(1))
        .save(domainToSave);
    Mockito.verify(mapper, Mockito.times(1))
        .toOutput(savedModel);

    Mockito.verifyNoMoreInteractions(restaurantsRepository, mapper, repository);
  }

  @Test
  @DisplayName("Não deve criar item de restaurante quando restaurant não existir e deve lançar NotFoundException")
  void shouldThrowNotFoundExceptionWhenRestaurantDoesNotExist() {
    final var restaurantUuid = UUID.randomUUID();

    final var input = Mockito.mock(RestaurantItemsInput.class);
    Mockito.when(input.getRestaurantUuid())
        .thenReturn(restaurantUuid);

    Mockito.when(restaurantsRepository.findByUuid(restaurantUuid))
        .thenReturn(Optional.empty());

    final var exception = Assertions.assertThrows(
        NotFoundException.class,
        () -> useCase.execute(input)
    );

    Assertions.assertEquals("Restaurant not found", exception.getMessage());

    Mockito.verify(restaurantsRepository, Mockito.times(1))
        .findByUuid(restaurantUuid);
    Mockito.verifyNoInteractions(mapper, repository);
  }
}
