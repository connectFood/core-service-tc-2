package com.connectfood.core.entrypoint.rest.mappers;

import com.connectfood.core.application.address.dto.RestaurantsAddressOutput;
import com.connectfood.core.entrypoint.rest.dto.address.RestaurantsAddressResponse;

import org.springframework.stereotype.Component;

@Component
public class RestaurantsAddressEntryMapper {

  private final RestaurantsEntryMapper restaurantsMapper;

  public RestaurantsAddressEntryMapper(RestaurantsEntryMapper restaurantsMapper) {
    this.restaurantsMapper = restaurantsMapper;
  }

  public RestaurantsAddressResponse toResponse(final RestaurantsAddressOutput output) {

    if(output == null) {
      return null;
    }

    return new RestaurantsAddressResponse(
        output.getUuid(),
        output.getStreet(),
        output.getNumber(),
        output.getComplement(),
        output.getNeighborhood(),
        output.getCity(),
        output.getState(),
        output.getCountry(),
        output.getZipCode(),
        output.getRestaurants() != null ? restaurantsMapper.toResponse(output.getRestaurants()) : null
    );
  }
}
