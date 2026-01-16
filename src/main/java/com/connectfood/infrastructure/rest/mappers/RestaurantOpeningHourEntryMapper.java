package com.connectfood.infrastructure.rest.mappers;

import com.connectfood.core.application.restaurant.dto.RestaurantOpeningHourInput;
import com.connectfood.core.application.restaurant.dto.RestaurantOpeningHourOutput;
import com.connectfood.infrastructure.rest.dto.restaurantopeninghour.RestaurantOpeningHourRequest;
import com.connectfood.infrastructure.rest.dto.restaurantopeninghour.RestaurantOpeningHourResponse;

import org.springframework.stereotype.Component;

@Component
public class RestaurantOpeningHourEntryMapper {

  public RestaurantOpeningHourEntryMapper() {
  }

  public RestaurantOpeningHourInput toInput(final RestaurantOpeningHourRequest request) {
    if (request == null) {
      return null;
    }

    return new RestaurantOpeningHourInput(
        request.getDayOfWeek(),
        request.getStartTime(),
        request.getEndTime()
    );
  }

  public RestaurantOpeningHourResponse toResponse(final RestaurantOpeningHourOutput output) {
    if (output == null) {
      return null;
    }

    return new RestaurantOpeningHourResponse(
        output.getUuid(),
        output.getDayOfWeek(),
        output.getStartTime(),
        output.getEndTime()
    );
  }
}
