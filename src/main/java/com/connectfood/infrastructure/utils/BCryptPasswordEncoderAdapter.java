package com.connectfood.infrastructure.utils;

import com.connectfood.core.domain.utils.PasswordGateway;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class BCryptPasswordEncoderAdapter implements PasswordGateway {

  private final PasswordEncoder encoder;

  public BCryptPasswordEncoderAdapter() {
    this.encoder = new BCryptPasswordEncoder();
  }

  @Override
  public String encode(final String password) {
    return encoder.encode(password);
  }

  @Override
  public Boolean matches(final String password, final String encodedPassword) {
    return encoder.matches(password, encodedPassword);
  }
}
