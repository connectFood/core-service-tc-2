package com.connectfood.core.application.restaurant.dto;

import java.time.DayOfWeek;
import java.time.LocalTime;

import lombok.Getter;

@Getter
public class RestaurantOpeningHourInput {

  private final DayOfWeek dayOfWeek;
  private final LocalTime startTime;
  private final LocalTime endTime;

  public RestaurantOpeningHourInput(final DayOfWeek dayOfWeek, final LocalTime startTime, final LocalTime endTime) {
    this.dayOfWeek = dayOfWeek;
    this.startTime = startTime;
    this.endTime = endTime;
  }
}
