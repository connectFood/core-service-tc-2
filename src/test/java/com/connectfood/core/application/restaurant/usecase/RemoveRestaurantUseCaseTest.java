package com.connectfood.core.application.restaurant.usecase;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.connectfood.core.application.security.RequestUser;
import com.connectfood.core.application.security.RequestUserGuard;
import com.connectfood.core.domain.exception.NotFoundException;
import com.connectfood.core.domain.model.Address;
import com.connectfood.core.domain.model.Restaurant;
import com.connectfood.core.domain.model.RestaurantItem;
import com.connectfood.core.domain.model.RestaurantAddress;
import com.connectfood.core.domain.model.enums.UsersType;
import com.connectfood.core.domain.repository.AddressGateway;
import com.connectfood.core.domain.repository.RestaurantAddressGateway;
import com.connectfood.core.domain.repository.RestaurantGateway;
import com.connectfood.core.domain.repository.RestaurantItemGateway;
import com.connectfood.core.domain.repository.RestaurantItemImageGateway;
import com.connectfood.core.domain.repository.RestaurantOpeningHourGateway;
import com.connectfood.core.domain.repository.UserRestaurantGateway;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class RemoveRestaurantUseCaseTest {

  @Mock
  private RestaurantGateway repository;

  @Mock
  private RequestUserGuard guard;

  @Mock
  private RestaurantItemGateway restaurantItemGateway;

  @Mock
  private RestaurantItemImageGateway restaurantItemImageGateway;

  @Mock
  private RestaurantOpeningHourGateway restaurantOpeningHourGateway;

  @Mock
  private RestaurantAddressGateway restaurantAddressGateway;

  @Mock
  private AddressGateway addressGateway;

  @Mock
  private UserRestaurantGateway userRestaurantGateway;

  @InjectMocks
  private RemoveRestaurantUseCase useCase;

  @Test
  @DisplayName("Não deve remover restaurante quando uuid não existir")
  void shouldThrowExceptionWhenRestaurantsNotFound() {
    final var requestUser = new RequestUser(UUID.randomUUID());
    final var uuid = UUID.randomUUID();

    Mockito.when(repository.findByUuid(uuid)).thenReturn(Optional.empty());

    final var exception = Assertions.assertThrows(
        NotFoundException.class,
        () -> useCase.execute(requestUser, uuid)
    );

    Assertions.assertEquals("Restaurants not found", exception.getMessage());

    Mockito.verify(guard, Mockito.times(1))
        .requireRole(requestUser, UsersType.OWNER.name());

    Mockito.verify(repository, Mockito.times(1))
        .findByUuid(uuid);

    Mockito.verifyNoInteractions(
        restaurantItemGateway,
        restaurantItemImageGateway,
        restaurantOpeningHourGateway,
        restaurantAddressGateway,
        addressGateway,
        userRestaurantGateway
    );

    Mockito.verify(repository, Mockito.never()).delete(Mockito.any());
    Mockito.verifyNoMoreInteractions(guard, repository);
  }

  @Test
  @DisplayName("Deve remover restaurante quando uuid existir e não houver dependências (nada para remover)")
  void shouldRemoveRestaurantsWhenUuidExistsAndNoDependencies() {
    final var requestUser = new RequestUser(UUID.randomUUID());
    final var uuid = UUID.randomUUID();

    final Restaurant restaurant = Mockito.mock(Restaurant.class);
    Mockito.when(repository.findByUuid(uuid)).thenReturn(Optional.of(restaurant));

    Mockito.when(restaurantItemGateway.existsByRestaurantUuid(uuid)).thenReturn(false);
    Mockito.when(restaurantOpeningHourGateway.existsByRestaurantUuid(uuid)).thenReturn(false);
    Mockito.when(restaurantAddressGateway.existsByRestaurantsUuid(uuid)).thenReturn(false);
    Mockito.when(userRestaurantGateway.existsByRestaurantsUuid(uuid)).thenReturn(false);

    Assertions.assertDoesNotThrow(() -> useCase.execute(requestUser, uuid));

    Mockito.verify(guard, Mockito.times(1))
        .requireRole(requestUser, UsersType.OWNER.name());

    Mockito.verify(repository, Mockito.times(1))
        .findByUuid(uuid);

    Mockito.verify(restaurantItemGateway, Mockito.times(1))
        .existsByRestaurantUuid(uuid);

    Mockito.verify(restaurantOpeningHourGateway, Mockito.times(1))
        .existsByRestaurantUuid(uuid);

    Mockito.verify(restaurantAddressGateway, Mockito.times(1))
        .existsByRestaurantsUuid(uuid);

    Mockito.verify(userRestaurantGateway, Mockito.times(1))
        .existsByRestaurantsUuid(uuid);

    Mockito.verify(repository, Mockito.times(1)).delete(uuid);

    Mockito.verifyNoMoreInteractions(
        guard,
        repository,
        restaurantItemGateway,
        restaurantItemImageGateway,
        restaurantOpeningHourGateway,
        restaurantAddressGateway,
        addressGateway,
        userRestaurantGateway
    );
  }

  @Test
  @DisplayName("Deve remover restaurante quando uuid existir e houver itens, imagens, horários, endereço e vínculo de usuário")
  void shouldRemoveRestaurantsWhenUuidExistsAndHasAllDependencies() {
    final var requestUser = new RequestUser(UUID.randomUUID());
    final var restaurantUuid = UUID.randomUUID();

    final Restaurant restaurant = Mockito.mock(Restaurant.class);
    Mockito.when(repository.findByUuid(restaurantUuid)).thenReturn(Optional.of(restaurant));

    Mockito.when(restaurantItemGateway.existsByRestaurantUuid(restaurantUuid)).thenReturn(true);

    final var item1Uuid = UUID.randomUUID();
    final var item2Uuid = UUID.randomUUID();

    final RestaurantItem item1 = Mockito.mock(RestaurantItem.class);
    Mockito.when(item1.getUuid()).thenReturn(item1Uuid);

    final RestaurantItem item2 = Mockito.mock(RestaurantItem.class);
    Mockito.when(item2.getUuid()).thenReturn(item2Uuid);

    Mockito.when(restaurantItemGateway.findAllByRestaurantUuid(restaurantUuid))
        .thenReturn(List.of(item1, item2));

    Mockito.when(restaurantItemImageGateway.existsByRestaurantItemUuid(item1Uuid)).thenReturn(true);
    Mockito.when(restaurantItemImageGateway.existsByRestaurantItemUuid(item2Uuid)).thenReturn(false);

    Mockito.when(restaurantOpeningHourGateway.existsByRestaurantUuid(restaurantUuid)).thenReturn(true);

    Mockito.when(restaurantAddressGateway.existsByRestaurantsUuid(restaurantUuid)).thenReturn(true);

    final var restaurantAddressUuid = UUID.randomUUID();
    final var addressUuid = UUID.randomUUID();

    final Address address = Mockito.mock(Address.class);
    Mockito.when(address.getUuid()).thenReturn(addressUuid);

    final RestaurantAddress restaurantAddress = Mockito.mock(RestaurantAddress.class);
    Mockito.when(restaurantAddress.getUuid()).thenReturn(restaurantAddressUuid);
    Mockito.when(restaurantAddress.getAddress()).thenReturn(address);

    Mockito.when(restaurantAddressGateway.findByRestaurantsUuid(restaurantUuid))
        .thenReturn(Optional.of(restaurantAddress));

    Mockito.when(userRestaurantGateway.existsByRestaurantsUuid(restaurantUuid)).thenReturn(true);

    Assertions.assertDoesNotThrow(() -> useCase.execute(requestUser, restaurantUuid));

    final InOrder inOrder = Mockito.inOrder(
        guard,
        repository,
        restaurantItemGateway,
        restaurantItemImageGateway,
        restaurantOpeningHourGateway,
        restaurantAddressGateway,
        addressGateway,
        userRestaurantGateway
    );

    inOrder.verify(guard).requireRole(requestUser, UsersType.OWNER.name());
    inOrder.verify(repository).findByUuid(restaurantUuid);

    inOrder.verify(restaurantItemGateway).existsByRestaurantUuid(restaurantUuid);
    inOrder.verify(restaurantItemGateway).findAllByRestaurantUuid(restaurantUuid);

    inOrder.verify(restaurantItemImageGateway).existsByRestaurantItemUuid(item1Uuid);
    inOrder.verify(restaurantItemImageGateway).deleteByRestaurantItemUuid(item1Uuid);
    inOrder.verify(restaurantItemGateway).delete(item1Uuid);

    inOrder.verify(restaurantItemImageGateway).existsByRestaurantItemUuid(item2Uuid);
    inOrder.verify(restaurantItemGateway).delete(item2Uuid);

    inOrder.verify(restaurantOpeningHourGateway).existsByRestaurantUuid(restaurantUuid);
    inOrder.verify(restaurantOpeningHourGateway).deleteByRestaurantUuid(restaurantUuid);

    inOrder.verify(restaurantAddressGateway).existsByRestaurantsUuid(restaurantUuid);
    inOrder.verify(restaurantAddressGateway).findByRestaurantsUuid(restaurantUuid);
    inOrder.verify(restaurantAddressGateway).delete(restaurantAddressUuid);
    inOrder.verify(addressGateway).delete(addressUuid);

    inOrder.verify(userRestaurantGateway).existsByRestaurantsUuid(restaurantUuid);
    inOrder.verify(userRestaurantGateway).deleteByRestaurantsUuid(restaurantUuid);

    inOrder.verify(repository).delete(restaurantUuid);

    Mockito.verifyNoMoreInteractions(
        guard,
        repository,
        restaurantItemGateway,
        restaurantItemImageGateway,
        restaurantOpeningHourGateway,
        restaurantAddressGateway,
        addressGateway,
        userRestaurantGateway
    );
  }
}
