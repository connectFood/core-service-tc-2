package com.connectfood.infrastructure.persistence.adapter;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.connectfood.core.domain.model.UserType;
import com.connectfood.core.domain.model.commons.PageModel;
import com.connectfood.core.domain.repository.UserTypeGateway;
import com.connectfood.infrastructure.persistence.entity.UserTypeEntity;
import com.connectfood.infrastructure.persistence.jpa.JpaUserTypeRepository;
import com.connectfood.infrastructure.persistence.mappers.UserTypeInfraMapper;
import com.connectfood.infrastructure.persistence.specification.UserTypeSpecification;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;

@Repository
public class UserTypeGatewayAdapter implements UserTypeGateway {

  private final JpaUserTypeRepository repository;
  private final UserTypeInfraMapper mapper;

  public UserTypeGatewayAdapter(final JpaUserTypeRepository repository, final UserTypeInfraMapper mapper) {
    this.repository = repository;
    this.mapper = mapper;
  }

  @Override
  public UserType save(final UserType userType) {
    final var entity = repository.save(mapper.toEntity(userType));

    return mapper.toDomain(entity);
  }

  @Override
  public UserType update(final UUID uuid, final UserType userType) {
    var entity = repository.findByUuid(uuid)
        .orElseThrow();

    entity = repository.save(mapper.toEntity(userType, entity));

    return mapper.toDomain(entity);
  }

  @Override
  public Optional<UserType> findByUuid(final UUID uuid) {
    final var entity = repository.findByUuid(uuid);

    return entity.map(mapper::toDomain);
  }

  @Override
  public PageModel<List<UserType>> findAll(final String name, final Integer page, final Integer size,
      final String sort, final String direction) {

    final var pageable = PageRequest.of(page, size,
        Sort.by(direction == null ? Sort.Direction.ASC : Sort.Direction.fromString(direction),
            sort == null ? "id" : sort
        )
    );

    final Specification<UserTypeEntity> spec = Specification.allOf(UserTypeSpecification.nameContains(name));

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

  @Override
  public boolean existsByName(final String name) {
    return repository.existsByName(name);
  }
}
