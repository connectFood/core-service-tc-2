package com.connectfood.core.application.restaurant.usecase;

import java.util.UUID;

import com.connectfood.core.application.restaurant.dto.RestaurantOpeningHourInput;
import com.connectfood.core.application.restaurant.dto.RestaurantOpeningHourOutput;
import com.connectfood.core.application.restaurant.mapper.RestaurantOpeningHourAppMapper;
import com.connectfood.core.application.security.RequestUser;
import com.connectfood.core.application.security.RequestUserGuard;
import com.connectfood.core.domain.exception.NotFoundException;
import com.connectfood.core.domain.model.enums.UsersType;
import com.connectfood.core.domain.repository.RestaurantOpeningHourGateway;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class UpdateRestaurantOpeningHourUseCase {

  private final RestaurantOpeningHourGateway repository;
  private final RestaurantOpeningHourAppMapper mapper;
  private final RequestUserGuard guard;

  public UpdateRestaurantOpeningHourUseCase(
      final RestaurantOpeningHourGateway repository,
      final RestaurantOpeningHourAppMapper mapper,
      final RequestUserGuard guard
  ) {
    this.repository = repository;
    this.mapper = mapper;
    this.guard = guard;
  }

  @Transactional
  public RestaurantOpeningHourOutput execute(final RequestUser requestUser, final UUID openingHoursUuid,
      final RestaurantOpeningHourInput input) {
    guard.requireRole(requestUser, UsersType.OWNER.name());
    repository.findByUuid(openingHoursUuid)
        .orElseThrow(() -> new NotFoundException("Restaurant opening hours not found"));

    final var modelUpdated = repository.update(openingHoursUuid, mapper.toDomain(openingHoursUuid, input));

    return mapper.toOutput(modelUpdated);
  }
}
