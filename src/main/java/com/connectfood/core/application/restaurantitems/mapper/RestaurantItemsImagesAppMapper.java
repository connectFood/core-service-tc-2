package com.connectfood.core.application.restaurantitems.mapper;

import java.util.UUID;

import com.connectfood.core.application.restaurantitems.dto.RestaurantItemsImagesInput;
import com.connectfood.core.application.restaurantitems.dto.RestaurantItemsImagesOutput;
import com.connectfood.core.domain.model.RestaurantItemsImages;

import org.springframework.stereotype.Component;

@Component
public class RestaurantItemsImagesAppMapper {

  public RestaurantItemsImagesAppMapper() {
  }

  public RestaurantItemsImages toDomain(final RestaurantItemsImagesInput input) {
    if (input == null) {
      return null;
    }

    return new RestaurantItemsImages(
        input.getName(),
        input.getDescription(),
        input.getPath()
    );
  }

  public RestaurantItemsImages toDomain(final UUID uuid, final RestaurantItemsImagesInput input) {
    if (input == null) {
      return null;
    }

    return new RestaurantItemsImages(
        uuid,
        input.getName(),
        input.getDescription(),
        input.getPath()
    );
  }

  public RestaurantItemsImagesOutput toOutput(final RestaurantItemsImages model) {
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
