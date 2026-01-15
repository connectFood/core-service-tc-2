package com.connectfood.core.application.restaurants.usecase;

import java.util.UUID;

import com.connectfood.core.application.restaurants.dto.RestaurantOpeningHoursInput;
import com.connectfood.core.application.restaurants.dto.RestaurantOpeningHoursOutput;
import com.connectfood.core.application.restaurants.mapper.RestaurantOpeningHoursAppMapper;
import com.connectfood.core.application.security.RequestUser;
import com.connectfood.core.application.security.RequestUserGuard;
import com.connectfood.core.domain.model.enums.UsersType;
import com.connectfood.core.domain.repository.RestaurantOpeningHoursRepository;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class CreateRestaurantOpeningHoursUseCase {

  private final RestaurantOpeningHoursRepository repository;
  private final RestaurantOpeningHoursAppMapper mapper;
  private final RequestUserGuard guard;

  public CreateRestaurantOpeningHoursUseCase(
      final RestaurantOpeningHoursRepository repository,
      final RestaurantOpeningHoursAppMapper mapper,
      final RequestUserGuard guard
  ) {
    this.repository = repository;
    this.mapper = mapper;
    this.guard = guard;
  }

  @Transactional
  public RestaurantOpeningHoursOutput execute(final RequestUser requestUser, final UUID restaurantUuid,
      final RestaurantOpeningHoursInput input) {
    guard.requireRole(requestUser, UsersType.OWNER.name());
    var model = repository.save(mapper.toDomain(input), restaurantUuid);

    return mapper.toOutput(model);
  }
}
