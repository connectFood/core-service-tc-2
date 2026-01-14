package com.connectfood.core.entrypoint.rest.controller;

import java.util.List;
import java.util.UUID;

import com.connectfood.core.application.dto.commons.PageOutput;
import com.connectfood.core.application.usertype.dto.UsersTypeInput;
import com.connectfood.core.application.usertype.dto.UsersTypeOutput;
import com.connectfood.core.application.usertype.usecase.CreateUserTypeUseCase;
import com.connectfood.core.application.usertype.usecase.FindUserTypeUseCase;
import com.connectfood.core.application.usertype.usecase.RemoveUserTypeUseCase;
import com.connectfood.core.application.usertype.usecase.SearchUserTypeUseCase;
import com.connectfood.core.application.usertype.usecase.UpdateUserTypeUseCase;
import com.connectfood.core.entrypoint.rest.dto.commons.BaseResponse;
import com.connectfood.core.entrypoint.rest.dto.commons.PageResponse;
import com.connectfood.core.entrypoint.rest.dto.userstype.UsersTypeRequest;
import com.connectfood.core.entrypoint.rest.dto.userstype.UsersTypeResponse;
import com.connectfood.core.entrypoint.rest.mappers.UsersTypeEntryMapper;

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
class UsersTypeControllerTest {

  @Mock
  private SearchUserTypeUseCase searchUseCase;

  @Mock
  private FindUserTypeUseCase findUseCase;

  @Mock
  private CreateUserTypeUseCase createUseCase;

  @Mock
  private UpdateUserTypeUseCase updateUseCase;

  @Mock
  private RemoveUserTypeUseCase removeUseCase;

  @Mock
  private UsersTypeEntryMapper mapper;

  @InjectMocks
  private UsersTypeController controller;

  @Test
  @DisplayName("Deve retornar status 200 e PageResponse com conteúdo mapeado quando houver resultados")
  void shouldReturnOkWithPageResponseWhenHasResults() {
    final var name = "ADMIN";
    final var page = 1;
    final var size = 10;
    final var sort = "name";
    final var direction = "ASC";

    final UsersTypeOutput out1 = Mockito.mock(UsersTypeOutput.class);
    final UsersTypeOutput out2 = Mockito.mock(UsersTypeOutput.class);

    Mockito.when(searchUseCase.execute(name, page, size, sort, direction))
        .thenReturn(new PageOutput<>(List.of(out1, out2), 2L));

    final UsersTypeResponse resp1 = Mockito.mock(UsersTypeResponse.class);
    final UsersTypeResponse resp2 = Mockito.mock(UsersTypeResponse.class);

    Mockito.when(mapper.toResponse(out1))
        .thenReturn(resp1);
    Mockito.when(mapper.toResponse(out2))
        .thenReturn(resp2);

    final var result = controller.search(name, page, size, sort, direction);

    Assertions.assertNotNull(result);
    Assertions.assertEquals(HttpStatus.OK, result.getStatusCode());
    Assertions.assertNotNull(result.getBody());

    final PageResponse<List<UsersTypeResponse>> body = result.getBody();
    Assertions.assertNotNull(body);
    Assertions.assertNotNull(body.content());
    Assertions.assertEquals(List.of(resp1, resp2), body.content());
    Assertions.assertEquals(2L, body.total());
    Assertions.assertEquals(page, body.page());
    Assertions.assertEquals(size, body.size());

    Mockito.verify(searchUseCase, Mockito.times(1))
        .execute(name, page, size, sort, direction);
    Mockito.verify(mapper, Mockito.times(1))
        .toResponse(out1);
    Mockito.verify(mapper, Mockito.times(1))
        .toResponse(out2);

    Mockito.verifyNoMoreInteractions(searchUseCase, mapper);
    Mockito.verifyNoInteractions(findUseCase, createUseCase, updateUseCase, removeUseCase);
  }

  @Test
  @DisplayName("Deve retornar status 200 e PageResponse vazio quando não houver resultados")
  void shouldReturnOkWithEmptyPageResponseWhenNoResults() {
    final String name = null;
    final var page = 0;
    final var size = 10;
    final String sort = null;
    final String direction = null;

    Mockito.when(searchUseCase.execute(name, page, size, sort, direction))
        .thenReturn(new PageOutput<>(List.of(), 0L));

    final var result = controller.search(name, page, size, sort, direction);

    Assertions.assertNotNull(result);
    Assertions.assertEquals(HttpStatus.OK, result.getStatusCode());
    Assertions.assertNotNull(result.getBody());

    final PageResponse<List<UsersTypeResponse>> body = result.getBody();
    Assertions.assertNotNull(body);
    Assertions.assertNotNull(body.content());
    Assertions.assertTrue(body.content()
        .isEmpty());
    Assertions.assertEquals(0L, body.total());
    Assertions.assertEquals(page, body.page());
    Assertions.assertEquals(size, body.size());

    Mockito.verify(searchUseCase, Mockito.times(1))
        .execute(name, page, size, sort, direction);

    Mockito.verifyNoMoreInteractions(searchUseCase);
    Mockito.verifyNoInteractions(mapper, findUseCase, createUseCase, updateUseCase, removeUseCase);
  }

  @Test
  @DisplayName("Deve retornar status 200 e BaseResponse quando buscar por uuid")
  void shouldReturnOkWithBaseResponseWhenFindByUuid() {
    final var uuid = UUID.randomUUID();

    final UsersTypeOutput out = Mockito.mock(UsersTypeOutput.class);
    Mockito.when(findUseCase.execute(uuid))
        .thenReturn(out);

    final UsersTypeResponse resp = Mockito.mock(UsersTypeResponse.class);
    Mockito.when(mapper.toResponse(out))
        .thenReturn(resp);

    final var result = controller.findByUuid(uuid);

    Assertions.assertNotNull(result);
    Assertions.assertEquals(HttpStatus.OK, result.getStatusCode());
    Assertions.assertNotNull(result.getBody());

    final BaseResponse<UsersTypeResponse> body = result.getBody();
    Assertions.assertNotNull(body);
    Assertions.assertSame(resp, body.content());

    Mockito.verify(findUseCase, Mockito.times(1))
        .execute(uuid);
    Mockito.verify(mapper, Mockito.times(1))
        .toResponse(out);

    Mockito.verifyNoMoreInteractions(findUseCase, mapper);
    Mockito.verifyNoInteractions(searchUseCase, createUseCase, updateUseCase, removeUseCase);
  }

  @Test
  @DisplayName("Deve retornar status 201 e BaseResponse quando criar")
  void shouldReturnCreatedWithBaseResponseWhenCreate() {
    final UsersTypeRequest request = Mockito.mock(UsersTypeRequest.class);

    final UsersTypeInput input = Mockito.mock(UsersTypeInput.class);
    Mockito.when(mapper.toInput(request))
        .thenReturn(input);

    final UsersTypeOutput out = Mockito.mock(UsersTypeOutput.class);
    Mockito.when(createUseCase.execute(input))
        .thenReturn(out);

    final UsersTypeResponse resp = Mockito.mock(UsersTypeResponse.class);
    Mockito.when(mapper.toResponse(out))
        .thenReturn(resp);

    final var result = controller.create(request);

    Assertions.assertNotNull(result);
    Assertions.assertEquals(HttpStatus.CREATED, result.getStatusCode());
    Assertions.assertNotNull(result.getBody());

    final BaseResponse<UsersTypeResponse> body = result.getBody();
    Assertions.assertNotNull(body);
    Assertions.assertSame(resp, body.content());

    Mockito.verify(mapper, Mockito.times(1))
        .toInput(request);
    Mockito.verify(createUseCase, Mockito.times(1))
        .execute(input);
    Mockito.verify(mapper, Mockito.times(1))
        .toResponse(out);

    Mockito.verifyNoMoreInteractions(createUseCase, mapper);
    Mockito.verifyNoInteractions(searchUseCase, findUseCase, updateUseCase, removeUseCase);
  }

  @Test
  @DisplayName("Deve retornar status 200 e BaseResponse quando atualizar")
  void shouldReturnOkWithBaseResponseWhenUpdate() {
    final var uuid = UUID.randomUUID();
    final UsersTypeRequest request = Mockito.mock(UsersTypeRequest.class);

    final UsersTypeInput input = Mockito.mock(UsersTypeInput.class);
    Mockito.when(mapper.toInput(request))
        .thenReturn(input);

    final UsersTypeOutput out = Mockito.mock(UsersTypeOutput.class);
    Mockito.when(updateUseCase.execute(uuid, input))
        .thenReturn(out);

    final UsersTypeResponse resp = Mockito.mock(UsersTypeResponse.class);
    Mockito.when(mapper.toResponse(out))
        .thenReturn(resp);

    final var result = controller.update(uuid, request);

    Assertions.assertNotNull(result);
    Assertions.assertEquals(HttpStatus.OK, result.getStatusCode());
    Assertions.assertNotNull(result.getBody());

    final BaseResponse<UsersTypeResponse> body = result.getBody();
    Assertions.assertNotNull(body);
    Assertions.assertSame(resp, body.content());

    Mockito.verify(mapper, Mockito.times(1))
        .toInput(request);
    Mockito.verify(updateUseCase, Mockito.times(1))
        .execute(uuid, input);
    Mockito.verify(mapper, Mockito.times(1))
        .toResponse(out);

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

    Mockito.verify(removeUseCase, Mockito.times(1))
        .execute(uuid);
    Mockito.verifyNoMoreInteractions(removeUseCase);
    Mockito.verifyNoInteractions(searchUseCase, findUseCase, createUseCase, updateUseCase, mapper);
  }
}
