package com.connectfood.core.application.restaurants.mapper;

import java.util.UUID;

import com.connectfood.core.application.restaurants.dto.RestaurantsInput;
import com.connectfood.core.application.restaurants.dto.RestaurantsOutput;
import com.connectfood.core.application.restaurantstype.mapper.RestaurantsTypeAppMapper;
import com.connectfood.core.domain.model.Restaurants;
import com.connectfood.core.domain.model.RestaurantsType;

import org.springframework.stereotype.Component;

@Component
public class RestaurantsAppMapper {

  private final RestaurantsTypeAppMapper restaurantsTypeMapper;

  public RestaurantsAppMapper(RestaurantsTypeAppMapper restaurantsTypeMapper) {
    this.restaurantsTypeMapper = restaurantsTypeMapper;
  }

  public Restaurants toDomain(final RestaurantsInput input, final RestaurantsType restaurantsType) {
    if (input == null) {
      return null;
    }

    return new Restaurants(
        input.getName(),
        restaurantsType
    );
  }

  public Restaurants toDomain(final UUID uuid, final RestaurantsInput input, final RestaurantsType restaurantsType) {
    if (input == null) {
      return null;
    }

    return new Restaurants(
        uuid,
        input.getName(),
        restaurantsType
    );
  }

  public RestaurantsOutput toOutput(final Restaurants model) {
    if (model == null) {
      return null;
    }

    return new RestaurantsOutput(
        model.getUuid(),
        model.getName(),
        model.getRestaurantsType() != null ? restaurantsTypeMapper.toOutput(model.getRestaurantsType()) : null,
        null,
        null
    );
  }
}
