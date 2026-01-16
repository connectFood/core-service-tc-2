package com.connectfood.core.application.usertype.usecase;

import com.connectfood.core.application.usertype.dto.UsersTypeOutput;
import com.connectfood.core.application.usertype.mapper.UsersTypeAppMapper;
import com.connectfood.core.domain.model.UsersType;
import com.connectfood.core.domain.model.commons.PageModel;
import com.connectfood.core.domain.repository.UsersTypeGateway;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SearchUserTypeUseCaseTest {

  @Mock
  private UsersTypeGateway repository;

  @Mock
  private UsersTypeAppMapper mapper;

  @InjectMocks
  private SearchUserTypeUseCase useCase;

  @Test
  @DisplayName("Deve retornar lista paginada de tipos de usuário com sucesso")
  void shouldReturnPagedUserTypesSuccessfully() {
    final var name = "CLIENT";
    final var page = 0;
    final var size = 10;
    final var sort = "name";
    final var direction = "ASC";

    final var uuid = UUID.randomUUID();
    final var usersType = new UsersType(uuid, "CLIENT", "Cliente do sistema");
    final var usersTypeOutput = new UsersTypeOutput(uuid, "CLIENT", "Cliente do sistema");

    final var pageModel = new PageModel<>(List.of(usersType), 1L);

    when(repository.findAll(name, page, size, sort, direction)).thenReturn(pageModel);
    when(mapper.toOutput(usersType)).thenReturn(usersTypeOutput);

    final var result = useCase.execute(name, page, size, sort, direction);

    Assertions.assertNotNull(result);
    Assertions.assertEquals(1L, result.total());
    Assertions.assertEquals(1, result.content().size());
    Assertions.assertEquals("CLIENT", result.content().getFirst().getName());

    verify(repository, times(1)).findAll(name, page, size, sort, direction);
    verify(mapper, times(1)).toOutput(usersType);
  }

  @Test
  @DisplayName("Deve retornar página vazia quando não encontrar resultados")
  void shouldReturnEmptyPageWhenNoResults() {
    final var pageModel = new PageModel<List<UsersType>>(Collections.emptyList(), 0L);

    when(repository.findAll(any(), any(), any(), any(), any())).thenReturn(pageModel);

    final var result = useCase.execute("NOME_INEXISTENTE", 0, 10, "id", "asc");

    Assertions.assertNotNull(result);
    Assertions.assertEquals(0L, result.total());
    Assertions.assertTrue(result.content().isEmpty());

    verify(mapper, never()).toOutput(any());
  }
}
