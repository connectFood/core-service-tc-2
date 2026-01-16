package com.connectfood.infrastructure.persistence.mappers;

import com.connectfood.core.domain.model.Restaurant;
import com.connectfood.infrastructure.persistence.entity.RestaurantEntity;
import com.connectfood.infrastructure.persistence.entity.RestaurantTypeEntity;

import org.springframework.stereotype.Component;

@Component
public class RestaurantInfraMapper {

  private final RestaurantTypeInfraMapper restaurantsTypeMapper;
  private final RestaurantOpeningHourInfraMapper restaurantOpeningHoursMapper;
  private final AddressInfraMapper addressMapper;
  private final UserInfraMapper usersMapper;

  public RestaurantInfraMapper(
      final RestaurantTypeInfraMapper restaurantsTypeMapper,
      final RestaurantOpeningHourInfraMapper restaurantOpeningHoursMapper,
      final AddressInfraMapper addressMapper,
      final UserInfraMapper usersMapper) {
    this.restaurantsTypeMapper = restaurantsTypeMapper;
    this.restaurantOpeningHoursMapper = restaurantOpeningHoursMapper;
    this.addressMapper = addressMapper;
    this.usersMapper = usersMapper;
  }

  public Restaurant toDomain(final RestaurantEntity entity) {
    if (entity == null) {
      return null;
    }

    return new Restaurant(
        entity.getUuid(),
        entity.getName(),
        entity.getRestaurantsType() != null ? restaurantsTypeMapper.toDomain(entity.getRestaurantsType()) : null,
        entity.getOpeningHours()
            .stream()
            .map(restaurantOpeningHoursMapper::toDomain)
            .toList(),
        entity.getAddress() != null ? addressMapper.toDomain(entity.getAddress()) : null,
        entity.getUsers() != null ? usersMapper.toDomain(entity.getUsers()) : null
    );
  }

  public RestaurantEntity toEntity(final Restaurant model, final RestaurantTypeEntity restaurantTypeEntity) {
    if (model == null) {
      return null;
    }

    var entity = new RestaurantEntity();
    entity.setUuid(model.getUuid());
    entity.setName(model.getName());
    entity.setRestaurantsType(restaurantTypeEntity);
    return entity;
  }

  public RestaurantEntity toEntity(final Restaurant model, final RestaurantEntity entity,
                                   final RestaurantTypeEntity restaurantTypeEntity) {
    entity.setName(model.getName());
    entity.setRestaurantsType(restaurantTypeEntity);
    return entity;
  }
}
