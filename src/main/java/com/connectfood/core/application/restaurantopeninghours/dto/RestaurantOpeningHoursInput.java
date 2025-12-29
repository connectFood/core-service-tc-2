package com.connectfood.core.application.restaurantopeninghours.dto;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.UUID;

import lombok.Getter;

@Getter
public class RestaurantOpeningHoursInput {

  private final DayOfWeek dayOfWeek;
  private final LocalTime startTime;
  private final LocalTime endTime;
  private final UUID restaurantUuid;

  public RestaurantOpeningHoursInput(final DayOfWeek dayOfWeek, final LocalTime startTime,
      final LocalTime endTime, final UUID restaurantUuid) {
    this.dayOfWeek = dayOfWeek;
    this.startTime = startTime;
    this.endTime = endTime;
    this.restaurantUuid = restaurantUuid;
  }
}
