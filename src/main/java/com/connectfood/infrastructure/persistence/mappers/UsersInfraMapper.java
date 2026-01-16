package com.connectfood.infrastructure.persistence.mappers;

import com.connectfood.core.domain.model.Users;
import com.connectfood.infrastructure.persistence.entity.UsersEntity;
import com.connectfood.infrastructure.persistence.entity.UsersTypeEntity;

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

  public UsersEntity toEntity(final Users model, final UsersTypeEntity usersTypeEntity) {
    if (model == null) {
      return null;
    }

    var entity = new UsersEntity();
    entity.setUuid(model.getUuid());
    entity.setFullName(model.getFullName());
    entity.setEmail(model.getEmail());
    entity.setPassword(model.getPasswordHash());
    entity.setUsersType(usersTypeEntity);
    return entity;
  }

  public UsersEntity toEntity(final Users model, final UsersEntity entity, final UsersTypeEntity usersTypeEntity) {
    entity.setFullName(model.getFullName());
    entity.setEmail(model.getEmail());
    entity.setPassword(model.getPasswordHash());
    entity.setUsersType(usersTypeEntity);
    return entity;
  }
}
