package com.connectfood.core.application.restaurantAddress.mapper;

import com.connectfood.core.application.restaurantAddress.dto.RestaurantsAddressOutput;
import com.connectfood.core.application.restaurants.mapper.RestaurantsAppMapper;

import com.connectfood.core.domain.model.Address;
import com.connectfood.core.domain.model.Restaurants;
import com.connectfood.core.domain.model.RestaurantsAddress;

import org.springframework.stereotype.Component;

@Component
public class RestaurantsAddressAppMapper {

  private final RestaurantsAppMapper restaurantsMapper;

  public RestaurantsAddressAppMapper(RestaurantsAppMapper restaurantsMapper) {
    this.restaurantsMapper = restaurantsMapper;
  }

  public RestaurantsAddress toDomain(final Restaurants restaurants, final Address address) {

    if(restaurants == null || address == null) {
      return null;
    }

    return new RestaurantsAddress(
        restaurants,
        address
    );
  }

  public RestaurantsAddressOutput toOutput(final RestaurantsAddress model) {
    if(model == null) {
      return null;
    }

    return new RestaurantsAddressOutput(
        model.getUuid(),
        model.getAddress().getStreet(),
        model.getAddress().getNumber(),
        model.getAddress().getComplement(),
        model.getAddress().getNeighborhood(),
        model.getAddress().getCity(),
        model.getAddress().getState(),
        model.getAddress().getCountry(),
        model.getAddress().getZipCode(),
        model.getRestaurants() != null ? restaurantsMapper.toOutput(model.getRestaurants()) : null
    );
  }
}
