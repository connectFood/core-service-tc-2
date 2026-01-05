package com.connectfood.core.application.restaurants.mapper;

import java.util.List;
import java.util.UUID;

import com.connectfood.core.application.address.dto.AddressOutput;
import com.connectfood.core.application.address.mapper.AddressAppMapper;
import com.connectfood.core.application.restaurantopeninghours.dto.RestaurantOpeningHoursOutput;
import com.connectfood.core.application.restaurantopeninghours.mapper.RestaurantOpeningHoursAppMapper;
import com.connectfood.core.application.restaurants.dto.RestaurantsInput;
import com.connectfood.core.application.restaurants.dto.RestaurantsOutput;
import com.connectfood.core.application.restaurantstype.mapper.RestaurantsTypeAppMapper;
import com.connectfood.core.domain.model.Restaurants;
import com.connectfood.core.domain.model.RestaurantsType;

import org.springframework.stereotype.Component;

@Component
public class RestaurantsAppMapper {

  private final RestaurantsTypeAppMapper restaurantsTypeMapper;
  private final RestaurantOpeningHoursAppMapper restaurantOpeningHoursMapper;
  private final AddressAppMapper addressMapper;

  public RestaurantsAppMapper(
      final RestaurantsTypeAppMapper restaurantsTypeMapper,
      final RestaurantOpeningHoursAppMapper restaurantOpeningHoursMapper,
      final AddressAppMapper addressMapper) {
    this.restaurantsTypeMapper = restaurantsTypeMapper;
    this.restaurantOpeningHoursMapper = restaurantOpeningHoursMapper;
    this.addressMapper = addressMapper;
  }

  public Restaurants toDomain(final RestaurantsInput input, final RestaurantsType restaurantsType) {
    if (input == null) {
      return null;
    }

    return new Restaurants(
        input.getName(),
        restaurantsType
    );
  }

  public Restaurants toDomain(final UUID uuid, final RestaurantsInput input, final RestaurantsType restaurantsType) {
    if (input == null) {
      return null;
    }

    return new Restaurants(
        uuid,
        input.getName(),
        restaurantsType,
        null,
        null
    );
  }

  public RestaurantsOutput toOutputAll(final Restaurants model) {
    if (model == null) {
      return null;
    }

    return new RestaurantsOutput(
        model.getUuid(),
        model.getName(),
        model.getRestaurantsType() != null ? restaurantsTypeMapper.toOutput(model.getRestaurantsType()) : null,
        null,
        null
    );
  }

  public RestaurantsOutput toOutput(final Restaurants model) {
    if (model == null) {
      return null;
    }

    return new RestaurantsOutput(
        model.getUuid(),
        model.getName(),
        model.getRestaurantsType() != null ? restaurantsTypeMapper.toOutput(model.getRestaurantsType()) : null,
        model.getOpeningHours()
            .stream()
            .map(restaurantOpeningHoursMapper::toOutput)
            .toList(),
        model.getAddress() != null ? addressMapper.toOutput(model.getAddress()) : null
    );
  }

  public RestaurantsOutput toOutput(final Restaurants model, final List<RestaurantOpeningHoursOutput> openingHours,
      final AddressOutput address) {
    if (model == null) {
      return null;
    }

    return new RestaurantsOutput(
        model.getUuid(),
        model.getName(),
        model.getRestaurantsType() != null ? restaurantsTypeMapper.toOutput(model.getRestaurantsType()) : null,
        openingHours,
        address
    );
  }
}
