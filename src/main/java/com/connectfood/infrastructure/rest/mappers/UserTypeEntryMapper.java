package com.connectfood.infrastructure.rest.mappers;

import com.connectfood.core.application.usertype.dto.UsersTypeInput;
import com.connectfood.core.application.usertype.dto.UsersTypeOutput;
import com.connectfood.infrastructure.rest.dto.usertype.UserTypeRequest;
import com.connectfood.infrastructure.rest.dto.usertype.UserTypeResponse;

import org.springframework.stereotype.Component;

@Component
public class UserTypeEntryMapper {

  public UserTypeEntryMapper() {
  }

  public UsersTypeInput toInput(final UserTypeRequest request) {
    if (request == null) {
      return null;
    }

    return new UsersTypeInput(
        request.getName(),
        request.getDescription()
    );
  }

  public UserTypeResponse toResponse(final UsersTypeOutput output) {
    if (output == null) {
      return null;
    }

    return new UserTypeResponse(
        output.getUuid(),
        output.getName(),
        output.getDescription()
    );
  }
}
