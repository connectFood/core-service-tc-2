package com.connectfood.core.application.restaurants.usecase;

import java.util.UUID;

import com.connectfood.core.application.restaurants.dto.RestaurantOpeningHoursInput;
import com.connectfood.core.application.restaurants.dto.RestaurantOpeningHoursOutput;
import com.connectfood.core.application.restaurants.mapper.RestaurantOpeningHoursAppMapper;
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
  public RestaurantOpeningHoursOutput execute(final UUID openingHoursUuid, final RestaurantOpeningHoursInput input) {
    repository.findByUuid(openingHoursUuid)
        .orElseThrow(() -> new NotFoundException("Restaurant opening hours not found"));

    final var modelUpdated = repository.update(openingHoursUuid, mapper.toDomain(openingHoursUuid, input));

    return mapper.toOutput(modelUpdated);
  }
}
