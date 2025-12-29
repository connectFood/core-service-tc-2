package com.connectfood.core.domain.model;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.UUID;

import com.connectfood.core.domain.exception.BadRequestException;

import lombok.Getter;

@Getter
public class RestaurantOpeningHours {

  private final UUID uuid;
  private final DayOfWeek dayWeek;
  private final LocalTime startTime;
  private final LocalTime endTime;
  private final Restaurants restaurant;

  public RestaurantOpeningHours(final UUID uuid, final DayOfWeek dayWeek,
      final LocalTime startTime, final LocalTime endTime, final Restaurants restaurant) {

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
    this.restaurant = restaurant;
  }

  public RestaurantOpeningHours(final DayOfWeek dayWeek, final LocalTime startTime, final LocalTime endTime,
      final Restaurants restaurant) {
    this(null, dayWeek, startTime, endTime, restaurant);
  }

  public RestaurantOpeningHours(final UUID uuid, final DayOfWeek dayWeek, final LocalTime startTime,
      final LocalTime endTime) {
    this(uuid, dayWeek, startTime, endTime, null);
  }
}
