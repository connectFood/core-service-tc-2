package com.connectfood.infrastructure.persistence.mappers;

import com.connectfood.core.domain.model.RestaurantItemImage;
import com.connectfood.infrastructure.persistence.entity.RestaurantItemEntity;
import com.connectfood.infrastructure.persistence.entity.RestaurantItemImageEntity;

import org.springframework.stereotype.Component;

@Component
public class RestaurantItemImageInfraMapper {

  public RestaurantItemImageInfraMapper() {
  }

  public RestaurantItemImage toDomain(final RestaurantItemImageEntity entity) {
    if (entity == null) {
      return null;
    }

    return new RestaurantItemImage(
        entity.getUuid(),
        entity.getName(),
        entity.getDescription(),
        entity.getPath()
    );
  }

  public RestaurantItemImageEntity toEntity(final RestaurantItemImage model,
      final RestaurantItemEntity restaurantsItemsEntity) {

    if (model == null || restaurantsItemsEntity == null) {
      return null;
    }

    var entity = new RestaurantItemImageEntity();
    entity.setUuid(model.getUuid());
    entity.setName(model.getName());
    entity.setDescription(model.getDescription());
    entity.setPath(model.getPath());
    entity.setRestaurantItems(restaurantsItemsEntity);

    return entity;
  }

  public RestaurantItemImageEntity toEntity(final RestaurantItemImage model,
      final RestaurantItemImageEntity entity) {
    entity.setName(model.getName());
    entity.setDescription(model.getDescription());

    return entity;
  }
}
