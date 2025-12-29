package com.connectfood.core.application.restaurantopeninghours.usecase;

import com.connectfood.core.application.restaurantopeninghours.dto.RestaurantOpeningHoursInput;
import com.connectfood.core.application.restaurantopeninghours.dto.RestaurantOpeningHoursOutput;
import com.connectfood.core.application.restaurantopeninghours.mapper.RestaurantOpeningHoursAppMapper;
import com.connectfood.core.domain.exception.NotFoundException;
import com.connectfood.core.domain.repository.RestaurantOpeningHoursRepository;
import com.connectfood.core.domain.repository.RestaurantsRepository;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class CreateRestaurantOpeningHoursUseCase {

  private final RestaurantOpeningHoursRepository repository;
  private final RestaurantOpeningHoursAppMapper mapper;
  private final RestaurantsRepository restaurantsRepository;

  public CreateRestaurantOpeningHoursUseCase(
      final RestaurantOpeningHoursRepository repository,
      final RestaurantOpeningHoursAppMapper mapper,
      final RestaurantsRepository restaurantsRepository) {
    this.repository = repository;
    this.mapper = mapper;
    this.restaurantsRepository = restaurantsRepository;
  }

  @Transactional
  public RestaurantOpeningHoursOutput execute(final RestaurantOpeningHoursInput input) {
    final var restaurants =
        restaurantsRepository.findByUuid(input.getRestaurantUuid())
            .orElseThrow(() -> new NotFoundException("Restaurant opening hours not found"));

    var model = repository.save(mapper.toDomain(input, restaurants));

    return mapper.toOutput(model);
  }
}
