package com.connectfood.infrastructure.persistence.adapter;

import com.connectfood.core.domain.model.RestaurantType;
import com.connectfood.core.domain.model.commons.PageModel;
import com.connectfood.core.domain.repository.RestaurantsTypeGateway;

import com.connectfood.infrastructure.persistence.entity.RestaurantsTypeEntity;
import com.connectfood.infrastructure.persistence.jpa.JpaRestaurantsTypeRepository;

import com.connectfood.infrastructure.persistence.mappers.RestaurantsTypeInfraMapper;

import com.connectfood.infrastructure.persistence.specification.RestaurantsTypeSpecification;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class RestaurantsTypeGatewayAdapter implements RestaurantsTypeGateway {

  private final JpaRestaurantsTypeRepository repository;
  private final RestaurantsTypeInfraMapper mapper;

  public RestaurantsTypeGatewayAdapter(final JpaRestaurantsTypeRepository repository,
                                       final RestaurantsTypeInfraMapper mapper) {
    this.repository = repository;
    this.mapper = mapper;
  }

  @Override
  public RestaurantType save(RestaurantType restaurantType) {
    final var entity = repository.save(mapper.toEntity(restaurantType));

    return mapper.toDomain(entity);
  }

  @Override
  public RestaurantType update(UUID uuid, RestaurantType restaurantType) {
    var entity = repository.findByUuid(uuid).orElseThrow();

    entity = repository.save(mapper.toEntity(restaurantType, entity));

    return mapper.toDomain(entity);
  }

  @Override
  public Optional<RestaurantType> findById(final UUID uuid) {
    final var entity = repository.findByUuid(uuid);

    return entity.map(mapper::toDomain);
  }

  @Override
  public PageModel<List<RestaurantType>> findAll(final String name, final Integer page, final Integer size,
      final String sort, final String direction) {
    final var pageable = PageRequest.of(page, size,
        Sort.by(direction == null ? Sort.Direction.ASC :
        Sort.Direction.fromString(direction), sort == null ? "id" : sort)
    );

    final Specification<RestaurantsTypeEntity> spec =
        Specification.allOf(RestaurantsTypeSpecification.nameContains(name));

    final var entities = repository.findAll(spec, pageable);
    final var results = entities.getContent()
        .stream()
        .map(mapper::toDomain)
        .toList();

    return new PageModel<>(results, entities.getTotalElements());
  }

  @Override
  public void delete(final UUID uuid) {
    repository.deleteByUuid(uuid);
  }
}
