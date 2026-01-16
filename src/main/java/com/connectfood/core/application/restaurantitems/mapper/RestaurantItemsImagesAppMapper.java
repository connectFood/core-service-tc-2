package com.connectfood.core.application.restaurantitems.mapper;

import java.util.UUID;

import com.connectfood.core.application.restaurantitems.dto.RestaurantItemsImagesInput;
import com.connectfood.core.application.restaurantitems.dto.RestaurantItemsImagesOutput;
import com.connectfood.core.domain.model.RestaurantItemImage;

import org.springframework.stereotype.Component;

@Component
public class RestaurantItemsImagesAppMapper {

  public RestaurantItemsImagesAppMapper() {
  }

  public RestaurantItemImage toDomain(final RestaurantItemsImagesInput input) {
    if (input == null) {
      return null;
    }

    return new RestaurantItemImage(
        input.getName(),
        input.getDescription(),
        input.getPath()
    );
  }

  public RestaurantItemImage toDomain(final UUID uuid, final RestaurantItemsImagesInput input) {
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

  public RestaurantItemsImagesOutput toOutput(final RestaurantItemImage model) {
    if (model == null) {
      return null;
    }

    return new RestaurantItemsImagesOutput(
        model.getUuid(),
        model.getName(),
        model.getDescription(),
        model.getPath()
    );
  }
}
