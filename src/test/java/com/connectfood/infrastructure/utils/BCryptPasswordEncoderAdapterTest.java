package com.connectfood.infrastructure.utils;

import com.connectfood.core.domain.utils.PasswordGateway;
import com.connectfood.infrastructure.utils.BCryptPasswordEncoderAdapter;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class BCryptPasswordEncoderAdapterTest {

  private final PasswordGateway passwordGateway = new BCryptPasswordEncoderAdapter();

  @Test
  @DisplayName("Deve codificar a senha e retornar um hash diferente da senha original")
  void shouldEncodePassword() {
    final var rawPassword = "my-secure-password";

    final var encodedPassword = passwordGateway.encode(rawPassword);

    Assertions.assertNotNull(encodedPassword);
    Assertions.assertNotEquals(rawPassword, encodedPassword);
  }

  @Test
  @DisplayName("Deve retornar true quando a senha corresponder ao hash")
  void shouldReturnTrueWhenPasswordMatches() {
    final var rawPassword = "my-secure-password";

    final var encodedPassword = passwordGateway.encode(rawPassword);

    final var matches = passwordGateway.matches(rawPassword, encodedPassword);

    Assertions.assertTrue(matches);
  }

  @Test
  @DisplayName("Deve retornar false quando a senha não corresponder ao hash")
  void shouldReturnFalseWhenPasswordDoesNotMatch() {
    final var rawPassword = "my-secure-password";
    final var wrongPassword = "wrong-password";

    final var encodedPassword = passwordGateway.encode(rawPassword);

    final var matches = passwordGateway.matches(wrongPassword, encodedPassword);

    Assertions.assertFalse(matches);
  }
}
