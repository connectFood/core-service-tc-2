package com.connectfood.infrastructure.persistence.mappers;

import com.connectfood.core.domain.model.RestaurantType;

import com.connectfood.infrastructure.persistence.entity.RestaurantTypeEntity;

import org.springframework.stereotype.Component;

@Component
public class RestaurantTypeInfraMapper {

  public RestaurantTypeInfraMapper() {
  }

  public RestaurantType toDomain(final RestaurantTypeEntity restaurantTypeEntity) {
    if (restaurantTypeEntity == null) {
      return null;
    }

    return new RestaurantType(
        restaurantTypeEntity.getUuid(),
        restaurantTypeEntity.getName(),
        restaurantTypeEntity.getDescription()
    );
  }

  public RestaurantTypeEntity toEntity(final RestaurantType model) {

    if(model == null) {
      return null;
    }

    var entity = new RestaurantTypeEntity();
    entity.setUuid(model.getUuid());
    entity.setName(model.getName());
    entity.setDescription(model.getDescription());

    return entity;
  }

  public RestaurantTypeEntity toEntity(final RestaurantType model, final RestaurantTypeEntity entity) {
    entity.setName(model.getName());
    entity.setDescription(model.getDescription());

    return entity;
  }
}
