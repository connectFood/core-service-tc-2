package com.connectfood.infrastructure.rest.controller;

import java.util.List;
import java.util.UUID;

import com.connectfood.core.application.dto.commons.PageOutput;
import com.connectfood.core.application.usertype.dto.UserTypeInput;
import com.connectfood.core.application.usertype.dto.UserTypeOutput;
import com.connectfood.core.application.usertype.usecase.CreateUserTypeUseCase;
import com.connectfood.core.application.usertype.usecase.FindUserTypeUseCase;
import com.connectfood.core.application.usertype.usecase.RemoveUserTypeUseCase;
import com.connectfood.core.application.usertype.usecase.SearchUsersTypeUseCase;
import com.connectfood.core.application.usertype.usecase.UpdateUserTypeUseCase;
import com.connectfood.infrastructure.rest.dto.commons.BaseResponse;
import com.connectfood.infrastructure.rest.dto.commons.PageResponse;
import com.connectfood.infrastructure.rest.dto.usertype.UserTypeRequest;
import com.connectfood.infrastructure.rest.dto.usertype.UserTypeResponse;
import com.connectfood.infrastructure.rest.mappers.UserTypeEntryMapper;

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
class UserTypeControllerTest {

  @Mock
  private SearchUsersTypeUseCase searchUseCase;

  @Mock
  private FindUserTypeUseCase findUseCase;

  @Mock
  private CreateUserTypeUseCase createUseCase;

  @Mock
  private UpdateUserTypeUseCase updateUseCase;

  @Mock
  private RemoveUserTypeUseCase removeUseCase;

  @Mock
  private UserTypeEntryMapper mapper;

  @InjectMocks
  private UserTypeController controller;

  @Test
  @DisplayName("Deve retornar status 200 e PageResponse com conteúdo mapeado quando houver resultados")
  void shouldReturnOkWithPageResponseWhenHasResults() {
    final var name = "ADMIN";
    final var page = 1;
    final var size = 10;
    final var sort = "name";
    final var direction = "ASC";

    final UserTypeOutput out1 = Mockito.mock(UserTypeOutput.class);
    final UserTypeOutput out2 = Mockito.mock(UserTypeOutput.class);

    Mockito.when(searchUseCase.execute(name, page, size, sort, direction))
        .thenReturn(new PageOutput<>(List.of(out1, out2), 2L));

    final UserTypeResponse resp1 = Mockito.mock(UserTypeResponse.class);
    final UserTypeResponse resp2 = Mockito.mock(UserTypeResponse.class);

    Mockito.when(mapper.toResponse(out1))
        .thenReturn(resp1);
    Mockito.when(mapper.toResponse(out2))
        .thenReturn(resp2);

    final var result = controller.search(name, page, size, sort, direction);

    Assertions.assertNotNull(result);
    Assertions.assertEquals(HttpStatus.OK, result.getStatusCode());
    Assertions.assertNotNull(result.getBody());

    final PageResponse<List<UserTypeResponse>> body = result.getBody();
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

    final PageResponse<List<UserTypeResponse>> body = result.getBody();
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

    final UserTypeOutput out = Mockito.mock(UserTypeOutput.class);
    Mockito.when(findUseCase.execute(uuid))
        .thenReturn(out);

    final UserTypeResponse resp = Mockito.mock(UserTypeResponse.class);
    Mockito.when(mapper.toResponse(out))
        .thenReturn(resp);

    final var result = controller.findByUuid(uuid);

    Assertions.assertNotNull(result);
    Assertions.assertEquals(HttpStatus.OK, result.getStatusCode());
    Assertions.assertNotNull(result.getBody());

    final BaseResponse<UserTypeResponse> body = result.getBody();
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
    final UserTypeRequest request = Mockito.mock(UserTypeRequest.class);

    final UserTypeInput input = Mockito.mock(UserTypeInput.class);
    Mockito.when(mapper.toInput(request))
        .thenReturn(input);

    final UserTypeOutput out = Mockito.mock(UserTypeOutput.class);
    Mockito.when(createUseCase.execute(input))
        .thenReturn(out);

    final UserTypeResponse resp = Mockito.mock(UserTypeResponse.class);
    Mockito.when(mapper.toResponse(out))
        .thenReturn(resp);

    final var result = controller.create(request);

    Assertions.assertNotNull(result);
    Assertions.assertEquals(HttpStatus.CREATED, result.getStatusCode());
    Assertions.assertNotNull(result.getBody());

    final BaseResponse<UserTypeResponse> body = result.getBody();
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
    final UserTypeRequest request = Mockito.mock(UserTypeRequest.class);

    final UserTypeInput input = Mockito.mock(UserTypeInput.class);
    Mockito.when(mapper.toInput(request))
        .thenReturn(input);

    final UserTypeOutput out = Mockito.mock(UserTypeOutput.class);
    Mockito.when(updateUseCase.execute(uuid, input))
        .thenReturn(out);

    final UserTypeResponse resp = Mockito.mock(UserTypeResponse.class);
    Mockito.when(mapper.toResponse(out))
        .thenReturn(resp);

    final var result = controller.update(uuid, request);

    Assertions.assertNotNull(result);
    Assertions.assertEquals(HttpStatus.OK, result.getStatusCode());
    Assertions.assertNotNull(result.getBody());

    final BaseResponse<UserTypeResponse> body = result.getBody();
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
