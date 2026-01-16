package com.connectfood.infrastructure.persistence.adapter;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.connectfood.core.domain.model.RestaurantItem;
import com.connectfood.core.domain.model.commons.PageModel;
import com.connectfood.core.domain.repository.RestaurantItemGateway;
import com.connectfood.infrastructure.persistence.entity.RestaurantItemEntity;
import com.connectfood.infrastructure.persistence.jpa.JpaRestaurantItemRepository;
import com.connectfood.infrastructure.persistence.jpa.JpaRestaurantRepository;
import com.connectfood.infrastructure.persistence.mappers.RestaurantItemInfraMapper;
import com.connectfood.infrastructure.persistence.specification.RestaurantItemSpecification;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class RestaurantItemGatewayAdapter implements RestaurantItemGateway {

  private final JpaRestaurantItemRepository repository;
  private final RestaurantItemInfraMapper mapper;
  private final JpaRestaurantRepository restaurantsRepository;

  public RestaurantItemGatewayAdapter(
      final JpaRestaurantItemRepository repository,
      final RestaurantItemInfraMapper mapper,
      final JpaRestaurantRepository restaurantsRepository) {
    this.repository = repository;
    this.mapper = mapper;
    this.restaurantsRepository = restaurantsRepository;
  }

  @Override
  public RestaurantItem save(final RestaurantItem model) {
    final var restaurant = restaurantsRepository.findByUuid(model.getRestaurant()
            .getUuid())
        .orElseThrow();

    final var entity = repository.save(mapper.toEntity(model, restaurant));

    return mapper.toDomain(entity);
  }

  @Override
  @Transactional
  public RestaurantItem update(final UUID uuid, final RestaurantItem model) {
    var entity = repository.findByUuid(uuid)
        .orElseThrow();

    entity = repository.save(mapper.toEntity(model, entity));

    return mapper.toDomain(entity);
  }

  @Override
  @Transactional(readOnly = true)
  public Optional<RestaurantItem> findByUuid(final UUID uuid) {
    final var entity = repository.findByUuid(uuid);

    return entity.map(mapper::toDomain);
  }

  @Override
  public PageModel<List<RestaurantItem>> findAll(final UUID restaurantUuid, final Integer page, final Integer size,
      final String sort, final String direction) {

    final var pageable = PageRequest.of(page, size,
        Sort.by(direction == null ? Sort.Direction.ASC : Sort.Direction.fromString(direction),
            sort == null ? "id" : sort
        )
    );

    final Specification<RestaurantItemEntity> spec =
        Specification.allOf(RestaurantItemSpecification.hasRestaurantUuid(restaurantUuid));

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
