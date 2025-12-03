package com.connectfood.core.infrastructure.persistence.adapter;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.connectfood.core.domain.model.Users;
import com.connectfood.core.domain.model.commons.PageModel;
import com.connectfood.core.domain.repository.UsersRepository;
import com.connectfood.core.infrastructure.persistence.entity.UsersEntity;
import com.connectfood.core.infrastructure.persistence.jpa.JpaUsersRepository;
import com.connectfood.core.infrastructure.persistence.mappers.UsersInfraMapper;
import com.connectfood.core.infrastructure.persistence.specification.UsersSpecification;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;

@Repository
public class UsersRepositoryAdapter implements UsersRepository {

  private final JpaUsersRepository repository;
  private final UsersInfraMapper mapper;

  public UsersRepositoryAdapter(final JpaUsersRepository repository, final UsersInfraMapper mapper) {
    this.repository = repository;
    this.mapper = mapper;
  }

  @Override
  public Users save(final Users users) {
    final var entity = repository.save(mapper.toEntity(users));

    return mapper.toDomain(entity);
  }

  @Override
  public Users update(final UUID uuid, final Users users) {
    var entity = repository.findByUuid(uuid)
        .orElseThrow();

    entity = repository.save(mapper.toEntity(users, entity));

    return mapper.toDomain(entity);
  }

  @Override
  public Optional<Users> findByUuid(final UUID uuid) {
    final var entity = repository.findByUuid(uuid);

    return entity.map(mapper::toDomain);
  }

  @Override
  public PageModel<List<Users>> findAll(final String fullName, final String email, final UUID usersTypeUuid,
      final Integer page,
      final Integer size, final String sort, final String direction) {

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
}
