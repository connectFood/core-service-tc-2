package com.connectfood.core.application.restaurants.usecase;

import java.util.UUID;

import com.connectfood.core.application.restaurants.dto.RestaurantOpeningHoursInput;
import com.connectfood.core.application.restaurants.dto.RestaurantOpeningHoursOutput;
import com.connectfood.core.application.restaurants.mapper.RestaurantOpeningHoursAppMapper;
import com.connectfood.core.application.security.RequestUser;
import com.connectfood.core.application.security.RequestUserGuard;
import com.connectfood.core.domain.exception.NotFoundException;
import com.connectfood.core.domain.model.enums.UsersType;
import com.connectfood.core.domain.repository.RestaurantOpeningHoursGateway;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class UpdateRestaurantOpeningHoursUseCase {

  private final RestaurantOpeningHoursGateway repository;
  private final RestaurantOpeningHoursAppMapper mapper;
  private final RequestUserGuard guard;

  public UpdateRestaurantOpeningHoursUseCase(
      final RestaurantOpeningHoursGateway repository,
      final RestaurantOpeningHoursAppMapper mapper,
      final RequestUserGuard guard
  ) {
    this.repository = repository;
    this.mapper = mapper;
    this.guard = guard;
  }

  @Transactional
  public RestaurantOpeningHoursOutput execute(final RequestUser requestUser, final UUID openingHoursUuid,
      final RestaurantOpeningHoursInput input) {
    guard.requireRole(requestUser, UsersType.OWNER.name());
    repository.findByUuid(openingHoursUuid)
        .orElseThrow(() -> new NotFoundException("Restaurant opening hours not found"));

    final var modelUpdated = repository.update(openingHoursUuid, mapper.toDomain(openingHoursUuid, input));

    return mapper.toOutput(modelUpdated);
  }
}
