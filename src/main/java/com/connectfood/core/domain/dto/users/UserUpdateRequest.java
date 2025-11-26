package com.connectfood.core.domain.dto.users;

import java.util.List;

import com.connectfood.core.domain.dto.address.AddressCreateRequest;

import lombok.AllArgsConstructor;
import lombok.Getter;


@Getter
@AllArgsConstructor
public class UserUpdateRequest {

  private String fullName;

  private String email;

  private String login;

  private String password;

  private List<String> roles;

  private List<AddressCreateRequest> addresses;
}
