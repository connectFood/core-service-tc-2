package com.connectfood.infrastructure.persistence.adapter;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.connectfood.core.domain.model.Restaurant;
import com.connectfood.core.domain.model.commons.PageModel;
import com.connectfood.core.domain.repository.RestaurantGateway;
import com.connectfood.infrastructure.persistence.entity.RestaurantEntity;
import com.connectfood.infrastructure.persistence.entity.RestaurantTypeEntity;
import com.connectfood.infrastructure.persistence.jpa.JpaRestaurantRepository;
import com.connectfood.infrastructure.persistence.jpa.JpaRestaurantTypeRepository;
import com.connectfood.infrastructure.persistence.mappers.RestaurantInfraMapper;
import com.connectfood.infrastructure.persistence.specification.RestaurantSpecification;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;

@Repository
public class RestaurantGatewayAdapter implements RestaurantGateway {

  private final JpaRestaurantRepository repository;
  private final RestaurantInfraMapper mapper;
  private final JpaRestaurantTypeRepository restaurantsTypeRepository;

  public RestaurantGatewayAdapter(final JpaRestaurantRepository repository,
      final RestaurantInfraMapper mapper,
      final JpaRestaurantTypeRepository restaurantsTypeRepository) {
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

    RestaurantTypeEntity restaurantTypeEntity = entity.getRestaurantsType();

    if (!restaurantTypeEntity.getUuid()
        .equals(restaurant.getRestaurantType()
            .getUuid())) {
      restaurantTypeEntity = restaurantType;
    }

    entity = repository.save(mapper.toEntity(restaurant, entity, restaurantTypeEntity));

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

    final Specification<RestaurantEntity> spec = Specification.allOf(RestaurantSpecification.nameContains(name),
        RestaurantSpecification.hasRestaurantsTypeUuid(restaurantsTypeUuid),
        RestaurantSpecification.streetContains(street), RestaurantSpecification.cityContains(city),
        RestaurantSpecification.stateContains(state)
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

  @Override
  public boolean existsByRestaurant(final String name, final UUID restaurantsTypeUuid, final UUID usersUuid) {
    return repository.existsByNameAndRestaurantsTypeUuidAndUsersUuid(name, restaurantsTypeUuid, usersUuid);
  }
}
