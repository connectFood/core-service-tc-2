package com.connectfood.infrastructure.persistence.mappers;

import java.util.UUID;

import com.connectfood.core.domain.model.UserRestaurant;
import com.connectfood.infrastructure.persistence.entity.RestaurantEntity;
import com.connectfood.infrastructure.persistence.entity.UserEntity;
import com.connectfood.infrastructure.persistence.entity.UserRestaurantEntity;

import org.springframework.stereotype.Component;

@Component
public class UserRestaurantInfraMapper {

  private final UserInfraMapper usersMapper;
  private final RestaurantInfraMapper restaurantsMapper;

  public UserRestaurantInfraMapper(final UserInfraMapper usersMapper,
                                   final RestaurantInfraMapper restaurantsMapper) {
    this.usersMapper = usersMapper;
    this.restaurantsMapper = restaurantsMapper;
  }

  public UserRestaurant toDomain(final UserRestaurantEntity entity) {
    if (entity == null) {
      return null;
    }

    return new UserRestaurant(
        entity.getUuid(),
        usersMapper.toDomain(entity.getUsers()),
        restaurantsMapper.toDomain(entity.getRestaurants())
    );
  }

  public UserRestaurantEntity toEntity(final UUID uuid, final UserEntity userEntity,
      final RestaurantEntity restaurantEntity) {
    if (userEntity == null || restaurantEntity == null) {
      return null;
    }

    var entity = new UserRestaurantEntity();
    entity.setUuid(uuid);
    entity.setUsers(userEntity);
    entity.setRestaurants(restaurantEntity);
    return entity;
  }
}
