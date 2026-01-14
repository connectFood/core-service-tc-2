package com.connectfood.core.application.restaurants.usecase;

import java.util.Optional;
import java.util.UUID;

import com.connectfood.core.domain.exception.NotFoundException;
import com.connectfood.core.domain.model.Address;
import com.connectfood.core.domain.model.RestaurantsAddress;
import com.connectfood.core.domain.repository.AddressRepository;
import com.connectfood.core.domain.repository.RestaurantsAddressRepository;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class RemoveRestaurantsAddressUseCaseTest {

  @Mock
  private RestaurantsAddressRepository repository;

  @Mock
  private AddressRepository addressRepository;

  @InjectMocks
  private RemoveRestaurantsAddressUseCase useCase;

  @Test
  @DisplayName("Deve lançar NotFoundException quando RestaurantsAddress não existir para o restaurantsUuid informado")
  void shouldThrowNotFoundExceptionWhenRestaurantsAddressNotFound() {
    final var restaurantsUuid = UUID.randomUUID();

    Mockito.when(repository.findByRestaurantsUuid(restaurantsUuid))
        .thenReturn(Optional.empty());

    final var ex = Assertions.assertThrows(
        NotFoundException.class,
        () -> useCase.execute(restaurantsUuid)
    );

    Assertions.assertEquals("Restaurants Address Not Found", ex.getMessage());

    Mockito.verify(repository, Mockito.times(1))
        .findByRestaurantsUuid(restaurantsUuid);
    Mockito.verifyNoInteractions(addressRepository);
    Mockito.verifyNoMoreInteractions(repository);
  }

  @Test
  @DisplayName("Deve remover vínculo e remover address relacionado quando RestaurantsAddress existir")
  void shouldDeleteRestaurantsAddressAndAddressWhenFound() {
    final var restaurantsUuid = UUID.randomUUID();
    final var restaurantsAddressUuid = UUID.randomUUID();
    final var addressUuid = UUID.randomUUID();

    final Address address = Mockito.mock(Address.class);
    Mockito.when(address.getUuid())
        .thenReturn(addressUuid);

    final RestaurantsAddress restaurantsAddress = Mockito.mock(RestaurantsAddress.class);
    Mockito.when(restaurantsAddress.getUuid())
        .thenReturn(restaurantsAddressUuid);
    Mockito.when(restaurantsAddress.getAddress())
        .thenReturn(address);

    Mockito.when(repository.findByRestaurantsUuid(restaurantsUuid))
        .thenReturn(Optional.of(restaurantsAddress));

    useCase.execute(restaurantsUuid);

    Mockito.verify(repository, Mockito.times(1))
        .findByRestaurantsUuid(restaurantsUuid);

    Mockito.verify(repository, Mockito.times(1))
        .delete(restaurantsAddressUuid);

    Mockito.verify(addressRepository, Mockito.times(1))
        .delete(addressUuid);

    Mockito.verifyNoMoreInteractions(repository, addressRepository);
  }
}
