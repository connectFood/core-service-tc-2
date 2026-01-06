package com.connectfood.core.application.restaurants.usecase;

import java.util.ArrayList;

import com.connectfood.core.application.address.usecase.CreateRestaurantsAddressUseCase;
import com.connectfood.core.application.restaurantopeninghours.dto.RestaurantOpeningHoursOutput;
import com.connectfood.core.application.restaurantopeninghours.usecase.CreateRestaurantOpeningHoursUseCase;
import com.connectfood.core.application.restaurants.dto.RestaurantsInput;
import com.connectfood.core.application.restaurants.dto.RestaurantsOutput;
import com.connectfood.core.application.restaurants.mapper.RestaurantsAppMapper;
import com.connectfood.core.domain.exception.NotFoundException;
import com.connectfood.core.domain.repository.RestaurantsRepository;
import com.connectfood.core.domain.repository.RestaurantsTypeRepository;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class CreateRestaurantsUseCase {

  private final RestaurantsRepository repository;
  private final RestaurantsAppMapper mapper;
  private final RestaurantsTypeRepository restaurantsTypeRepository;
  private final CreateRestaurantsAddressUseCase createRestaurantsAddressUseCase;
  private final CreateRestaurantOpeningHoursUseCase createRestaurantOpeningHoursUseCase;

  public CreateRestaurantsUseCase(
      final RestaurantsRepository repository,
      final RestaurantsAppMapper mapper,
      final RestaurantsTypeRepository restaurantsTypeRepository,
      final CreateRestaurantsAddressUseCase createRestaurantsAddressUseCase,
      final CreateRestaurantOpeningHoursUseCase createRestaurantOpeningHoursUseCase
  ) {
    this.repository = repository;
    this.mapper = mapper;
    this.restaurantsTypeRepository = restaurantsTypeRepository;
    this.createRestaurantsAddressUseCase = createRestaurantsAddressUseCase;
    this.createRestaurantOpeningHoursUseCase = createRestaurantOpeningHoursUseCase;
  }

  @Transactional
  public RestaurantsOutput execute(final RestaurantsInput input) {
    final var restaurantsType = restaurantsTypeRepository
        .findById(input.getRestaurantsTypeUuid())
        .orElseThrow(() -> new NotFoundException("Restaurants Type not found"));

    final var restaurants = repository.save(mapper.toDomain(input, restaurantsType));

    final var address = createRestaurantsAddressUseCase.execute(restaurants.getUuid(), input.getAddress());

    var openingHours = new ArrayList<RestaurantOpeningHoursOutput>();
    for (var openingHour : input.getOpeningHours()) {
      openingHours.add(createRestaurantOpeningHoursUseCase.execute(restaurants.getUuid(), openingHour));
    }

    return mapper.toOutput(restaurants, openingHours, address);
  }
}
