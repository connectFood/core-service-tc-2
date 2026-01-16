package com.connectfood.infrastructure.persistence.mappers;

import com.connectfood.core.domain.model.RestaurantType;

import com.connectfood.infrastructure.persistence.entity.RestaurantsTypeEntity;

import org.springframework.stereotype.Component;

@Component
public class RestaurantsTypeInfraMapper {

  public RestaurantsTypeInfraMapper() {
  }

  public RestaurantType toDomain(final RestaurantsTypeEntity restaurantsTypeEntity) {
    if (restaurantsTypeEntity == null) {
      return null;
    }

    return new RestaurantType(
        restaurantsTypeEntity.getUuid(),
        restaurantsTypeEntity.getName(),
        restaurantsTypeEntity.getDescription()
    );
  }

  public RestaurantsTypeEntity toEntity(final RestaurantType model) {

    if(model == null) {
      return null;
    }

    var entity = new RestaurantsTypeEntity();
    entity.setUuid(model.getUuid());
    entity.setName(model.getName());
    entity.setDescription(model.getDescription());

    return entity;
  }

  public RestaurantsTypeEntity toEntity(final RestaurantType model, final RestaurantsTypeEntity entity) {
    entity.setName(model.getName());
    entity.setDescription(model.getDescription());

    return entity;
  }
}
