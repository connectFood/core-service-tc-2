package com.connectfood.core.infrastructure.persistence.adapter;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.connectfood.core.domain.model.RestaurantItems;
import com.connectfood.core.domain.model.commons.PageModel;
import com.connectfood.core.domain.repository.RestaurantItemsRepository;
import com.connectfood.core.infrastructure.persistence.entity.RestaurantItemsEntity;
import com.connectfood.core.infrastructure.persistence.jpa.JpaRestaurantItemsRepository;
import com.connectfood.core.infrastructure.persistence.jpa.JpaRestaurantsRepository;
import com.connectfood.core.infrastructure.persistence.mappers.RestaurantItemsInfraMapper;
import com.connectfood.core.infrastructure.persistence.specification.RestaurantItemsSpecification;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;

@Repository
public class RestaurantItemsRepositoryAdapter implements RestaurantItemsRepository {

  private final JpaRestaurantItemsRepository repository;
  private final RestaurantItemsInfraMapper mapper;
  private final JpaRestaurantsRepository restaurantsRepository;

  public RestaurantItemsRepositoryAdapter(
      final JpaRestaurantItemsRepository repository,
      final RestaurantItemsInfraMapper mapper,
      final JpaRestaurantsRepository restaurantsRepository) {
    this.repository = repository;
    this.mapper = mapper;
    this.restaurantsRepository = restaurantsRepository;
  }

  @Override
  public RestaurantItems save(final RestaurantItems model) {
    final var restaurant = restaurantsRepository.findByUuid(model.getRestaurant()
            .getUuid())
        .orElseThrow();

    final var entity = repository.save(mapper.toEntity(model, restaurant));

    return mapper.toDomain(entity);
  }

  @Override
  public RestaurantItems update(final UUID uuid, final RestaurantItems model) {
    var entity = repository.findByUuid(uuid)
        .orElseThrow();

    entity = repository.save(mapper.toEntity(model, entity));

    return mapper.toDomain(entity);
  }

  @Override
  public Optional<RestaurantItems> findByUuid(final UUID uuid) {
    final var entity = repository.findByUuid(uuid);

    return entity.map(mapper::toDomain);
  }

  @Override
  public PageModel<List<RestaurantItems>> findAll(final UUID restaurantUuid, final Integer page, final Integer size,
      final String sort, final String direction) {

    final var pageable = PageRequest.of(page, size,
        Sort.by(direction == null ? Sort.Direction.ASC : Sort.Direction.fromString(direction),
            sort == null ? "id" : sort
        )
    );

    final Specification<RestaurantItemsEntity> spec =
        Specification.allOf(RestaurantItemsSpecification.hasRestaurantUuid(restaurantUuid));

    final var entities = repository.findAll(spec, pageable);

    final var result = entities.getContent()
        .stream()
        .map(mapper::toDomain)
        .toList();

    return new PageModel<>(result, entities.getTotalElements());
  }

  @Override
  public void delete(final UUID uuid) {
    repository.deleteByUuid(uuid);
  }
}
