package com.connectfood.core.application.restaurantitems.usecase;

import com.connectfood.core.application.restaurantitems.dto.RestaurantItemsInput;
import com.connectfood.core.application.restaurantitems.dto.RestaurantItemsOutput;
import com.connectfood.core.application.restaurantitems.mapper.RestaurantItemsAppMapper;
import com.connectfood.core.domain.exception.NotFoundException;
import com.connectfood.core.domain.repository.RestaurantItemsRepository;
import com.connectfood.core.domain.repository.RestaurantsRepository;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class CreateRestaurantItemsUseCase {

  private final RestaurantItemsRepository repository;
  private final RestaurantItemsAppMapper mapper;
  private final RestaurantsRepository restaurantsRepository;

  public CreateRestaurantItemsUseCase(
      final RestaurantItemsRepository repository,
      final RestaurantItemsAppMapper mapper,
      final RestaurantsRepository restaurantsRepository) {
    this.repository = repository;
    this.mapper = mapper;
    this.restaurantsRepository = restaurantsRepository;
  }

  @Transactional
  public RestaurantItemsOutput execute(final RestaurantItemsInput input) {
    final var restaurants =
        restaurantsRepository.findByUuid(input.getRestaurantUuid())
            .orElseThrow(() -> new NotFoundException("Restaurant not found"));

    final var model = repository.save(mapper.toDomain(input, restaurants));

    return mapper.toOutput(model);
  }
}
