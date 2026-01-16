package com.connectfood.infrastructure.rest.mappers;

import com.connectfood.core.application.restaurant.dto.RestaurantInput;
import com.connectfood.core.application.restaurant.dto.RestaurantOutput;
import com.connectfood.infrastructure.rest.dto.restaurant.RestaurantRequest;
import com.connectfood.infrastructure.rest.dto.restaurant.RestaurantResponse;

import org.springframework.stereotype.Component;

@Component
public class RestaurantEntryMapper {

  private final RestaurantTypeEntryMapper restaurantsTypeMapper;
  private final RestaurantOpeningHourEntryMapper restaurantOpeningHoursMapper;
  private final AddressEntryMapper addressMapper;
  private final UserEntryMapper usersMapper;

  public RestaurantEntryMapper(final RestaurantTypeEntryMapper restaurantsTypeMapper,
                               final RestaurantOpeningHourEntryMapper restaurantOpeningHoursMapper,
                               final AddressEntryMapper addressMapper,
                               final UserEntryMapper usersMapper) {
    this.restaurantsTypeMapper = restaurantsTypeMapper;
    this.restaurantOpeningHoursMapper = restaurantOpeningHoursMapper;
    this.addressMapper = addressMapper;
    this.usersMapper = usersMapper;
  }

  public RestaurantInput toInput(final RestaurantRequest request) {
    if (request == null) {
      return null;
    }

    return new RestaurantInput(
        request.getName(),
        request.getRestaurantsTypeUuid(),
        request.getOpeningHours()
            .stream()
            .map(restaurantOpeningHoursMapper::toInput)
            .toList(),
        addressMapper.toInput(request.getAddress()),
        request.getUsersUuid()
    );
  }

  public RestaurantResponse toResponse(final RestaurantOutput output) {
    if (output == null) {
      return null;
    }

    return new RestaurantResponse(
        output.getUuid(),
        output.getName(),
        output.getRestaurantsType() != null ? restaurantsTypeMapper.toResponse(output.getRestaurantsType()) : null,
        output.getOpeningHours() != null ? output.getOpeningHours()
            .stream()
            .map(restaurantOpeningHoursMapper::toResponse)
            .toList() : null,
        output.getAddress() != null ? addressMapper.toResponse(output.getAddress()) : null,
        output.getUsers() != null ? usersMapper.toResponse(output.getUsers()) : null
    );
  }
}
