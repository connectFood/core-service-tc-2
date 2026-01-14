package com.connectfood.core.application.restaurants.mapper;

import java.util.UUID;

import com.connectfood.core.application.restaurants.dto.UsersRestaurantOutput;
import com.connectfood.core.domain.exception.BadRequestException;
import com.connectfood.core.domain.model.Restaurants;
import com.connectfood.core.domain.model.Users;
import com.connectfood.core.domain.model.UsersRestaurant;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class UsersRestaurantAppMapperTest {

  private final UsersRestaurantAppMapper mapper = new UsersRestaurantAppMapper();

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
    final UsersRestaurant model = Mockito.mock(UsersRestaurant.class);
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
    final Users user = Mockito.mock(Users.class);
    Mockito.when(user.getUuid())
        .thenReturn(UUID.randomUUID());

    final UsersRestaurant model = Mockito.mock(UsersRestaurant.class);
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
    final Users user = Mockito.mock(Users.class);
    Mockito.when(user.getUuid())
        .thenReturn(null);

    final Restaurants restaurant = Mockito.mock(Restaurants.class);
    Mockito.when(restaurant.getUuid())
        .thenReturn(UUID.randomUUID());

    final UsersRestaurant model = Mockito.mock(UsersRestaurant.class);
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
    final Users user = Mockito.mock(Users.class);
    Mockito.when(user.getUuid())
        .thenReturn(UUID.randomUUID());

    final Restaurants restaurant = Mockito.mock(Restaurants.class);
    Mockito.when(restaurant.getUuid())
        .thenReturn(null);

    final UsersRestaurant model = Mockito.mock(UsersRestaurant.class);
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

    final Users user = Mockito.mock(Users.class);
    Mockito.when(user.getUuid())
        .thenReturn(usersUuid);

    final Restaurants restaurant = Mockito.mock(Restaurants.class);
    Mockito.when(restaurant.getUuid())
        .thenReturn(restaurantUuid);

    final UsersRestaurant model = Mockito.mock(UsersRestaurant.class);
    Mockito.when(model.getUuid())
        .thenReturn(usersRestaurantUuid);
    Mockito.when(model.getUser())
        .thenReturn(user);
    Mockito.when(model.getRestaurant())
        .thenReturn(restaurant);

    final UsersRestaurantOutput result = mapper.toOutput(model);

    Assertions.assertNotNull(result);
    Assertions.assertEquals(usersRestaurantUuid, result.getUuid());
    Assertions.assertEquals(usersUuid, result.getUsersUuid());
    Assertions.assertEquals(restaurantUuid, result.getRestaurantUuid());
  }

  @Test
  @DisplayName("toDomain(uuid,user,restaurant) deve criar domínio com uuid explícito")
  void toDomainWithUuidShouldCreateDomain() {
    final var uuid = UUID.randomUUID();
    final Users user = Mockito.mock(Users.class);
    final Restaurants restaurant = Mockito.mock(Restaurants.class);

    final UsersRestaurant result = mapper.toDomain(uuid, user, restaurant);

    Assertions.assertNotNull(result);
    Assertions.assertEquals(uuid, result.getUuid());
    Assertions.assertEquals(user, result.getUser());
    Assertions.assertEquals(restaurant, result.getRestaurant());
  }

  @Test
  @DisplayName("toDomain(user,restaurant) deve criar domínio gerando uuid")
  void toDomainWithoutUuidShouldCreateDomainGeneratingUuid() {
    final Users user = Mockito.mock(Users.class);
    final Restaurants restaurant = Mockito.mock(Restaurants.class);

    final UsersRestaurant result = mapper.toDomain(user, restaurant);

    Assertions.assertNotNull(result);
    Assertions.assertNotNull(result.getUuid());
    Assertions.assertEquals(user, result.getUser());
    Assertions.assertEquals(restaurant, result.getRestaurant());
  }

  @Test
  @DisplayName("toDomain(uuid,user,restaurant) deve lançar BadRequestException quando user for null")
  void toDomainWithUuidShouldThrowBadRequestWhenUserIsNull() {
    final var uuid = UUID.randomUUID();
    final Restaurants restaurant = Mockito.mock(Restaurants.class);

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
    final Users user = Mockito.mock(Users.class);

    final var ex = Assertions.assertThrows(
        BadRequestException.class,
        () -> mapper.toDomain(uuid, user, null)
    );

    Assertions.assertEquals("Restaurant is required", ex.getMessage());
  }
}
