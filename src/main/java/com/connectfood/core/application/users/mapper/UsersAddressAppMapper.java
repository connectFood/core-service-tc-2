package com.connectfood.core.application.users.mapper;

import com.connectfood.core.domain.model.Address;
import com.connectfood.core.domain.model.Users;
import com.connectfood.core.domain.model.UsersAddress;

import org.springframework.stereotype.Component;

@Component
public class UsersAddressAppMapper {

  public UsersAddressAppMapper() {
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
}
