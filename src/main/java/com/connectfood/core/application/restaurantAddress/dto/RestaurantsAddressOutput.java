package com.connectfood.core.application.restaurantAddress.dto;

import com.connectfood.core.application.restaurants.dto.RestaurantsOutput;

import lombok.Getter;

import java.util.UUID;

@Getter
public class RestaurantsAddressOutput {

  private final UUID uuid;
  private final String street;
  private final String number;
  private final String complement;
  private final String neighborhood;
  private final String city;
  private final String state;
  private final String country;
  private final String zipCode;
  private final RestaurantsOutput restaurants;

  public RestaurantsAddressOutput(
      final UUID uuid,
      final String street,
      final String number,
      final String complement,
      final String neighborhood,
      final String city,
      final String state,
      final String country,
      final String zipCode,
      final RestaurantsOutput restaurants) {
    this.uuid = uuid;
    this.street = street;
    this.number = number;
    this.complement = complement;
    this.neighborhood = neighborhood;
    this.city = city;
    this.state = state;
    this.country = country;
    this.zipCode = zipCode;
    this.restaurants = restaurants;
  }
}
