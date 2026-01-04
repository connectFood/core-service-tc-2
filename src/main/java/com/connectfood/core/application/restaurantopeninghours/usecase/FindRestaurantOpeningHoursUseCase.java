package com.connectfood.core.application.restaurantopeninghours.usecase;

import java.util.UUID;

import com.connectfood.core.application.restaurantopeninghours.dto.RestaurantOpeningHoursOutput;
import com.connectfood.core.application.restaurantopeninghours.mapper.RestaurantOpeningHoursAppMapper;
import com.connectfood.core.domain.exception.NotFoundException;
import com.connectfood.core.domain.repository.RestaurantItemsRepository;
import com.connectfood.core.domain.repository.RestaurantOpeningHoursRepository;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class FindRestaurantOpeningHoursUseCase {

  private final RestaurantOpeningHoursRepository repository;
  private final RestaurantOpeningHoursAppMapper mapper;

  public FindRestaurantOpeningHoursUseCase(
      final RestaurantOpeningHoursRepository repository,
      final RestaurantOpeningHoursAppMapper mapper) {
    this.repository = repository;
    this.mapper = mapper;
  }

  @Transactional(readOnly = true)
  public RestaurantOpeningHoursOutput execute(final UUID uuid) {
    final var model = repository.findByUuid(uuid)
        .orElseThrow(() -> new NotFoundException("Restaurant opening hours not found"));

    return mapper.toOutput(model);
  }
}
