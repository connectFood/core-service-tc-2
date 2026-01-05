package com.connectfood.core.application.restaurantopeninghours.mapper;

import java.util.UUID;

import com.connectfood.core.application.restaurantopeninghours.dto.RestaurantOpeningHoursInput;
import com.connectfood.core.application.restaurantopeninghours.dto.RestaurantOpeningHoursOutput;
import com.connectfood.core.domain.model.RestaurantOpeningHours;
import com.connectfood.core.domain.model.Restaurants;

import org.springframework.stereotype.Component;

@Component
public class RestaurantOpeningHoursAppMapper {


  public RestaurantOpeningHoursAppMapper() {
  }

  public RestaurantOpeningHours toDomain(final RestaurantOpeningHoursInput input) {
    if (input == null) {
      return null;
    }

    return new RestaurantOpeningHours(
        input.getDayOfWeek(),
        input.getStartTime(),
        input.getEndTime()
    );
  }

  public RestaurantOpeningHours toDomain(final UUID uuid, final RestaurantOpeningHoursInput input,
      final Restaurants restaurants) {
    if (input == null || restaurants == null) {
      return null;
    }

    return new RestaurantOpeningHours(
        uuid,
        input.getDayOfWeek(),
        input.getStartTime(),
        input.getEndTime()
    );
  }

  public RestaurantOpeningHoursOutput toOutput(final RestaurantOpeningHours model) {
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
