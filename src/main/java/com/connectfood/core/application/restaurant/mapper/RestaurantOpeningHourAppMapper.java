package com.connectfood.core.application.restaurant.mapper;

import java.util.UUID;

import com.connectfood.core.application.restaurant.dto.RestaurantOpeningHourInput;
import com.connectfood.core.application.restaurant.dto.RestaurantOpeningHourOutput;
import com.connectfood.core.domain.model.RestaurantOpeningHour;

import org.springframework.stereotype.Component;

@Component
public class RestaurantOpeningHourAppMapper {


  public RestaurantOpeningHourAppMapper() {
  }

  public RestaurantOpeningHour toDomain(final RestaurantOpeningHourInput input) {
    if (input == null) {
      return null;
    }

    return new RestaurantOpeningHour(
        input.getDayOfWeek(),
        input.getStartTime(),
        input.getEndTime()
    );
  }

  public RestaurantOpeningHour toDomain(final UUID uuid, final RestaurantOpeningHourInput input) {
    if (input == null) {
      return null;
    }

    return new RestaurantOpeningHour(
        uuid,
        input.getDayOfWeek(),
        input.getStartTime(),
        input.getEndTime()
    );
  }

  public RestaurantOpeningHourOutput toOutput(final RestaurantOpeningHour model) {
    if (model == null) {
      return null;
    }

    return new RestaurantOpeningHourOutput(
        model.getUuid(),
        model.getDayWeek(),
        model.getStartTime(),
        model.getEndTime()
    );
  }
}
