package com.connectfood.core.domain.utils;

public interface PasswordUtils {

  String encode(String password);

  Boolean matches(String password, String encodedPassword);
}
