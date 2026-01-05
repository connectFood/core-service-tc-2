package com.connectfood.core.infrastructure.persistence.adapter;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.connectfood.core.domain.model.Restaurants;
import com.connectfood.core.domain.model.commons.PageModel;
import com.connectfood.core.domain.repository.RestaurantsRepository;
import com.connectfood.core.infrastructure.persistence.entity.RestaurantsEntity;
import com.connectfood.core.infrastructure.persistence.entity.RestaurantsTypeEntity;
import com.connectfood.core.infrastructure.persistence.jpa.JpaRestaurantsRepository;
import com.connectfood.core.infrastructure.persistence.jpa.JpaRestaurantsTypeRepository;
import com.connectfood.core.infrastructure.persistence.mappers.AddressInfraMapper;
import com.connectfood.core.infrastructure.persistence.mappers.RestaurantOpeningHoursInfraMapper;
import com.connectfood.core.infrastructure.persistence.mappers.RestaurantsInfraMapper;
import com.connectfood.core.infrastructure.persistence.specification.RestaurantsSpecification;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;

@Repository
public class RestaurantsRepositoryAdapter implements RestaurantsRepository {

  private final JpaRestaurantsRepository repository;
  private final RestaurantsInfraMapper mapper;
  private final JpaRestaurantsTypeRepository restaurantsTypeRepository;
  private final RestaurantOpeningHoursInfraMapper restaurantOpeningHoursMapper;
  private final AddressInfraMapper addressMapper;

  public RestaurantsRepositoryAdapter(final JpaRestaurantsRepository repository,
      final RestaurantsInfraMapper mapper,
      final JpaRestaurantsTypeRepository restaurantsTypeRepository,
      final RestaurantOpeningHoursInfraMapper restaurantOpeningHoursMapper,
      final AddressInfraMapper addressMapper) {
    this.repository = repository;
    this.mapper = mapper;
    this.restaurantsTypeRepository = restaurantsTypeRepository;
    this.restaurantOpeningHoursMapper = restaurantOpeningHoursMapper;
    this.addressMapper = addressMapper;
  }

  @Override
  public Restaurants save(final Restaurants restaurants) {
    final var restaurantsType = restaurantsTypeRepository.findByUuid(restaurants.getRestaurantsType()
            .getUuid())
        .orElseThrow();

    final var entity = repository.save(mapper.toEntity(restaurants, restaurantsType));

    return mapper.toDomain(entity);
  }

  @Override
  public Restaurants update(UUID uuid, Restaurants restaurants) {
    var entity = repository.findByUuid(uuid)
        .orElseThrow();

    var restaurantType = restaurantsTypeRepository.findByUuid(restaurants.getRestaurantsType()
            .getUuid())
        .orElseThrow();

    RestaurantsTypeEntity restaurantsTypeEntity = entity.getRestaurantsType();

    if (!restaurantsTypeEntity.getUuid()
        .equals(restaurants.getRestaurantsType()
            .getUuid())) {
      restaurantsTypeEntity = restaurantType;
    }

    entity = repository.save(mapper.toEntity(restaurants, entity, restaurantsTypeEntity));

    return mapper.toDomain(entity);
  }

  @Override
  public Optional<Restaurants> findByUuid(UUID uuid) {
    final var entity = repository.findByUuid(uuid);

    return entity.map(mapper::toDomain);
  }

  @Override
  public PageModel<List<Restaurants>> findAll(
      final String name,
      final UUID restaurantsTypeUuid,
      final Integer page,
      final Integer size,
      final String sort,
      final String direction
  ) {
    final var pageable = PageRequest.of(page, size,
        Sort.by(direction == null ? Sort.Direction.ASC : Sort.Direction.fromString(direction),
            sort == null ? "id" : sort
        )
    );

    final Specification<RestaurantsEntity> spec = Specification.allOf(RestaurantsSpecification.nameContains(name),
        RestaurantsSpecification.hasRestaurantsTypeUuid(restaurantsTypeUuid)
    );

    final var entities = repository.findAll(spec, pageable);

    final var result = entities.getContent()
        .stream()
        .map(mapper::toDomain)
        .toList();

    return new PageModel<>(result, entities.getTotalElements());
  }

  @Override
  public void delete(UUID uuid) {
    repository.deleteByUuid(uuid);
  }
}
