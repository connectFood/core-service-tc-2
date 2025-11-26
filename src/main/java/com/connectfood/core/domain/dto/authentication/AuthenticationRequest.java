package com.connectfood.core.domain.dto.authentication;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AuthenticationRequest {

  private String login;

  private String password;
}
