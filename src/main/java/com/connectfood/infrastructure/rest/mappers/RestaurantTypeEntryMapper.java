package com.connectfood.infrastructure.rest.mappers;

import com.connectfood.core.application.restauranttype.dto.RestaurantTypeInput;

import com.connectfood.core.application.restauranttype.dto.RestaurantTypeOutput;
import com.connectfood.infrastructure.rest.dto.restauranttype.RestaurantTypeRequest;
import com.connectfood.infrastructure.rest.dto.restauranttype.RestaurantTypeResponse;

import org.springframework.stereotype.Component;

@Component
public class RestaurantTypeEntryMapper {

  public RestaurantTypeEntryMapper() {
  }

  public RestaurantTypeInput toInput(final RestaurantTypeRequest request) {

    if (request == null) {
      return null;
    }

    return new RestaurantTypeInput(
        request.getName(),
        request.getDescription()
    );
  }

  public RestaurantTypeResponse toResponse(final RestaurantTypeOutput output) {
    if (output == null) {
      return null;
    }

    return new RestaurantTypeResponse(
        output.getUuid(),
        output.getName(),
        output.getDescription()
    );
  }
}
