package com.connectfood.infrastructure.rest.mappers;

import com.connectfood.core.application.users.dto.UsersInput;
import com.connectfood.core.application.users.dto.UsersOutput;
import com.connectfood.infrastructure.rest.dto.users.UsersRequest;
import com.connectfood.infrastructure.rest.dto.users.UsersResponse;

import org.springframework.stereotype.Component;

@Component
public class UsersEntryMapper {

  private final UsersTypeEntryMapper usersTypeMapper;
  private final AddressEntryMapper addressMapper;

  public UsersEntryMapper(
      final UsersTypeEntryMapper usersTypeMapper,
      final AddressEntryMapper addressMapper) {
    this.usersTypeMapper = usersTypeMapper;
    this.addressMapper = addressMapper;
  }

  public UsersInput toInput(final UsersRequest request) {
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

  public UsersResponse toResponse(final UsersOutput output) {
    if (output == null) {
      return null;
    }

    return new UsersResponse(
        output.getUuid(),
        output.getFullName(),
        output.getEmail(),
        output.getUsersType() != null ? usersTypeMapper.toResponse(output.getUsersType()) : null,
        output.getAddress() != null ? addressMapper.toResponse(output.getAddress()) : null
    );
  }
}
