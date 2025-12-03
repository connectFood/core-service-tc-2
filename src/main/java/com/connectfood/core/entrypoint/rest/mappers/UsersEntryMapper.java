package com.connectfood.core.entrypoint.rest.mappers;

import com.connectfood.core.application.users.dto.UsersInput;
import com.connectfood.core.application.users.dto.UsersOutput;
import com.connectfood.core.entrypoint.rest.dto.users.UsersRequest;
import com.connectfood.core.entrypoint.rest.dto.users.UsersResponse;

import org.springframework.stereotype.Component;

@Component
public class UsersEntryMapper {

  private final UsersTypeEntryMapper usersTypeMapper;

  public UsersEntryMapper(final UsersTypeEntryMapper usersTypeMapper) {
    this.usersTypeMapper = usersTypeMapper;
  }

  public UsersInput toInput(final UsersRequest request) {
    if (request == null) {
      return null;
    }

    return new UsersInput(
        request.getFullName(),
        request.getEmail(),
        request.getPassword(),
        request.getUsersTypeUuid()
    );
  }

  public UsersResponse toResponse(final UsersOutput output) {
    if (output == null) {
      return null;
    }

    return new UsersResponse(
        output.getUuid(),
        output.getFullName(),
        output.getEmail(),
        output.getUsersType() != null ? usersTypeMapper.toResponse(output.getUsersType()) : null
    );
  }
}
