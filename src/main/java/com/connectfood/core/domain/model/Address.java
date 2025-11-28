package com.connectfood.core.domain.model;

import java.time.LocalDateTime;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Address {

  private final Long id;
  private final UUID uuid;
  private final String street;
  private final String number;
  private final String complement;
  private final String neighborhood;
  private final String city;
  private final String state;
  private final String country;
  private final String zipCode;
  private final LocalDateTime createdAt;
  private final LocalDateTime updatedAt;
  private final Integer version;
}
