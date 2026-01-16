package com.connectfood.core.application.restaurant.mapper;

import java.util.UUID;

import com.connectfood.core.application.restaurant.dto.UserRestaurantOutput;
import com.connectfood.core.domain.exception.BadRequestException;
import com.connectfood.core.domain.model.Restaurant;
import com.connectfood.core.domain.model.User;
import com.connectfood.core.domain.model.UserRestaurant;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class UserRestaurantAppMapperTest {

  private final UserRestaurantAppMapper mapper = new UserRestaurantAppMapper();

  @Test
  @DisplayName("toOutput deve lançar NullPointerException quando model for null")
  void toOutputShouldThrowWhenModelIsNull() {
    final var ex = Assertions.assertThrows(
        NullPointerException.class,
        () -> mapper.toOutput(null)
    );

    Assertions.assertEquals("UsersRestaurant is required", ex.getMessage());
  }

  @Test
  @DisplayName("toOutput deve lançar NullPointerException quando user for null")
  void toOutputShouldThrowWhenUserIsNull() {
    final UserRestaurant model = Mockito.mock(UserRestaurant.class);
    Mockito.when(model.getUser())
        .thenReturn(null);

    final var ex = Assertions.assertThrows(
        NullPointerException.class,
        () -> mapper.toOutput(model)
    );

    Assertions.assertEquals("User is required", ex.getMessage());
  }

  @Test
  @DisplayName("toOutput deve lançar NullPointerException quando restaurant for null")
  void toOutputShouldThrowWhenRestaurantIsNull() {
    final User user = Mockito.mock(User.class);
    Mockito.when(user.getUuid())
        .thenReturn(UUID.randomUUID());

    final UserRestaurant model = Mockito.mock(UserRestaurant.class);
    Mockito.when(model.getUser())
        .thenReturn(user);
    Mockito.when(model.getRestaurant())
        .thenReturn(null);

    final var ex = Assertions.assertThrows(
        NullPointerException.class,
        () -> mapper.toOutput(model)
    );

    Assertions.assertEquals("Restaurant is required", ex.getMessage());
  }

  @Test
  @DisplayName("toOutput deve lançar NullPointerException quando user uuid for null")
  void toOutputShouldThrowWhenUserUuidIsNull() {
    final User user = Mockito.mock(User.class);
    Mockito.when(user.getUuid())
        .thenReturn(null);

    final Restaurant restaurant = Mockito.mock(Restaurant.class);
    Mockito.when(restaurant.getUuid())
        .thenReturn(UUID.randomUUID());

    final UserRestaurant model = Mockito.mock(UserRestaurant.class);
    Mockito.when(model.getUser())
        .thenReturn(user);
    Mockito.when(model.getRestaurant())
        .thenReturn(restaurant);

    final var ex = Assertions.assertThrows(
        NullPointerException.class,
        () -> mapper.toOutput(model)
    );

    Assertions.assertEquals("User uuid is required", ex.getMessage());
  }

  @Test
  @DisplayName("toOutput deve lançar NullPointerException quando restaurant uuid for null")
  void toOutputShouldThrowWhenRestaurantUuidIsNull() {
    final User user = Mockito.mock(User.class);
    Mockito.when(user.getUuid())
        .thenReturn(UUID.randomUUID());

    final Restaurant restaurant = Mockito.mock(Restaurant.class);
    Mockito.when(restaurant.getUuid())
        .thenReturn(null);

    final UserRestaurant model = Mockito.mock(UserRestaurant.class);
    Mockito.when(model.getUser())
        .thenReturn(user);
    Mockito.when(model.getRestaurant())
        .thenReturn(restaurant);

    final var ex = Assertions.assertThrows(
        NullPointerException.class,
        () -> mapper.toOutput(model)
    );

    Assertions.assertEquals("Restaurant uuid is required", ex.getMessage());
  }

  @Test
  @DisplayName("toOutput deve mapear corretamente quando todos os dados forem válidos")
  void toOutputShouldMapCorrectlyWhenValid() {
    final var usersUuid = UUID.randomUUID();
    final var restaurantUuid = UUID.randomUUID();
    final var usersRestaurantUuid = UUID.randomUUID();

    final User user = Mockito.mock(User.class);
    Mockito.when(user.getUuid())
        .thenReturn(usersUuid);

    final Restaurant restaurant = Mockito.mock(Restaurant.class);
    Mockito.when(restaurant.getUuid())
        .thenReturn(restaurantUuid);

    final UserRestaurant model = Mockito.mock(UserRestaurant.class);
    Mockito.when(model.getUuid())
        .thenReturn(usersRestaurantUuid);
    Mockito.when(model.getUser())
        .thenReturn(user);
    Mockito.when(model.getRestaurant())
        .thenReturn(restaurant);

    final UserRestaurantOutput result = mapper.toOutput(model);

    Assertions.assertNotNull(result);
    Assertions.assertEquals(usersRestaurantUuid, result.getUuid());
    Assertions.assertEquals(usersUuid, result.getUsersUuid());
    Assertions.assertEquals(restaurantUuid, result.getRestaurantUuid());
  }

  @Test
  @DisplayName("toDomain(uuid,user,restaurant) deve criar domínio com uuid explícito")
  void toDomainWithUuidShouldCreateDomain() {
    final var uuid = UUID.randomUUID();
    final User user = Mockito.mock(User.class);
    final Restaurant restaurant = Mockito.mock(Restaurant.class);

    final UserRestaurant result = mapper.toDomain(uuid, user, restaurant);

    Assertions.assertNotNull(result);
    Assertions.assertEquals(uuid, result.getUuid());
    Assertions.assertEquals(user, result.getUser());
    Assertions.assertEquals(restaurant, result.getRestaurant());
  }

  @Test
  @DisplayName("toDomain(user,restaurant) deve criar domínio gerando uuid")
  void toDomainWithoutUuidShouldCreateDomainGeneratingUuid() {
    final User user = Mockito.mock(User.class);
    final Restaurant restaurant = Mockito.mock(Restaurant.class);

    final UserRestaurant result = mapper.toDomain(user, restaurant);

    Assertions.assertNotNull(result);
    Assertions.assertNotNull(result.getUuid());
    Assertions.assertEquals(user, result.getUser());
    Assertions.assertEquals(restaurant, result.getRestaurant());
  }

  @Test
  @DisplayName("toDomain(uuid,user,restaurant) deve lançar BadRequestException quando user for null")
  void toDomainWithUuidShouldThrowBadRequestWhenUserIsNull() {
    final var uuid = UUID.randomUUID();
    final Restaurant restaurant = Mockito.mock(Restaurant.class);

    final var ex = Assertions.assertThrows(
        BadRequestException.class,
        () -> mapper.toDomain(uuid, null, restaurant)
    );

    Assertions.assertEquals("User is required", ex.getMessage());
  }

  @Test
  @DisplayName("toDomain(uuid,user,restaurant) deve lançar BadRequestException quando restaurant for null")
  void toDomainWithUuidShouldThrowBadRequestWhenRestaurantIsNull() {
    final var uuid = UUID.randomUUID();
    final User user = Mockito.mock(User.class);

    final var ex = Assertions.assertThrows(
        BadRequestException.class,
        () -> mapper.toDomain(uuid, user, null)
    );

    Assertions.assertEquals("Restaurant is required", ex.getMessage());
  }
}
