package com.connectfood.core.application.address.usecase;

import java.util.UUID;

import com.connectfood.core.application.address.dto.AddressInput;
import com.connectfood.core.application.address.dto.AddressOutput;
import com.connectfood.core.application.address.mapper.AddressAppMapper;
import com.connectfood.core.application.address.mapper.RestaurantsAddressAppMapper;
import com.connectfood.core.domain.exception.NotFoundException;
import com.connectfood.core.domain.repository.AddressRepository;
import com.connectfood.core.domain.repository.RestaurantsAddressRepository;
import com.connectfood.core.domain.repository.RestaurantsRepository;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class CreateRestaurantsAddressUseCase {

  private final AddressRepository repository;
  private final AddressAppMapper mapper;
  private final RestaurantsRepository restaurantsRepository;
  private final RestaurantsAddressRepository restaurantsAddressRepository;
  private final RestaurantsAddressAppMapper restaurantsAddressMapper;


  public CreateRestaurantsAddressUseCase(
      final AddressRepository repository,
      final AddressAppMapper mapper,
      final RestaurantsRepository restaurantsRepository,
      final RestaurantsAddressRepository restaurantsAddressRepository,
      final RestaurantsAddressAppMapper restaurantsAddressMapper
  ) {
    this.repository = repository;
    this.mapper = mapper;
    this.restaurantsRepository = restaurantsRepository;
    this.restaurantsAddressRepository = restaurantsAddressRepository;
    this.restaurantsAddressMapper = restaurantsAddressMapper;
  }

  @Transactional
  public AddressOutput execute(final UUID restaurantUuid, final AddressInput input) {
    final var restaurants = restaurantsRepository.findByUuid(restaurantUuid)
        .orElseThrow(() -> new NotFoundException("Restaurant not found"));

    final var address = repository.save(mapper.toDomain(input));
    final var restaurantsAddress = restaurantsAddressRepository.save(restaurantsAddressMapper.toDomain(restaurants,
        address
    ));

    return mapper.toOutput(restaurantsAddress.getAddress());
  }
}
