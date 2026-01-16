package com.connectfood.infrastructure.persistence.mappers;

import com.connectfood.core.domain.model.UserType;
import com.connectfood.infrastructure.persistence.entity.UsersTypeEntity;

import org.springframework.stereotype.Component;

@Component
public class UsersTypeInfraMapper {

  public UsersTypeInfraMapper() {
  }

  public UserType toDomain(final UsersTypeEntity entity) {
    if (entity == null) {
      return null;
    }

    return new UserType(
        entity.getUuid(),
        entity.getName(),
        entity.getDescription()
    );
  }

  public UsersTypeEntity toEntity(final UserType model) {
    if (model == null) {
      return null;
    }

    var entity = new UsersTypeEntity();
    entity.setUuid(model.getUuid());
    entity.setName(model.getName());
    entity.setDescription(model.getDescription());
    return entity;
  }

  public UsersTypeEntity toEntity(final UserType model, final UsersTypeEntity entity) {
    entity.setName(model.getName());
    entity.setDescription(model.getDescription());
    return entity;
  }
}
