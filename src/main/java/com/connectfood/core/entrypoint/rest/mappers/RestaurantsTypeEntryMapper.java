package com.connectfood.core.entrypoint.rest.mappers;

import com.connectfood.core.application.restaurantstype.dto.RestaurantsTypeInput;

import com.connectfood.core.application.restaurantstype.dto.RestaurantsTypeOutput;
import com.connectfood.core.entrypoint.rest.dto.restaurantstype.RestaurantsTypeRequest;
import com.connectfood.core.entrypoint.rest.dto.restaurantstype.RestaurantsTypeResponse;

import org.springframework.stereotype.Component;

@Component
public class RestaurantsTypeEntryMapper {

  public RestaurantsTypeEntryMapper() {
  }

  public RestaurantsTypeInput toInput(final RestaurantsTypeRequest request) {

    if (request == null) {
      return null;
    }

    return new RestaurantsTypeInput(
        request.getName(),
        request.getDescription()
    );
  }

  public RestaurantsTypeResponse toResponse(final RestaurantsTypeOutput output) {
    if (output == null) {
      return null;
    }

    return new RestaurantsTypeResponse(
        output.getUuid(),
        output.getName(),
        output.getDescription()
    );
  }
}
