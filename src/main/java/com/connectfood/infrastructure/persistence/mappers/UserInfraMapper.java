package com.connectfood.infrastructure.persistence.mappers;

import com.connectfood.core.domain.model.User;
import com.connectfood.infrastructure.persistence.entity.UserEntity;
import com.connectfood.infrastructure.persistence.entity.UserTypeEntity;

import org.springframework.stereotype.Component;

@Component
public class UserInfraMapper {

  private final UserTypeInfraMapper usersTypeMapper;

  public UserInfraMapper(final UserTypeInfraMapper usersTypeMapper) {
    this.usersTypeMapper = usersTypeMapper;
  }

  public User toDomain(final UserEntity entity) {
    if (entity == null) {
      return null;
    }

    return new User(
        entity.getUuid(),
        entity.getFullName(),
        entity.getEmail(),
        entity.getPassword(),
        entity.getUsersType() != null ? usersTypeMapper.toDomain(entity.getUsersType()) : null
    );
  }

  public UserEntity toEntity(final User model, final UserTypeEntity userTypeEntity) {
    if (model == null) {
      return null;
    }

    var entity = new UserEntity();
    entity.setUuid(model.getUuid());
    entity.setFullName(model.getFullName());
    entity.setEmail(model.getEmail());
    entity.setPassword(model.getPasswordHash());
    entity.setUsersType(userTypeEntity);
    return entity;
  }

  public UserEntity toEntity(final User model, final UserEntity entity, final UserTypeEntity userTypeEntity) {
    entity.setFullName(model.getFullName());
    entity.setEmail(model.getEmail());
    entity.setPassword(model.getPasswordHash());
    entity.setUsersType(userTypeEntity);
    return entity;
  }
}
