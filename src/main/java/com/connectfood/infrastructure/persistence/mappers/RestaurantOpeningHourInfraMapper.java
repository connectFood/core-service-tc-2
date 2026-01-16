package com.connectfood.infrastructure.persistence.mappers;

import com.connectfood.core.domain.model.RestaurantOpeningHour;
import com.connectfood.infrastructure.persistence.entity.RestaurantOpeningHourEntity;
import com.connectfood.infrastructure.persistence.entity.RestaurantEntity;

import org.springframework.stereotype.Component;

@Component
public class RestaurantOpeningHourInfraMapper {

  public RestaurantOpeningHourInfraMapper() {
  }

  public RestaurantOpeningHour toDomain(final RestaurantOpeningHourEntity entity) {
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

  public RestaurantOpeningHourEntity toEntity(final RestaurantOpeningHour model,
                                              final RestaurantEntity restaurantEntity) {

    if (model == null || restaurantEntity == null) {
      return null;
    }

    var entity = new RestaurantOpeningHourEntity();
    entity.setUuid(model.getUuid());
    entity.setDayWeek(model.getDayWeek());
    entity.setStartTime(model.getStartTime());
    entity.setEndTime(model.getEndTime());
    entity.setRestaurant(restaurantEntity);

    return entity;
  }

  public RestaurantOpeningHourEntity toEntity(final RestaurantOpeningHour model,
                                              final RestaurantOpeningHourEntity entity) {
    entity.setStartTime(model.getStartTime());
    entity.setEndTime(model.getEndTime());

    return entity;
  }
}
