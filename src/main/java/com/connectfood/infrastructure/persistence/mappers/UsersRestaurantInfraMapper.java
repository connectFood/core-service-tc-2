package com.connectfood.infrastructure.persistence.mappers;

import java.util.UUID;

import com.connectfood.core.domain.model.UserRestaurant;
import com.connectfood.infrastructure.persistence.entity.RestaurantsEntity;
import com.connectfood.infrastructure.persistence.entity.UsersEntity;
import com.connectfood.infrastructure.persistence.entity.UsersRestaurantEntity;

import org.springframework.stereotype.Component;

@Component
public class UsersRestaurantInfraMapper {

  private final UsersInfraMapper usersMapper;
  private final RestaurantsInfraMapper restaurantsMapper;

  public UsersRestaurantInfraMapper(final UsersInfraMapper usersMapper,
      final RestaurantsInfraMapper restaurantsMapper) {
    this.usersMapper = usersMapper;
    this.restaurantsMapper = restaurantsMapper;
  }

  public UserRestaurant toDomain(final UsersRestaurantEntity entity) {
    if (entity == null) {
      return null;
    }

    return new UserRestaurant(
        entity.getUuid(),
        usersMapper.toDomain(entity.getUsers()),
        restaurantsMapper.toDomain(entity.getRestaurants())
    );
  }

  public UsersRestaurantEntity toEntity(final UUID uuid, final UsersEntity usersEntity,
      final RestaurantsEntity restaurantsEntity) {
    if (usersEntity == null || restaurantsEntity == null) {
      return null;
    }

    var entity = new UsersRestaurantEntity();
    entity.setUuid(uuid);
    entity.setUsers(usersEntity);
    entity.setRestaurants(restaurantsEntity);
    return entity;
  }
}
