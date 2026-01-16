package com.connectfood.core.application.users.mapper;

import com.connectfood.core.domain.model.Address;
import com.connectfood.core.domain.model.User;
import com.connectfood.core.domain.model.UserAddress;

import org.springframework.stereotype.Component;

@Component
public class UsersAddressAppMapper {

  public UsersAddressAppMapper() {
  }

  public UserAddress toDomain(final User user, final Address address) {
    if (user == null || address == null) {
      return null;
    }

    return new UserAddress(
        user,
        address
    );
  }
}
