package com.connectfood.core.application.restaurantstype.usecase;

import com.connectfood.core.application.dto.commons.PageOutput;
import com.connectfood.core.application.restaurantstype.dto.RestaurantsTypeOutput;
import com.connectfood.core.application.restaurantstype.mapper.RestaurantsTypeAppMapper;
import com.connectfood.core.domain.repository.RestaurantsTypeRepository;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
public class SearchRestaurantTypeUseCase {

  private final RestaurantsTypeRepository repository;
  private final RestaurantsTypeAppMapper mapper;

  public SearchRestaurantTypeUseCase(RestaurantsTypeRepository repository, RestaurantsTypeAppMapper mapper) {
    this.repository = repository;
    this.mapper = mapper;
  }

  @Transactional(readOnly = true)
  public PageOutput<List<RestaurantsTypeOutput>> execute(final String name, final Integer page, final Integer size,
      final String sort, final String direction) {
    final var restaurantsTypes = repository.findAll(name, page, size, sort, direction);

    final var results = restaurantsTypes.content()
        .stream()
        .map(entity -> mapper.toOutput(entity))
        .toList();

    return new PageOutput<>(results, restaurantsTypes.total());
  }
}
