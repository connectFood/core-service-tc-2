package com.connectfood.core.infrastructure.persistence.adapter;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.connectfood.core.domain.model.RestaurantItemsImages;
import com.connectfood.core.domain.model.commons.PageModel;
import com.connectfood.core.domain.repository.RestaurantItemsImagesRepository;
import com.connectfood.core.infrastructure.persistence.entity.RestaurantItemsImagesEntity;
import com.connectfood.core.infrastructure.persistence.jpa.JpaRestaurantItemsImagesRepository;
import com.connectfood.core.infrastructure.persistence.jpa.JpaRestaurantItemsRepository;
import com.connectfood.core.infrastructure.persistence.mappers.RestaurantItemsImageInfraMapper;
import com.connectfood.core.infrastructure.persistence.specification.RestaurantItemsImagesSpecification;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class RestaurantItemsImagesRepositoryAdapter implements RestaurantItemsImagesRepository {

  private final JpaRestaurantItemsImagesRepository repository;
  private final RestaurantItemsImageInfraMapper mapper;
  private final JpaRestaurantItemsRepository restaurantItemsRepository;

  public RestaurantItemsImagesRepositoryAdapter(
      final JpaRestaurantItemsImagesRepository repository,
      final RestaurantItemsImageInfraMapper mapper,
      final JpaRestaurantItemsRepository restaurantItemsRepository) {
    this.repository = repository;
    this.mapper = mapper;
    this.restaurantItemsRepository = restaurantItemsRepository;
  }

  @Override
  public RestaurantItemsImages save(final UUID restaurantItemsUuid, final RestaurantItemsImages model) {
    final var restaurantItems = restaurantItemsRepository.findByUuid(restaurantItemsUuid)
        .orElseThrow();

    final var entity = repository.save(mapper.toEntity(model, restaurantItems));

    return mapper.toDomain(entity);
  }

  @Override
  public RestaurantItemsImages update(final UUID uuid, final RestaurantItemsImages model) {
    var entity = repository.findByUuid(uuid)
        .orElseThrow();

    entity = repository.save(mapper.toEntity(model, entity));

    return mapper.toDomain(entity);
  }

  @Override
  public Optional<RestaurantItemsImages> findByUuid(final UUID uuid) {
    final var entity = repository.findByUuid(uuid);

    return entity.map(mapper::toDomain);
  }

  @Override
  public PageModel<List<RestaurantItemsImages>> findAll(final UUID restaurantItemsUuid, final Integer page,
      final Integer size,
      final String sort, final String direction) {

    final var pageable = PageRequest.of(page, size,
        Sort.by(direction == null ? Sort.Direction.ASC : Sort.Direction.fromString(direction),
            sort == null ? "id" : sort
        )
    );

    final Specification<RestaurantItemsImagesEntity> spec =
        Specification.allOf(RestaurantItemsImagesSpecification.hasRestaurantItemsUuid(restaurantItemsUuid));

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
