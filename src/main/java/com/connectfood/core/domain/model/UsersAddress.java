package com.connectfood.core.domain.model;

import java.time.LocalDateTime;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UsersAddress {

  private final Long id;
  private final UUID uuid;
  private final Users users;
  private final Address address;
  private final LocalDateTime createdAt;
  private final LocalDateTime updatedAt;
  private final Integer version;
}
