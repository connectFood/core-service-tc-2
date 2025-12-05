package com.connectfood.core.application.restaurantstype.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class RestaurantsTypeInput {

  private final String name;
  private final String description;

}
