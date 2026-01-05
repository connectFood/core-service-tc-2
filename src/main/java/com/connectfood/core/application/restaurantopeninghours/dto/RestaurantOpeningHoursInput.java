package com.connectfood.core.application.restaurantopeninghours.dto;

import java.time.DayOfWeek;
import java.time.LocalTime;

import lombok.Getter;

@Getter
public class RestaurantOpeningHoursInput {

  private final DayOfWeek dayOfWeek;
  private final LocalTime startTime;
  private final LocalTime endTime;

  public RestaurantOpeningHoursInput(final DayOfWeek dayOfWeek, final LocalTime startTime, final LocalTime endTime) {
    this.dayOfWeek = dayOfWeek;
    this.startTime = startTime;
    this.endTime = endTime;
  }
}
