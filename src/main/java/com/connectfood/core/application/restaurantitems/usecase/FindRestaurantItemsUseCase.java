package com.connectfood.core.application.restaurantitems.usecase;

import java.util.UUID;

import com.connectfood.core.application.restaurantitems.dto.RestaurantItemsOutput;
import com.connectfood.core.application.restaurantitems.mapper.RestaurantItemsAppMapper;
import com.connectfood.core.domain.exception.NotFoundException;
import com.connectfood.core.domain.repository.RestaurantItemsRepository;
import com.connectfood.core.domain.repository.RestaurantsRepository;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class FindRestaurantItemsUseCase {

  private final RestaurantItemsRepository repository;
  private final RestaurantItemsAppMapper mapper;
  private final RestaurantsRepository restaurantsRepository;

  public FindRestaurantItemsUseCase(
      final RestaurantItemsRepository repository,
      final RestaurantItemsAppMapper mapper,
      final RestaurantsRepository restaurantsRepository) {
    this.repository = repository;
    this.mapper = mapper;
    this.restaurantsRepository = restaurantsRepository;
  }

  @Transactional(readOnly = true)
  public RestaurantItemsOutput execute(final UUID uuid) {
    final var model = repository.findByUuid(uuid)
        .orElseThrow(() -> new NotFoundException("Restaurant Items not found"));

    return mapper.toOutput(model);
  }
}
