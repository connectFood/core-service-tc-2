package com.connectfood.core.application.restaurantAddress.useCase;

import com.connectfood.core.application.address.dto.AddressInput;
import com.connectfood.core.application.address.mapper.AddressAppMapper;
import com.connectfood.core.application.restaurantAddress.dto.RestaurantsAddressOutput;
import com.connectfood.core.application.restaurantAddress.mapper.RestaurantsAddressAppMapper;
import com.connectfood.core.domain.exception.NotFoundException;
import com.connectfood.core.domain.repository.AddressRepository;

import com.connectfood.core.domain.repository.RestaurantsAddressRepository;
import com.connectfood.core.domain.repository.RestaurantsRepository;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Component
public class CreateRestaurantsAddressUseCase {

  private final RestaurantsRepository restaurantsRepository;
  private final AddressRepository repository;
  private final AddressAppMapper mapper;
  private final RestaurantsAddressRepository restaurantsAddressRepository;
  private final RestaurantsAddressAppMapper restaurantsAddressMapper;


  public CreateRestaurantsAddressUseCase(
      final RestaurantsRepository restaurantsRepository,
      final AddressRepository repository,
      final AddressAppMapper mapper,
      final RestaurantsAddressRepository restaurantsAddressRepository,
      final RestaurantsAddressAppMapper restaurantsAddressMapper
  ) {
    this.restaurantsRepository = restaurantsRepository;
    this.repository = repository;
    this.mapper = mapper;
    this.restaurantsAddressRepository = restaurantsAddressRepository;
    this.restaurantsAddressMapper = restaurantsAddressMapper;
  }

  @Transactional
  public RestaurantsAddressOutput execute(final UUID restaurantUuid, final AddressInput input) {
    final var restaurants = restaurantsRepository.findByUuid(restaurantUuid)
        .orElseThrow(() -> new NotFoundException("Restaurant not found"));

    final var address = repository.save(mapper.toDomain(input));
    final var restaurantsAddress = restaurantsAddressRepository.save(restaurantsAddressMapper.toDomain(restaurants,
        address));

    return restaurantsAddressMapper.toOutput(restaurantsAddress);
  }
}
