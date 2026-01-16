package com.connectfood.core.application.usertype.mapper;

import java.util.UUID;

import com.connectfood.core.application.usertype.dto.UserTypeInput;
import com.connectfood.core.application.usertype.dto.UserTypeOutput;
import com.connectfood.core.domain.model.UserType;

import org.springframework.stereotype.Component;

@Component
public class UserTypeAppMapper {

  public UserTypeAppMapper() {
  }

  public UserType toDomain(final UserTypeInput input) {
    if (input == null) {
      return null;
    }

    return new UserType(
        input.getName(),
        input.getDescription()
    );
  }

  public UserType toDomain(final UUID uuid, final UserTypeInput input) {
    if (input == null) {
      return null;
    }

    return new UserType(
        uuid,
        input.getName(),
        input.getDescription()
    );
  }

  public UserTypeOutput toOutput(final UserType model) {
    if (model == null) {
      return null;
    }

    return new UserTypeOutput(
        model.getUuid(),
        model.getName(),
        model.getDescription()
    );
  }
}
