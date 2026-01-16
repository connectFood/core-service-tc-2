package com.connectfood.infrastructure.rest.mappers;

import com.connectfood.core.application.restaurantitems.dto.RestaurantItemsImagesInput;
import com.connectfood.core.application.restaurantitems.dto.RestaurantItemsImagesOutput;
import com.connectfood.infrastructure.rest.dto.restaurantitem.RestaurantItemImageRequest;
import com.connectfood.infrastructure.rest.dto.restaurantitem.RestaurantItemImageResponse;

import org.springframework.stereotype.Component;

@Component
public class RestaurantItemImageEntryMapper {

  public RestaurantItemImageEntryMapper() {
  }

  public RestaurantItemsImagesInput toInput(final RestaurantItemImageRequest request) {
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

  public RestaurantItemImageResponse toResponse(final RestaurantItemsImagesOutput output) {
    if (output == null) {
      return null;
    }

    return new RestaurantItemImageResponse(
        output.getUuid(),
        output.getName(),
        output.getDescription(),
        output.getPath()
    );
  }
}
