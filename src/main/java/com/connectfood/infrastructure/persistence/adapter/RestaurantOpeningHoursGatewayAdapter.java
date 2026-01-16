package com.connectfood.infrastructure.persistence.adapter;

import java.util.Optional;
import java.util.UUID;

import com.connectfood.core.domain.model.RestaurantOpeningHour;
import com.connectfood.core.domain.repository.RestaurantOpeningHoursGateway;
import com.connectfood.infrastructure.persistence.jpa.JpaRestaurantOpeningHoursRepository;
import com.connectfood.infrastructure.persistence.jpa.JpaRestaurantsRepository;
import com.connectfood.infrastructure.persistence.mappers.RestaurantOpeningHoursInfraMapper;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class RestaurantOpeningHoursGatewayAdapter implements RestaurantOpeningHoursGateway {

  private final JpaRestaurantOpeningHoursRepository repository;
  private final RestaurantOpeningHoursInfraMapper mapper;
  private final JpaRestaurantsRepository restaurantsRepository;

  public RestaurantOpeningHoursGatewayAdapter(
      final JpaRestaurantOpeningHoursRepository repository,
      final RestaurantOpeningHoursInfraMapper mapper,
      final JpaRestaurantsRepository restaurantsRepository) {
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
