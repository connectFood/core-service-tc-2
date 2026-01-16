package com.connectfood.core.application.restaurant.mapper;

import java.util.List;
import java.util.UUID;

import com.connectfood.core.application.address.dto.AddressOutput;
import com.connectfood.core.application.address.mapper.AddressAppMapper;
import com.connectfood.core.application.restaurant.dto.RestaurantOpeningHourOutput;
import com.connectfood.core.application.restaurant.dto.RestaurantInput;
import com.connectfood.core.application.restaurant.dto.RestaurantOutput;
import com.connectfood.core.application.restauranttype.mapper.RestaurantTypeAppMapper;
import com.connectfood.core.application.user.dto.UserOutput;
import com.connectfood.core.application.user.mapper.UserAppMapper;
import com.connectfood.core.domain.model.Restaurant;
import com.connectfood.core.domain.model.RestaurantType;

import org.springframework.stereotype.Component;

@Component
public class RestaurantAppMapper {

  private final RestaurantTypeAppMapper restaurantsTypeMapper;
  private final RestaurantOpeningHourAppMapper restaurantOpeningHoursMapper;
  private final AddressAppMapper addressMapper;
  private final UserAppMapper usersMapper;

  public RestaurantAppMapper(
      final RestaurantTypeAppMapper restaurantsTypeMapper,
      final RestaurantOpeningHourAppMapper restaurantOpeningHoursMapper,
      final AddressAppMapper addressMapper,
      final UserAppMapper usersMapper) {
    this.restaurantsTypeMapper = restaurantsTypeMapper;
    this.restaurantOpeningHoursMapper = restaurantOpeningHoursMapper;
    this.addressMapper = addressMapper;
    this.usersMapper = usersMapper;
  }

  public Restaurant toDomain(final RestaurantInput input, final RestaurantType restaurantType) {
    if (input == null) {
      return null;
    }

    return new Restaurant(
        input.getName(),
        restaurantType
    );
  }

  public Restaurant toDomain(final UUID uuid, final RestaurantInput input, final RestaurantType restaurantType) {
    if (input == null) {
      return null;
    }

    return new Restaurant(
        uuid,
        input.getName(),
        restaurantType
    );
  }

  public RestaurantOutput toOutputAll(final Restaurant model) {
    if (model == null) {
      return null;
    }

    return new RestaurantOutput(
        model.getUuid(),
        model.getName(),
        model.getRestaurantType() != null ? restaurantsTypeMapper.toOutput(model.getRestaurantType()) : null
    );
  }

  public RestaurantOutput toOutput(final Restaurant model) {
    if (model == null) {
      return null;
    }

    return new RestaurantOutput(
        model.getUuid(),
        model.getName(),
        model.getRestaurantType() != null ? restaurantsTypeMapper.toOutput(model.getRestaurantType()) : null,
        model.getOpeningHours()
            .stream()
            .map(restaurantOpeningHoursMapper::toOutput)
            .toList(),
        model.getAddress() != null ? addressMapper.toOutput(model.getAddress()) : null,
        model.getUser() != null ? usersMapper.toOutput(model.getUser()) : null
    );
  }

  public RestaurantOutput toOutput(final Restaurant model, final List<RestaurantOpeningHourOutput> openingHours,
                                    final AddressOutput address, final UserOutput users) {
    if (model == null) {
      return null;
    }

    return new RestaurantOutput(
        model.getUuid(),
        model.getName(),
        model.getRestaurantType() != null ? restaurantsTypeMapper.toOutput(model.getRestaurantType()) : null,
        openingHours,
        address,
        users
    );
  }
}
