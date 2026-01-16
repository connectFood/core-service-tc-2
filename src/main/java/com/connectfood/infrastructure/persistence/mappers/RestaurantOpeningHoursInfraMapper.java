package com.connectfood.infrastructure.persistence.mappers;

import com.connectfood.core.domain.model.RestaurantOpeningHour;
import com.connectfood.infrastructure.persistence.entity.RestaurantOpeningHoursEntity;
import com.connectfood.infrastructure.persistence.entity.RestaurantsEntity;

import org.springframework.stereotype.Component;

@Component
public class RestaurantOpeningHoursInfraMapper {

  public RestaurantOpeningHoursInfraMapper() {
  }

  public RestaurantOpeningHour toDomain(final RestaurantOpeningHoursEntity entity) {
    if (entity == null) {
      return null;
    }

    return new RestaurantOpeningHour(
        entity.getUuid(),
        entity.getDayOfWeek(),
        entity.getStartTime(),
        entity.getEndTime()
    );
  }

  public RestaurantOpeningHoursEntity toEntity(final RestaurantOpeningHour model,
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

  public RestaurantOpeningHoursEntity toEntity(final RestaurantOpeningHour model,
      final RestaurantOpeningHoursEntity entity) {
    entity.setStartTime(model.getStartTime());
    entity.setEndTime(model.getEndTime());

    return entity;
  }
}
