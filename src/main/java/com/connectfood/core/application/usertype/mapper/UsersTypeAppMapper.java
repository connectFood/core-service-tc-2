package com.connectfood.core.application.usertype.mapper;

import java.util.UUID;

import com.connectfood.core.application.usertype.dto.UsersTypeInput;
import com.connectfood.core.application.usertype.dto.UsersTypeOutput;
import com.connectfood.core.domain.model.UsersType;

import org.springframework.stereotype.Component;

@Component
public class UsersTypeAppMapper {

  public UsersTypeAppMapper() {
  }

  public UsersType toDomain(final UsersTypeInput input) {
    if (input == null) {
      return null;
    }

    return new UsersType(
        input.getName(),
        input.getDescription()
    );
  }

  public UsersType toDomain(final UUID uuid, final UsersTypeInput input) {
    if (input == null) {
      return null;
    }

    return new UsersType(
        uuid,
        input.getName(),
        input.getDescription()
    );
  }

  public UsersTypeOutput toOutput(final UsersType model) {
    if (model == null) {
      return null;
    }

    return new UsersTypeOutput(
        model.getUuid(),
        model.getName(),
        model.getDescription()
    );
  }
}
