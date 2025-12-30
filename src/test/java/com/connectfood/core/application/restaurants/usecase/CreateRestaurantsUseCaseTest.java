package com.connectfood.core.application.restaurants.usecase;

import java.util.Optional;
import java.util.UUID;

import com.connectfood.core.application.restaurants.dto.RestaurantsInput;
import com.connectfood.core.application.restaurants.dto.RestaurantsOutput;
import com.connectfood.core.application.restaurants.mapper.RestaurantsAppMapper;
import com.connectfood.core.domain.exception.NotFoundException;
import com.connectfood.core.domain.model.Restaurants;
import com.connectfood.core.domain.model.RestaurantsType;
import com.connectfood.core.domain.repository.RestaurantsRepository;
import com.connectfood.core.domain.repository.RestaurantsTypeRepository;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CreateRestaurantsUseCaseTest {

  @Mock
  private RestaurantsRepository repository;

  @Mock
  private RestaurantsAppMapper mapper;

  @Mock
  private RestaurantsTypeRepository restaurantsTypeRepository;

  @InjectMocks
  private CreateRestaurantsUseCase useCase;

  @Test
  @DisplayName("Não deve criar restaurante quando tipo de restaurante não existir")
  void shouldThrowExceptionWhenRestaurantsTypeNotFound() {
    final var restaurantsTypeUuid = UUID.randomUUID();
    final var input = new RestaurantsInput("Restaurant A", restaurantsTypeUuid);

    Mockito.when(restaurantsTypeRepository.findById(restaurantsTypeUuid))
        .thenReturn(Optional.empty());

    final var exception = Assertions.assertThrows(
        NotFoundException.class,
        () -> useCase.execute(input)
    );

    Assertions.assertEquals("Restaurants Type not found", exception.getMessage());

    Mockito.verify(restaurantsTypeRepository, Mockito.times(1))
        .findById(restaurantsTypeUuid);
    Mockito.verifyNoInteractions(repository, mapper);
  }

  @Test
  @DisplayName("Deve criar restaurante quando dados forem válidos")
  void shouldCreateRestaurantWhenDataIsValid() {
    final var restaurantsTypeUuid = UUID.randomUUID();
    final var input = new RestaurantsInput("Restaurant B", restaurantsTypeUuid);

    final RestaurantsType restaurantsType = Mockito.mock(RestaurantsType.class);
    final Restaurants restaurantsDomain = Mockito.mock(Restaurants.class);
    final RestaurantsOutput output = Mockito.mock(RestaurantsOutput.class);

    Mockito.when(restaurantsTypeRepository.findById(restaurantsTypeUuid))
        .thenReturn(Optional.of(restaurantsType));

    Mockito.when(mapper.toDomain(input, restaurantsType))
        .thenReturn(restaurantsDomain);

    Mockito.when(repository.save(restaurantsDomain))
        .thenReturn(restaurantsDomain);

    Mockito.when(mapper.toOutput(restaurantsDomain))
        .thenReturn(output);

    final var result = useCase.execute(input);

    Assertions.assertNotNull(result);
    Assertions.assertSame(output, result);

    Mockito.verify(restaurantsTypeRepository, Mockito.times(1))
        .findById(restaurantsTypeUuid);
    Mockito.verify(mapper, Mockito.times(1))
        .toDomain(input, restaurantsType);
    Mockito.verify(repository, Mockito.times(1))
        .save(restaurantsDomain);
    Mockito.verify(mapper, Mockito.times(1))
        .toOutput(restaurantsDomain);
  }
}
