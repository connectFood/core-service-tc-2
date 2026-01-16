package com.connectfood.core.domain.model;

import java.util.UUID;

import com.connectfood.core.domain.exception.BadRequestException;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class UserRestaurantTest {

  @Test
  @DisplayName("Deve criar UsersRestaurant com UUID explícito e dados válidos")
  void shouldCreateUsersRestaurantWithExplicitUuid() {
    final var uuid = UUID.randomUUID();
    final User user = Mockito.mock(User.class);
    final Restaurant restaurant = Mockito.mock(Restaurant.class);

    final var usersRestaurant = new UserRestaurant(uuid, user, restaurant);

    Assertions.assertEquals(uuid, usersRestaurant.getUuid());
    Assertions.assertEquals(user, usersRestaurant.getUser());
    Assertions.assertEquals(restaurant, usersRestaurant.getRestaurant());
  }

  @Test
  @DisplayName("Deve criar UsersRestaurant sem UUID explícito e gerar UUID automaticamente")
  void shouldCreateUsersRestaurantWithoutExplicitUuid() {
    final User user = Mockito.mock(User.class);
    final Restaurant restaurant = Mockito.mock(Restaurant.class);

    final var usersRestaurant = new UserRestaurant(user, restaurant);

    Assertions.assertNotNull(usersRestaurant.getUuid());
    Assertions.assertEquals(user, usersRestaurant.getUser());
    Assertions.assertEquals(restaurant, usersRestaurant.getRestaurant());
  }

  @Test
  @DisplayName("Não deve criar UsersRestaurant sem User e deve lançar BadRequestException")
  void shouldNotCreateUsersRestaurantWithoutUserAndThrowBadRequestException() {
    final Restaurant restaurant = Mockito.mock(Restaurant.class);

    final var exception = Assertions.assertThrows(
        BadRequestException.class,
        () -> new UserRestaurant(null, restaurant)
    );

    Assertions.assertEquals("User is required", exception.getMessage());
  }

  @Test
  @DisplayName("Não deve criar UsersRestaurant sem Restaurant e deve lançar BadRequestException")
  void shouldNotCreateUsersRestaurantWithoutRestaurantAndThrowBadRequestException() {
    final User user = Mockito.mock(User.class);

    final var exception = Assertions.assertThrows(
        BadRequestException.class,
        () -> new UserRestaurant(user, null)
    );

    Assertions.assertEquals("Restaurant is required", exception.getMessage());
  }
}
