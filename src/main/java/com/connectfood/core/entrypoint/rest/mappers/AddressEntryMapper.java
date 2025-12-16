package com.connectfood.core.entrypoint.rest.mappers;

import com.connectfood.core.application.address.dto.AddressInput;
import com.connectfood.core.application.address.dto.UsersAddressOutput;
import com.connectfood.core.entrypoint.rest.dto.address.AddressRequest;
import com.connectfood.core.entrypoint.rest.dto.address.UsersAddressResponse;

import org.springframework.stereotype.Component;

@Component
public class AddressEntryMapper {

  private final UsersEntryMapper usersMapper;

  public AddressEntryMapper(final UsersEntryMapper usersMapper) {
    this.usersMapper = usersMapper;
  }

  public AddressInput toInput(final AddressRequest request) {
    if (request == null) {
      return null;
    }

    return new AddressInput(
        request.getStreet(),
        request.getNumber(),
        request.getComplement(),
        request.getNeighborhood(),
        request.getCity(),
        request.getState(),
        request.getCountry(),
        request.getZipCode()
    );
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
