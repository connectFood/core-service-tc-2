package com.connectfood.infrastructure.persistence.adapter;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.connectfood.core.domain.model.RestaurantItemImage;
import com.connectfood.core.domain.model.commons.PageModel;
import com.connectfood.core.domain.repository.RestaurantItemImageGateway;
import com.connectfood.infrastructure.persistence.entity.RestaurantItemImageEntity;
import com.connectfood.infrastructure.persistence.jpa.JpaRestaurantItemImagesRepository;
import com.connectfood.infrastructure.persistence.jpa.JpaRestaurantItemRepository;
import com.connectfood.infrastructure.persistence.mappers.RestaurantItemImageInfraMapper;
import com.connectfood.infrastructure.persistence.specification.RestaurantItemImageSpecification;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class RestaurantItemImageGatewayAdapter implements RestaurantItemImageGateway {

  private final JpaRestaurantItemImagesRepository repository;
  private final RestaurantItemImageInfraMapper mapper;
  private final JpaRestaurantItemRepository restaurantItemsRepository;

  public RestaurantItemImageGatewayAdapter(
      final JpaRestaurantItemImagesRepository repository,
      final RestaurantItemImageInfraMapper mapper,
      final JpaRestaurantItemRepository restaurantItemsRepository) {
    this.repository = repository;
    this.mapper = mapper;
    this.restaurantItemsRepository = restaurantItemsRepository;
  }

  @Override
  public RestaurantItemImage save(final UUID restaurantItemsUuid, final RestaurantItemImage model) {
    final var restaurantItems = restaurantItemsRepository.findByUuid(restaurantItemsUuid)
        .orElseThrow();

    final var entity = repository.save(mapper.toEntity(model, restaurantItems));

    return mapper.toDomain(entity);
  }

  @Override
  public RestaurantItemImage update(final UUID uuid, final RestaurantItemImage model) {
    var entity = repository.findByUuid(uuid)
        .orElseThrow();

    entity = repository.save(mapper.toEntity(model, entity));

    return mapper.toDomain(entity);
  }

  @Override
  public Optional<RestaurantItemImage> findByUuid(final UUID uuid) {
    final var entity = repository.findByUuid(uuid);

    return entity.map(mapper::toDomain);
  }

  @Override
  public PageModel<List<RestaurantItemImage>> findAll(final UUID restaurantItemsUuid, final Integer page,
      final Integer size,
      final String sort, final String direction) {

    final var pageable = PageRequest.of(page, size,
        Sort.by(direction == null ? Sort.Direction.ASC : Sort.Direction.fromString(direction),
            sort == null ? "id" : sort
        )
    );

    final Specification<RestaurantItemImageEntity> spec =
        Specification.allOf(RestaurantItemImageSpecification.hasRestaurantItemsUuid(restaurantItemsUuid));

    final var entities = repository.findAll(spec, pageable);

    final var result = entities.getContent()
        .stream()
        .map(mapper::toDomain)
        .toList();

    return new PageModel<>(result, entities.getTotalElements());
  }

  @Override
  @Transactional
  public void delete(final UUID uuid) {
    repository.deleteByUuid(uuid);
  }
}
