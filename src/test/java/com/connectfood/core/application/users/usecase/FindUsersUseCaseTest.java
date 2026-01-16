package com.connectfood.core.application.users.usecase;

import com.connectfood.core.application.users.dto.UsersOutput;
import com.connectfood.core.application.users.mapper.UsersAppMapper;
import com.connectfood.core.application.usertype.dto.UsersTypeOutput;
import com.connectfood.core.domain.exception.NotFoundException;
import com.connectfood.core.domain.model.Users;
import com.connectfood.core.domain.model.UsersType;
import com.connectfood.core.domain.repository.UsersAddressGateway;
import com.connectfood.core.domain.repository.UsersGateway;
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
class FindUsersUseCaseTest {

  @Mock
  private UsersGateway repository;
  @Mock
  private UsersAppMapper mapper;
  @Mock
  private UsersAddressGateway usersAddressGateway;

  @InjectMocks
  private FindUsersUseCase useCase;

  @Test
  @DisplayName("Deve retornar UsersOutput quando usuário existir")
  void shouldReturnUserWhenExists() {
    final var uuid = UUID.randomUUID();
    final var userType = new UsersType(UUID.randomUUID(), "ADMIN", "Admin");
    final var user = new Users(uuid, "Pilar", "pilar@test.com", "senha", userType);
    final var output = new UsersOutput(uuid, "Pilar", "pilar@test.com", new UsersTypeOutput(userType.getUuid(), "ADMIN", "Admin"));

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
