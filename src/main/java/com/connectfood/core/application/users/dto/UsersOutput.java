package com.connectfood.core.application.users.dto;

import java.util.UUID;

import com.connectfood.core.application.address.dto.AddressOutput;
import com.connectfood.core.application.usertype.dto.UsersTypeOutput;

import lombok.Getter;

@Getter
public class UsersOutput {

  private final UUID uuid;
  private final String fullName;
  private final String email;
  private final UsersTypeOutput usersType;
  private final AddressOutput address;

  public UsersOutput(final UUID uuid, final String fullName, final String email, final UsersTypeOutput usersType,
      final AddressOutput address) {
    this.uuid = uuid;
    this.fullName = fullName;
    this.email = email;
    this.usersType = usersType;
    this.address = address;
  }

  public UsersOutput(final UUID uuid, final String fullName, final String email, final UsersTypeOutput usersType) {
    this(uuid, fullName, email, usersType, null);
  }
}
