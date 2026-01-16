package com.connectfood.core.domain.model;

import java.util.UUID;

import com.connectfood.core.domain.exception.BadRequestException;

import lombok.Getter;

@Getter
public class User {

  private final UUID uuid;
  private final String fullName;
  private final String email;
  private final String passwordHash;
  private final UserType userType;

  public User(final UUID uuid, final String fullName, final String email, final String passwordHash,
              final UserType userType) {

    if (fullName == null || fullName.isBlank()) {
      throw new BadRequestException("Full name is required");
    }

    if (email == null || email.isBlank()) {
      throw new BadRequestException("Email is required");
    }

    if (passwordHash == null || passwordHash.isBlank()) {
      throw new BadRequestException("Password hash is required");
    }

    if (userType == null) {
      throw new BadRequestException("Users type is required");
    }

    this.uuid = uuid == null ? UUID.randomUUID() : uuid;
    this.fullName = fullName;
    this.email = email;
    this.passwordHash = passwordHash;
    this.userType = userType;
  }

  public User(final String fullName, final String email, final String passwordHash, final UserType userType) {
    this(null, fullName, email, passwordHash, userType);
  }
}
