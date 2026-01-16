package com.connectfood.core.application.restaurantitems.usecase;

import java.util.List;
import java.util.UUID;

import com.connectfood.core.application.dto.commons.PageOutput;
import com.connectfood.core.application.restaurantitems.dto.RestaurantItemsOutput;
import com.connectfood.core.application.restaurantitems.mapper.RestaurantItemsAppMapper;
import com.connectfood.core.domain.exception.NotFoundException;
import com.connectfood.core.domain.repository.RestaurantItemGateway;
import com.connectfood.core.domain.repository.RestaurantGateway;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class SearchRestaurantItemsUseCase {

  private final RestaurantItemGateway repository;
  private final RestaurantItemsAppMapper mapper;
  private final RestaurantGateway restaurantGateway;

  public SearchRestaurantItemsUseCase(
      final RestaurantItemGateway repository,
      final RestaurantItemsAppMapper mapper,
      final RestaurantGateway restaurantGateway) {
    this.repository = repository;
    this.mapper = mapper;
    this.restaurantGateway = restaurantGateway;
  }

  @Transactional(readOnly = true)
  public PageOutput<List<RestaurantItemsOutput>> execute(final UUID restaurantUuid, final Integer page,
      final Integer size, final String sort, final String direction) {
    restaurantGateway.findByUuid(restaurantUuid)
        .orElseThrow(() -> new NotFoundException("Restaurant not found"));

    final var models = repository.findAll(restaurantUuid, page, size, sort, direction);

    final var results = models.content()
        .stream()
        .map(mapper::toOutput)
        .toList();

    return new PageOutput<>(results, models.total());
  }
}
