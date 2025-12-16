package com.connectfood.core.domain.model;

import java.util.UUID;

import com.connectfood.core.domain.exception.BadRequestException;

import lombok.Getter;

@Getter
public class Address {

  private final UUID uuid;
  private final String street;
  private final String number;
  private final String complement;
  private final String neighborhood;
  private final String city;
  private final String state;
  private final String country;
  private final String zipCode;

  public Address(final UUID uuid, final String street, final String number, final String complement,
      final String neighborhood, final String city, final String state, final String country, final String zipCode) {

    if (street == null || street.isBlank()) {
      throw new BadRequestException("Street is required");
    }

    if (number == null || number.isBlank()) {
      throw new BadRequestException("Number is required");
    }

    if (neighborhood == null || neighborhood.isBlank()) {
      throw new BadRequestException("Neighborhood is required");
    }

    if (city == null || city.isBlank()) {
      throw new BadRequestException("City is required");
    }

    if (state == null || state.isBlank()) {
      throw new BadRequestException("State is required");
    }

    if (country == null || country.isBlank()) {
      throw new BadRequestException("Country is required");
    }

    if (zipCode == null || zipCode.isBlank()) {
      throw new BadRequestException("Zip code is required");
    }

    this.uuid = uuid == null ? UUID.randomUUID() : uuid;
    this.street = street;
    this.number = number;
    this.complement = complement;
    this.neighborhood = neighborhood;
    this.city = city;
    this.state = state;
    this.country = country;
    this.zipCode = zipCode;
  }

  public Address(final String street, final String number, final String complement, final String neighborhood,
      final String city, final String state, final String country, final String zipCode) {
    this(null, street, number, complement, neighborhood, city, state, country, zipCode);
  }
}
