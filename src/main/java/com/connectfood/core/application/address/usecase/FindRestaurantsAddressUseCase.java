package com.connectfood.core.application.address.usecase;

import com.connectfood.core.application.address.dto.RestaurantsAddressOutput;
import com.connectfood.core.application.address.mapper.RestaurantsAddressAppMapper;
import com.connectfood.core.domain.exception.NotFoundException;
import com.connectfood.core.domain.repository.RestaurantsAddressRepository;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Component
public class FindRestaurantsAddressUseCase {

  private final RestaurantsAddressRepository repository;
  private final RestaurantsAddressAppMapper mapper;

  public FindRestaurantsAddressUseCase(RestaurantsAddressRepository repository, RestaurantsAddressAppMapper mapper) {
    this.repository = repository;
    this.mapper = mapper;
  }

  @Transactional(readOnly = true)
  public RestaurantsAddressOutput execute(final UUID uuid) {
    final var restaurantsAddress = repository.findByRestaurantsUuid(uuid)
        .orElseThrow(() -> new NotFoundException("Restaurants Address not found"));

    return mapper.toOutput(restaurantsAddress);
  }
}
