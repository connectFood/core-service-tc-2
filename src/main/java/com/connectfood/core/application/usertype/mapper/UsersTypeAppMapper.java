package com.connectfood.core.application.usertype.mapper;

import java.util.UUID;

import com.connectfood.core.application.usertype.dto.UsersTypeInput;
import com.connectfood.core.application.usertype.dto.UsersTypeOutput;
import com.connectfood.core.domain.model.UserType;

import org.springframework.stereotype.Component;

@Component
public class UsersTypeAppMapper {

  public UsersTypeAppMapper() {
  }

  public UserType toDomain(final UsersTypeInput input) {
    if (input == null) {
      return null;
    }

    return new UserType(
        input.getName(),
        input.getDescription()
    );
  }

  public UserType toDomain(final UUID uuid, final UsersTypeInput input) {
    if (input == null) {
      return null;
    }

    return new UserType(
        uuid,
        input.getName(),
        input.getDescription()
    );
  }

  public UsersTypeOutput toOutput(final UserType model) {
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
