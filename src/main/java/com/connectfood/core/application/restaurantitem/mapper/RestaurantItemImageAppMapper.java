package com.connectfood.core.application.restaurantitem.mapper;

import java.util.UUID;

import com.connectfood.core.application.restaurantitem.dto.RestaurantItemImageInput;
import com.connectfood.core.application.restaurantitem.dto.RestaurantItemImageOutput;
import com.connectfood.core.domain.model.RestaurantItemImage;

import org.springframework.stereotype.Component;

@Component
public class RestaurantItemImageAppMapper {

  public RestaurantItemImageAppMapper() {
  }

  public RestaurantItemImage toDomain(final RestaurantItemImageInput input) {
    if (input == null) {
      return null;
    }

    return new RestaurantItemImage(
        input.getName(),
        input.getDescription(),
        input.getPath()
    );
  }

  public RestaurantItemImage toDomain(final UUID uuid, final RestaurantItemImageInput input) {
    if (input == null) {
      return null;
    }

    return new RestaurantItemImage(
        uuid,
        input.getName(),
        input.getDescription(),
        input.getPath()
    );
  }

  public RestaurantItemImageOutput toOutput(final RestaurantItemImage model) {
    if (model == null) {
      return null;
    }

    return new RestaurantItemImageOutput(
        model.getUuid(),
        model.getName(),
        model.getDescription(),
        model.getPath()
    );
  }
}
