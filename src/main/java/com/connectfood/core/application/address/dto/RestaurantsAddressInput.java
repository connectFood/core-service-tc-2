package com.connectfood.core.application.address.dto;

import lombok.Getter;

import java.util.UUID;

@Getter
public class RestaurantsAddressInput {

  private final UUID restaurantsUuid;
  private final UUID addressUuid;

  public RestaurantsAddressInput(
      final UUID restaurantsUuid,
      final UUID addressUuid
  ) {
    this.restaurantsUuid = restaurantsUuid;
    this.addressUuid = addressUuid;
  }
}
