package com.connectfood.core.entrypoint.rest.mappers;

import com.connectfood.core.application.restaurants.dto.RestaurantsInput;

import com.connectfood.core.application.restaurants.dto.RestaurantsOutput;
import com.connectfood.core.entrypoint.rest.dto.restaurants.RestaurantsRequest;

import com.connectfood.core.entrypoint.rest.dto.restaurants.RestaurantsResponse;

import org.springframework.stereotype.Component;

@Component
public class RestaurantsEntryMapper {

  private final RestaurantsTypeEntryMapper restaurantsTypeMapper;

  public RestaurantsEntryMapper(RestaurantsTypeEntryMapper restaurantsTypeMapper) {
    this.restaurantsTypeMapper = restaurantsTypeMapper;
  }

  public RestaurantsInput toInput(final RestaurantsRequest request) {
    if(request == null) {
      return null;
    }

    return new RestaurantsInput(
        request.getName(),
        request.getRestaurantsTypeUuid()
    );
  }

  public RestaurantsResponse toResponse(final RestaurantsOutput output) {
    if(output == null) {
      return null;
    }

    return new RestaurantsResponse(
        output.getUuid(),
        output.getName(),
        output.getRestaurantsType() != null ? restaurantsTypeMapper.toResponse(output.getRestaurantsType()) : null
    );
  }
}
