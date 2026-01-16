package com.connectfood.infrastructure.persistence.mappers;

import com.connectfood.core.domain.model.RestaurantsType;

import com.connectfood.infrastructure.persistence.entity.RestaurantsTypeEntity;

import org.springframework.stereotype.Component;

@Component
public class RestaurantsTypeInfraMapper {

  public RestaurantsTypeInfraMapper() {
  }

  public RestaurantsType toDomain(final RestaurantsTypeEntity restaurantsTypeEntity) {
    if (restaurantsTypeEntity == null) {
      return null;
    }

    return new RestaurantsType(
        restaurantsTypeEntity.getUuid(),
        restaurantsTypeEntity.getName(),
        restaurantsTypeEntity.getDescription()
    );
  }

  public RestaurantsTypeEntity toEntity(final RestaurantsType model) {

    if(model == null) {
      return null;
    }

    var entity = new RestaurantsTypeEntity();
    entity.setUuid(model.getUuid());
    entity.setName(model.getName());
    entity.setDescription(model.getDescription());

    return entity;
  }

  public RestaurantsTypeEntity toEntity(final RestaurantsType model, final RestaurantsTypeEntity entity) {
    entity.setName(model.getName());
    entity.setDescription(model.getDescription());

    return entity;
  }
}
