package com.connectfood.core.application.restaurantopeninghours.usecase;

import java.util.Optional;
import java.util.UUID;

import com.connectfood.core.application.restaurantopeninghours.dto.RestaurantOpeningHoursInput;
import com.connectfood.core.application.restaurantopeninghours.dto.RestaurantOpeningHoursOutput;
import com.connectfood.core.application.restaurantopeninghours.mapper.RestaurantOpeningHoursAppMapper;
import com.connectfood.core.domain.exception.NotFoundException;
import com.connectfood.core.domain.model.RestaurantOpeningHours;
import com.connectfood.core.domain.model.Restaurants;
import com.connectfood.core.domain.repository.RestaurantOpeningHoursRepository;
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
class CreateRestaurantOpeningHoursUseCaseTest {

  @Mock
  private RestaurantOpeningHoursRepository repository;

  @Mock
  private RestaurantOpeningHoursAppMapper mapper;

  @Mock
  private RestaurantsRepository restaurantsRepository;

  @InjectMocks
  private CreateRestaurantOpeningHoursUseCase useCase;

  @Test
  @DisplayName("Deve criar horário de funcionamento quando restaurant existir")
  void shouldCreateOpeningHoursWhenRestaurantExists() {
    final var restaurantUuid = UUID.randomUUID();

    final var input = Mockito.mock(RestaurantOpeningHoursInput.class);
    Mockito.when(input.getRestaurantUuid())
        .thenReturn(restaurantUuid);

    final Restaurants restaurants = Mockito.mock(Restaurants.class);
    Mockito.when(restaurantsRepository.findByUuid(restaurantUuid))
        .thenReturn(Optional.of(restaurants));

    final RestaurantOpeningHours domain = Mockito.mock(RestaurantOpeningHours.class);
    Mockito.when(mapper.toDomain(input, restaurants))
        .thenReturn(domain);

    final RestaurantOpeningHours saved = Mockito.mock(RestaurantOpeningHours.class);
    Mockito.when(repository.save(domain))
        .thenReturn(saved);

    final RestaurantOpeningHoursOutput output = Mockito.mock(RestaurantOpeningHoursOutput.class);
    Mockito.when(mapper.toOutput(saved))
        .thenReturn(output);

    final var result = useCase.execute(input);

    Assertions.assertNotNull(result);
    Assertions.assertSame(output, result);

    Mockito.verify(restaurantsRepository, Mockito.times(1))
        .findByUuid(restaurantUuid);
    Mockito.verify(mapper, Mockito.times(1))
        .toDomain(input, restaurants);
    Mockito.verify(repository, Mockito.times(1))
        .save(domain);
    Mockito.verify(mapper, Mockito.times(1))
        .toOutput(saved);
    Mockito.verifyNoMoreInteractions(restaurantsRepository, repository, mapper);
  }

  @Test
  @DisplayName("Não deve criar horário de funcionamento quando restaurant não existir")
  void shouldThrowNotFoundExceptionWhenRestaurantDoesNotExist() {
    final var restaurantUuid = UUID.randomUUID();

    final var input = Mockito.mock(RestaurantOpeningHoursInput.class);
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
    Mockito.verifyNoInteractions(repository, mapper);
    Mockito.verifyNoMoreInteractions(restaurantsRepository);
  }
}
