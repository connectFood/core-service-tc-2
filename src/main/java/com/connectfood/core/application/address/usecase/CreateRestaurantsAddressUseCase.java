package com.connectfood.core.application.address.usecase;

import com.connectfood.core.application.address.dto.AddressInput;
import com.connectfood.core.application.address.dto.AddressOutput;
import com.connectfood.core.application.address.mapper.AddressAppMapper;
import com.connectfood.core.application.address.mapper.RestaurantsAddressAppMapper;
import com.connectfood.core.domain.model.Restaurants;
import com.connectfood.core.domain.repository.AddressRepository;
import com.connectfood.core.domain.repository.RestaurantsAddressRepository;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class CreateRestaurantsAddressUseCase {

  private final AddressRepository repository;
  private final AddressAppMapper mapper;
  private final RestaurantsAddressRepository restaurantsAddressRepository;
  private final RestaurantsAddressAppMapper restaurantsAddressMapper;


  public CreateRestaurantsAddressUseCase(
      final AddressRepository repository,
      final AddressAppMapper mapper,
      final RestaurantsAddressRepository restaurantsAddressRepository,
      final RestaurantsAddressAppMapper restaurantsAddressMapper
  ) {
    this.repository = repository;
    this.mapper = mapper;
    this.restaurantsAddressRepository = restaurantsAddressRepository;
    this.restaurantsAddressMapper = restaurantsAddressMapper;
  }

  @Transactional
  public AddressOutput execute(final Restaurants restaurants, final AddressInput input) {
    final var address = repository.save(mapper.toDomain(input));
    final var restaurantsAddress = restaurantsAddressRepository.save(restaurantsAddressMapper.toDomain(restaurants,
        address
    ));

    return mapper.toOutput(restaurantsAddress.getAddress());
  }
}
