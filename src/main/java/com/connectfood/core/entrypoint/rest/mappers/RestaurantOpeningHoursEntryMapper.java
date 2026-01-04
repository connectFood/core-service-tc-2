package com.connectfood.core.entrypoint.rest.mappers;

import com.connectfood.core.application.restaurantopeninghours.dto.RestaurantOpeningHoursInput;
import com.connectfood.core.application.restaurantopeninghours.dto.RestaurantOpeningHoursOutput;
import com.connectfood.core.entrypoint.rest.dto.restaurantopeninghours.RestaurantOpeningHoursRequest;
import com.connectfood.core.entrypoint.rest.dto.restaurantopeninghours.RestaurantOpeningHoursResponse;

import org.springframework.stereotype.Component;

@Component
public class RestaurantOpeningHoursEntryMapper {

  private final RestaurantsEntryMapper restaurantsMapper;

  public RestaurantOpeningHoursEntryMapper(final RestaurantsEntryMapper restaurantsMapper) {
    this.restaurantsMapper = restaurantsMapper;
  }

  public RestaurantOpeningHoursInput toInput(final RestaurantOpeningHoursRequest request) {
    if (request == null) {
      return null;
    }

    return new RestaurantOpeningHoursInput(
        request.getDayOfWeek(),
        request.getStartTime(),
        request.getEndTime(),
        request.getRestaurantUuid()
    );
  }

  public RestaurantOpeningHoursResponse toResponse(final RestaurantOpeningHoursOutput output) {
    if (output == null) {
      return null;
    }

    return new RestaurantOpeningHoursResponse(
        output.getUuid(),
        output.getDayOfWeek(),
        output.getStartTime(),
        output.getEndTime(),
        output.getRestaurant() != null ? restaurantsMapper.toResponse(output.getRestaurant()) : null
    );
  }
}
