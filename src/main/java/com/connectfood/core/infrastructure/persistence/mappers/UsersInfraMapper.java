package com.connectfood.core.infrastructure.persistence.mappers;

import com.connectfood.core.domain.model.Users;
import com.connectfood.core.infrastructure.persistence.entity.UsersEntity;

import org.springframework.stereotype.Component;

@Component
public class UsersInfraMapper {

  private final UsersTypeInfraMapper usersTypeMapper;

  public UsersInfraMapper(final UsersTypeInfraMapper usersTypeMapper) {
    this.usersTypeMapper = usersTypeMapper;
  }

  public Users toDomain(final UsersEntity entity) {
    if (entity == null) {
      return null;
    }

    return new Users(
        entity.getUuid(),
        entity.getFullName(),
        entity.getEmail(),
        entity.getPassword(),
        entity.getUsersType() != null ? usersTypeMapper.toDomain(entity.getUsersType()) : null
    );
  }

  public UsersEntity toEntity(final Users model) {
    if (model == null) {
      return null;
    }

    var entity = new UsersEntity();
    entity.setFullName(model.getFullName());
    entity.setEmail(model.getEmail());
    entity.setPassword(model.getPasswordHash());
    entity.setUsersType(usersTypeMapper.toEntity(model.getUsersType()));
    return entity;
  }

  public UsersEntity toEntity(final Users model, final UsersEntity entity) {
    entity.setFullName(model.getFullName());
    entity.setEmail(model.getEmail());
    entity.setPassword(model.getPasswordHash());
    entity.setUsersType(usersTypeMapper.toEntity(model.getUsersType()));
    return entity;
  }
}
