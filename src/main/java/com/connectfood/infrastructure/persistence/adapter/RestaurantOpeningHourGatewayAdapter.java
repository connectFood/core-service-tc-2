package com.connectfood.infrastructure.persistence.adapter;

import java.util.Optional;
import java.util.UUID;

import com.connectfood.core.domain.model.RestaurantOpeningHour;
import com.connectfood.core.domain.repository.RestaurantOpeningHourGateway;
import com.connectfood.infrastructure.persistence.jpa.JpaRestaurantOpeningHourRepository;
import com.connectfood.infrastructure.persistence.jpa.JpaRestaurantRepository;
import com.connectfood.infrastructure.persistence.mappers.RestaurantOpeningHourInfraMapper;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class RestaurantOpeningHourGatewayAdapter implements RestaurantOpeningHourGateway {

  private final JpaRestaurantOpeningHourRepository repository;
  private final RestaurantOpeningHourInfraMapper mapper;
  private final JpaRestaurantRepository restaurantsRepository;

  public RestaurantOpeningHourGatewayAdapter(
      final JpaRestaurantOpeningHourRepository repository,
      final RestaurantOpeningHourInfraMapper mapper,
      final JpaRestaurantRepository restaurantsRepository) {
    this.repository = repository;
    this.mapper = mapper;
    this.restaurantsRepository = restaurantsRepository;
  }

  @Override
  public RestaurantOpeningHour save(final RestaurantOpeningHour model, final UUID restaurantUuid) {
    final var restaurant = restaurantsRepository.findByUuid(restaurantUuid)
        .orElseThrow();

    final var entity = repository.save(mapper.toEntity(model, restaurant));

    return mapper.toDomain(entity);
  }

  @Override
  @Transactional
  public RestaurantOpeningHour update(final UUID uuid, final RestaurantOpeningHour model) {
    var entity = repository.findByUuid(uuid)
        .orElseThrow();

    entity = repository.save(mapper.toEntity(model, entity));

    return mapper.toDomain(entity);
  }

  @Override
  @Transactional(readOnly = true)
  public Optional<RestaurantOpeningHour> findByUuid(final UUID uuid) {
    final var entity = repository.findByUuid(uuid);

    return entity.map(mapper::toDomain);
  }

  @Override
  public void delete(final UUID uuid) {
    repository.deleteByUuid(uuid);
  }
}
