package com.connectfood.infrastructure.rest.mappers;

import com.connectfood.core.application.restaurantitems.dto.RestaurantItemsInput;
import com.connectfood.core.application.restaurantitems.dto.RestaurantItemsOutput;
import com.connectfood.infrastructure.rest.dto.restaurantitem.RestaurantItemRequest;
import com.connectfood.infrastructure.rest.dto.restaurantitem.RestaurantItemResponse;

import org.springframework.stereotype.Component;

@Component
public class RestaurantItemEntryMapper {

  private final RestaurantEntryMapper restaurantsMapper;
  private final RestaurantItemImageEntryMapper restaurantItemsImagesMapper;

  public RestaurantItemEntryMapper(final RestaurantEntryMapper restaurantsMapper,
                                   final RestaurantItemImageEntryMapper restaurantItemsImagesMapper) {
    this.restaurantsMapper = restaurantsMapper;
    this.restaurantItemsImagesMapper = restaurantItemsImagesMapper;
  }

  public RestaurantItemsInput toInput(final RestaurantItemRequest request) {
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

  public RestaurantItemResponse toResponse(final RestaurantItemsOutput output) {
    if (output == null) {
      return null;
    }

    return new RestaurantItemResponse(
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
