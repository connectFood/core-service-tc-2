package com.connectfood.core.application.address.mapper;

import com.connectfood.core.domain.model.Address;
import com.connectfood.core.domain.model.Restaurants;
import com.connectfood.core.domain.model.RestaurantsAddress;

import org.springframework.stereotype.Component;

@Component
public class RestaurantsAddressAppMapper {

  public RestaurantsAddressAppMapper() {
  }

  public RestaurantsAddress toDomain(final Restaurants restaurants, final Address address) {

    if (restaurants == null || address == null) {
      return null;
    }

    return new RestaurantsAddress(
        restaurants,
        address
    );
  }
}
