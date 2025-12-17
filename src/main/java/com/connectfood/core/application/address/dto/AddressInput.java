package com.connectfood.core.application.address.dto;

import lombok.Getter;

@Getter
public class AddressInput {

  private final String street;
  private final String number;
  private final String complement;
  private final String neighborhood;
  private final String city;
  private final String state;
  private final String country;
  private final String zipCode;

  public AddressInput(final String street, final String number, final String complement, final String neighborhood,
      final String city, final String state, final String country, final String zipCode) {
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
