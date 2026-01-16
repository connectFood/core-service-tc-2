package com.connectfood.core.application.restaurants.mapper;

import java.util.UUID;

import com.connectfood.core.application.restaurants.dto.RestaurantOpeningHoursInput;
import com.connectfood.core.application.restaurants.dto.RestaurantOpeningHoursOutput;
import com.connectfood.core.domain.model.RestaurantOpeningHour;

import org.springframework.stereotype.Component;

@Component
public class RestaurantOpeningHoursAppMapper {


  public RestaurantOpeningHoursAppMapper() {
  }

  public RestaurantOpeningHour toDomain(final RestaurantOpeningHoursInput input) {
    if (input == null) {
      return null;
    }

    return new RestaurantOpeningHour(
        input.getDayOfWeek(),
        input.getStartTime(),
        input.getEndTime()
    );
  }

  public RestaurantOpeningHour toDomain(final UUID uuid, final RestaurantOpeningHoursInput input) {
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

  public RestaurantOpeningHoursOutput toOutput(final RestaurantOpeningHour model) {
    if (model == null) {
      return null;
    }

    return new RestaurantOpeningHoursOutput(
        model.getUuid(),
        model.getDayWeek(),
        model.getStartTime(),
        model.getEndTime()
    );
  }
}
