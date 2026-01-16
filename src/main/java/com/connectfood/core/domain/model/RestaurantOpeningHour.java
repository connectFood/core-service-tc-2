package com.connectfood.core.domain.model;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.UUID;

import com.connectfood.core.domain.exception.BadRequestException;

import lombok.Getter;

@Getter
public class RestaurantOpeningHour {

  private final UUID uuid;
  private final DayOfWeek dayWeek;
  private final LocalTime startTime;
  private final LocalTime endTime;

  public RestaurantOpeningHour(final UUID uuid, final DayOfWeek dayWeek,
                               final LocalTime startTime, final LocalTime endTime) {

    if (dayWeek == null) {
      throw new BadRequestException("Day of week is required");
    }

    if (startTime == null) {
      throw new BadRequestException("Start time is required");
    }

    if (endTime == null) {
      throw new BadRequestException("End time is required");
    }

    this.uuid = uuid == null ? UUID.randomUUID() : uuid;
    this.dayWeek = dayWeek;
    this.startTime = startTime;
    this.endTime = endTime;
  }

  public RestaurantOpeningHour(final DayOfWeek dayWeek, final LocalTime startTime, final LocalTime endTime) {
    this(null, dayWeek, startTime, endTime);
  }
}
