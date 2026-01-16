package com.connectfood.core.application.user.usecase;

import com.connectfood.core.application.user.dto.UserOutput;
import com.connectfood.core.application.user.mapper.UserAppMapper;
import com.connectfood.core.domain.model.User;
import com.connectfood.core.domain.model.UserType;
import com.connectfood.core.domain.model.commons.PageModel;
import com.connectfood.core.domain.repository.UserGateway;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SearchUsersUseCaseTest {

  @Mock
  private UserGateway repository;

  @Mock
  private UserAppMapper mapper;

  @InjectMocks
  private SearchUsersUseCase useCase;

  @Test
  @DisplayName("Deve retornar lista paginada de usuários com sucesso")
  void shouldReturnPagedUsersSuccessfully() {
    final var fullName = "Pilar";
    final var email = "pilar@email.com";
    final var typeUuid = UUID.randomUUID();
    final var page = 0;
    final var size = 10;
    final var sort = "name";
    final var direction = "ASC";

    final var userType = new UserType(typeUuid, "ADMIN", "Admin");
    final var user = new User(UUID.randomUUID(), "Pilar", "pilar@email.com", "hash", userType);

    final var usersList = List.of(user);
    final var pageModel = new PageModel<>(usersList, 1L);

    final var userOutput = new UserOutput(user.getUuid(), "Pilar", "pilar@email.com", null);

    when(repository.findAll(fullName, email, typeUuid, page, size, sort, direction))
        .thenReturn(pageModel);

    when(mapper.toOutput(user)).thenReturn(userOutput);

    final var result = useCase.execute(fullName, email, typeUuid, page, size, sort, direction);

    Assertions.assertNotNull(result);
    Assertions.assertEquals(1L, result.total());
    Assertions.assertEquals(1, result.content().size());
    Assertions.assertEquals("Pilar", result.content().getFirst().getFullName());

    verify(repository, times(1)).findAll(fullName, email, typeUuid, page, size, sort, direction);
  }

  @Test
  @DisplayName("Deve retornar página vazia quando não encontrar resultados")
  void shouldReturnEmptyListWhenNoResults() {
    final var pageModel = new PageModel<>(List.<User>of(), 0L);

    when(repository.findAll(any(), any(), any(), any(), any(), any(), any()))
        .thenReturn(pageModel);

    final var result = useCase.execute(null, null, null, 0, 10, "id", "asc");

    Assertions.assertNotNull(result);
    Assertions.assertEquals(0L, result.total());
    Assertions.assertTrue(result.content().isEmpty());

    verify(mapper, never()).toOutput(any());
  }
}
