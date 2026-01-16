package com.connectfood.core.application.restaurantitems.usecase;

import java.util.List;
import java.util.UUID;

import com.connectfood.core.application.dto.commons.PageOutput;
import com.connectfood.core.application.restaurantitems.dto.RestaurantItemsOutput;
import com.connectfood.core.application.restaurantitems.mapper.RestaurantItemsAppMapper;
import com.connectfood.core.domain.exception.NotFoundException;
import com.connectfood.core.domain.repository.RestaurantItemsGateway;
import com.connectfood.core.domain.repository.RestaurantsGateway;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class SearchRestaurantItemsUseCase {

  private final RestaurantItemsGateway repository;
  private final RestaurantItemsAppMapper mapper;
  private final RestaurantsGateway restaurantsGateway;

  public SearchRestaurantItemsUseCase(
      final RestaurantItemsGateway repository,
      final RestaurantItemsAppMapper mapper,
      final RestaurantsGateway restaurantsGateway) {
    this.repository = repository;
    this.mapper = mapper;
    this.restaurantsGateway = restaurantsGateway;
  }

  @Transactional(readOnly = true)
  public PageOutput<List<RestaurantItemsOutput>> execute(final UUID restaurantUuid, final Integer page,
      final Integer size, final String sort, final String direction) {
    restaurantsGateway.findByUuid(restaurantUuid)
        .orElseThrow(() -> new NotFoundException("Restaurant not found"));

    final var models = repository.findAll(restaurantUuid, page, size, sort, direction);

    final var results = models.content()
        .stream()
        .map(mapper::toOutput)
        .toList();

    return new PageOutput<>(results, models.total());
  }
}
