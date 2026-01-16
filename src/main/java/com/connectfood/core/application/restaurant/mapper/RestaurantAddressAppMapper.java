package com.connectfood.core.application.restaurant.mapper;

import com.connectfood.core.domain.model.Address;
import com.connectfood.core.domain.model.Restaurant;
import com.connectfood.core.domain.model.RestaurantsAddress;

import org.springframework.stereotype.Component;

@Component
public class RestaurantAddressAppMapper {

  public RestaurantAddressAppMapper() {
  }

  public RestaurantsAddress toDomain(final Restaurant restaurant, final Address address) {

    if (restaurant == null || address == null) {
      return null;
    }

    return new RestaurantsAddress(
        restaurant,
        address
    );
  }
}
