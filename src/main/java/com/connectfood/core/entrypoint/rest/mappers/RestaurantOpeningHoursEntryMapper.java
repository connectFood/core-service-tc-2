package com.connectfood.core.entrypoint.rest.mappers;

import com.connectfood.core.application.restaurantopeninghours.dto.RestaurantOpeningHoursInput;
import com.connectfood.core.application.restaurantopeninghours.dto.RestaurantOpeningHoursOutput;
import com.connectfood.core.entrypoint.rest.dto.restaurantopeninghours.RestaurantOpeningHoursRequest;
import com.connectfood.core.entrypoint.rest.dto.restaurantopeninghours.RestaurantOpeningHoursResponse;

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
