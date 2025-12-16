package com.connectfood.core.application.address.dto;

import java.util.UUID;

import lombok.Getter;

@Getter
public class AddressOutput {

  private final UUID uuid;
  private final String street;
  private final String number;
  private final String complement;
  private final String neighborhood;
  private final String city;
  private final String state;
  private final String country;
  private final String zipCode;

  public AddressOutput(final UUID uuid, final String street, final String number, final String complement,
      final String neighborhood, final String city, final String state, final String country, final String zipCode) {
    this.uuid = uuid;
    this.street = street;
    this.number = number;
    this.complement = complement;
    this.neighborhood = neighborhood;
    this.city = city;
    this.state = state;
    this.country = country;
    this.zipCode = zipCode;
  }
}
