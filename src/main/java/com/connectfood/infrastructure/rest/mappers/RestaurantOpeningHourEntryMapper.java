package com.connectfood.infrastructure.rest.mappers;

import com.connectfood.core.application.restaurants.dto.RestaurantOpeningHoursInput;
import com.connectfood.core.application.restaurants.dto.RestaurantOpeningHoursOutput;
import com.connectfood.infrastructure.rest.dto.restaurantopeninghour.RestaurantOpeningHourRequest;
import com.connectfood.infrastructure.rest.dto.restaurantopeninghour.RestaurantOpeningHourResponse;

import org.springframework.stereotype.Component;

@Component
public class RestaurantOpeningHourEntryMapper {

  public RestaurantOpeningHourEntryMapper() {
  }

  public RestaurantOpeningHoursInput toInput(final RestaurantOpeningHourRequest request) {
    if (request == null) {
      return null;
    }

    return new RestaurantOpeningHoursInput(
        request.getDayOfWeek(),
        request.getStartTime(),
        request.getEndTime()
    );
  }

  public RestaurantOpeningHourResponse toResponse(final RestaurantOpeningHoursOutput output) {
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
