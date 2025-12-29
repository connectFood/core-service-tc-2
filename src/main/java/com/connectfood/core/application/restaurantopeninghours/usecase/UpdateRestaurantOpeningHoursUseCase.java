package com.connectfood.core.application.restaurantopeninghours.usecase;

import java.util.UUID;

import com.connectfood.core.application.restaurantopeninghours.dto.RestaurantOpeningHoursInput;
import com.connectfood.core.application.restaurantopeninghours.dto.RestaurantOpeningHoursOutput;
import com.connectfood.core.application.restaurantopeninghours.mapper.RestaurantOpeningHoursAppMapper;
import com.connectfood.core.domain.exception.NotFoundException;
import com.connectfood.core.domain.repository.RestaurantOpeningHoursRepository;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class UpdateRestaurantOpeningHoursUseCase {

  private final RestaurantOpeningHoursRepository repository;
  private final RestaurantOpeningHoursAppMapper mapper;

  public UpdateRestaurantOpeningHoursUseCase(
      final RestaurantOpeningHoursRepository repository,
      final RestaurantOpeningHoursAppMapper mapper) {
    this.repository = repository;
    this.mapper = mapper;
  }

  @Transactional
  public RestaurantOpeningHoursOutput execute(final UUID uuid, final RestaurantOpeningHoursInput input) {
    final var model = repository.findByUuid(uuid)
        .orElseThrow(() -> new NotFoundException("Restaurant Items not found"));

    final var modelUpdated = repository.update(uuid, mapper.toDomain(uuid, input, model.getRestaurant()));

    return mapper.toOutput(modelUpdated);
  }
}
