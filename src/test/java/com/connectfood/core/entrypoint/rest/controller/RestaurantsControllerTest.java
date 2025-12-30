package com.connectfood.core.entrypoint.rest.controller;

import java.util.List;
import java.util.UUID;

import com.connectfood.core.application.dto.commons.PageOutput;
import com.connectfood.core.application.restaurants.dto.RestaurantsInput;
import com.connectfood.core.application.restaurants.dto.RestaurantsOutput;
import com.connectfood.core.application.restaurants.usecase.CreateRestaurantsUseCase;
import com.connectfood.core.application.restaurants.usecase.FindRestaurantsUseCase;
import com.connectfood.core.application.restaurants.usecase.RemoveRestaurantsUseCase;
import com.connectfood.core.application.restaurants.usecase.SearchRestaurantsUseCase;
import com.connectfood.core.application.restaurants.usecase.UpdateRestaurantsUseCase;
import com.connectfood.core.entrypoint.rest.dto.commons.BaseResponse;
import com.connectfood.core.entrypoint.rest.dto.commons.PageResponse;
import com.connectfood.core.entrypoint.rest.dto.restaurants.RestaurantsRequest;
import com.connectfood.core.entrypoint.rest.dto.restaurants.RestaurantsResponse;
import com.connectfood.core.entrypoint.rest.mappers.RestaurantsEntryMapper;

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
class RestaurantsControllerTest {

  @Mock
  private SearchRestaurantsUseCase searchUseCase;

  @Mock
  private FindRestaurantsUseCase findUseCase;

  @Mock
  private CreateRestaurantsUseCase createUseCase;

  @Mock
  private UpdateRestaurantsUseCase updateUseCase;

  @Mock
  private RemoveRestaurantsUseCase removeUseCase;

  @Mock
  private RestaurantsEntryMapper mapper;

  @InjectMocks
  private RestaurantsController controller;

  @Test
  @DisplayName("Deve retornar status 200 e PageResponse com conteúdo mapeado quando houver resultados")
  void shouldReturnOkWithPageResponseWhenHasResults() {
    final var name = "pizza";
    final var restaurantsTypeUuid = UUID.randomUUID();
    final var page = 1;
    final var size = 10;
    final var sort = "name";
    final var direction = "ASC";

    final RestaurantsOutput out1 = Mockito.mock(RestaurantsOutput.class);
    final RestaurantsOutput out2 = Mockito.mock(RestaurantsOutput.class);

    Mockito.when(searchUseCase.execute(name, restaurantsTypeUuid, page, size, sort, direction))
        .thenReturn(new PageOutput<>(List.of(out1, out2), 2L));

    final RestaurantsResponse resp1 = Mockito.mock(RestaurantsResponse.class);
    final RestaurantsResponse resp2 = Mockito.mock(RestaurantsResponse.class);

    Mockito.when(mapper.toResponse(out1))
        .thenReturn(resp1);
    Mockito.when(mapper.toResponse(out2))
        .thenReturn(resp2);

    final var result = controller.search(name, restaurantsTypeUuid, page, size, sort, direction);

    Assertions.assertNotNull(result);
    Assertions.assertEquals(HttpStatus.OK, result.getStatusCode());
    Assertions.assertNotNull(result.getBody());

    final PageResponse<List<RestaurantsResponse>> body = result.getBody();

    Assertions.assertNotNull(body.content());
    Assertions.assertEquals(List.of(resp1, resp2), body.content());
    Assertions.assertEquals(2L, body.total());
    Assertions.assertEquals(page, body.page());
    Assertions.assertEquals(size, body.size());

    Mockito.verify(searchUseCase, Mockito.times(1))
        .execute(name, restaurantsTypeUuid, page, size, sort, direction);
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
    final UUID restaurantsTypeUuid = null;
    final var page = 0;
    final var size = 10;
    final String sort = null;
    final String direction = null;

    Mockito.when(searchUseCase.execute(name, restaurantsTypeUuid, page, size, sort, direction))
        .thenReturn(new PageOutput<>(List.of(), 0L));

    final var result = controller.search(name, restaurantsTypeUuid, page, size, sort, direction);

    Assertions.assertNotNull(result);
    Assertions.assertEquals(HttpStatus.OK, result.getStatusCode());
    Assertions.assertNotNull(result.getBody());

    final PageResponse<List<RestaurantsResponse>> body = result.getBody();

    Assertions.assertNotNull(body.content());
    Assertions.assertTrue(body.content()
        .isEmpty());
    Assertions.assertEquals(0L, body.total());
    Assertions.assertEquals(page, body.page());
    Assertions.assertEquals(size, body.size());

    Mockito.verify(searchUseCase, Mockito.times(1))
        .execute(name, restaurantsTypeUuid, page, size, sort, direction);

    Mockito.verifyNoMoreInteractions(searchUseCase);
    Mockito.verifyNoInteractions(mapper, findUseCase, createUseCase, updateUseCase, removeUseCase);
  }

  @Test
  @DisplayName("Deve retornar status 200 e BaseResponse quando buscar por uuid")
  void shouldReturnOkWithBaseResponseWhenFindByUuid() {
    final var uuid = UUID.randomUUID();

    final RestaurantsOutput out = Mockito.mock(RestaurantsOutput.class);
    Mockito.when(findUseCase.execute(uuid))
        .thenReturn(out);

    final RestaurantsResponse resp = Mockito.mock(RestaurantsResponse.class);
    Mockito.when(mapper.toResponse(out))
        .thenReturn(resp);

    final var result = controller.findByUuid(uuid);

    Assertions.assertNotNull(result);
    Assertions.assertEquals(HttpStatus.OK, result.getStatusCode());
    Assertions.assertNotNull(result.getBody());

    final BaseResponse<RestaurantsResponse> body = result.getBody();
    Assertions.assertNotNull(body.content());
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
    final RestaurantsRequest request = Mockito.mock(RestaurantsRequest.class);

    final RestaurantsInput input = Mockito.mock(RestaurantsInput.class);
    Mockito.when(mapper.toInput(request))
        .thenReturn(input);

    final RestaurantsOutput out = Mockito.mock(RestaurantsOutput.class);
    Mockito.when(createUseCase.execute(input))
        .thenReturn(out);

    final RestaurantsResponse resp = Mockito.mock(RestaurantsResponse.class);
    Mockito.when(mapper.toResponse(out))
        .thenReturn(resp);

    final var result = controller.create(request);

    Assertions.assertNotNull(result);
    Assertions.assertEquals(HttpStatus.CREATED, result.getStatusCode());
    Assertions.assertNotNull(result.getBody());

    final BaseResponse<RestaurantsResponse> body = result.getBody();
    Assertions.assertNotNull(body.content());
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
    final RestaurantsRequest request = Mockito.mock(RestaurantsRequest.class);

    final RestaurantsInput input = Mockito.mock(RestaurantsInput.class);
    Mockito.when(mapper.toInput(request))
        .thenReturn(input);

    final RestaurantsOutput out = Mockito.mock(RestaurantsOutput.class);
    Mockito.when(updateUseCase.execute(uuid, input))
        .thenReturn(out);

    final RestaurantsResponse resp = Mockito.mock(RestaurantsResponse.class);
    Mockito.when(mapper.toResponse(out))
        .thenReturn(resp);

    final var result = controller.update(uuid, request);

    Assertions.assertNotNull(result);
    Assertions.assertEquals(HttpStatus.OK, result.getStatusCode());
    Assertions.assertNotNull(result.getBody());

    final BaseResponse<RestaurantsResponse> body = result.getBody();
    Assertions.assertNotNull(body.content());
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
