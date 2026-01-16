package com.connectfood.core.application.user.dto;

import java.util.UUID;

import com.connectfood.core.application.address.dto.AddressOutput;
import com.connectfood.core.application.usertype.dto.UserTypeOutput;

import lombok.Getter;

@Getter
public class UserOutput {

  private final UUID uuid;
  private final String fullName;
  private final String email;
  private final UserTypeOutput usersType;
  private final AddressOutput address;

  public UserOutput(final UUID uuid, final String fullName, final String email, final UserTypeOutput usersType,
                    final AddressOutput address) {
    this.uuid = uuid;
    this.fullName = fullName;
    this.email = email;
    this.usersType = usersType;
    this.address = address;
  }

  public UserOutput(final UUID uuid, final String fullName, final String email, final UserTypeOutput usersType) {
    this(uuid, fullName, email, usersType, null);
  }
}
