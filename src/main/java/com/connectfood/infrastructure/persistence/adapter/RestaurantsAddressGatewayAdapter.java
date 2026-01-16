package com.connectfood.infrastructure.persistence.adapter;

import java.util.Optional;
import java.util.UUID;

import com.connectfood.core.domain.model.RestaurantsAddress;
import com.connectfood.core.domain.repository.RestaurantsAddressGateway;
import com.connectfood.infrastructure.persistence.jpa.JpaAddressRepository;
import com.connectfood.infrastructure.persistence.jpa.JpaRestaurantsAddressRepository;
import com.connectfood.infrastructure.persistence.jpa.JpaRestaurantsRepository;
import com.connectfood.infrastructure.persistence.mappers.RestaurantsAddressInfraMapper;

import org.springframework.stereotype.Repository;

@Repository
public class RestaurantsAddressGatewayAdapter implements RestaurantsAddressGateway {

  private final JpaRestaurantsAddressRepository repository;
  private final RestaurantsAddressInfraMapper mapper;
  private final JpaRestaurantsRepository restaurantsRepository;
  private final JpaAddressRepository addressRepository;

  public RestaurantsAddressGatewayAdapter(
      final JpaRestaurantsAddressRepository repository,
      final RestaurantsAddressInfraMapper mapper,
      final JpaRestaurantsRepository restaurantsRepository,
      final JpaAddressRepository addressRepository
  ) {
    this.repository = repository;
    this.mapper = mapper;
    this.restaurantsRepository = restaurantsRepository;
    this.addressRepository = addressRepository;
  }

  @Override
  public RestaurantsAddress save(RestaurantsAddress restaurantsAddress) {
    final var restaurants = restaurantsRepository.findByUuid(restaurantsAddress.getRestaurant()
            .getUuid())
        .orElseThrow();
    final var address = addressRepository.findByUuid(restaurantsAddress.getAddress()
            .getUuid())
        .orElseThrow();

    final var entity = repository.save(mapper.toEntity(restaurantsAddress.getUuid(), restaurants, address));

    return mapper.toDomain(entity);
  }

  @Override
  public Optional<RestaurantsAddress> findByRestaurantsUuid(UUID restaurantsUuid) {
    final var entity = repository.findByRestaurantsUuid(restaurantsUuid);

    return entity.map(mapper::toDomain);
  }

  @Override
  public void delete(UUID uuid) {
    repository.deleteByUuid(uuid);
  }
}
