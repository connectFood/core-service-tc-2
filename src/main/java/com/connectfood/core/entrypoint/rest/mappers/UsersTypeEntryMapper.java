package com.connectfood.core.entrypoint.rest.mappers;

import com.connectfood.core.application.usertype.dto.UsersTypeInput;
import com.connectfood.core.application.usertype.dto.UsersTypeOutput;
import com.connectfood.core.entrypoint.rest.dto.userstype.UsersTypeRequest;
import com.connectfood.core.entrypoint.rest.dto.userstype.UsersTypeResponse;

import org.springframework.stereotype.Component;

@Component
public class UsersTypeEntryMapper {

  public UsersTypeEntryMapper() {
  }

  public UsersTypeInput toInput(final UsersTypeRequest request) {
    if (request == null) {
      return null;
    }

    return new UsersTypeInput(
        request.getName(),
        request.getDescription()
    );
  }

  public UsersTypeResponse toResponse(final UsersTypeOutput output) {
    if (output == null) {
      return null;
    }

    return new UsersTypeResponse(
        output.getUuid(),
        output.getName(),
        output.getDescription()
    );
  }
}
