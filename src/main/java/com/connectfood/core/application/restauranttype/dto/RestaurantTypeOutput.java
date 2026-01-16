package com.connectfood.core.application.restauranttype.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@Getter
@AllArgsConstructor
public class RestaurantTypeOutput {

  private final UUID uuid;
  private final String name;
  private final String description;
}
