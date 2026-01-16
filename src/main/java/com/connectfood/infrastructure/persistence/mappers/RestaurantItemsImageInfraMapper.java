package com.connectfood.infrastructure.persistence.mappers;

import com.connectfood.core.domain.model.RestaurantItemImage;
import com.connectfood.infrastructure.persistence.entity.RestaurantItemsEntity;
import com.connectfood.infrastructure.persistence.entity.RestaurantItemsImagesEntity;

import org.springframework.stereotype.Component;

@Component
public class RestaurantItemsImageInfraMapper {

  public RestaurantItemsImageInfraMapper() {
  }

  public RestaurantItemImage toDomain(final RestaurantItemsImagesEntity entity) {
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

  public RestaurantItemsImagesEntity toEntity(final RestaurantItemImage model,
      final RestaurantItemsEntity restaurantsItemsEntity) {

    if (model == null || restaurantsItemsEntity == null) {
      return null;
    }

    var entity = new RestaurantItemsImagesEntity();
    entity.setUuid(model.getUuid());
    entity.setName(model.getName());
    entity.setDescription(model.getDescription());
    entity.setPath(model.getPath());
    entity.setRestaurantItems(restaurantsItemsEntity);

    return entity;
  }

  public RestaurantItemsImagesEntity toEntity(final RestaurantItemImage model,
      final RestaurantItemsImagesEntity entity) {
    entity.setName(model.getName());
    entity.setDescription(model.getDescription());

    return entity;
  }
}
