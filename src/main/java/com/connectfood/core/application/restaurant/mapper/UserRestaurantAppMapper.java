package com.connectfood.core.application.restaurant.mapper;

import java.util.Objects;
import java.util.UUID;

import org.springframework.stereotype.Component;

import com.connectfood.core.application.restaurant.dto.UserRestaurantOutput;
import com.connectfood.core.domain.model.Restaurant;
import com.connectfood.core.domain.model.User;
import com.connectfood.core.domain.model.UserRestaurant;

@Component
public class UserRestaurantAppMapper {

  public UserRestaurantOutput toOutput(final UserRestaurant model) {
    Objects.requireNonNull(model, "UsersRestaurant is required");

    final User user = Objects.requireNonNull(model.getUser(), "User is required");
    final Restaurant restaurant = Objects.requireNonNull(model.getRestaurant(), "Restaurant is required");

    final UUID usersUuid = Objects.requireNonNull(user.getUuid(), "User uuid is required");
    final UUID restaurantUuid = Objects.requireNonNull(restaurant.getUuid(), "Restaurant uuid is required");

    return new UserRestaurantOutput(
        model.getUuid(),
        usersUuid,
        restaurantUuid
    );
  }

  public UserRestaurant toDomain(final UUID uuid, final User user, final Restaurant restaurant) {
    return new UserRestaurant(uuid, user, restaurant);
  }

  public UserRestaurant toDomain(final User user, final Restaurant restaurant) {
    return new UserRestaurant(user, restaurant);
  }
}
