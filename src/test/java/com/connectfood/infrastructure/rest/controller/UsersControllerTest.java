package com.connectfood.infrastructure.rest.controller;

import java.util.List;
import java.util.UUID;

import com.connectfood.core.application.dto.commons.PageOutput;
import com.connectfood.core.application.users.dto.UsersInput;
import com.connectfood.core.application.users.dto.UsersOutput;
import com.connectfood.core.application.users.usecase.CreateUsersUseCase;
import com.connectfood.core.application.users.usecase.FindUsersUseCase;
import com.connectfood.core.application.users.usecase.RemoveUsersUseCase;
import com.connectfood.core.application.users.usecase.SearchUsersUseCase;
import com.connectfood.core.application.users.usecase.UpdateUsersUseCase;
import com.connectfood.infrastructure.rest.dto.commons.BaseResponse;
import com.connectfood.infrastructure.rest.dto.commons.PageResponse;
import com.connectfood.infrastructure.rest.dto.users.UsersRequest;
import com.connectfood.infrastructure.rest.dto.users.UsersResponse;
import com.connectfood.infrastructure.rest.mappers.UsersEntryMapper;
import com.connectfood.infrastructure.rest.controller.UsersController;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.http.HttpStatus;

@ExtendWith(MockitoExtension.class)
class UsersControllerTest {

  @Mock
  private SearchUsersUseCase searchUseCase;

  @Mock
  private FindUsersUseCase findUseCase;

  @Mock
  private CreateUsersUseCase createUseCase;

  @Mock
  private UpdateUsersUseCase updateUseCase;

  @Mock
  private RemoveUsersUseCase removeUseCase;

  @Mock
  private UsersEntryMapper mapper;

  @InjectMocks
  private UsersController controller;

  @Test
  @DisplayName("Deve retornar status 200 e PageResponse com conteúdo mapeado quando houver resultados")
  void shouldReturnOkWithPageResponseWhenHasResults() {
    final var fullName = "Maria";
    final var email = "maria@test.com";
    final var usersTypeUuid = UUID.randomUUID();
    final var page = 1;
    final var size = 10;
    final var sort = "fullName";
    final var direction = "ASC";

    final UsersOutput out1 = Mockito.mock(UsersOutput.class);
    final UsersOutput out2 = Mockito.mock(UsersOutput.class);

    Mockito.when(searchUseCase.execute(fullName, email, usersTypeUuid, page, size, sort, direction))
        .thenReturn(new PageOutput<>(List.of(out1, out2), 2L));

    final UsersResponse resp1 = Mockito.mock(UsersResponse.class);
    final UsersResponse resp2 = Mockito.mock(UsersResponse.class);

    Mockito.when(mapper.toResponse(out1)).thenReturn(resp1);
    Mockito.when(mapper.toResponse(out2)).thenReturn(resp2);

    final var result = controller.search(fullName, email, usersTypeUuid, page, size, sort, direction);

    Assertions.assertNotNull(result);
    Assertions.assertEquals(HttpStatus.OK, result.getStatusCode());
    Assertions.assertNotNull(result.getBody());

    final PageResponse<List<UsersResponse>> body = result.getBody();
    Assertions.assertNotNull(body);
    Assertions.assertEquals(List.of(resp1, resp2), body.content());
    Assertions.assertEquals(2L, body.total());
    Assertions.assertEquals(page, body.page());
    Assertions.assertEquals(size, body.size());

    Mockito.verify(searchUseCase, Mockito.times(1))
        .execute(fullName, email, usersTypeUuid, page, size, sort, direction);
    Mockito.verify(mapper, Mockito.times(1)).toResponse(out1);
    Mockito.verify(mapper, Mockito.times(1)).toResponse(out2);

    Mockito.verifyNoMoreInteractions(searchUseCase, mapper);
    Mockito.verifyNoInteractions(findUseCase, createUseCase, updateUseCase, removeUseCase);
  }

  @Test
  @DisplayName("Deve retornar status 200 e PageResponse vazio quando não houver resultados")
  void shouldReturnOkWithEmptyPageResponseWhenNoResults() {
    final String fullName = null;
    final String email = null;
    final UUID usersTypeUuid = null;
    final var page = 0;
    final var size = 10;
    final String sort = null;
    final String direction = null;

    Mockito.when(searchUseCase.execute(fullName, email, usersTypeUuid, page, size, sort, direction))
        .thenReturn(new PageOutput<>(List.of(), 0L));

    final var result = controller.search(fullName, email, usersTypeUuid, page, size, sort, direction);

    Assertions.assertNotNull(result);
    Assertions.assertEquals(HttpStatus.OK, result.getStatusCode());
    Assertions.assertNotNull(result.getBody());

    final PageResponse<List<UsersResponse>> body = result.getBody();
    Assertions.assertNotNull(body);
    Assertions.assertNotNull(body.content());
    Assertions.assertTrue(body.content().isEmpty());
    Assertions.assertEquals(0L, body.total());
    Assertions.assertEquals(page, body.page());
    Assertions.assertEquals(size, body.size());

    Mockito.verify(searchUseCase, Mockito.times(1))
        .execute(fullName, email, usersTypeUuid, page, size, sort, direction);

    Mockito.verifyNoMoreInteractions(searchUseCase);
    Mockito.verifyNoInteractions(mapper, findUseCase, createUseCase, updateUseCase, removeUseCase);
  }

  @Test
  @DisplayName("Deve retornar status 200 e BaseResponse quando buscar por uuid")
  void shouldReturnOkWithBaseResponseWhenFindByUuid() {
    final var uuid = UUID.randomUUID();

    final UsersOutput out = Mockito.mock(UsersOutput.class);
    Mockito.when(findUseCase.execute(uuid)).thenReturn(out);

    final UsersResponse resp = Mockito.mock(UsersResponse.class);
    Mockito.when(mapper.toResponse(out)).thenReturn(resp);

    final var result = controller.findByUuid(uuid);

    Assertions.assertNotNull(result);
    Assertions.assertEquals(HttpStatus.OK, result.getStatusCode());
    Assertions.assertNotNull(result.getBody());

    final BaseResponse<UsersResponse> body = result.getBody();
    Assertions.assertNotNull(body);
    Assertions.assertSame(resp, body.content());

    Mockito.verify(findUseCase, Mockito.times(1)).execute(uuid);
    Mockito.verify(mapper, Mockito.times(1)).toResponse(out);

    Mockito.verifyNoMoreInteractions(findUseCase, mapper);
    Mockito.verifyNoInteractions(searchUseCase, createUseCase, updateUseCase, removeUseCase);
  }

  @Test
  @DisplayName("Deve retornar status 201 e BaseResponse quando criar")
  void shouldReturnCreatedWithBaseResponseWhenCreate() {
    final UsersRequest request = Mockito.mock(UsersRequest.class);

    final UsersInput input = Mockito.mock(UsersInput.class);
    Mockito.when(mapper.toInput(request)).thenReturn(input);

    final UsersOutput out = Mockito.mock(UsersOutput.class);
    Mockito.when(createUseCase.execute(input)).thenReturn(out);

    final UsersResponse resp = Mockito.mock(UsersResponse.class);
    Mockito.when(mapper.toResponse(out)).thenReturn(resp);

    final var result = controller.create(request);

    Assertions.assertNotNull(result);
    Assertions.assertEquals(HttpStatus.CREATED, result.getStatusCode());
    Assertions.assertNotNull(result.getBody());

    final BaseResponse<UsersResponse> body = result.getBody();
    Assertions.assertNotNull(body);
    Assertions.assertSame(resp, body.content());

    Mockito.verify(mapper, Mockito.times(1)).toInput(request);
    Mockito.verify(createUseCase, Mockito.times(1)).execute(input);
    Mockito.verify(mapper, Mockito.times(1)).toResponse(out);

    Mockito.verifyNoMoreInteractions(createUseCase, mapper);
    Mockito.verifyNoInteractions(searchUseCase, findUseCase, updateUseCase, removeUseCase);
  }

  @Test
  @DisplayName("Deve retornar status 200 e BaseResponse quando atualizar")
  void shouldReturnOkWithBaseResponseWhenUpdate() {
    final var uuid = UUID.randomUUID();
    final UsersRequest request = Mockito.mock(UsersRequest.class);

    final UsersInput input = Mockito.mock(UsersInput.class);
    Mockito.when(mapper.toInput(request)).thenReturn(input);

    final UsersOutput out = Mockito.mock(UsersOutput.class);
    Mockito.when(updateUseCase.execute(uuid, input)).thenReturn(out);

    final UsersResponse resp = Mockito.mock(UsersResponse.class);
    Mockito.when(mapper.toResponse(out)).thenReturn(resp);

    final var result = controller.update(uuid, request);

    Assertions.assertNotNull(result);
    Assertions.assertEquals(HttpStatus.OK, result.getStatusCode());
    Assertions.assertNotNull(result.getBody());

    final BaseResponse<UsersResponse> body = result.getBody();
    Assertions.assertNotNull(body);
    Assertions.assertSame(resp, body.content());

    Mockito.verify(mapper, Mockito.times(1)).toInput(request);
    Mockito.verify(updateUseCase, Mockito.times(1)).execute(uuid, input);
    Mockito.verify(mapper, Mockito.times(1)).toResponse(out);

    Mockito.verifyNoMoreInteractions(updateUseCase, mapper);
    Mockito.verifyNoInteractions(searchUseCase, findUseCase, createUseCase, removeUseCase);
  }

  @Test
  @DisplayName("Deve retornar status 204 quando remover")
  void shouldReturnNoContentWhenRemove() {
    final var uuid = UUID.randomUUID();

    final var result = controller.delete(uuid);

    Assertions.assertNotNull(result);
    Assertions.assertEquals(HttpStatus.NO_CONTENT, result.getStatusCode());
    Assertions.assertNull(result.getBody());

    Mockito.verify(removeUseCase, Mockito.times(1)).execute(uuid);
    Mockito.verifyNoMoreInteractions(removeUseCase);
    Mockito.verifyNoInteractions(searchUseCase, findUseCase, createUseCase, updateUseCase, mapper);
  }
}
