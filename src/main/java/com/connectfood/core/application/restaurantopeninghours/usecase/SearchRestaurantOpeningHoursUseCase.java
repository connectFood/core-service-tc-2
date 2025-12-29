package com.connectfood.core.application.restaurantopeninghours.usecase;

import java.util.List;
import java.util.UUID;

import com.connectfood.core.application.dto.commons.PageOutput;
import com.connectfood.core.application.restaurantopeninghours.dto.RestaurantOpeningHoursOutput;
import com.connectfood.core.application.restaurantopeninghours.mapper.RestaurantOpeningHoursAppMapper;
import com.connectfood.core.domain.exception.NotFoundException;
import com.connectfood.core.domain.repository.RestaurantOpeningHoursRepository;
import com.connectfood.core.domain.repository.RestaurantsRepository;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class SearchRestaurantOpeningHoursUseCase {

  private final RestaurantOpeningHoursRepository repository;
  private final RestaurantOpeningHoursAppMapper mapper;
  private final RestaurantsRepository restaurantsRepository;

  public SearchRestaurantOpeningHoursUseCase(
      final RestaurantOpeningHoursRepository repository,
      final RestaurantOpeningHoursAppMapper mapper,
      final RestaurantsRepository restaurantsRepository) {
    this.repository = repository;
    this.mapper = mapper;
    this.restaurantsRepository = restaurantsRepository;
  }

  @Transactional(readOnly = true)
  public PageOutput<List<RestaurantOpeningHoursOutput>> execute(final UUID restaurantUuid, final Integer page,
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
