package com.connectfood.infrastructure.persistence.adapter;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.connectfood.core.domain.model.Restaurant;
import com.connectfood.core.domain.model.commons.PageModel;
import com.connectfood.core.domain.repository.RestaurantsGateway;
import com.connectfood.infrastructure.persistence.entity.RestaurantsEntity;
import com.connectfood.infrastructure.persistence.entity.RestaurantsTypeEntity;
import com.connectfood.infrastructure.persistence.jpa.JpaRestaurantsRepository;
import com.connectfood.infrastructure.persistence.jpa.JpaRestaurantsTypeRepository;
import com.connectfood.infrastructure.persistence.mappers.RestaurantsInfraMapper;
import com.connectfood.infrastructure.persistence.specification.RestaurantsSpecification;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;

@Repository
public class RestaurantsGatewayAdapter implements RestaurantsGateway {

  private final JpaRestaurantsRepository repository;
  private final RestaurantsInfraMapper mapper;
  private final JpaRestaurantsTypeRepository restaurantsTypeRepository;

  public RestaurantsGatewayAdapter(final JpaRestaurantsRepository repository,
                                   final RestaurantsInfraMapper mapper,
                                   final JpaRestaurantsTypeRepository restaurantsTypeRepository) {
    this.repository = repository;
    this.mapper = mapper;
    this.restaurantsTypeRepository = restaurantsTypeRepository;
  }

  @Override
  public Restaurant save(final Restaurant restaurant) {
    final var restaurantsType = restaurantsTypeRepository.findByUuid(restaurant.getRestaurantType()
            .getUuid())
        .orElseThrow();

    final var entity = repository.save(mapper.toEntity(restaurant, restaurantsType));

    return mapper.toDomain(entity);
  }

  @Override
  public Restaurant update(UUID uuid, Restaurant restaurant) {
    var entity = repository.findByUuid(uuid)
        .orElseThrow();

    var restaurantType = restaurantsTypeRepository.findByUuid(restaurant.getRestaurantType()
            .getUuid())
        .orElseThrow();

    RestaurantsTypeEntity restaurantsTypeEntity = entity.getRestaurantsType();

    if (!restaurantsTypeEntity.getUuid()
        .equals(restaurant.getRestaurantType()
            .getUuid())) {
      restaurantsTypeEntity = restaurantType;
    }

    entity = repository.save(mapper.toEntity(restaurant, entity, restaurantsTypeEntity));

    return mapper.toDomain(entity);
  }

  @Override
  public Optional<Restaurant> findByUuid(UUID uuid) {
    final var entity = repository.findByUuid(uuid);

    return entity.map(mapper::toDomain);
  }

  @Override
  public PageModel<List<Restaurant>> findAll(final String name, final UUID restaurantsTypeUuid, final String street,
      final String city, final String state, final Integer page, final Integer size, final String sort,
      final String direction) {
    final var pageable = PageRequest.of(page, size,
        Sort.by(direction == null ? Sort.Direction.ASC : Sort.Direction.fromString(direction),
            sort == null ? "id" : sort
        )
    );

    final Specification<RestaurantsEntity> spec = Specification.allOf(RestaurantsSpecification.nameContains(name),
        RestaurantsSpecification.hasRestaurantsTypeUuid(restaurantsTypeUuid),
        RestaurantsSpecification.streetContains(street), RestaurantsSpecification.cityContains(city),
        RestaurantsSpecification.stateContains(state)
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
