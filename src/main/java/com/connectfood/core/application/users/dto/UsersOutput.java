package com.connectfood.core.application.users.dto;

import java.util.UUID;

import com.connectfood.core.application.usertype.dto.UsersTypeOutput;
import com.connectfood.core.domain.model.UsersType;

import lombok.Getter;

@Getter
public class UsersOutput {

  private final UUID uuid;
  private final String fullName;
  private final String email;
  private final UsersTypeOutput usersType;

  public UsersOutput(final UUID uuid, final String fullName, final String email, final UsersTypeOutput usersType) {
    this.uuid = uuid;
    this.fullName = fullName;
    this.email = email;
    this.usersType = usersType;
  }
}
