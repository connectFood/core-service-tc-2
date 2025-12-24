package com.connectfood.core.application.restaurantitems.usecase;

import java.util.List;
import java.util.UUID;

import com.connectfood.core.application.dto.commons.PageOutput;
import com.connectfood.core.application.restaurantitems.dto.RestaurantItemsOutput;
import com.connectfood.core.application.restaurantitems.mapper.RestaurantItemsAppMapper;
import com.connectfood.core.domain.exception.NotFoundException;
import com.connectfood.core.domain.repository.RestaurantItemsRepository;
import com.connectfood.core.domain.repository.RestaurantsRepository;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class SearchRestaurantItemsUseCase {

  private final RestaurantItemsRepository repository;
  private final RestaurantItemsAppMapper mapper;
  private final RestaurantsRepository restaurantsRepository;

  public SearchRestaurantItemsUseCase(
      final RestaurantItemsRepository repository,
      final RestaurantItemsAppMapper mapper,
      final RestaurantsRepository restaurantsRepository) {
    this.repository = repository;
    this.mapper = mapper;
    this.restaurantsRepository = restaurantsRepository;
  }

  @Transactional(readOnly = true)
  public PageOutput<List<RestaurantItemsOutput>> execute(final UUID restaurantUuid, final Integer page,
      final Integer size, final String sort, final String direction) {
    restaurantsRepository.findByUuid(restaurantUuid)
        .orElseThrow(() -> new NotFoundException("Restaurant not found"));

    final var models = repository.findAll(restaurantUuid, page, size, sort, direction);

    final var results = models.content()
        .stream()
        .map(mapper::toOutput)
        .toList();

    return new PageOutput<>(results, models.total());
  }
}
