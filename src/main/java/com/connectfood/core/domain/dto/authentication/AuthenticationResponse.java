package com.connectfood.core.domain.dto.authentication;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AuthenticationResponse {

  private String accessToken;

  private String tokenType;

  private Long expiresIn;
}
