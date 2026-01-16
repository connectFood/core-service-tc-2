package com.connectfood.infrastructure.rest.mappers;

import com.connectfood.core.application.restaurants.dto.RestaurantOpeningHoursInput;
import com.connectfood.core.application.restaurants.dto.RestaurantOpeningHoursOutput;
import com.connectfood.infrastructure.rest.dto.restaurantopeninghours.RestaurantOpeningHoursRequest;
import com.connectfood.infrastructure.rest.dto.restaurantopeninghours.RestaurantOpeningHoursResponse;

import org.springframework.stereotype.Component;

@Component
public class RestaurantOpeningHoursEntryMapper {

  public RestaurantOpeningHoursEntryMapper() {
  }

  public RestaurantOpeningHoursInput toInput(final RestaurantOpeningHoursRequest request) {
    if (request == null) {
      return null;
    }

    return new RestaurantOpeningHoursInput(
        request.getDayOfWeek(),
        request.getStartTime(),
        request.getEndTime()
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
        output.getEndTime()
    );
  }
}
