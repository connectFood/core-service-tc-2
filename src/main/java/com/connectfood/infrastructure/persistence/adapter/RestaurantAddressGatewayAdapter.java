package com.connectfood.infrastructure.persistence.adapter;

import java.util.Optional;
import java.util.UUID;

import com.connectfood.core.domain.model.RestaurantsAddress;
import com.connectfood.core.domain.repository.RestaurantAddressGateway;
import com.connectfood.infrastructure.persistence.jpa.JpaAddressRepository;
import com.connectfood.infrastructure.persistence.jpa.JpaRestaurantAddressRepository;
import com.connectfood.infrastructure.persistence.jpa.JpaRestaurantRepository;
import com.connectfood.infrastructure.persistence.mappers.RestaurantAddressInfraMapper;

import org.springframework.stereotype.Repository;

@Repository
public class RestaurantAddressGatewayAdapter implements RestaurantAddressGateway {

  private final JpaRestaurantAddressRepository repository;
  private final RestaurantAddressInfraMapper mapper;
  private final JpaRestaurantRepository restaurantsRepository;
  private final JpaAddressRepository addressRepository;

  public RestaurantAddressGatewayAdapter(
      final JpaRestaurantAddressRepository repository,
      final RestaurantAddressInfraMapper mapper,
      final JpaRestaurantRepository restaurantsRepository,
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

  @Override
  public boolean existsByRestaurantsUuid(final UUID restaurantUuid) {
    return repository.existsByRestaurantsUuid(restaurantUuid);
  }
}
