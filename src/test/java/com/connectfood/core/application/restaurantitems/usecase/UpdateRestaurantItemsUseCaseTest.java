package com.connectfood.core.application.restaurantitems.usecase;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import com.connectfood.core.application.restaurantitems.dto.RestaurantItemsInput;
import com.connectfood.core.application.restaurantitems.dto.RestaurantItemsOutput;
import com.connectfood.core.application.restaurantitems.mapper.RestaurantItemsAppMapper;
import com.connectfood.core.domain.exception.NotFoundException;
import com.connectfood.core.domain.model.RestaurantItems;
import com.connectfood.core.domain.model.Restaurants;
import com.connectfood.core.domain.model.enums.RestaurantItemServiceType;
import com.connectfood.core.domain.repository.RestaurantItemsRepository;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class UpdateRestaurantItemsUseCaseTest {

  @Mock
  private RestaurantItemsRepository repository;

  @Mock
  private RestaurantItemsAppMapper mapper;

  @InjectMocks
  private UpdateRestaurantItemsUseCase useCase;

  @Test
  @DisplayName("Deve atualizar item de restaurante quando existir e retornar output")
  void shouldUpdateRestaurantItemsWhenExistsAndReturnOutput() {
    final var uuid = UUID.randomUUID();

    final var input = new RestaurantItemsInput(
        "ITEM",
        "DESC",
        BigDecimal.valueOf(12.34),
        RestaurantItemServiceType.DELIVERY,
        UUID.randomUUID()
    );

    final Restaurants restaurants = Mockito.mock(Restaurants.class);

    final RestaurantItems existingModel = Mockito.mock(RestaurantItems.class);
    Mockito.when(existingModel.getRestaurant())
        .thenReturn(restaurants);

    Mockito.when(repository.findByUuid(uuid))
        .thenReturn(Optional.of(existingModel));

    final RestaurantItems domainToUpdate = Mockito.mock(RestaurantItems.class);
    Mockito.when(mapper.toDomain(uuid, input, restaurants))
        .thenReturn(domainToUpdate);

    final RestaurantItems updatedModel = Mockito.mock(RestaurantItems.class);
    Mockito.when(repository.update(uuid, domainToUpdate))
        .thenReturn(updatedModel);

    final RestaurantItemsOutput expectedOutput = Mockito.mock(RestaurantItemsOutput.class);
    Mockito.when(mapper.toOutput(updatedModel))
        .thenReturn(expectedOutput);

    final var result = useCase.execute(uuid, input);

    Assertions.assertSame(expectedOutput, result);

    Mockito.verify(repository, Mockito.times(1))
        .findByUuid(uuid);
    Mockito.verify(mapper, Mockito.times(1))
        .toDomain(uuid, input, restaurants);
    Mockito.verify(repository, Mockito.times(1))
        .update(uuid, domainToUpdate);
    Mockito.verify(mapper, Mockito.times(1))
        .toOutput(updatedModel);

    Mockito.verifyNoMoreInteractions(repository, mapper);
  }

  @Test
  @DisplayName("Não deve atualizar item de restaurante quando não existir e deve lançar NotFoundException")
  void shouldThrowNotFoundExceptionWhenRestaurantItemsDoesNotExist() {
    final var uuid = UUID.randomUUID();

    final var input = Mockito.mock(RestaurantItemsInput.class);

    Mockito.when(repository.findByUuid(uuid))
        .thenReturn(Optional.empty());

    final var exception = Assertions.assertThrows(
        NotFoundException.class,
        () -> useCase.execute(uuid, input)
    );

    Assertions.assertEquals("Restaurant Items not found", exception.getMessage());

    Mockito.verify(repository, Mockito.times(1))
        .findByUuid(uuid);
    Mockito.verifyNoInteractions(mapper);
    Mockito.verify(repository, Mockito.never())
        .update(Mockito.any(), Mockito.any());
  }
}
