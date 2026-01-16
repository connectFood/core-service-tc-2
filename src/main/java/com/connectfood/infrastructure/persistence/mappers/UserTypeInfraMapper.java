package com.connectfood.infrastructure.persistence.mappers;

import com.connectfood.core.domain.model.UserType;
import com.connectfood.infrastructure.persistence.entity.UserTypeEntity;

import org.springframework.stereotype.Component;

@Component
public class UserTypeInfraMapper {

  public UserTypeInfraMapper() {
  }

  public UserType toDomain(final UserTypeEntity entity) {
    if (entity == null) {
      return null;
    }

    return new UserType(
        entity.getUuid(),
        entity.getName(),
        entity.getDescription()
    );
  }

  public UserTypeEntity toEntity(final UserType model) {
    if (model == null) {
      return null;
    }

    var entity = new UserTypeEntity();
    entity.setUuid(model.getUuid());
    entity.setName(model.getName());
    entity.setDescription(model.getDescription());
    return entity;
  }

  public UserTypeEntity toEntity(final UserType model, final UserTypeEntity entity) {
    entity.setName(model.getName());
    entity.setDescription(model.getDescription());
    return entity;
  }
}
