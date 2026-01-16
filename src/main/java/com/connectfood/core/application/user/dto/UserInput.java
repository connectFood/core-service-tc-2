package com.connectfood.core.application.user.dto;

import java.util.UUID;

import com.connectfood.core.application.address.dto.AddressInput;

import lombok.Getter;

@Getter
public class UserInput {

  private final String fullName;
  private final String email;
  private final String password;
  private final UUID usersTypeUuid;
  private final AddressInput address;

  public UserInput(final String fullName, final String email, final String password, final UUID usersTypeUuid,
                   final AddressInput address) {
    this.fullName = fullName;
    this.email = email;
    this.password = password;
    this.usersTypeUuid = usersTypeUuid;
    this.address = address;
  }
}
