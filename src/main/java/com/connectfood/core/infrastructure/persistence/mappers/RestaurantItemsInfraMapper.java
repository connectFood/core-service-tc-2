package com.connectfood.core.infrastructure.persistence.mappers;

import com.connectfood.core.domain.model.RestaurantItems;
import com.connectfood.core.infrastructure.persistence.entity.RestaurantItemsEntity;
import com.connectfood.core.infrastructure.persistence.entity.RestaurantsEntity;

import org.springframework.stereotype.Component;

@Component
public class RestaurantItemsInfraMapper {

  private final RestaurantsInfraMapper restaurantsMapper;

  public RestaurantItemsInfraMapper(final RestaurantsInfraMapper restaurantsMapper) {
    this.restaurantsMapper = restaurantsMapper;
  }

  public RestaurantItems toDomain(final RestaurantItemsEntity entity) {
    if (entity == null) {
      return null;
    }

    return new RestaurantItems(
        entity.getUuid(),
        entity.getName(),
        entity.getDescription(),
        entity.getValue(),
        entity.getRequestType(),
        entity.getRestaurant() != null ? restaurantsMapper.toDomain(entity.getRestaurant()) : null
    );
  }

  public RestaurantItemsEntity toEntity(final RestaurantItems model, final RestaurantsEntity restaurantsEntity) {

    if (model == null || restaurantsEntity == null) {
      return null;
    }

    var entity = new RestaurantItemsEntity();
    entity.setUuid(model.getUuid());
    entity.setName(model.getName());
    entity.setDescription(model.getDescription());
    entity.setValue(model.getValue());
    entity.setRequestType(model.getRequestType());
    entity.setRestaurant(restaurantsEntity);

    return entity;
  }

  public RestaurantItemsEntity toEntity(final RestaurantItems model, final RestaurantItemsEntity entity) {
    entity.setName(model.getName());
    entity.setDescription(model.getDescription());
    entity.setValue(model.getValue());
    entity.setRequestType(model.getRequestType());

    return entity;
  }
}
