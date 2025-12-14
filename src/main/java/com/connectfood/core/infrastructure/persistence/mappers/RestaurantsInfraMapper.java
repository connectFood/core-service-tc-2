package com.connectfood.core.infrastructure.persistence.mappers;

import com.connectfood.core.domain.model.Restaurants;

import com.connectfood.core.infrastructure.persistence.entity.RestaurantsEntity;

import com.connectfood.core.infrastructure.persistence.entity.RestaurantsTypeEntity;

import org.springframework.stereotype.Component;

@Component
public class RestaurantsInfraMapper {

  private final RestaurantsTypeInfraMapper restaurantsTypeMapper;

  public RestaurantsInfraMapper(final RestaurantsTypeInfraMapper restaurantsTypeMapper) {
    this.restaurantsTypeMapper = restaurantsTypeMapper;
  }

  public Restaurants toDomain(final RestaurantsEntity entity) {
    if(entity == null) {
      return null;
    }

    return new Restaurants(
        entity.getUuid(),
        entity.getName(),
        entity.getRestaurantsType() != null ? restaurantsTypeMapper.toDomain(entity.getRestaurantsType()) : null
    );
  }

  public RestaurantsEntity toEntity(final Restaurants model, final RestaurantsTypeEntity restaurantsTypeEntity) {
    if(model == null) {
      return null;
    }

    var entity = new RestaurantsEntity();
    entity.setUuid(model.getUuid());
    entity.setName(model.getName());
    entity.setRestaurantsType(restaurantsTypeEntity);
    return entity;
  }

  public RestaurantsEntity toEntity(final Restaurants model, final RestaurantsEntity entity,
      final RestaurantsTypeEntity restaurantsTypeEntity) {
    entity.setName(model.getName());
    entity.setRestaurantsType(restaurantsTypeEntity);
    return entity;
  }
}
