package com.connectfood.infrastructure.rest.mappers;

import com.connectfood.core.application.users.dto.UsersInput;
import com.connectfood.core.application.users.dto.UsersOutput;
import com.connectfood.infrastructure.rest.dto.user.UserRequest;
import com.connectfood.infrastructure.rest.dto.user.UserResponse;

import org.springframework.stereotype.Component;

@Component
public class UserEntryMapper {

  private final UserTypeEntryMapper usersTypeMapper;
  private final AddressEntryMapper addressMapper;

  public UserEntryMapper(
      final UserTypeEntryMapper usersTypeMapper,
      final AddressEntryMapper addressMapper) {
    this.usersTypeMapper = usersTypeMapper;
    this.addressMapper = addressMapper;
  }

  public UsersInput toInput(final UserRequest request) {
    if (request == null) {
      return null;
    }

    return new UsersInput(
        request.getFullName(),
        request.getEmail(),
        request.getPassword(),
        request.getUsersTypeUuid(),
        request.getAddress() != null ? addressMapper.toInput(request.getAddress()) : null
    );
  }

  public UserResponse toResponse(final UsersOutput output) {
    if (output == null) {
      return null;
    }

    return new UserResponse(
        output.getUuid(),
        output.getFullName(),
        output.getEmail(),
        output.getUsersType() != null ? usersTypeMapper.toResponse(output.getUsersType()) : null,
        output.getAddress() != null ? addressMapper.toResponse(output.getAddress()) : null
    );
  }
}
