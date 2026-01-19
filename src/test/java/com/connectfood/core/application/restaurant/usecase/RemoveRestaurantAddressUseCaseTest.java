package com.connectfood.core.application.restaurant.usecase;

import java.util.Optional;
import java.util.UUID;

import com.connectfood.core.application.security.RequestUser;
import com.connectfood.core.application.security.RequestUserGuard;
import com.connectfood.core.domain.exception.NotFoundException;
import com.connectfood.core.domain.model.Address;
import com.connectfood.core.domain.model.RestaurantAddress;
import com.connectfood.core.domain.model.enums.UsersType;
import com.connectfood.core.domain.repository.AddressGateway;
import com.connectfood.core.domain.repository.RestaurantAddressGateway;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class RemoveRestaurantAddressUseCaseTest {

  @Mock
  private RestaurantAddressGateway repository;

  @Mock
  private AddressGateway addressGateway;

  @Mock
  private RequestUserGuard guard;

  @InjectMocks
  private RemoveRestaurantAddressUseCase useCase;

  @Test
  @DisplayName("Deve lançar NotFoundException quando RestaurantsAddress não existir para o restaurantsUuid informado")
  void shouldThrowNotFoundExceptionWhenRestaurantsAddressNotFound() {
    final var requestUserUuid = UUID.randomUUID();
    final var requestUser = new RequestUser(requestUserUuid);

    final var restaurantsUuid = UUID.randomUUID();

    Mockito.when(repository.findByRestaurantsUuid(restaurantsUuid))
        .thenReturn(Optional.empty());

    final var ex = Assertions.assertThrows(
        NotFoundException.class,
        () -> useCase.execute(requestUser, restaurantsUuid)
    );

    Assertions.assertEquals("Restaurants Address Not Found", ex.getMessage());

    Mockito.verify(guard, Mockito.times(1))
        .requireRole(requestUser, UsersType.OWNER.name());

    Mockito.verify(repository, Mockito.times(1))
        .findByRestaurantsUuid(restaurantsUuid);

    Mockito.verifyNoInteractions(addressGateway);
    Mockito.verifyNoMoreInteractions(guard, repository);
  }

  @Test
  @DisplayName("Deve remover vínculo e remover address relacionado quando RestaurantsAddress existir")
  void shouldDeleteRestaurantsAddressAndAddressWhenFound() {
    final var requestUserUuid = UUID.randomUUID();
    final var requestUser = new RequestUser(requestUserUuid);

    final var restaurantsUuid = UUID.randomUUID();
    final var restaurantsAddressUuid = UUID.randomUUID();
    final var addressUuid = UUID.randomUUID();

    final Address address = Mockito.mock(Address.class);
    Mockito.when(address.getUuid())
        .thenReturn(addressUuid);

    final RestaurantAddress restaurantAddress = Mockito.mock(RestaurantAddress.class);
    Mockito.when(restaurantAddress.getUuid())
        .thenReturn(restaurantsAddressUuid);
    Mockito.when(restaurantAddress.getAddress())
        .thenReturn(address);

    Mockito.when(repository.findByRestaurantsUuid(restaurantsUuid))
        .thenReturn(Optional.of(restaurantAddress));

    Assertions.assertDoesNotThrow(() -> useCase.execute(requestUser, restaurantsUuid));

    Mockito.verify(guard, Mockito.times(1))
        .requireRole(requestUser, UsersType.OWNER.name());

    Mockito.verify(repository, Mockito.times(1))
        .findByRestaurantsUuid(restaurantsUuid);

    Mockito.verify(repository, Mockito.times(1))
        .delete(restaurantsAddressUuid);

    Mockito.verify(addressGateway, Mockito.times(1))
        .delete(addressUuid);

    Mockito.verifyNoMoreInteractions(guard, repository, addressGateway);
  }
}
