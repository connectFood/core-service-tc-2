package com.connectfood.core.domain.dto.users;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import com.connectfood.core.domain.dto.address.AddressResponse;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserResponse {

  private String uuid;

  private String fullName;

  private String email;

  private String login;

  private List<String> roles;

  private LocalDate createdAt;

  private LocalDateTime updatedAt;

  private List<AddressResponse> addresses;
}
