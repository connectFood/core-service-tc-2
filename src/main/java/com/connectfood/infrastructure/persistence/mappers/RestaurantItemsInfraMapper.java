package com.connectfood.infrastructure.persistence.mappers;

import java.util.List;

import com.connectfood.core.domain.model.RestaurantItems;
import com.connectfood.core.domain.model.RestaurantItemsImages;
import com.connectfood.core.domain.model.enums.RestaurantItemServiceType;
import com.connectfood.infrastructure.persistence.entity.RestaurantItemsEntity;
import com.connectfood.infrastructure.persistence.entity.RestaurantsEntity;

import org.springframework.stereotype.Component;

@Component
public class RestaurantItemsInfraMapper {

  private final RestaurantsInfraMapper restaurantsMapper;
  private final RestaurantItemsImageInfraMapper restaurantItemsImageMapper;

  public RestaurantItemsInfraMapper(final RestaurantsInfraMapper restaurantsMapper,
      final RestaurantItemsImageInfraMapper restaurantItemsImageMapper) {
    this.restaurantsMapper = restaurantsMapper;
    this.restaurantItemsImageMapper = restaurantItemsImageMapper;
  }

  public RestaurantItems toDomain(final RestaurantItemsEntity entity) {
    if (entity == null) {
      return null;
    }

    final List<RestaurantItemsImages> images = entity.getImages() == null
        ? List.of()
        : entity.getImages()
        .stream()
        .map(restaurantItemsImageMapper::toDomain)
        .toList();

    return new RestaurantItems(
        entity.getUuid(),
        entity.getName(),
        entity.getDescription(),
        entity.getValue(),
        RestaurantItemServiceType.valueOf(entity.getRequestType()),
        entity.getRestaurant() != null ? restaurantsMapper.toDomain(entity.getRestaurant()) : null,
        images
    );
  }

  public RestaurantItems toDomainAll(final RestaurantItemsEntity entity) {
    if (entity == null) {
      return null;
    }

    return new RestaurantItems(
        entity.getUuid(),
        entity.getName(),
        entity.getDescription(),
        entity.getValue(),
        RestaurantItemServiceType.valueOf(entity.getRequestType())
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
    entity.setRequestType(model.getRequestType()
        .toString());
    entity.setRestaurant(restaurantsEntity);

    return entity;
  }

  public RestaurantItemsEntity toEntity(final RestaurantItems model, final RestaurantItemsEntity entity) {
    entity.setName(model.getName());
    entity.setDescription(model.getDescription());
    entity.setValue(model.getValue());
    entity.setRequestType(model.getRequestType()
        .toString());

    return entity;
  }
}
