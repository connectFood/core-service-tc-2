package com.connectfood.core.domain.model;

import java.util.UUID;

import com.connectfood.core.domain.exception.BadRequestException;

import lombok.Getter;

@Getter
public class UserAddress {

  private final UUID uuid;
  private final User user;
  private final Address address;

  public UserAddress(final UUID uuid, final User user, final Address address) {

    if (user == null) {
      throw new BadRequestException("Users is required");
    }

    if (address == null) {
      throw new BadRequestException("Address is required");
    }

    this.uuid = uuid == null ? UUID.randomUUID() : uuid;
    this.user = user;
    this.address = address;
  }

  public UserAddress(final User user, final Address address) {
    this(null, user, address);
  }
}
