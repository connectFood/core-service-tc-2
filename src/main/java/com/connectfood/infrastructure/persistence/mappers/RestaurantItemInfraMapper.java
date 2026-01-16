package com.connectfood.infrastructure.persistence.mappers;

import java.util.List;

import com.connectfood.core.domain.model.RestaurantItem;
import com.connectfood.core.domain.model.RestaurantItemImage;
import com.connectfood.core.domain.model.enums.RestaurantItemServiceType;
import com.connectfood.infrastructure.persistence.entity.RestaurantItemEntity;
import com.connectfood.infrastructure.persistence.entity.RestaurantEntity;

import org.springframework.stereotype.Component;

@Component
public class RestaurantItemInfraMapper {

  private final RestaurantInfraMapper restaurantsMapper;
  private final RestaurantItemImageInfraMapper restaurantItemsImageMapper;

  public RestaurantItemInfraMapper(final RestaurantInfraMapper restaurantsMapper,
                                   final RestaurantItemImageInfraMapper restaurantItemsImageMapper) {
    this.restaurantsMapper = restaurantsMapper;
    this.restaurantItemsImageMapper = restaurantItemsImageMapper;
  }

  public RestaurantItem toDomain(final RestaurantItemEntity entity) {
    if (entity == null) {
      return null;
    }

    final List<RestaurantItemImage> images = entity.getImages() == null
        ? List.of()
        : entity.getImages()
        .stream()
        .map(restaurantItemsImageMapper::toDomain)
        .toList();

    return new RestaurantItem(
        entity.getUuid(),
        entity.getName(),
        entity.getDescription(),
        entity.getValue(),
        RestaurantItemServiceType.valueOf(entity.getRequestType()),
        entity.getRestaurant() != null ? restaurantsMapper.toDomain(entity.getRestaurant()) : null,
        images
    );
  }

  public RestaurantItem toDomainAll(final RestaurantItemEntity entity) {
    if (entity == null) {
      return null;
    }

    return new RestaurantItem(
        entity.getUuid(),
        entity.getName(),
        entity.getDescription(),
        entity.getValue(),
        RestaurantItemServiceType.valueOf(entity.getRequestType())
    );
  }

  public RestaurantItemEntity toEntity(final RestaurantItem model, final RestaurantEntity restaurantEntity) {

    if (model == null || restaurantEntity == null) {
      return null;
    }

    var entity = new RestaurantItemEntity();
    entity.setUuid(model.getUuid());
    entity.setName(model.getName());
    entity.setDescription(model.getDescription());
    entity.setValue(model.getValue());
    entity.setRequestType(model.getRequestType()
        .toString());
    entity.setRestaurant(restaurantEntity);

    return entity;
  }

  public RestaurantItemEntity toEntity(final RestaurantItem model, final RestaurantItemEntity entity) {
    entity.setName(model.getName());
    entity.setDescription(model.getDescription());
    entity.setValue(model.getValue());
    entity.setRequestType(model.getRequestType()
        .toString());

    return entity;
  }
}
