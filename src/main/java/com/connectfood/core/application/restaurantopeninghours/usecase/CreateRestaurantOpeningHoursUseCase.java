package com.connectfood.core.application.restaurantopeninghours.usecase;

import java.util.UUID;

import com.connectfood.core.application.restaurantopeninghours.dto.RestaurantOpeningHoursInput;
import com.connectfood.core.application.restaurantopeninghours.dto.RestaurantOpeningHoursOutput;
import com.connectfood.core.application.restaurantopeninghours.mapper.RestaurantOpeningHoursAppMapper;
import com.connectfood.core.domain.model.Restaurants;
import com.connectfood.core.domain.repository.RestaurantOpeningHoursRepository;
import com.connectfood.core.domain.repository.RestaurantsRepository;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class CreateRestaurantOpeningHoursUseCase {

  private final RestaurantOpeningHoursRepository repository;
  private final RestaurantOpeningHoursAppMapper mapper;

  public CreateRestaurantOpeningHoursUseCase(
      final RestaurantOpeningHoursRepository repository,
      final RestaurantOpeningHoursAppMapper mapper) {
    this.repository = repository;
    this.mapper = mapper;
  }

  @Transactional
  public RestaurantOpeningHoursOutput execute(final UUID restaurantUuid, final RestaurantOpeningHoursInput input) {
    var model = repository.save(mapper.toDomain(input), restaurantUuid);

    return mapper.toOutput(model);
  }
}
