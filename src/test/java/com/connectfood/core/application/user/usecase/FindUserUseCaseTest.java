package com.connectfood.core.application.user.usecase;

import com.connectfood.core.application.user.dto.UserOutput;
import com.connectfood.core.application.user.mapper.UserAppMapper;
import com.connectfood.core.application.usertype.dto.UserTypeOutput;
import com.connectfood.core.domain.exception.NotFoundException;
import com.connectfood.core.domain.model.User;
import com.connectfood.core.domain.model.UserType;
import com.connectfood.core.domain.repository.UserAddressGateway;
import com.connectfood.core.domain.repository.UserGateway;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FindUserUseCaseTest {

  @Mock
  private UserGateway repository;
  @Mock
  private UserAppMapper mapper;
  @Mock
  private UserAddressGateway userAddressGateway;

  @InjectMocks
  private FindUserUseCase useCase;

  @Test
  @DisplayName("Deve retornar UsersOutput quando usuário existir")
  void shouldReturnUserWhenExists() {
    final var uuid = UUID.randomUUID();
    final var userType = new UserType(UUID.randomUUID(), "ADMIN", "Admin");
    final var user = new User(uuid, "Pilar", "pilar@test.com", "senha", userType);
    final var output = new UserOutput(uuid, "Pilar", "pilar@test.com", new UserTypeOutput(userType.getUuid(), "ADMIN", "Admin"));

    when(repository.findByUuid(uuid)).thenReturn(Optional.of(user));
    when(mapper.toOutput(user)).thenReturn(output);

    final var result = useCase.execute(uuid);

    Assertions.assertNotNull(result);
    Assertions.assertEquals(uuid, result.getUuid());
    verify(repository, times(1)).findByUuid(uuid);
  }

  @Test
  @DisplayName("Deve lançar NotFoundException quando id não existir")
  void shouldThrowNotFoundWhenIdDoesNotExist() {
    final var uuid = UUID.randomUUID();
    when(repository.findByUuid(uuid)).thenReturn(Optional.empty());

    Assertions.assertThrows(NotFoundException.class, () -> {
      useCase.execute(uuid);
    });
  }
}
