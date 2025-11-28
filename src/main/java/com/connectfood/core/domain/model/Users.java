package com.connectfood.core.domain.model;

import java.time.LocalDateTime;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Users {

  private final Long id;
  private final UUID uuid;
  private final String fullName;
  private final String email;
  private final String password;
  private final UsersType usersType;
  private final LocalDateTime createdAt;
  private final LocalDateTime updatedAt;
  private final Integer version;
}
