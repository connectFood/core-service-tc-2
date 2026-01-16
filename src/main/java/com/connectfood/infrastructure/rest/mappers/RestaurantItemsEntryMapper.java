package com.connectfood.infrastructure.rest.mappers;

import com.connectfood.core.application.restaurantitems.dto.RestaurantItemsInput;
import com.connectfood.core.application.restaurantitems.dto.RestaurantItemsOutput;
import com.connectfood.infrastructure.rest.dto.restaurantitems.RestaurantItemsRequest;
import com.connectfood.infrastructure.rest.dto.restaurantitems.RestaurantItemsResponse;

import org.springframework.stereotype.Component;

@Component
public class RestaurantItemsEntryMapper {

  private final RestaurantsEntryMapper restaurantsMapper;
  private final RestaurantItemsImagesEntryMapper restaurantItemsImagesMapper;

  public RestaurantItemsEntryMapper(final RestaurantsEntryMapper restaurantsMapper,
      final RestaurantItemsImagesEntryMapper restaurantItemsImagesMapper) {
    this.restaurantsMapper = restaurantsMapper;
    this.restaurantItemsImagesMapper = restaurantItemsImagesMapper;
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
        request.getRestaurantUuid(),
        request.getImages()
            .stream()
            .map(restaurantItemsImagesMapper::toInput)
            .toList()
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
        output.getRestaurant() != null ? restaurantsMapper.toResponse(output.getRestaurant()) : null,
        output.getImages() != null ? output.getImages()
            .stream()
            .map(restaurantItemsImagesMapper::toResponse)
            .toList() : null
    );
  }
}
