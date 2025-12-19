package com.connectfood.core.application.restaurantitems.usecase;

import java.util.UUID;

import com.connectfood.core.application.restaurantitems.dto.RestaurantItemsInput;
import com.connectfood.core.application.restaurantitems.dto.RestaurantItemsOutput;
import com.connectfood.core.application.restaurantitems.mapper.RestaurantItemsAppMapper;
import com.connectfood.core.domain.exception.NotFoundException;
import com.connectfood.core.domain.repository.RestaurantItemsRepository;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class UpdateRestaurantItemsUseCase {

  private final RestaurantItemsRepository repository;
  private final RestaurantItemsAppMapper mapper;

  public UpdateRestaurantItemsUseCase(
      final RestaurantItemsRepository repository,
      final RestaurantItemsAppMapper mapper) {
    this.repository = repository;
    this.mapper = mapper;
  }

  @Transactional
  public RestaurantItemsOutput execute(final UUID uuid, final RestaurantItemsInput input) {
    final var model = repository.findByUuid(uuid)
        .orElseThrow(() -> new NotFoundException("Restaurant Items not found"));

    final var modelUpdated = repository.update(uuid, mapper.toDomain(uuid, input, model.getRestaurant()));

    return mapper.toOutput(modelUpdated);
  }
}
