package com.connectfood.core.application.address.mapper;

import com.connectfood.core.application.address.dto.UsersAddressOutput;
import com.connectfood.core.application.users.mapper.UsersAppMapper;
import com.connectfood.core.domain.model.Address;
import com.connectfood.core.domain.model.Users;
import com.connectfood.core.domain.model.UsersAddress;

import org.springframework.stereotype.Component;

@Component
public class UsersAddressAppMapper {

  private final UsersAppMapper usersMapper;

  public UsersAddressAppMapper(final UsersAppMapper usersMapper) {
    this.usersMapper = usersMapper;
  }

  public UsersAddress toDomain(final Users users, final Address address) {
    if (users == null || address == null) {
      return null;
    }

    return new UsersAddress(
        users,
        address
    );
  }

  public UsersAddressOutput toOutput(final UsersAddress model) {
    if (model == null) {
      return null;
    }

    return new UsersAddressOutput(
        model.getUuid(),
        model.getAddress()
            .getStreet(),
        model.getAddress()
            .getNumber(),
        model.getAddress()
            .getComplement(),
        model.getAddress()
            .getNeighborhood(),
        model.getAddress()
            .getCity(),
        model.getAddress()
            .getState(),
        model.getAddress()
            .getCountry(),
        model.getAddress()
            .getZipCode(),
        model.getUsers() != null ? usersMapper.toOutput(model.getUsers()) : null
    );
  }
}
