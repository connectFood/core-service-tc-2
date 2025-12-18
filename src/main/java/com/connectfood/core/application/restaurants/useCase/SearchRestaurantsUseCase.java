package com.connectfood.core.application.restaurants.useCase;

import com.connectfood.core.application.dto.commons.PageOutput;
import com.connectfood.core.application.restaurants.dto.RestaurantsOutput;
import com.connectfood.core.application.restaurants.mapper.RestaurantsAppMapper;
import com.connectfood.core.domain.repository.RestaurantsRepository;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Component
public class SearchRestaurantsUseCase {

  private final RestaurantsRepository repository;
  private final RestaurantsAppMapper mapper;

  public SearchRestaurantsUseCase(RestaurantsRepository repository, RestaurantsAppMapper mapper) {
    this.repository = repository;
    this.mapper = mapper;
  }

  @Transactional(readOnly = true)
  public PageOutput<List<RestaurantsOutput>> execute(
      final String name,
      final UUID restaurantsTypeUuid,
      final Integer page,
      final Integer size,
      final String sort,
      final String direction
  ) {
    final var restaurants = repository.findAll(name, restaurantsTypeUuid, page, size, sort, direction);

    final var results = restaurants.content()
        .stream()
        .map(mapper::toOutput)
        .toList();

    return new PageOutput<>(results, restaurants.total());
  }
}
