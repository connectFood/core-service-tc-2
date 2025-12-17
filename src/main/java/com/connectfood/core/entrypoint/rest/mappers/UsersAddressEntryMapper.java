package com.connectfood.core.entrypoint.rest.mappers;

import com.connectfood.core.application.address.dto.AddressInput;
import com.connectfood.core.application.address.dto.AddressOutput;
import com.connectfood.core.application.address.dto.UsersAddressOutput;
import com.connectfood.core.entrypoint.rest.dto.address.AddressRequest;
import com.connectfood.core.entrypoint.rest.dto.address.AddressResponse;
import com.connectfood.core.entrypoint.rest.dto.address.UsersAddressResponse;

import org.springframework.stereotype.Component;

@Component
public class UsersAddressEntryMapper {

  private final UsersEntryMapper usersMapper;

  public UsersAddressEntryMapper(final UsersEntryMapper usersMapper) {
    this.usersMapper = usersMapper;
  }

  public UsersAddressResponse toResponse(final UsersAddressOutput output) {
    if (output == null) {
      return null;
    }

    return new UsersAddressResponse(
        output.getUuid(),
        output.getStreet(),
        output.getNumber(),
        output.getComplement(),
        output.getNeighborhood(),
        output.getCity(),
        output.getState(),
        output.getCountry(),
        output.getZipCode(),
        output.getUsers() != null ? usersMapper.toResponse(output.getUsers()) : null
    );
  }
}
