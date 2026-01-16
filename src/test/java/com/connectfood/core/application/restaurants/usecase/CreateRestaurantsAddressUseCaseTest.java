package com.connectfood.core.application.restaurants.usecase;

import java.util.Optional;
import java.util.UUID;

import com.connectfood.core.application.address.dto.AddressInput;
import com.connectfood.core.application.address.dto.AddressOutput;
import com.connectfood.core.application.address.mapper.AddressAppMapper;
import com.connectfood.core.application.restaurants.mapper.RestaurantsAddressAppMapper;
import com.connectfood.core.application.security.RequestUser;
import com.connectfood.core.application.security.RequestUserGuard;
import com.connectfood.core.domain.exception.NotFoundException;
import com.connectfood.core.domain.model.Address;
import com.connectfood.core.domain.model.Restaurant;
import com.connectfood.core.domain.model.RestaurantsAddress;
import com.connectfood.core.domain.model.enums.UsersType;
import com.connectfood.core.domain.repository.AddressGateway;
import com.connectfood.core.domain.repository.RestaurantsAddressGateway;
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
class CreateRestaurantsAddressUseCaseTest {

  @Mock
  private AddressGateway repository;

  @Mock
  private AddressAppMapper mapper;

  @Mock
  private RequestUserGuard guard;

  @Mock
  private RestaurantsGateway restaurantsGateway;

  @Mock
  private RestaurantsAddressGateway restaurantsAddressGateway;

  @Mock
  private RestaurantsAddressAppMapper restaurantsAddressMapper;

  @InjectMocks
  private CreateRestaurantsAddressUseCase useCase;

  @Test
  @DisplayName("Deve criar endereço e vincular ao restaurante quando restaurante existir")
  void shouldCreateAddressAndLinkToRestaurantWhenRestaurantExists() {
    final var requestUserUuid = UUID.randomUUID();
    final var requestUser = new RequestUser(requestUserUuid);

    final var restaurantUuid = UUID.randomUUID();
    final AddressInput input = Mockito.mock(AddressInput.class);

    final Restaurant restaurant = Mockito.mock(Restaurant.class);
    Mockito.when(restaurantsGateway.findByUuid(restaurantUuid))
        .thenReturn(Optional.of(restaurant));

    final Address addressDomainToSave = Mockito.mock(Address.class);
    Mockito.when(mapper.toDomain(input))
        .thenReturn(addressDomainToSave);

    final Address savedAddress = Mockito.mock(Address.class);
    Mockito.when(repository.save(addressDomainToSave))
        .thenReturn(savedAddress);

    final RestaurantsAddress restaurantsAddressDomain = Mockito.mock(RestaurantsAddress.class);
    Mockito.when(restaurantsAddressMapper.toDomain(restaurant, savedAddress))
        .thenReturn(restaurantsAddressDomain);

    final RestaurantsAddress savedRestaurantsAddress = Mockito.mock(RestaurantsAddress.class);
    Mockito.when(restaurantsAddressGateway.save(restaurantsAddressDomain))
        .thenReturn(savedRestaurantsAddress);

    final Address linkedAddress = Mockito.mock(Address.class);
    Mockito.when(savedRestaurantsAddress.getAddress())
        .thenReturn(linkedAddress);

    final AddressOutput output = Mockito.mock(AddressOutput.class);
    Mockito.when(mapper.toOutput(linkedAddress))
        .thenReturn(output);

    final var result = useCase.execute(requestUser, restaurantUuid, input);

    Assertions.assertNotNull(result);
    Assertions.assertSame(output, result);

    Mockito.verify(guard, Mockito.times(1))
        .requireRole(requestUser, UsersType.OWNER.name());

    Mockito.verify(restaurantsGateway, Mockito.times(1))
        .findByUuid(restaurantUuid);

    Mockito.verify(mapper, Mockito.times(1))
        .toDomain(input);

    Mockito.verify(repository, Mockito.times(1))
        .save(addressDomainToSave);

    Mockito.verify(restaurantsAddressMapper, Mockito.times(1))
        .toDomain(restaurant, savedAddress);

    Mockito.verify(restaurantsAddressGateway, Mockito.times(1))
        .save(restaurantsAddressDomain);

    Mockito.verify(mapper, Mockito.times(1))
        .toOutput(linkedAddress);

    Mockito.verifyNoMoreInteractions(
        guard,
        restaurantsGateway,
        mapper,
        repository,
        restaurantsAddressMapper,
        restaurantsAddressGateway
    );
  }

  @Test
  @DisplayName("Deve lançar NotFoundException quando restaurante não existir")
  void shouldThrowNotFoundExceptionWhenRestaurantDoesNotExist() {
    final var requestUserUuid = UUID.randomUUID();
    final var requestUser = new RequestUser(requestUserUuid);

    final var restaurantUuid = UUID.randomUUID();
    final AddressInput input = Mockito.mock(AddressInput.class);

    Mockito.when(restaurantsGateway.findByUuid(restaurantUuid))
        .thenReturn(Optional.empty());

    final var ex = Assertions.assertThrows(
        NotFoundException.class,
        () -> useCase.execute(requestUser, restaurantUuid, input)
    );

    Assertions.assertEquals("Restaurant not found", ex.getMessage());

    Mockito.verify(guard, Mockito.times(1))
        .requireRole(requestUser, UsersType.OWNER.name());

    Mockito.verify(restaurantsGateway, Mockito.times(1))
        .findByUuid(restaurantUuid);

    Mockito.verifyNoInteractions(mapper, repository, restaurantsAddressMapper, restaurantsAddressGateway);
    Mockito.verifyNoMoreInteractions(guard, restaurantsGateway);
  }
}
