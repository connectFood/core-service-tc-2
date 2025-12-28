package com.connectfood.core.entrypoint.rest.controller;

import java.util.List;
import java.util.UUID;

import com.connectfood.core.application.dto.commons.PageOutput;
import com.connectfood.core.application.restaurantitems.dto.RestaurantItemsInput;
import com.connectfood.core.application.restaurantitems.dto.RestaurantItemsOutput;
import com.connectfood.core.application.restaurantitems.usecase.CreateRestaurantItemsUseCase;
import com.connectfood.core.application.restaurantitems.usecase.FindRestaurantItemsUseCase;
import com.connectfood.core.application.restaurantitems.usecase.RemoveRestaurantItemsUseCase;
import com.connectfood.core.application.restaurantitems.usecase.SearchRestaurantItemsUseCase;
import com.connectfood.core.application.restaurantitems.usecase.UpdateRestaurantItemsUseCase;
import com.connectfood.core.entrypoint.rest.dto.commons.BaseResponse;
import com.connectfood.core.entrypoint.rest.dto.commons.PageResponse;
import com.connectfood.core.entrypoint.rest.dto.restaurantitems.RestaurantItemsRequest;
import com.connectfood.core.entrypoint.rest.dto.restaurantitems.RestaurantItemsResponse;
import com.connectfood.core.entrypoint.rest.mappers.RestaurantItemsEntryMapper;

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
class RestaurantItemsControllerTest {

  @Mock
  private SearchRestaurantItemsUseCase searchUseCase;

  @Mock
  private FindRestaurantItemsUseCase findUseCase;

  @Mock
  private CreateRestaurantItemsUseCase createUseCase;

  @Mock
  private UpdateRestaurantItemsUseCase updateUseCase;

  @Mock
  private RemoveRestaurantItemsUseCase removeUseCase;

  @Mock
  private RestaurantItemsEntryMapper mapper;

  @InjectMocks
  private RestaurantItemsController controller;

  @Test
  @DisplayName("Deve retornar PageResponse com status 200 e dados paginados")
  void searchShouldReturnPageResponseOk() {
    final var restaurantUuid = UUID.randomUUID();
    final var page = 1;
    final var size = 10;
    final var sort = "id";
    final var direction = "ASC";

    final RestaurantItemsOutput out1 = Mockito.mock(RestaurantItemsOutput.class);
    final RestaurantItemsOutput out2 = Mockito.mock(RestaurantItemsOutput.class);

    final PageOutput<List<RestaurantItemsOutput>> pageOutput = Mockito.mock(PageOutput.class);
    Mockito.when(pageOutput.content()).thenReturn(List.of(out1, out2));
    Mockito.when(pageOutput.total()).thenReturn(25L);

    Mockito.when(searchUseCase.execute(restaurantUuid, page, size, sort, direction)).thenReturn(pageOutput);

    final RestaurantItemsResponse res1 = Mockito.mock(RestaurantItemsResponse.class);
    final RestaurantItemsResponse res2 = Mockito.mock(RestaurantItemsResponse.class);
    Mockito.when(mapper.toResponse(out1)).thenReturn(res1);
    Mockito.when(mapper.toResponse(out2)).thenReturn(res2);

    final var responseEntity = controller.search(restaurantUuid, page, size, sort, direction);

    Assertions.assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    Assertions.assertNotNull(responseEntity.getBody());

    final PageResponse<List<RestaurantItemsResponse>> body = responseEntity.getBody();
    Assertions.assertNotNull(body.content());
    Assertions.assertEquals(List.of(res1, res2), body.content());
    Assertions.assertEquals(25L, body.total());
    Assertions.assertEquals(page, body.page());
    Assertions.assertEquals(size, body.size());

    Mockito.verify(searchUseCase, Mockito.times(1)).execute(restaurantUuid, page, size, sort, direction);
    Mockito.verify(mapper, Mockito.times(1)).toResponse(out1);
    Mockito.verify(mapper, Mockito.times(1)).toResponse(out2);
    Mockito.verifyNoMoreInteractions(searchUseCase, mapper);
    Mockito.verifyNoInteractions(findUseCase, createUseCase, updateUseCase, removeUseCase);
  }

  @Test
  @DisplayName("Deve retornar BaseResponse com status 200")
  void findByUuidShouldReturnBaseResponseOk() {
    final var uuid = UUID.randomUUID();

    final RestaurantItemsOutput output = Mockito.mock(RestaurantItemsOutput.class);
    Mockito.when(findUseCase.execute(uuid)).thenReturn(output);

    final RestaurantItemsResponse response = Mockito.mock(RestaurantItemsResponse.class);
    Mockito.when(mapper.toResponse(output)).thenReturn(response);

    final var responseEntity = controller.findByUuid(uuid);

    Assertions.assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    Assertions.assertNotNull(responseEntity.getBody());

    final BaseResponse<RestaurantItemsResponse> body = responseEntity.getBody();
    Assertions.assertEquals(response, body.content());

    Mockito.verify(findUseCase, Mockito.times(1)).execute(uuid);
    Mockito.verify(mapper, Mockito.times(1)).toResponse(output);
    Mockito.verifyNoMoreInteractions(findUseCase, mapper);
    Mockito.verifyNoInteractions(searchUseCase, createUseCase, updateUseCase, removeUseCase);
  }

  @Test
  @DisplayName("Deve retornar BaseResponse com status 201")
  void createShouldReturnCreated() {
    final RestaurantItemsRequest request = Mockito.mock(RestaurantItemsRequest.class);

    final RestaurantItemsInput input = Mockito.mock(RestaurantItemsInput.class);
    Mockito.when(mapper.toInput(request)).thenReturn(input);

    final RestaurantItemsOutput output = Mockito.mock(RestaurantItemsOutput.class);
    Mockito.when(createUseCase.execute(input)).thenReturn(output);

    final RestaurantItemsResponse response = Mockito.mock(RestaurantItemsResponse.class);
    Mockito.when(mapper.toResponse(output)).thenReturn(response);

    final var responseEntity = controller.create(request);

    Assertions.assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
    Assertions.assertNotNull(responseEntity.getBody());

    final BaseResponse<RestaurantItemsResponse> body = responseEntity.getBody();
    Assertions.assertEquals(response, body.content());

    Mockito.verify(mapper, Mockito.times(1)).toInput(request);
    Mockito.verify(createUseCase, Mockito.times(1)).execute(input);
    Mockito.verify(mapper, Mockito.times(1)).toResponse(output);

    Mockito.verifyNoMoreInteractions(createUseCase, mapper);
    Mockito.verifyNoInteractions(searchUseCase, findUseCase, updateUseCase, removeUseCase);
  }

  @Test
  @DisplayName("Deve retornar BaseResponse com status 200")
  void updateShouldReturnOk() {
    final var uuid = UUID.randomUUID();
    final RestaurantItemsRequest request = Mockito.mock(RestaurantItemsRequest.class);

    final RestaurantItemsInput input = Mockito.mock(RestaurantItemsInput.class);
    Mockito.when(mapper.toInput(request)).thenReturn(input);

    final RestaurantItemsOutput output = Mockito.mock(RestaurantItemsOutput.class);
    Mockito.when(updateUseCase.execute(uuid, input)).thenReturn(output);

    final RestaurantItemsResponse response = Mockito.mock(RestaurantItemsResponse.class);
    Mockito.when(mapper.toResponse(output)).thenReturn(response);

    final var responseEntity = controller.update(uuid, request);

    Assertions.assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    Assertions.assertNotNull(responseEntity.getBody());

    final BaseResponse<RestaurantItemsResponse> body = responseEntity.getBody();
    Assertions.assertEquals(response, body.content());

    Mockito.verify(mapper, Mockito.times(1)).toInput(request);
    Mockito.verify(updateUseCase, Mockito.times(1)).execute(uuid, input);
    Mockito.verify(mapper, Mockito.times(1)).toResponse(output);

    Mockito.verifyNoMoreInteractions(updateUseCase, mapper);
    Mockito.verifyNoInteractions(searchUseCase, findUseCase, createUseCase, removeUseCase);
  }

  @Test
  @DisplayName("Deve retornar 204 e executar remoção")
  void deleteShouldReturnNoContent() {
    final var uuid = UUID.randomUUID();

    final var responseEntity = controller.delete(uuid);

    Assertions.assertEquals(HttpStatus.NO_CONTENT, responseEntity.getStatusCode());
    Assertions.assertNull(responseEntity.getBody());

    Mockito.verify(removeUseCase, Mockito.times(1)).execute(uuid);
    Mockito.verifyNoMoreInteractions(removeUseCase);
    Mockito.verifyNoInteractions(searchUseCase, findUseCase, createUseCase, updateUseCase, mapper);
  }
}
