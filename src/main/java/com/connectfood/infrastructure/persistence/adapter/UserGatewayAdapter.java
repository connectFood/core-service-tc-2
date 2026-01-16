package com.connectfood.infrastructure.persistence.adapter;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.connectfood.core.domain.model.User;
import com.connectfood.core.domain.model.commons.PageModel;
import com.connectfood.core.domain.repository.UserGateway;
import com.connectfood.infrastructure.persistence.entity.UsersEntity;
import com.connectfood.infrastructure.persistence.entity.UsersTypeEntity;
import com.connectfood.infrastructure.persistence.jpa.JpaUsersRepository;
import com.connectfood.infrastructure.persistence.jpa.JpaUsersTypeRepository;
import com.connectfood.infrastructure.persistence.mappers.UsersInfraMapper;
import com.connectfood.infrastructure.persistence.specification.UsersSpecification;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;

@Repository
public class UserGatewayAdapter implements UserGateway {

  private final JpaUsersRepository repository;
  private final UsersInfraMapper mapper;
  private final JpaUsersTypeRepository usersTypeRepository;

  public UserGatewayAdapter(
      final JpaUsersRepository repository,
      final UsersInfraMapper mapper,
      final JpaUsersTypeRepository usersTypeRepository) {
    this.repository = repository;
    this.mapper = mapper;
    this.usersTypeRepository = usersTypeRepository;
  }

  @Override
  public User save(final User user) {
    final var userType = usersTypeRepository.findByUuid(user.getUserType()
            .getUuid())
        .orElseThrow();

    final var entity = repository.save(mapper.toEntity(user, userType));

    return mapper.toDomain(entity);
  }

  @Override
  public User update(final UUID uuid, final User user) {
    var entity = repository.findByUuid(uuid)
        .orElseThrow();

    var userType = usersTypeRepository.findByUuid(user.getUserType()
            .getUuid())
        .orElseThrow();

    UsersTypeEntity usersTypeEntity = entity.getUsersType();

    if (!usersTypeEntity.getUuid()
        .equals(userType.getUuid())) {
      usersTypeEntity = userType;
    }

    entity = repository.save(mapper.toEntity(user, entity, usersTypeEntity));

    return mapper.toDomain(entity);
  }

  @Override
  public Optional<User> findByUuid(final UUID uuid) {
    final var entity = repository.findByUuid(uuid);

    return entity.map(mapper::toDomain);
  }

  @Override
  public PageModel<List<User>> findAll(final String fullName, final String email, final UUID usersTypeUuid,
      final Integer page, final Integer size, final String sort, final String direction) {

    final var pageable = PageRequest.of(page, size,
        Sort.by(direction == null ? Sort.Direction.ASC : Sort.Direction.fromString(direction),
            sort == null ? "id" : sort
        )
    );

    final Specification<UsersEntity> spec = Specification.allOf(UsersSpecification.nameContains(fullName),
        UsersSpecification.emailContains(email), UsersSpecification.hasUsersTypeUuid(usersTypeUuid)
    );

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

  @Override
  public boolean existsByEmail(final String email) {
    return repository.existsByEmail(email);
  }
}
