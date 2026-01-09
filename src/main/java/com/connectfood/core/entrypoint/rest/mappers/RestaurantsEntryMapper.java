package com.connectfood.core.entrypoint.rest.mappers;

import com.connectfood.core.application.restaurants.dto.RestaurantsInput;
import com.connectfood.core.application.restaurants.dto.RestaurantsOutput;
import com.connectfood.core.entrypoint.rest.dto.restaurants.RestaurantsRequest;
import com.connectfood.core.entrypoint.rest.dto.restaurants.RestaurantsResponse;

import org.springframework.stereotype.Component;

@Component
public class RestaurantsEntryMapper {

  private final RestaurantsTypeEntryMapper restaurantsTypeMapper;
  private final RestaurantOpeningHoursEntryMapper restaurantOpeningHoursMapper;
  private final AddressEntryMapper addressMapper;
  private final UsersEntryMapper usersMapper;

  public RestaurantsEntryMapper(final RestaurantsTypeEntryMapper restaurantsTypeMapper,
      final RestaurantOpeningHoursEntryMapper restaurantOpeningHoursMapper,
      final AddressEntryMapper addressMapper,
      final UsersEntryMapper usersMapper) {
    this.restaurantsTypeMapper = restaurantsTypeMapper;
    this.restaurantOpeningHoursMapper = restaurantOpeningHoursMapper;
    this.addressMapper = addressMapper;
    this.usersMapper = usersMapper;
  }

  public RestaurantsInput toInput(final RestaurantsRequest request) {
    if (request == null) {
      return null;
    }

    return new RestaurantsInput(
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

  public RestaurantsResponse toResponse(final RestaurantsOutput output) {
    if (output == null) {
      return null;
    }

    return new RestaurantsResponse(
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
