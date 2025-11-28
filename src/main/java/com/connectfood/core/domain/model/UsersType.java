package com.connectfood.core.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@AllArgsConstructor
public class UsersType {

  private final Long id;
  private final UUID uuid;
  private final String name;
  private final String description;
  private final LocalDateTime createdAt;
  private final LocalDateTime updatedAt;
  private final Integer version;
}
