package com.connectfood.core.domain.model;

import java.util.UUID;

import com.connectfood.core.domain.exception.BadRequestException;

import lombok.Getter;

@Getter
public class Users {

  private final UUID uuid;
  private final String fullName;
  private final String email;
  private final String passwordHash;
  private final UsersType usersType;

  public Users(final UUID uuid, final String fullName, final String email, final String passwordHash,
      final UsersType usersType) {

    if (fullName == null || fullName.isBlank()) {
      throw new BadRequestException("Full name is required");
    }

    if (email == null || email.isBlank()) {
      throw new BadRequestException("Email is required");
    }

    if (passwordHash == null || passwordHash.isBlank()) {
      throw new BadRequestException("Password hash is required");
    }

    if (usersType == null) {
      throw new BadRequestException("Users type is required");
    }

    this.uuid = uuid == null ? UUID.randomUUID() : uuid;
    this.fullName = fullName;
    this.email = email;
    this.passwordHash = passwordHash;
    this.usersType = usersType;
  }
}
