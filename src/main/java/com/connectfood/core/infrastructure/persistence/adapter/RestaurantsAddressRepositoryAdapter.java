package com.connectfood.core.infrastructure.persistence.adapter;

import com.connectfood.core.domain.model.RestaurantsAddress;
import com.connectfood.core.domain.model.commons.PageModel;
import com.connectfood.core.domain.repository.RestaurantsAddressRepository;

import com.connectfood.core.infrastructure.persistence.entity.AddressEntity;
import com.connectfood.core.infrastructure.persistence.entity.RestaurantsAddressEntity;
import com.connectfood.core.infrastructure.persistence.entity.RestaurantsEntity;
import com.connectfood.core.infrastructure.persistence.jpa.JpaAddressRepository;
import com.connectfood.core.infrastructure.persistence.jpa.JpaRestaurantsAddressRepository;

import com.connectfood.core.infrastructure.persistence.jpa.JpaRestaurantsRepository;
import com.connectfood.core.infrastructure.persistence.mappers.RestaurantsAddressInfraMapper;

import com.connectfood.core.infrastructure.persistence.specification.RestaurantsAddressSpecification;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class RestaurantsAddressRepositoryAdapter implements RestaurantsAddressRepository {

  private final JpaRestaurantsAddressRepository repository;
  private final RestaurantsAddressInfraMapper mapper;
  private final JpaRestaurantsRepository restaurantsRepository;
  private final JpaAddressRepository addressRepository;

  public RestaurantsAddressRepositoryAdapter(
      final JpaRestaurantsAddressRepository repository,
      final RestaurantsAddressInfraMapper mapper,
      final JpaRestaurantsRepository restaurantsRepository,
      final JpaAddressRepository addressRepository
  ) {
    this.repository = repository;
    this.mapper = mapper;
    this.restaurantsRepository = restaurantsRepository;
    this.addressRepository = addressRepository;
  }

  @Override
  public RestaurantsAddress save(RestaurantsAddress restaurantsAddress) {
    final var restaurants = restaurantsRepository.findByUuid(restaurantsAddress.getRestaurants().getUuid()).orElseThrow();
    final var address = addressRepository.findByUuid(restaurantsAddress.getAddress()
        .getUuid()).orElseThrow();

    final var entity = repository.save(mapper.toEntity(restaurantsAddress.getUuid(), restaurants, address));

    return mapper.toDomain(entity);
  }

  @Override
  public RestaurantsAddress update(UUID uuid, RestaurantsAddress restaurantsAddress, UUID addressTypeUuid) {
    var entity = repository.findByRestaurantsUuid(uuid).orElseThrow();

    var restaurants = restaurantsRepository.findByUuid(restaurantsAddress.getRestaurants()
            .getUuid())
            .orElseThrow();

    RestaurantsEntity restaurantsEntity = entity.getRestaurants();

    if(!restaurantsEntity.getUuid()
        .equals(restaurantsAddress.getRestaurants())) {
      restaurantsEntity = restaurants;
    }

    var address = addressRepository.findByUuid(restaurantsAddress.getAddress()
        .getUuid())
        .orElseThrow();

    AddressEntity addressEntity = entity.getAddress();

    if(!addressEntity.getUuid()
        .equals(restaurantsAddress.getAddress())) {
      addressEntity = address;
    }

    entity = repository.save(mapper.toEntity(uuid, restaurantsEntity, addressEntity));

    return mapper.toDomain(entity);
  }

  @Override
  public Optional<RestaurantsAddress> findByRestaurantsUuid(UUID restaurantsUuid) {
    final var entity = repository.findByRestaurantsUuid(restaurantsUuid);

    return entity.map(mapper::toDomain);
  }

  @Override
  public PageModel<List<RestaurantsAddress>> findAll(
      final String city,
      final String state,
      final String country,
      final UUID restaurantsUuid,
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

    final Specification<RestaurantsAddressEntity> spec = Specification.allOf(
        RestaurantsAddressSpecification.cityContains(city),
        RestaurantsAddressSpecification.stateContains(state),
        RestaurantsAddressSpecification.countryContains(country),
        RestaurantsAddressSpecification.hasRestaurantsUuid(restaurantsUuid));

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
