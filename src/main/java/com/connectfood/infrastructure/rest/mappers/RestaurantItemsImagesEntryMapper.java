package com.connectfood.infrastructure.rest.mappers;

import com.connectfood.core.application.restaurantitems.dto.RestaurantItemsImagesInput;
import com.connectfood.core.application.restaurantitems.dto.RestaurantItemsImagesOutput;
import com.connectfood.infrastructure.rest.dto.restaurantitems.RestaurantItemsImagesRequest;
import com.connectfood.infrastructure.rest.dto.restaurantitems.RestaurantItemsImagesResponse;

import org.springframework.stereotype.Component;

@Component
public class RestaurantItemsImagesEntryMapper {

  public RestaurantItemsImagesEntryMapper() {
  }

  public RestaurantItemsImagesInput toInput(final RestaurantItemsImagesRequest request) {
    if (request == null) {
      return null;
    }

    return new RestaurantItemsImagesInput(
        request.getUuid() != null ? request.getUuid() : null,
        request.getName(),
        request.getDescription(),
        request.getPath()
    );
  }

  public RestaurantItemsImagesResponse toResponse(final RestaurantItemsImagesOutput output) {
    if (output == null) {
      return null;
    }

    return new RestaurantItemsImagesResponse(
        output.getUuid(),
        output.getName(),
        output.getDescription(),
        output.getPath()
    );
  }
}
