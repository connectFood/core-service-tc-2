package com.connectfood.core.application.restaurants.mapper;

import java.util.Objects;
import java.util.UUID;

import org.springframework.stereotype.Component;

import com.connectfood.core.application.restaurants.dto.UsersRestaurantOutput;
import com.connectfood.core.domain.model.Restaurants;
import com.connectfood.core.domain.model.Users;
import com.connectfood.core.domain.model.UsersRestaurant;

@Component
public class UsersRestaurantAppMapper {

  public UsersRestaurantOutput toOutput(final UsersRestaurant model) {
    Objects.requireNonNull(model, "UsersRestaurant is required");

    final Users user = Objects.requireNonNull(model.getUser(), "User is required");
    final Restaurants restaurant = Objects.requireNonNull(model.getRestaurant(), "Restaurant is required");

    final UUID usersUuid = Objects.requireNonNull(user.getUuid(), "User uuid is required");
    final UUID restaurantUuid = Objects.requireNonNull(restaurant.getUuid(), "Restaurant uuid is required");

    return new UsersRestaurantOutput(
        model.getUuid(),
        usersUuid,
        restaurantUuid
    );
  }

  public UsersRestaurant toDomain(final UUID uuid, final Users user, final Restaurants restaurant) {
    return new UsersRestaurant(uuid, user, restaurant);
  }

  public UsersRestaurant toDomain(final Users user, final Restaurants restaurant) {
    return new UsersRestaurant(user, restaurant);
  }
}
