package com.connectfood.core.application.restaurantstype.mapper;

import com.connectfood.core.application.restaurantstype.dto.RestaurantsTypeInput;
import com.connectfood.core.application.restaurantstype.dto.RestaurantsTypeOutput;
import com.connectfood.core.domain.model.RestaurantType;

import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class RestaurantsTypeAppMapper {

  public RestaurantsTypeAppMapper() {
  }

  public RestaurantType toDomain(final RestaurantsTypeInput input) {
    if (input == null) {
      return null;
    }

    return new RestaurantType(
        input.getName(),
        input.getDescription()
    );
  }

  public RestaurantType toDomain(final UUID uuid, final RestaurantsTypeInput input) {
    if (input == null) {
      return null;
    }

    return new RestaurantType(
        uuid,
        input.getName(),
        input.getDescription()
    );
  }

  public RestaurantsTypeOutput toOutput(final RestaurantType model) {
    if(model == null) {
      return null;
    }

    return new RestaurantsTypeOutput(
        model.getUuid(),
        model.getName(),
        model.getDescription()
    );
  }
}
