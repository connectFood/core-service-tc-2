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
class UpdateRestaurantsUseCaseTest {

  @Mock
  private RestaurantsRepository repository;

  @Mock
  private RestaurantsAppMapper mapper;

  @Mock
  private RestaurantsTypeRepository restaurantsTypeRepository;

  @InjectMocks
  private UpdateRestaurantsUseCase useCase;

  @Test
  @DisplayName("Deve atualizar restaurante mantendo o tipo quando restaurantsTypeUuid for o mesmo")
  void shouldUpdateKeepingRestaurantsTypeWhenUuidIsTheSame() {
    final UUID restaurantUuid = UUID.randomUUID();
    final UUID restaurantsTypeUuid = UUID.randomUUID();

    final RestaurantsType currentType = Mockito.mock(RestaurantsType.class);
    Mockito.when(currentType.getUuid())
        .thenReturn(restaurantsTypeUuid);

    final Restaurants existingRestaurant = Mockito.mock(Restaurants.class);
    Mockito.when(existingRestaurant.getRestaurantsType())
        .thenReturn(currentType);

    Mockito.when(repository.findByUuid(restaurantUuid))
        .thenReturn(Optional.of(existingRestaurant));

    final RestaurantsInput input = new RestaurantsInput("New Name", restaurantsTypeUuid);

    final Restaurants mappedDomain = Mockito.mock(Restaurants.class);
    Mockito.when(mapper.toDomain(restaurantUuid, input, currentType))
        .thenReturn(mappedDomain);

    final Restaurants updatedRestaurant = Mockito.mock(Restaurants.class);
    Mockito.when(repository.update(restaurantUuid, mappedDomain))
        .thenReturn(updatedRestaurant);

    final RestaurantsOutput expectedOutput = Mockito.mock(RestaurantsOutput.class);
    Mockito.when(mapper.toOutput(updatedRestaurant))
        .thenReturn(expectedOutput);

    final RestaurantsOutput result = useCase.execute(restaurantUuid, input);

    Assertions.assertNotNull(result);
    Assertions.assertSame(expectedOutput, result);

    Mockito.verify(repository, Mockito.times(1))
        .findByUuid(restaurantUuid);

    Mockito.verifyNoInteractions(restaurantsTypeRepository);

    Mockito.verify(mapper, Mockito.times(1))
        .toDomain(restaurantUuid, input, currentType);

    Mockito.verify(repository, Mockito.times(1))
        .update(restaurantUuid, mappedDomain);

    Mockito.verify(mapper, Mockito.times(1))
        .toOutput(updatedRestaurant);
  }

  @Test
  @DisplayName("Deve atualizar restaurante buscando novo tipo quando restaurantsTypeUuid for diferente")
  void shouldUpdateFetchingNewRestaurantsTypeWhenUuidIsDifferent() {
    final UUID restaurantUuid = UUID.randomUUID();
    final UUID currentTypeUuid = UUID.randomUUID();
    final UUID newTypeUuid = UUID.randomUUID();

    final RestaurantsType currentType = Mockito.mock(RestaurantsType.class);
    Mockito.when(currentType.getUuid())
        .thenReturn(currentTypeUuid);

    final Restaurants existingRestaurant = Mockito.mock(Restaurants.class);
    Mockito.when(existingRestaurant.getRestaurantsType())
        .thenReturn(currentType);

    Mockito.when(repository.findByUuid(restaurantUuid))
        .thenReturn(Optional.of(existingRestaurant));

    final RestaurantsType newType = Mockito.mock(RestaurantsType.class);
    Mockito.when(restaurantsTypeRepository.findById(newTypeUuid))
        .thenReturn(Optional.of(newType));

    final RestaurantsInput input = new RestaurantsInput("New Name", newTypeUuid);

    final Restaurants mappedDomain = Mockito.mock(Restaurants.class);
    Mockito.when(mapper.toDomain(restaurantUuid, input, newType))
        .thenReturn(mappedDomain);

    final Restaurants updatedRestaurant = Mockito.mock(Restaurants.class);
    Mockito.when(repository.update(restaurantUuid, mappedDomain))
        .thenReturn(updatedRestaurant);

    final RestaurantsOutput expectedOutput = Mockito.mock(RestaurantsOutput.class);
    Mockito.when(mapper.toOutput(updatedRestaurant))
        .thenReturn(expectedOutput);

    final RestaurantsOutput result = useCase.execute(restaurantUuid, input);

    Assertions.assertNotNull(result);
    Assertions.assertSame(expectedOutput, result);

    Mockito.verify(repository, Mockito.times(1))
        .findByUuid(restaurantUuid);

    Mockito.verify(restaurantsTypeRepository, Mockito.times(1))
        .findById(newTypeUuid);

    Mockito.verify(mapper, Mockito.times(1))
        .toDomain(restaurantUuid, input, newType);

    Mockito.verify(repository, Mockito.times(1))
        .update(restaurantUuid, mappedDomain);

    Mockito.verify(mapper, Mockito.times(1))
        .toOutput(updatedRestaurant);
  }

  @Test
  @DisplayName("Não deve atualizar restaurante quando restaurante não existir")
  void shouldThrowWhenRestaurantDoesNotExist() {
    final UUID restaurantUuid = UUID.randomUUID();
    final RestaurantsInput input = new RestaurantsInput("Any", UUID.randomUUID());

    Mockito.when(repository.findByUuid(restaurantUuid))
        .thenReturn(Optional.empty());

    final var ex = Assertions.assertThrows(NotFoundException.class,
        () -> useCase.execute(restaurantUuid, input)
    );

    Assertions.assertEquals("Restaurants not found", ex.getMessage());

    Mockito.verify(repository, Mockito.times(1))
        .findByUuid(restaurantUuid);

    Mockito.verifyNoInteractions(restaurantsTypeRepository, mapper);
    Mockito.verify(repository, Mockito.never())
        .update(Mockito.any(), Mockito.any());
  }

  @Test
  @DisplayName("Não deve atualizar restaurante quando tipo informado não existir e uuid for diferente")
  void shouldThrowWhenRestaurantsTypeDoesNotExistAndUuidIsDifferent() {
    final UUID restaurantUuid = UUID.randomUUID();
    final UUID currentTypeUuid = UUID.randomUUID();
    final UUID newTypeUuid = UUID.randomUUID();

    final RestaurantsType currentType = Mockito.mock(RestaurantsType.class);
    Mockito.when(currentType.getUuid())
        .thenReturn(currentTypeUuid);

    final Restaurants existingRestaurant = Mockito.mock(Restaurants.class);
    Mockito.when(existingRestaurant.getRestaurantsType())
        .thenReturn(currentType);

    Mockito.when(repository.findByUuid(restaurantUuid))
        .thenReturn(Optional.of(existingRestaurant));

    Mockito.when(restaurantsTypeRepository.findById(newTypeUuid))
        .thenReturn(Optional.empty());

    final RestaurantsInput input = new RestaurantsInput("New Name", newTypeUuid);

    final var ex = Assertions.assertThrows(NotFoundException.class,
        () -> useCase.execute(restaurantUuid, input)
    );

    Assertions.assertEquals("Restaurant type not found", ex.getMessage());

    Mockito.verify(repository, Mockito.times(1))
        .findByUuid(restaurantUuid);

    Mockito.verify(restaurantsTypeRepository, Mockito.times(1))
        .findById(newTypeUuid);

    Mockito.verifyNoInteractions(mapper);
    Mockito.verify(repository, Mockito.never())
        .update(Mockito.any(), Mockito.any());
  }
}
