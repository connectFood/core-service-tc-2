package com.connectfood.core.application.address.usecase;

import java.util.List;
import java.util.UUID;

import com.connectfood.core.application.address.dto.RestaurantsAddressOutput;
import com.connectfood.core.application.address.mapper.RestaurantsAddressAppMapper;
import com.connectfood.core.application.dto.commons.PageOutput;
import com.connectfood.core.domain.repository.RestaurantsAddressRepository;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class SearchRestaurantsAddressUseCase {

  private final RestaurantsAddressRepository repository;
  private final RestaurantsAddressAppMapper mapper;

  public SearchRestaurantsAddressUseCase(RestaurantsAddressRepository repository, RestaurantsAddressAppMapper mapper) {
    this.repository = repository;
    this.mapper = mapper;
  }

  @Transactional(readOnly = true)
  public PageOutput<List<RestaurantsAddressOutput>> execute(
      final String city,
      final String state,
      final String country,
      final UUID restaurantsUuid,
      final Integer page,
      final Integer size,
      final String sort,
      final String direction
  ) {
    final var restaurantsAddress = repository.findAll(city, state, country, restaurantsUuid, page, size, sort,
        direction
    );

//    final var results = restaurantsAddress.content()
//        .stream()
//        .map(mapper::toOutput)
//        .toList();

    return new PageOutput<>(List.of(), restaurantsAddress.total());
  }
}
