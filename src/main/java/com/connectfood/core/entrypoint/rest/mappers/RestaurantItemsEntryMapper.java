package com.connectfood.core.entrypoint.rest.mappers;

import com.connectfood.core.application.restaurantitems.dto.RestaurantItemsInput;
import com.connectfood.core.application.restaurantitems.dto.RestaurantItemsOutput;
import com.connectfood.core.entrypoint.rest.dto.restaurantitems.RestaurantItemsRequest;
import com.connectfood.core.entrypoint.rest.dto.restaurantitems.RestaurantItemsResponse;

import org.springframework.stereotype.Component;

@Component
public class RestaurantItemsEntryMapper {

  private final RestaurantsEntryMapper restaurantsMapper;

  public RestaurantItemsEntryMapper(final RestaurantsEntryMapper restaurantsMapper) {
    this.restaurantsMapper = restaurantsMapper;
  }

  public RestaurantItemsInput toInput(final RestaurantItemsRequest request) {
    if (request == null) {
      return null;
    }

    return new RestaurantItemsInput(
        request.getName(),
        request.getDescription(),
        request.getValue(),
        request.getRequestType(),
        request.getRestaurantUuid()
    );
  }

  public RestaurantItemsResponse toResponse(final RestaurantItemsOutput output) {
    if (output == null) {
      return null;
    }

    return new RestaurantItemsResponse(
        output.getUuid(),
        output.getName(),
        output.getDescription(),
        output.getValue(),
        output.getRequestType(),
        output.getRestaurant() != null ? restaurantsMapper.toResponse(output.getRestaurant()) : null
    );
  }
}
