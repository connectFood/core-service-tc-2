package com.connectfood.infrastructure.rest.controller;

import java.util.List;
import java.util.UUID;

import com.connectfood.core.application.dto.commons.PageOutput;
import com.connectfood.core.application.restaurantstype.dto.RestaurantsTypeInput;
import com.connectfood.core.application.restaurantstype.dto.RestaurantsTypeOutput;
import com.connectfood.core.application.restaurantstype.usecase.CreateRestaurantTypeUseCase;
import com.connectfood.core.application.restaurantstype.usecase.FindRestaurantTypeUseCase;
import com.connectfood.core.application.restaurantstype.usecase.RemoveRestaurantTypeUseCase;
import com.connectfood.core.application.restaurantstype.usecase.SearchRestaurantTypeUseCase;
import com.connectfood.core.application.restaurantstype.usecase.UpdateRestaurantTypeUseCase;
import com.connectfood.infrastructure.rest.dto.commons.BaseResponse;
import com.connectfood.infrastructure.rest.dto.commons.PageResponse;
import com.connectfood.infrastructure.rest.dto.restaurantstype.RestaurantsTypeRequest;
import com.connectfood.infrastructure.rest.dto.restaurantstype.RestaurantsTypeResponse;
import com.connectfood.infrastructure.rest.mappers.RestaurantsTypeEntryMapper;
import com.connectfood.infrastructure.rest.controller.RestaurantsTypeController;

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
class RestaurantsTypeControllerTest {

  @Mock
  private SearchRestaurantTypeUseCase searchUseCase;

  @Mock
  private FindRestaurantTypeUseCase findUseCase;

  @Mock
  private CreateRestaurantTypeUseCase createUseCase;

  @Mock
  private UpdateRestaurantTypeUseCase updateUseCase;

  @Mock
  private RemoveRestaurantTypeUseCase removeUseCase;

  @Mock
  private RestaurantsTypeEntryMapper mapper;

  @InjectMocks
  private RestaurantsTypeController controller;

  @Test
  @DisplayName("Deve retornar status 200 e PageResponse com conteúdo mapeado quando houver resultados")
  void shouldReturnOkWithPageResponseWhenHasResults() {
    final var name = "pizza";
    final var page = 1;
    final var size = 10;
    final var sort = "name";
    final var direction = "ASC";

    final RestaurantsTypeOutput out1 = Mockito.mock(RestaurantsTypeOutput.class);
    final RestaurantsTypeOutput out2 = Mockito.mock(RestaurantsTypeOutput.class);

    final var useCaseResult = new PageOutput<>(List.of(out1, out2), 2L);

    Mockito.when(searchUseCase.execute(name, page, size, sort, direction))
        .thenReturn(useCaseResult);

    final RestaurantsTypeResponse resp1 = Mockito.mock(RestaurantsTypeResponse.class);
    final RestaurantsTypeResponse resp2 = Mockito.mock(RestaurantsTypeResponse.class);

    Mockito.when(mapper.toResponse(out1))
        .thenReturn(resp1);
    Mockito.when(mapper.toResponse(out2))
        .thenReturn(resp2);

    final var result = controller.search(name, page, size, sort, direction);

    Assertions.assertNotNull(result);
    Assertions.assertEquals(HttpStatus.OK, result.getStatusCode());
    Assertions.assertNotNull(result.getBody());

    final PageResponse<List<RestaurantsTypeResponse>> body = result.getBody();

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
    final var name = "burger";
    final var page = 0;
    final var size = 10;
    final String sort = null;
    final String direction = null;

    final PageOutput<List<RestaurantsTypeOutput>> useCaseResult = new PageOutput<>(List.of(), 0L);

    Mockito.when(searchUseCase.execute(name, page, size, sort, direction))
        .thenReturn(useCaseResult);

    final var result = controller.search(name, page, size, sort, direction);

    Assertions.assertNotNull(result);
    Assertions.assertEquals(HttpStatus.OK, result.getStatusCode());
    Assertions.assertNotNull(result.getBody());

    final PageResponse<List<RestaurantsTypeResponse>> body = result.getBody();

    Assertions.assertNotNull(body.content());
    Assertions.assertTrue(body.content()
        .isEmpty());
    Assertions.assertEquals(0L, body.total());
    Assertions.assertEquals(page, body.page());
    Assertions.assertEquals(size, body.size());

    Mockito.verify(searchUseCase, Mockito.times(1))
        .execute(name, page, size, sort, direction);
    Mockito.verifyNoInteractions(mapper);
    Mockito.verifyNoMoreInteractions(searchUseCase);
    Mockito.verifyNoInteractions(findUseCase, createUseCase, updateUseCase, removeUseCase);
  }

  @Test
  @DisplayName("Deve retornar status 200 e BaseResponse quando buscar por uuid")
  void shouldReturnOkWithBaseResponseWhenFindByUuid() {
    final var uuid = UUID.randomUUID();

    final RestaurantsTypeOutput out = Mockito.mock(RestaurantsTypeOutput.class);
    Mockito.when(findUseCase.execute(uuid))
        .thenReturn(out);

    final RestaurantsTypeResponse resp = Mockito.mock(RestaurantsTypeResponse.class);
    Mockito.when(mapper.toResponse(out))
        .thenReturn(resp);

    final var result = controller.find(uuid);

    Assertions.assertNotNull(result);
    Assertions.assertEquals(HttpStatus.OK, result.getStatusCode());
    Assertions.assertNotNull(result.getBody());

    final BaseResponse<RestaurantsTypeResponse> body = result.getBody();
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
    final RestaurantsTypeRequest request = Mockito.mock(RestaurantsTypeRequest.class);

    final RestaurantsTypeInput input = Mockito.mock(RestaurantsTypeInput.class);
    Mockito.when(mapper.toInput(request))
        .thenReturn(input);

    final RestaurantsTypeOutput out = Mockito.mock(RestaurantsTypeOutput.class);
    Mockito.when(createUseCase.execute(input))
        .thenReturn(out);

    final RestaurantsTypeResponse resp = Mockito.mock(RestaurantsTypeResponse.class);
    Mockito.when(mapper.toResponse(out))
        .thenReturn(resp);

    final var result = controller.create(request);

    Assertions.assertNotNull(result);
    Assertions.assertEquals(HttpStatus.CREATED, result.getStatusCode());
    Assertions.assertNotNull(result.getBody());

    final BaseResponse<RestaurantsTypeResponse> body = result.getBody();
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
    final RestaurantsTypeRequest request = Mockito.mock(RestaurantsTypeRequest.class);

    final RestaurantsTypeInput input = Mockito.mock(RestaurantsTypeInput.class);
    Mockito.when(mapper.toInput(request))
        .thenReturn(input);

    final RestaurantsTypeOutput out = Mockito.mock(RestaurantsTypeOutput.class);
    Mockito.when(updateUseCase.execute(uuid, input))
        .thenReturn(out);

    final RestaurantsTypeResponse resp = Mockito.mock(RestaurantsTypeResponse.class);
    Mockito.when(mapper.toResponse(out))
        .thenReturn(resp);

    final var result = controller.update(uuid, request);

    Assertions.assertNotNull(result);
    Assertions.assertEquals(HttpStatus.OK, result.getStatusCode());
    Assertions.assertNotNull(result.getBody());

    final BaseResponse<RestaurantsTypeResponse> body = result.getBody();
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
