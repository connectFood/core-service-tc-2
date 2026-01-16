package com.connectfood.infrastructure.rest.controller;

import java.util.List;
import java.util.UUID;

import com.connectfood.core.application.dto.commons.PageOutput;
import com.connectfood.core.application.restaurants.dto.RestaurantOpeningHoursInput;
import com.connectfood.core.application.restaurants.dto.RestaurantOpeningHoursOutput;
import com.connectfood.core.application.restaurants.dto.RestaurantsInput;
import com.connectfood.core.application.restaurants.dto.RestaurantsOutput;
import com.connectfood.core.application.restaurants.usecase.CreateRestaurantOpeningHoursUseCase;
import com.connectfood.core.application.restaurants.usecase.CreateRestaurantsAddressUseCase;
import com.connectfood.core.application.restaurants.usecase.CreateRestaurantsUseCase;
import com.connectfood.core.application.restaurants.usecase.FindRestaurantsUseCase;
import com.connectfood.core.application.restaurants.usecase.RemoveRestaurantOpeningHoursUseCase;
import com.connectfood.core.application.restaurants.usecase.RemoveRestaurantsAddressUseCase;
import com.connectfood.core.application.restaurants.usecase.RemoveRestaurantsUseCase;
import com.connectfood.core.application.restaurants.usecase.SearchRestaurantsUseCase;
import com.connectfood.core.application.restaurants.usecase.UpdateRestaurantOpeningHoursUseCase;
import com.connectfood.core.application.restaurants.usecase.UpdateRestaurantsAddressUseCase;
import com.connectfood.core.application.restaurants.usecase.UpdateRestaurantsUseCase;
import com.connectfood.core.application.security.RequestUser;
import com.connectfood.infrastructure.rest.dto.address.AddressRequest;
import com.connectfood.infrastructure.rest.dto.address.AddressResponse;
import com.connectfood.infrastructure.rest.dto.commons.BaseResponse;
import com.connectfood.infrastructure.rest.dto.commons.PageResponse;
import com.connectfood.infrastructure.rest.dto.restaurantopeninghours.RestaurantOpeningHoursRequest;
import com.connectfood.infrastructure.rest.dto.restaurantopeninghours.RestaurantOpeningHoursResponse;
import com.connectfood.infrastructure.rest.dto.restaurants.RestaurantsRequest;
import com.connectfood.infrastructure.rest.dto.restaurants.RestaurantsResponse;
import com.connectfood.infrastructure.rest.mappers.AddressEntryMapper;
import com.connectfood.infrastructure.rest.mappers.RestaurantOpeningHoursEntryMapper;
import com.connectfood.infrastructure.rest.mappers.RestaurantsEntryMapper;
import com.connectfood.infrastructure.rest.controller.RestaurantsController;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
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

  @Mock
  private CreateRestaurantOpeningHoursUseCase createRestaurantOpeningHoursUseCase;

  @Mock
  private UpdateRestaurantOpeningHoursUseCase updateRestaurantOpeningHoursUseCase;

  @Mock
  private RemoveRestaurantOpeningHoursUseCase removeRestaurantOpeningHoursUseCase;

  @Mock
  private RestaurantOpeningHoursEntryMapper restaurantOpeningHoursMapper;

  @Mock
  private CreateRestaurantsAddressUseCase createRestaurantsAddressUseCase;

  @Mock
  private UpdateRestaurantsAddressUseCase updateRestaurantsAddressUseCase;

  @Mock
  private RemoveRestaurantsAddressUseCase removeRestaurantsAddressUseCase;

  @Mock
  private AddressEntryMapper addressMapper;

  @InjectMocks
  private RestaurantsController controller;

  @Test
  @DisplayName("Deve retornar status 200 e PageResponse com conteúdo mapeado quando houver resultados")
  void shouldReturnOkWithPageResponseWhenHasResults() {
    final var name = "pizza";
    final var restaurantsTypeUuid = UUID.randomUUID();
    final var street = "Rua A";
    final var city = "São Paulo";
    final var state = "SP";
    final var page = 1;
    final var size = 10;
    final var sort = "name";
    final var direction = "ASC";

    final RestaurantsOutput out1 = Mockito.mock(RestaurantsOutput.class);
    final RestaurantsOutput out2 = Mockito.mock(RestaurantsOutput.class);

    Mockito.when(searchUseCase.execute(name, restaurantsTypeUuid, street, city, state, page, size, sort, direction))
        .thenReturn(new PageOutput<>(List.of(out1, out2), 2L));

    final RestaurantsResponse resp1 = Mockito.mock(RestaurantsResponse.class);
    final RestaurantsResponse resp2 = Mockito.mock(RestaurantsResponse.class);

    Mockito.when(mapper.toResponse(out1))
        .thenReturn(resp1);
    Mockito.when(mapper.toResponse(out2))
        .thenReturn(resp2);

    final var result = controller.search(name, restaurantsTypeUuid, street, city, state, page, size, sort, direction);

    Assertions.assertNotNull(result);
    Assertions.assertEquals(HttpStatus.OK, result.getStatusCode());
    Assertions.assertNotNull(result.getBody());

    final PageResponse<List<RestaurantsResponse>> body = result.getBody();
    Assertions.assertEquals(List.of(resp1, resp2), body.content());
    Assertions.assertEquals(2L, body.total());
    Assertions.assertEquals(page, body.page());
    Assertions.assertEquals(size, body.size());

    Mockito.verify(searchUseCase, Mockito.times(1))
        .execute(name, restaurantsTypeUuid, street, city, state, page, size, sort, direction);
    Mockito.verify(mapper, Mockito.times(1))
        .toResponse(out1);
    Mockito.verify(mapper, Mockito.times(1))
        .toResponse(out2);

    Mockito.verifyNoMoreInteractions(searchUseCase, mapper);
    Mockito.verifyNoInteractions(
        findUseCase,
        createUseCase,
        updateUseCase,
        removeUseCase,
        createRestaurantOpeningHoursUseCase,
        updateRestaurantOpeningHoursUseCase,
        removeRestaurantOpeningHoursUseCase,
        restaurantOpeningHoursMapper,
        createRestaurantsAddressUseCase,
        updateRestaurantsAddressUseCase,
        removeRestaurantsAddressUseCase,
        addressMapper
    );
  }

  @Test
  @DisplayName("Deve retornar status 200 e PageResponse vazio quando não houver resultados")
  void shouldReturnOkWithEmptyPageResponseWhenNoResults() {
    final String name = null;
    final UUID restaurantsTypeUuid = null;
    final String street = null;
    final String city = null;
    final String state = null;
    final var page = 0;
    final var size = 10;
    final String sort = null;
    final String direction = null;

    Mockito.when(searchUseCase.execute(name, restaurantsTypeUuid, street, city, state, page, size, sort, direction))
        .thenReturn(new PageOutput<>(List.of(), 0L));

    final var result = controller.search(name, restaurantsTypeUuid, street, city, state, page, size, sort, direction);

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
        .execute(name, restaurantsTypeUuid, street, city, state, page, size, sort, direction);

    Mockito.verifyNoMoreInteractions(searchUseCase);
    Mockito.verifyNoInteractions(
        mapper,
        findUseCase,
        createUseCase,
        updateUseCase,
        removeUseCase,
        createRestaurantOpeningHoursUseCase,
        updateRestaurantOpeningHoursUseCase,
        removeRestaurantOpeningHoursUseCase,
        restaurantOpeningHoursMapper,
        createRestaurantsAddressUseCase,
        updateRestaurantsAddressUseCase,
        removeRestaurantsAddressUseCase,
        addressMapper
    );
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
    Assertions.assertSame(resp, body.content());

    Mockito.verify(findUseCase, Mockito.times(1))
        .execute(uuid);
    Mockito.verify(mapper, Mockito.times(1))
        .toResponse(out);

    Mockito.verifyNoMoreInteractions(findUseCase, mapper);
    Mockito.verifyNoInteractions(
        searchUseCase,
        createUseCase,
        updateUseCase,
        removeUseCase,
        createRestaurantOpeningHoursUseCase,
        updateRestaurantOpeningHoursUseCase,
        removeRestaurantOpeningHoursUseCase,
        restaurantOpeningHoursMapper,
        createRestaurantsAddressUseCase,
        updateRestaurantsAddressUseCase,
        removeRestaurantsAddressUseCase,
        addressMapper
    );
  }

  @Test
  @DisplayName("Deve retornar status 201 e BaseResponse quando criar restaurante (com header Request-User-Uuid)")
  void shouldReturnCreatedWithBaseResponseWhenCreate() {
    final var requestUserUuid = UUID.randomUUID();
    final RestaurantsRequest request = Mockito.mock(RestaurantsRequest.class);

    final RestaurantsInput input = Mockito.mock(RestaurantsInput.class);
    Mockito.when(mapper.toInput(request))
        .thenReturn(input);

    final RestaurantsOutput out = Mockito.mock(RestaurantsOutput.class);
    Mockito.when(createUseCase.execute(Mockito.any(RequestUser.class), Mockito.eq(input)))
        .thenReturn(out);

    final RestaurantsResponse resp = Mockito.mock(RestaurantsResponse.class);
    Mockito.when(mapper.toResponse(out))
        .thenReturn(resp);

    final var result = controller.create(requestUserUuid, request);

    Assertions.assertNotNull(result);
    Assertions.assertEquals(HttpStatus.CREATED, result.getStatusCode());
    Assertions.assertNotNull(result.getBody());
    Assertions.assertSame(resp, result.getBody()
        .content()
    );

    final var requestUserCaptor = ArgumentCaptor.forClass(RequestUser.class);
    Mockito.verify(createUseCase, Mockito.times(1))
        .execute(requestUserCaptor.capture(), Mockito.eq(input));
    Assertions.assertEquals(requestUserUuid, requestUserCaptor.getValue()
        .uuid()
    );

    Mockito.verify(mapper, Mockito.times(1))
        .toInput(request);
    Mockito.verify(mapper, Mockito.times(1))
        .toResponse(out);

    Mockito.verifyNoMoreInteractions(createUseCase, mapper);
    Mockito.verifyNoInteractions(
        searchUseCase,
        findUseCase,
        updateUseCase,
        removeUseCase,
        createRestaurantOpeningHoursUseCase,
        updateRestaurantOpeningHoursUseCase,
        removeRestaurantOpeningHoursUseCase,
        restaurantOpeningHoursMapper,
        createRestaurantsAddressUseCase,
        updateRestaurantsAddressUseCase,
        removeRestaurantsAddressUseCase,
        addressMapper
    );
  }

  @Test
  @DisplayName("Deve retornar status 200 e BaseResponse quando atualizar restaurante (com header Request-User-Uuid)")
  void shouldReturnOkWithBaseResponseWhenUpdate() {
    final var requestUserUuid = UUID.randomUUID();
    final var uuid = UUID.randomUUID();
    final RestaurantsRequest request = Mockito.mock(RestaurantsRequest.class);

    final RestaurantsInput input = Mockito.mock(RestaurantsInput.class);
    Mockito.when(mapper.toInput(request))
        .thenReturn(input);

    final RestaurantsOutput out = Mockito.mock(RestaurantsOutput.class);
    Mockito.when(updateUseCase.execute(Mockito.any(RequestUser.class), Mockito.eq(uuid), Mockito.eq(input)))
        .thenReturn(out);

    final RestaurantsResponse resp = Mockito.mock(RestaurantsResponse.class);
    Mockito.when(mapper.toResponse(out))
        .thenReturn(resp);

    final var result = controller.update(requestUserUuid, uuid, request);

    Assertions.assertNotNull(result);
    Assertions.assertEquals(HttpStatus.OK, result.getStatusCode());
    Assertions.assertNotNull(result.getBody());
    Assertions.assertSame(resp, result.getBody()
        .content()
    );

    final var requestUserCaptor = ArgumentCaptor.forClass(RequestUser.class);
    Mockito.verify(updateUseCase, Mockito.times(1))
        .execute(requestUserCaptor.capture(), Mockito.eq(uuid), Mockito.eq(input));
    Assertions.assertEquals(requestUserUuid, requestUserCaptor.getValue()
        .uuid()
    );

    Mockito.verify(mapper, Mockito.times(1))
        .toInput(request);
    Mockito.verify(mapper, Mockito.times(1))
        .toResponse(out);

    Mockito.verifyNoMoreInteractions(updateUseCase, mapper);
    Mockito.verifyNoInteractions(
        searchUseCase,
        findUseCase,
        createUseCase,
        removeUseCase,
        createRestaurantOpeningHoursUseCase,
        updateRestaurantOpeningHoursUseCase,
        removeRestaurantOpeningHoursUseCase,
        restaurantOpeningHoursMapper,
        createRestaurantsAddressUseCase,
        updateRestaurantsAddressUseCase,
        removeRestaurantsAddressUseCase,
        addressMapper
    );
  }

  @Test
  @DisplayName("Deve retornar status 204 quando remover restaurante (com header Request-User-Uuid)")
  void shouldReturnNoContentWhenRemove() {
    final var requestUserUuid = UUID.randomUUID();
    final var uuid = UUID.randomUUID();

    final var result = controller.delete(requestUserUuid, uuid);

    Assertions.assertNotNull(result);
    Assertions.assertEquals(HttpStatus.NO_CONTENT, result.getStatusCode());
    Assertions.assertNull(result.getBody());

    final var requestUserCaptor = ArgumentCaptor.forClass(RequestUser.class);
    Mockito.verify(removeUseCase, Mockito.times(1))
        .execute(requestUserCaptor.capture(), Mockito.eq(uuid));
    Assertions.assertEquals(requestUserUuid, requestUserCaptor.getValue()
        .uuid()
    );

    Mockito.verifyNoMoreInteractions(removeUseCase);
    Mockito.verifyNoInteractions(
        searchUseCase,
        findUseCase,
        createUseCase,
        updateUseCase,
        mapper,
        createRestaurantOpeningHoursUseCase,
        updateRestaurantOpeningHoursUseCase,
        removeRestaurantOpeningHoursUseCase,
        restaurantOpeningHoursMapper,
        createRestaurantsAddressUseCase,
        updateRestaurantsAddressUseCase,
        removeRestaurantsAddressUseCase,
        addressMapper
    );
  }

  @Test
  @DisplayName("Deve retornar status 201 e BaseResponse quando criar opening hours (com header Request-User-Uuid)")
  void shouldReturnCreatedWithBaseResponseWhenCreateOpeningHours() {
    final var requestUserUuid = UUID.randomUUID();
    final var restaurantUuid = UUID.randomUUID();
    final RestaurantOpeningHoursRequest request = Mockito.mock(RestaurantOpeningHoursRequest.class);

    final RestaurantOpeningHoursInput input = Mockito.mock(RestaurantOpeningHoursInput.class);
    Mockito.when(restaurantOpeningHoursMapper.toInput(request))
        .thenReturn(input);

    final RestaurantOpeningHoursOutput out = Mockito.mock(RestaurantOpeningHoursOutput.class);
    Mockito.when(createRestaurantOpeningHoursUseCase.execute(Mockito.any(RequestUser.class), Mockito.eq(restaurantUuid),
            Mockito.eq(input)
        ))
        .thenReturn(out);

    final RestaurantOpeningHoursResponse resp = Mockito.mock(RestaurantOpeningHoursResponse.class);
    Mockito.when(restaurantOpeningHoursMapper.toResponse(out))
        .thenReturn(resp);

    final var result = controller.createOpeningHours(requestUserUuid, restaurantUuid, request);

    Assertions.assertNotNull(result);
    Assertions.assertEquals(HttpStatus.CREATED, result.getStatusCode());
    Assertions.assertNotNull(result.getBody());
    Assertions.assertSame(resp, result.getBody()
        .content()
    );

    final var requestUserCaptor = ArgumentCaptor.forClass(RequestUser.class);
    Mockito.verify(createRestaurantOpeningHoursUseCase, Mockito.times(1))
        .execute(requestUserCaptor.capture(), Mockito.eq(restaurantUuid), Mockito.eq(input));
    Assertions.assertEquals(requestUserUuid, requestUserCaptor.getValue()
        .uuid()
    );

    Mockito.verify(restaurantOpeningHoursMapper, Mockito.times(1))
        .toInput(request);
    Mockito.verify(restaurantOpeningHoursMapper, Mockito.times(1))
        .toResponse(out);

    Mockito.verifyNoMoreInteractions(createRestaurantOpeningHoursUseCase, restaurantOpeningHoursMapper);
    Mockito.verifyNoInteractions(
        searchUseCase,
        findUseCase,
        createUseCase,
        updateUseCase,
        removeUseCase,
        mapper,
        updateRestaurantOpeningHoursUseCase,
        removeRestaurantOpeningHoursUseCase,
        createRestaurantsAddressUseCase,
        updateRestaurantsAddressUseCase,
        removeRestaurantsAddressUseCase,
        addressMapper
    );
  }

  @Test
  @DisplayName("Deve retornar status 200 e BaseResponse quando atualizar opening hours (com header Request-User-Uuid)")
  void shouldReturnOkWithBaseResponseWhenUpdateOpeningHours() {
    final var requestUserUuid = UUID.randomUUID();
    final var openingHoursUuid = UUID.randomUUID();
    final RestaurantOpeningHoursRequest request = Mockito.mock(RestaurantOpeningHoursRequest.class);

    final RestaurantOpeningHoursInput input = Mockito.mock(RestaurantOpeningHoursInput.class);
    Mockito.when(restaurantOpeningHoursMapper.toInput(request))
        .thenReturn(input);

    final RestaurantOpeningHoursOutput out = Mockito.mock(RestaurantOpeningHoursOutput.class);
    Mockito.when(
            updateRestaurantOpeningHoursUseCase.execute(Mockito.any(RequestUser.class), Mockito.eq(openingHoursUuid),
                Mockito.eq(input)
            ))
        .thenReturn(out);

    final RestaurantOpeningHoursResponse resp = Mockito.mock(RestaurantOpeningHoursResponse.class);
    Mockito.when(restaurantOpeningHoursMapper.toResponse(out))
        .thenReturn(resp);

    final var result = controller.update(requestUserUuid, openingHoursUuid, request);

    Assertions.assertNotNull(result);
    Assertions.assertEquals(HttpStatus.OK, result.getStatusCode());
    Assertions.assertNotNull(result.getBody());
    Assertions.assertSame(resp, result.getBody()
        .content()
    );

    final var requestUserCaptor = ArgumentCaptor.forClass(RequestUser.class);
    Mockito.verify(updateRestaurantOpeningHoursUseCase, Mockito.times(1))
        .execute(requestUserCaptor.capture(), Mockito.eq(openingHoursUuid), Mockito.eq(input));
    Assertions.assertEquals(requestUserUuid, requestUserCaptor.getValue()
        .uuid()
    );

    Mockito.verify(restaurantOpeningHoursMapper, Mockito.times(1))
        .toInput(request);
    Mockito.verify(restaurantOpeningHoursMapper, Mockito.times(1))
        .toResponse(out);

    Mockito.verifyNoMoreInteractions(updateRestaurantOpeningHoursUseCase, restaurantOpeningHoursMapper);
    Mockito.verifyNoInteractions(
        searchUseCase,
        findUseCase,
        createUseCase,
        updateUseCase,
        removeUseCase,
        mapper,
        createRestaurantOpeningHoursUseCase,
        removeRestaurantOpeningHoursUseCase,
        createRestaurantsAddressUseCase,
        updateRestaurantsAddressUseCase,
        removeRestaurantsAddressUseCase,
        addressMapper
    );
  }

  @Test
  @DisplayName("Deve retornar status 204 quando remover opening hours (com header Request-User-Uuid)")
  void shouldReturnNoContentWhenDeleteOpeningHours() {
    final var requestUserUuid = UUID.randomUUID();
    final var openingHoursUuid = UUID.randomUUID();

    final var result = controller.deleteOpeningHoursUuid(requestUserUuid, openingHoursUuid);

    Assertions.assertNotNull(result);
    Assertions.assertEquals(HttpStatus.NO_CONTENT, result.getStatusCode());
    Assertions.assertNull(result.getBody());

    final var requestUserCaptor = ArgumentCaptor.forClass(RequestUser.class);
    Mockito.verify(removeRestaurantOpeningHoursUseCase, Mockito.times(1))
        .execute(requestUserCaptor.capture(), Mockito.eq(openingHoursUuid));
    Assertions.assertEquals(requestUserUuid, requestUserCaptor.getValue()
        .uuid()
    );

    Mockito.verifyNoMoreInteractions(removeRestaurantOpeningHoursUseCase);
    Mockito.verifyNoInteractions(
        searchUseCase,
        findUseCase,
        createUseCase,
        updateUseCase,
        removeUseCase,
        mapper,
        createRestaurantOpeningHoursUseCase,
        updateRestaurantOpeningHoursUseCase,
        restaurantOpeningHoursMapper,
        createRestaurantsAddressUseCase,
        updateRestaurantsAddressUseCase,
        removeRestaurantsAddressUseCase,
        addressMapper
    );
  }

  @Test
  @DisplayName("Deve retornar status 201 e BaseResponse quando criar address (com header Request-User-Uuid)")
  void shouldReturnCreatedWithBaseResponseWhenCreateAddress() {
    final var requestUserUuid = UUID.randomUUID();
    final var restaurantUuid = UUID.randomUUID();
    final AddressRequest request = Mockito.mock(AddressRequest.class);

    final var addressInput = Mockito.mock(com.connectfood.core.application.address.dto.AddressInput.class);
    Mockito.when(addressMapper.toInput(request))
        .thenReturn(addressInput);

    final var addressOutput = Mockito.mock(com.connectfood.core.application.address.dto.AddressOutput.class);
    Mockito.when(createRestaurantsAddressUseCase.execute(Mockito.any(RequestUser.class), Mockito.eq(restaurantUuid),
            Mockito.eq(addressInput)
        ))
        .thenReturn(addressOutput);

    final AddressResponse response = Mockito.mock(AddressResponse.class);
    Mockito.when(addressMapper.toResponse(addressOutput))
        .thenReturn(response);

    final var result = controller.createAddress(requestUserUuid, restaurantUuid, request);

    Assertions.assertNotNull(result);
    Assertions.assertEquals(HttpStatus.CREATED, result.getStatusCode());
    Assertions.assertNotNull(result.getBody());
    Assertions.assertSame(response, result.getBody()
        .content()
    );

    final var requestUserCaptor = ArgumentCaptor.forClass(RequestUser.class);
    Mockito.verify(createRestaurantsAddressUseCase, Mockito.times(1))
        .execute(requestUserCaptor.capture(), Mockito.eq(restaurantUuid), Mockito.eq(addressInput));
    Assertions.assertEquals(requestUserUuid, requestUserCaptor.getValue()
        .uuid()
    );

    Mockito.verify(addressMapper, Mockito.times(1))
        .toInput(request);
    Mockito.verify(addressMapper, Mockito.times(1))
        .toResponse(addressOutput);

    Mockito.verifyNoMoreInteractions(createRestaurantsAddressUseCase, addressMapper);
    Mockito.verifyNoInteractions(
        searchUseCase,
        findUseCase,
        createUseCase,
        updateUseCase,
        removeUseCase,
        mapper,
        createRestaurantOpeningHoursUseCase,
        updateRestaurantOpeningHoursUseCase,
        removeRestaurantOpeningHoursUseCase,
        restaurantOpeningHoursMapper,
        updateRestaurantsAddressUseCase,
        removeRestaurantsAddressUseCase
    );
  }

  @Test
  @DisplayName("Deve retornar status 200 e BaseResponse quando atualizar address (com header Request-User-Uuid)")
  void shouldReturnOkWithBaseResponseWhenUpdateAddress() {
    final var requestUserUuid = UUID.randomUUID();
    final var restaurantUuid = UUID.randomUUID();
    final AddressRequest request = Mockito.mock(AddressRequest.class);

    final var addressInput = Mockito.mock(com.connectfood.core.application.address.dto.AddressInput.class);
    Mockito.when(addressMapper.toInput(request))
        .thenReturn(addressInput);

    final var addressOutput = Mockito.mock(com.connectfood.core.application.address.dto.AddressOutput.class);
    Mockito.when(updateRestaurantsAddressUseCase.execute(Mockito.any(RequestUser.class), Mockito.eq(restaurantUuid),
            Mockito.eq(addressInput)
        ))
        .thenReturn(addressOutput);

    final AddressResponse response = Mockito.mock(AddressResponse.class);
    Mockito.when(addressMapper.toResponse(addressOutput))
        .thenReturn(response);

    final var result = controller.updateAddress(requestUserUuid, restaurantUuid, request);

    Assertions.assertNotNull(result);
    Assertions.assertEquals(HttpStatus.OK, result.getStatusCode());
    Assertions.assertNotNull(result.getBody());
    Assertions.assertSame(response, result.getBody()
        .content()
    );

    final var requestUserCaptor = ArgumentCaptor.forClass(RequestUser.class);
    Mockito.verify(updateRestaurantsAddressUseCase, Mockito.times(1))
        .execute(requestUserCaptor.capture(), Mockito.eq(restaurantUuid), Mockito.eq(addressInput));
    Assertions.assertEquals(requestUserUuid, requestUserCaptor.getValue()
        .uuid()
    );

    Mockito.verify(addressMapper, Mockito.times(1))
        .toInput(request);
    Mockito.verify(addressMapper, Mockito.times(1))
        .toResponse(addressOutput);

    Mockito.verifyNoMoreInteractions(updateRestaurantsAddressUseCase, addressMapper);
    Mockito.verifyNoInteractions(
        searchUseCase,
        findUseCase,
        createUseCase,
        updateUseCase,
        removeUseCase,
        mapper,
        createRestaurantOpeningHoursUseCase,
        updateRestaurantOpeningHoursUseCase,
        removeRestaurantOpeningHoursUseCase,
        restaurantOpeningHoursMapper,
        createRestaurantsAddressUseCase,
        removeRestaurantsAddressUseCase
    );
  }

  @Test
  @DisplayName("Deve retornar status 204 quando remover address (com header Request-User-Uuid)")
  void shouldReturnNoContentWhenDeleteAddress() {
    final var requestUserUuid = UUID.randomUUID();
    final var restaurantUuid = UUID.randomUUID();

    final var result = controller.deleteAddress(requestUserUuid, restaurantUuid);

    Assertions.assertNotNull(result);
    Assertions.assertEquals(HttpStatus.NO_CONTENT, result.getStatusCode());
    Assertions.assertNull(result.getBody());

    final var requestUserCaptor = ArgumentCaptor.forClass(RequestUser.class);
    Mockito.verify(removeRestaurantsAddressUseCase, Mockito.times(1))
        .execute(requestUserCaptor.capture(), Mockito.eq(restaurantUuid));
    Assertions.assertEquals(requestUserUuid, requestUserCaptor.getValue()
        .uuid()
    );

    Mockito.verifyNoMoreInteractions(removeRestaurantsAddressUseCase);
    Mockito.verifyNoInteractions(
        searchUseCase,
        findUseCase,
        createUseCase,
        updateUseCase,
        removeUseCase,
        mapper,
        createRestaurantOpeningHoursUseCase,
        updateRestaurantOpeningHoursUseCase,
        removeRestaurantOpeningHoursUseCase,
        restaurantOpeningHoursMapper,
        createRestaurantsAddressUseCase,
        updateRestaurantsAddressUseCase,
        addressMapper
    );
  }
}
