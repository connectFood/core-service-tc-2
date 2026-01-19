package com.connectfood.core.application.restaurant.usecase;

import java.util.Optional;
import java.util.UUID;

import com.connectfood.core.application.address.dto.AddressInput;
import com.connectfood.core.application.address.dto.AddressOutput;
import com.connectfood.core.application.address.mapper.AddressAppMapper;
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
class UpdateRestaurantAddressUseCaseTest {

  @Mock
  private RestaurantAddressGateway repository;

  @Mock
  private AddressAppMapper mapper;

  @Mock
  private RequestUserGuard guard;

  @Mock
  private AddressGateway addressGateway;

  @InjectMocks
  private UpdateRestaurantAddressUseCase useCase;

  @Test
  @DisplayName("Deve atualizar endereço quando RestaurantsAddress existir")
  void shouldUpdateAddressWhenRestaurantsAddressExists() {
    final var requestUserUuid = UUID.randomUUID();
    final var requestUser = new RequestUser(requestUserUuid);

    final var restaurantUuid = UUID.randomUUID();
    final AddressInput input = Mockito.mock(AddressInput.class);

    final RestaurantAddress restaurantAddress = Mockito.mock(RestaurantAddress.class);
    final Address currentAddress = Mockito.mock(Address.class);
    final var addressUuid = UUID.randomUUID();

    Mockito.when(repository.findByRestaurantsUuid(restaurantUuid))
        .thenReturn(Optional.of(restaurantAddress));

    Mockito.when(restaurantAddress.getAddress())
        .thenReturn(currentAddress);

    Mockito.when(currentAddress.getUuid())
        .thenReturn(addressUuid);

    final Address domainToUpdate = Mockito.mock(Address.class);
    Mockito.when(mapper.toDomain(addressUuid, input))
        .thenReturn(domainToUpdate);

    final Address updated = Mockito.mock(Address.class);
    Mockito.when(addressGateway.update(addressUuid, domainToUpdate))
        .thenReturn(updated);

    final AddressOutput output = Mockito.mock(AddressOutput.class);
    Mockito.when(mapper.toOutput(updated))
        .thenReturn(output);

    final var result = useCase.execute(requestUser, restaurantUuid, input);

    Assertions.assertNotNull(result);
    Assertions.assertSame(output, result);

    Mockito.verify(guard, Mockito.times(1))
        .requireRole(requestUser, UsersType.OWNER.name());

    Mockito.verify(repository, Mockito.times(1))
        .findByRestaurantsUuid(restaurantUuid);

    Mockito.verify(restaurantAddress, Mockito.times(1))
        .getAddress();

    Mockito.verify(currentAddress, Mockito.times(1))
        .getUuid();

    Mockito.verify(mapper, Mockito.times(1))
        .toDomain(addressUuid, input);

    Mockito.verify(addressGateway, Mockito.times(1))
        .update(addressUuid, domainToUpdate);

    Mockito.verify(mapper, Mockito.times(1))
        .toOutput(updated);

    Mockito.verifyNoMoreInteractions(
        guard, repository, mapper, addressGateway, restaurantAddress, currentAddress
    );
  }

  @Test
  @DisplayName("Deve lançar NotFoundException quando RestaurantsAddress não existir")
  void shouldThrowNotFoundExceptionWhenRestaurantsAddressDoesNotExist() {
    final var requestUserUuid = UUID.randomUUID();
    final var requestUser = new RequestUser(requestUserUuid);

    final var restaurantUuid = UUID.randomUUID();
    final AddressInput input = Mockito.mock(AddressInput.class);

    Mockito.when(repository.findByRestaurantsUuid(restaurantUuid))
        .thenReturn(Optional.empty());

    final var exception = Assertions.assertThrows(
        NotFoundException.class,
        () -> useCase.execute(requestUser, restaurantUuid, input)
    );

    Assertions.assertEquals("Restaurants Address Not Found", exception.getMessage());

    Mockito.verify(guard, Mockito.times(1))
        .requireRole(requestUser, UsersType.OWNER.name());

    Mockito.verify(repository, Mockito.times(1))
        .findByRestaurantsUuid(restaurantUuid);

    Mockito.verifyNoInteractions(mapper, addressGateway);
    Mockito.verifyNoMoreInteractions(guard, repository);
  }
}
