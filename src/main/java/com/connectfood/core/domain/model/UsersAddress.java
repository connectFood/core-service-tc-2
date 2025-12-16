package com.connectfood.core.domain.model;

import java.util.UUID;

import com.connectfood.core.domain.exception.BadRequestException;

import lombok.Getter;

@Getter
public class UsersAddress {

  private final UUID uuid;
  private final Users users;
  private final Address address;

  public UsersAddress(final UUID uuid, final Users users, final Address address) {

    if (users == null) {
      throw new BadRequestException("Users is required");
    }

    if (address == null) {
      throw new BadRequestException("Address is required");
    }

    this.uuid = uuid == null ? UUID.randomUUID() : uuid;
    this.users = users;
    this.address = address;
  }

  public UsersAddress(final Users users, final Address address) {
    this(null, users, address);
  }
}
