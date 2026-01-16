package com.connectfood.core.application.restaurants.mapper;

import java.util.List;
import java.util.UUID;

import com.connectfood.core.application.address.dto.AddressOutput;
import com.connectfood.core.application.address.mapper.AddressAppMapper;
import com.connectfood.core.application.restaurants.dto.RestaurantOpeningHoursOutput;
import com.connectfood.core.application.restaurants.dto.RestaurantsInput;
import com.connectfood.core.application.restaurants.dto.RestaurantsOutput;
import com.connectfood.core.application.restaurantstype.mapper.RestaurantsTypeAppMapper;
import com.connectfood.core.application.users.dto.UsersOutput;
import com.connectfood.core.application.users.mapper.UsersAppMapper;
import com.connectfood.core.domain.model.Restaurant;
import com.connectfood.core.domain.model.RestaurantType;

import org.springframework.stereotype.Component;

@Component
public class RestaurantsAppMapper {

  private final RestaurantsTypeAppMapper restaurantsTypeMapper;
  private final RestaurantOpeningHoursAppMapper restaurantOpeningHoursMapper;
  private final AddressAppMapper addressMapper;
  private final UsersAppMapper usersMapper;

  public RestaurantsAppMapper(
      final RestaurantsTypeAppMapper restaurantsTypeMapper,
      final RestaurantOpeningHoursAppMapper restaurantOpeningHoursMapper,
      final AddressAppMapper addressMapper,
      final UsersAppMapper usersMapper) {
    this.restaurantsTypeMapper = restaurantsTypeMapper;
    this.restaurantOpeningHoursMapper = restaurantOpeningHoursMapper;
    this.addressMapper = addressMapper;
    this.usersMapper = usersMapper;
  }

  public Restaurant toDomain(final RestaurantsInput input, final RestaurantType restaurantType) {
    if (input == null) {
      return null;
    }

    return new Restaurant(
        input.getName(),
        restaurantType
    );
  }

  public Restaurant toDomain(final UUID uuid, final RestaurantsInput input, final RestaurantType restaurantType) {
    if (input == null) {
      return null;
    }

    return new Restaurant(
        uuid,
        input.getName(),
        restaurantType
    );
  }

  public RestaurantsOutput toOutputAll(final Restaurant model) {
    if (model == null) {
      return null;
    }

    return new RestaurantsOutput(
        model.getUuid(),
        model.getName(),
        model.getRestaurantType() != null ? restaurantsTypeMapper.toOutput(model.getRestaurantType()) : null
    );
  }

  public RestaurantsOutput toOutput(final Restaurant model) {
    if (model == null) {
      return null;
    }

    return new RestaurantsOutput(
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

  public RestaurantsOutput toOutput(final Restaurant model, final List<RestaurantOpeningHoursOutput> openingHours,
                                    final AddressOutput address, final UsersOutput users) {
    if (model == null) {
      return null;
    }

    return new RestaurantsOutput(
        model.getUuid(),
        model.getName(),
        model.getRestaurantType() != null ? restaurantsTypeMapper.toOutput(model.getRestaurantType()) : null,
        openingHours,
        address,
        users
    );
  }
}
