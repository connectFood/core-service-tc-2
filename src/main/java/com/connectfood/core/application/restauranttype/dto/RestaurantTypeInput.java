package com.connectfood.core.application.restauranttype.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class RestaurantTypeInput {

  private final String name;
  private final String description;

}
