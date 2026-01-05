package com.connectfood.core.infrastructure.persistence.adapter;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.connectfood.core.domain.model.RestaurantOpeningHours;
import com.connectfood.core.domain.model.commons.PageModel;
import com.connectfood.core.domain.repository.RestaurantOpeningHoursRepository;
import com.connectfood.core.infrastructure.persistence.entity.RestaurantOpeningHoursEntity;
import com.connectfood.core.infrastructure.persistence.jpa.JpaRestaurantOpeningHoursRepository;
import com.connectfood.core.infrastructure.persistence.jpa.JpaRestaurantsRepository;
import com.connectfood.core.infrastructure.persistence.mappers.RestaurantOpeningHoursInfraMapper;
import com.connectfood.core.infrastructure.persistence.specification.RestaurantOpeningHoursSpecification;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class RestaurantOpeningHoursRepositoryAdapter implements RestaurantOpeningHoursRepository {

  private final JpaRestaurantOpeningHoursRepository repository;
  private final RestaurantOpeningHoursInfraMapper mapper;
  private final JpaRestaurantsRepository restaurantsRepository;

  public RestaurantOpeningHoursRepositoryAdapter(
      final JpaRestaurantOpeningHoursRepository repository,
      final RestaurantOpeningHoursInfraMapper mapper,
      final JpaRestaurantsRepository restaurantsRepository) {
    this.repository = repository;
    this.mapper = mapper;
    this.restaurantsRepository = restaurantsRepository;
  }

  @Override
  public RestaurantOpeningHours save(final RestaurantOpeningHours model, final UUID restaurantUuid) {
    final var restaurant = restaurantsRepository.findByUuid(restaurantUuid)
        .orElseThrow();

    final var entity = repository.save(mapper.toEntity(model, restaurant));

    return mapper.toDomain(entity);
  }

  @Override
  @Transactional
  public RestaurantOpeningHours update(final UUID uuid, final RestaurantOpeningHours model) {
    var entity = repository.findByUuid(uuid)
        .orElseThrow();

    entity = repository.save(mapper.toEntity(model, entity));

    return mapper.toDomain(entity);
  }

  @Override
  @Transactional(readOnly = true)
  public Optional<RestaurantOpeningHours> findByUuid(final UUID uuid) {
    final var entity = repository.findByUuid(uuid);

    return entity.map(mapper::toDomain);
  }

  @Override
  public PageModel<List<RestaurantOpeningHours>> findAll(final UUID restaurantUuid, final Integer page,
      final Integer size,
      final String sort, final String direction) {

    final var pageable = PageRequest.of(page, size,
        Sort.by(direction == null ? Sort.Direction.ASC : Sort.Direction.fromString(direction),
            sort == null ? "id" : sort
        )
    );

    final Specification<RestaurantOpeningHoursEntity> spec =
        Specification.allOf(RestaurantOpeningHoursSpecification.hasRestaurantUuid(restaurantUuid));

    final var entities = repository.findAll(spec, pageable);

    final var result = entities.getContent()
        .stream()
        .map(mapper::toDomainAll)
        .toList();

    return new PageModel<>(result, entities.getTotalElements());
  }

  @Override
  public void delete(final UUID uuid) {
    repository.deleteByUuid(uuid);
  }
}
