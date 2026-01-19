package com.connectfood.core.application.restaurantitem.usecase;

import java.util.UUID;

import com.connectfood.core.application.restaurantitem.dto.RestaurantItemOutput;
import com.connectfood.core.application.restaurantitem.mapper.RestaurantItemAppMapper;
import com.connectfood.core.domain.exception.NotFoundException;
import com.connectfood.core.domain.repository.RestaurantItemGateway;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class FindRestaurantItemUseCase {

  private final RestaurantItemGateway repository;
  private final RestaurantItemAppMapper mapper;

  public FindRestaurantItemUseCase(
      final RestaurantItemGateway repository,
      final RestaurantItemAppMapper mapper) {
    this.repository = repository;
    this.mapper = mapper;
  }

  @Transactional(readOnly = true)
  public RestaurantItemOutput execute(final UUID uuid) {
    final var model = repository.findByUuid(uuid)
        .orElseThrow(() -> new NotFoundException("Restaurant Items not found"));

    return mapper.toOutput(model);
  }
}
