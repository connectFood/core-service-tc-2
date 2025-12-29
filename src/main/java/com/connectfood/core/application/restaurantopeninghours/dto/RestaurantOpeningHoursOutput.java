package com.connectfood.core.application.restaurantopeninghours.dto;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.UUID;

import com.connectfood.core.application.restaurants.dto.RestaurantsOutput;

import lombok.Getter;

@Getter
public class RestaurantOpeningHoursOutput {

  private final UUID uuid;
  private final DayOfWeek dayOfWeek;
  private final LocalTime startTime;
  private final LocalTime endTime;
  private final RestaurantsOutput restaurant;

  public RestaurantOpeningHoursOutput(final UUID uuid, final DayOfWeek dayOfWeek, final LocalTime startTime,
      final LocalTime endTime, final RestaurantsOutput restaurant) {
    this.uuid = uuid;
    this.dayOfWeek = dayOfWeek;
    this.startTime = startTime;
    this.endTime = endTime;
    this.restaurant = restaurant;
  }

  public RestaurantOpeningHoursOutput(final UUID uuid, final DayOfWeek dayOfWeek, final LocalTime startTime,
      final LocalTime endTime) {
    this(uuid, dayOfWeek, startTime, endTime, null);
  }
}
