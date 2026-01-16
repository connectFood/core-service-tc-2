package com.connectfood.core.application.restauranttype.mapper;

import com.connectfood.core.application.restauranttype.dto.RestaurantTypeInput;
import com.connectfood.core.application.restauranttype.dto.RestaurantTypeOutput;
import com.connectfood.core.domain.model.RestaurantType;

import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class RestaurantTypeAppMapper {

  public RestaurantTypeAppMapper() {
  }

  public RestaurantType toDomain(final RestaurantTypeInput input) {
    if (input == null) {
      return null;
    }

    return new RestaurantType(
        input.getName(),
        input.getDescription()
    );
  }

  public RestaurantType toDomain(final UUID uuid, final RestaurantTypeInput input) {
    if (input == null) {
      return null;
    }

    return new RestaurantType(
        uuid,
        input.getName(),
        input.getDescription()
    );
  }

  public RestaurantTypeOutput toOutput(final RestaurantType model) {
    if(model == null) {
      return null;
    }

    return new RestaurantTypeOutput(
        model.getUuid(),
        model.getName(),
        model.getDescription()
    );
  }
}
