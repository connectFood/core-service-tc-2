package com.connectfood.core.infrastructure.persistence.mappers;

import com.connectfood.core.domain.model.RestaurantOpeningHours;
import com.connectfood.core.infrastructure.persistence.entity.RestaurantOpeningHoursEntity;
import com.connectfood.core.infrastructure.persistence.entity.RestaurantsEntity;

import org.springframework.stereotype.Component;

@Component
public class RestaurantOpeningHoursInfraMapper {

  private final RestaurantsInfraMapper restaurantsMapper;

  public RestaurantOpeningHoursInfraMapper(final RestaurantsInfraMapper restaurantsMapper) {
    this.restaurantsMapper = restaurantsMapper;
  }

  public RestaurantOpeningHours toDomain(final RestaurantOpeningHoursEntity entity) {
    if (entity == null) {
      return null;
    }

    return new RestaurantOpeningHours(
        entity.getUuid(),
        entity.getDayOfWeek(),
        entity.getStartTime(),
        entity.getEndTime(),
        entity.getRestaurant() != null ? restaurantsMapper.toDomain(entity.getRestaurant()) : null
    );
  }

  public RestaurantOpeningHours toDomainAll(final RestaurantOpeningHoursEntity entity) {
    if (entity == null) {
      return null;
    }

    return new RestaurantOpeningHours(
        entity.getUuid(),
        entity.getDayOfWeek(),
        entity.getStartTime(),
        entity.getEndTime()
    );
  }

  public RestaurantOpeningHoursEntity toEntity(final RestaurantOpeningHours model,
      final RestaurantsEntity restaurantsEntity) {

    if (model == null || restaurantsEntity == null) {
      return null;
    }

    var entity = new RestaurantOpeningHoursEntity();
    entity.setUuid(model.getUuid());
    entity.setDayWeek(model.getDayWeek());
    entity.setStartTime(model.getStartTime());
    entity.setEndTime(model.getEndTime());
    entity.setRestaurant(restaurantsEntity);

    return entity;
  }

  public RestaurantOpeningHoursEntity toEntity(final RestaurantOpeningHours model,
      final RestaurantOpeningHoursEntity entity) {
    entity.setStartTime(model.getStartTime());
    entity.setEndTime(model.getEndTime());

    return entity;
  }
}
