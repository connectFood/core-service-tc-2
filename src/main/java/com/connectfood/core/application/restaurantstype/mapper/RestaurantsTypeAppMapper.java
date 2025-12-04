package com.connectfood.core.application.restaurantstype.mapper;

import com.connectfood.core.application.restaurantstype.dto.RestaurantsTypeInput;
import com.connectfood.core.application.restaurantstype.dto.RestaurantsTypeOutput;
import com.connectfood.core.domain.model.RestaurantsType;

import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class RestaurantsTypeAppMapper {

  public RestaurantsTypeAppMapper() {
  }

  public RestaurantsType toDomain(final RestaurantsTypeInput input) {
    if (input == null) {
      return null;
    }

    return new RestaurantsType(
        input.getName(),
        input.getDescription()
    );
  }

  public RestaurantsType toDomain(final UUID uuid, final RestaurantsTypeInput input) {
    if (input == null) {
      return null;
    }

    return new RestaurantsType(
        uuid,
        input.getName(),
        input.getDescription()
    );
  }

  public RestaurantsTypeOutput toOutput(final RestaurantsType model) {
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
