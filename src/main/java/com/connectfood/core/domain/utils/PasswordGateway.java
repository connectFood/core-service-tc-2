package com.connectfood.core.domain.utils;

public interface PasswordGateway {

  String encode(String password);

  Boolean matches(String password, String encodedPassword);
}
