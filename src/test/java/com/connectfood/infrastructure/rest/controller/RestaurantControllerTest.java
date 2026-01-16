package com.connectfood.infrastructure.rest.controller;

import java.util.List;
import java.util.UUID;

import com.connectfood.core.application.dto.commons.PageOutput;
import com.connectfood.core.application.restaurant.dto.RestaurantOpeningHourInput;
import com.connectfood.core.application.restaurant.dto.RestaurantOpeningHourOutput;
import com.connectfood.core.application.restaurant.dto.RestaurantInput;
import com.connectfood.core.application.restaurant.dto.RestaurantOutput;
import com.connectfood.core.application.restaurant.usecase.CreateRestaurantOpeningHourUseCase;
import com.connectfood.core.application.restaurant.usecase.CreateRestaurantAddressUseCase;
import com.connectfood.core.application.restaurant.usecase.CreateRestaurantUseCase;
import com.connectfood.core.application.restaurant.usecase.FindRestaurantUseCase;
import com.connectfood.core.application.restaurant.usecase.RemoveRestaurantOpeningHourUseCase;
import com.connectfood.core.application.restaurant.usecase.RemoveRestaurantAddressUseCase;
import com.connectfood.core.application.restaurant.usecase.RemoveRestaurantUseCase;
import com.connectfood.core.application.restaurant.usecase.SearchRestaurantsUseCase;
import com.connectfood.core.application.restaurant.usecase.UpdateRestaurantOpeningHourUseCase;
import com.connectfood.core.application.restaurant.usecase.UpdateRestaurantAddressUseCase;
import com.connectfood.core.application.restaurant.usecase.UpdateRestaurantUseCase;
import com.connectfood.core.application.security.RequestUser;
import com.connectfood.infrastructure.rest.dto.address.AddressRequest;
import com.connectfood.infrastructure.rest.dto.address.AddressResponse;
import com.connectfood.infrastructure.rest.dto.commons.BaseResponse;
import com.connectfood.infrastructure.rest.dto.commons.PageResponse;
import com.connectfood.infrastructure.rest.dto.restaurantopeninghour.RestaurantOpeningHourRequest;
import com.connectfood.infrastructure.rest.dto.restaurantopeninghour.RestaurantOpeningHourResponse;
import com.connectfood.infrastructure.rest.dto.restaurant.RestaurantRequest;
import com.connectfood.infrastructure.rest.dto.restaurant.RestaurantResponse;
import com.connectfood.infrastructure.rest.mappers.AddressEntryMapper;
import com.connectfood.infrastructure.rest.mappers.RestaurantOpeningHourEntryMapper;
import com.connectfood.infrastructure.rest.mappers.RestaurantEntryMapper;

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
class RestaurantControllerTest {

  @Mock
  private SearchRestaurantsUseCase searchUseCase;

  @Mock
  private FindRestaurantUseCase findUseCase;

  @Mock
  private CreateRestaurantUseCase createUseCase;

  @Mock
  private UpdateRestaurantUseCase updateUseCase;

  @Mock
  private RemoveRestaurantUseCase removeUseCase;

  @Mock
  private RestaurantEntryMapper mapper;

  @Mock
  private CreateRestaurantOpeningHourUseCase createRestaurantOpeningHourUseCase;

  @Mock
  private UpdateRestaurantOpeningHourUseCase updateRestaurantOpeningHourUseCase;

  @Mock
  private RemoveRestaurantOpeningHourUseCase removeRestaurantOpeningHourUseCase;

  @Mock
  private RestaurantOpeningHourEntryMapper restaurantOpeningHoursMapper;

  @Mock
  private CreateRestaurantAddressUseCase createRestaurantAddressUseCase;

  @Mock
  private UpdateRestaurantAddressUseCase updateRestaurantAddressUseCase;

  @Mock
  private RemoveRestaurantAddressUseCase removeRestaurantAddressUseCase;

  @Mock
  private AddressEntryMapper addressMapper;

  @InjectMocks
  private RestaurantController controller;

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

    final RestaurantOutput out1 = Mockito.mock(RestaurantOutput.class);
    final RestaurantOutput out2 = Mockito.mock(RestaurantOutput.class);

    Mockito.when(searchUseCase.execute(name, restaurantsTypeUuid, street, city, state, page, size, sort, direction))
        .thenReturn(new PageOutput<>(List.of(out1, out2), 2L));

    final RestaurantResponse resp1 = Mockito.mock(RestaurantResponse.class);
    final RestaurantResponse resp2 = Mockito.mock(RestaurantResponse.class);

    Mockito.when(mapper.toResponse(out1))
        .thenReturn(resp1);
    Mockito.when(mapper.toResponse(out2))
        .thenReturn(resp2);

    final var result = controller.search(name, restaurantsTypeUuid, street, city, state, page, size, sort, direction);

    Assertions.assertNotNull(result);
    Assertions.assertEquals(HttpStatus.OK, result.getStatusCode());
    Assertions.assertNotNull(result.getBody());

    final PageResponse<List<RestaurantResponse>> body = result.getBody();
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
        createRestaurantOpeningHourUseCase,
        updateRestaurantOpeningHourUseCase,
        removeRestaurantOpeningHourUseCase,
        restaurantOpeningHoursMapper,
        createRestaurantAddressUseCase,
        updateRestaurantAddressUseCase,
        removeRestaurantAddressUseCase,
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

    final PageResponse<List<RestaurantResponse>> body = result.getBody();
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
        createRestaurantOpeningHourUseCase,
        updateRestaurantOpeningHourUseCase,
        removeRestaurantOpeningHourUseCase,
        restaurantOpeningHoursMapper,
        createRestaurantAddressUseCase,
        updateRestaurantAddressUseCase,
        removeRestaurantAddressUseCase,
        addressMapper
    );
  }

  @Test
  @DisplayName("Deve retornar status 200 e BaseResponse quando buscar por uuid")
  void shouldReturnOkWithBaseResponseWhenFindByUuid() {
    final var uuid = UUID.randomUUID();

    final RestaurantOutput out = Mockito.mock(RestaurantOutput.class);
    Mockito.when(findUseCase.execute(uuid))
        .thenReturn(out);

    final RestaurantResponse resp = Mockito.mock(RestaurantResponse.class);
    Mockito.when(mapper.toResponse(out))
        .thenReturn(resp);

    final var result = controller.findByUuid(uuid);

    Assertions.assertNotNull(result);
    Assertions.assertEquals(HttpStatus.OK, result.getStatusCode());
    Assertions.assertNotNull(result.getBody());

    final BaseResponse<RestaurantResponse> body = result.getBody();
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
        createRestaurantOpeningHourUseCase,
        updateRestaurantOpeningHourUseCase,
        removeRestaurantOpeningHourUseCase,
        restaurantOpeningHoursMapper,
        createRestaurantAddressUseCase,
        updateRestaurantAddressUseCase,
        removeRestaurantAddressUseCase,
        addressMapper
    );
  }

  @Test
  @DisplayName("Deve retornar status 201 e BaseResponse quando criar restaurante (com header Request-User-Uuid)")
  void shouldReturnCreatedWithBaseResponseWhenCreate() {
    final var requestUserUuid = UUID.randomUUID();
    final RestaurantRequest request = Mockito.mock(RestaurantRequest.class);

    final RestaurantInput input = Mockito.mock(RestaurantInput.class);
    Mockito.when(mapper.toInput(request))
        .thenReturn(input);

    final RestaurantOutput out = Mockito.mock(RestaurantOutput.class);
    Mockito.when(createUseCase.execute(Mockito.any(RequestUser.class), Mockito.eq(input)))
        .thenReturn(out);

    final RestaurantResponse resp = Mockito.mock(RestaurantResponse.class);
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
        createRestaurantOpeningHourUseCase,
        updateRestaurantOpeningHourUseCase,
        removeRestaurantOpeningHourUseCase,
        restaurantOpeningHoursMapper,
        createRestaurantAddressUseCase,
        updateRestaurantAddressUseCase,
        removeRestaurantAddressUseCase,
        addressMapper
    );
  }

  @Test
  @DisplayName("Deve retornar status 200 e BaseResponse quando atualizar restaurante (com header Request-User-Uuid)")
  void shouldReturnOkWithBaseResponseWhenUpdate() {
    final var requestUserUuid = UUID.randomUUID();
    final var uuid = UUID.randomUUID();
    final RestaurantRequest request = Mockito.mock(RestaurantRequest.class);

    final RestaurantInput input = Mockito.mock(RestaurantInput.class);
    Mockito.when(mapper.toInput(request))
        .thenReturn(input);

    final RestaurantOutput out = Mockito.mock(RestaurantOutput.class);
    Mockito.when(updateUseCase.execute(Mockito.any(RequestUser.class), Mockito.eq(uuid), Mockito.eq(input)))
        .thenReturn(out);

    final RestaurantResponse resp = Mockito.mock(RestaurantResponse.class);
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
        createRestaurantOpeningHourUseCase,
        updateRestaurantOpeningHourUseCase,
        removeRestaurantOpeningHourUseCase,
        restaurantOpeningHoursMapper,
        createRestaurantAddressUseCase,
        updateRestaurantAddressUseCase,
        removeRestaurantAddressUseCase,
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
        createRestaurantOpeningHourUseCase,
        updateRestaurantOpeningHourUseCase,
        removeRestaurantOpeningHourUseCase,
        restaurantOpeningHoursMapper,
        createRestaurantAddressUseCase,
        updateRestaurantAddressUseCase,
        removeRestaurantAddressUseCase,
        addressMapper
    );
  }

  @Test
  @DisplayName("Deve retornar status 201 e BaseResponse quando criar opening hours (com header Request-User-Uuid)")
  void shouldReturnCreatedWithBaseResponseWhenCreateOpeningHours() {
    final var requestUserUuid = UUID.randomUUID();
    final var restaurantUuid = UUID.randomUUID();
    final RestaurantOpeningHourRequest request = Mockito.mock(RestaurantOpeningHourRequest.class);

    final RestaurantOpeningHourInput input = Mockito.mock(RestaurantOpeningHourInput.class);
    Mockito.when(restaurantOpeningHoursMapper.toInput(request))
        .thenReturn(input);

    final RestaurantOpeningHourOutput out = Mockito.mock(RestaurantOpeningHourOutput.class);
    Mockito.when(createRestaurantOpeningHourUseCase.execute(Mockito.any(RequestUser.class), Mockito.eq(restaurantUuid),
            Mockito.eq(input)
        ))
        .thenReturn(out);

    final RestaurantOpeningHourResponse resp = Mockito.mock(RestaurantOpeningHourResponse.class);
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
    Mockito.verify(createRestaurantOpeningHourUseCase, Mockito.times(1))
        .execute(requestUserCaptor.capture(), Mockito.eq(restaurantUuid), Mockito.eq(input));
    Assertions.assertEquals(requestUserUuid, requestUserCaptor.getValue()
        .uuid()
    );

    Mockito.verify(restaurantOpeningHoursMapper, Mockito.times(1))
        .toInput(request);
    Mockito.verify(restaurantOpeningHoursMapper, Mockito.times(1))
        .toResponse(out);

    Mockito.verifyNoMoreInteractions(createRestaurantOpeningHourUseCase, restaurantOpeningHoursMapper);
    Mockito.verifyNoInteractions(
        searchUseCase,
        findUseCase,
        createUseCase,
        updateUseCase,
        removeUseCase,
        mapper,
        updateRestaurantOpeningHourUseCase,
        removeRestaurantOpeningHourUseCase,
        createRestaurantAddressUseCase,
        updateRestaurantAddressUseCase,
        removeRestaurantAddressUseCase,
        addressMapper
    );
  }

  @Test
  @DisplayName("Deve retornar status 200 e BaseResponse quando atualizar opening hours (com header Request-User-Uuid)")
  void shouldReturnOkWithBaseResponseWhenUpdateOpeningHours() {
    final var requestUserUuid = UUID.randomUUID();
    final var openingHoursUuid = UUID.randomUUID();
    final RestaurantOpeningHourRequest request = Mockito.mock(RestaurantOpeningHourRequest.class);

    final RestaurantOpeningHourInput input = Mockito.mock(RestaurantOpeningHourInput.class);
    Mockito.when(restaurantOpeningHoursMapper.toInput(request))
        .thenReturn(input);

    final RestaurantOpeningHourOutput out = Mockito.mock(RestaurantOpeningHourOutput.class);
    Mockito.when(
            updateRestaurantOpeningHourUseCase.execute(Mockito.any(RequestUser.class), Mockito.eq(openingHoursUuid),
                Mockito.eq(input)
            ))
        .thenReturn(out);

    final RestaurantOpeningHourResponse resp = Mockito.mock(RestaurantOpeningHourResponse.class);
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
    Mockito.verify(updateRestaurantOpeningHourUseCase, Mockito.times(1))
        .execute(requestUserCaptor.capture(), Mockito.eq(openingHoursUuid), Mockito.eq(input));
    Assertions.assertEquals(requestUserUuid, requestUserCaptor.getValue()
        .uuid()
    );

    Mockito.verify(restaurantOpeningHoursMapper, Mockito.times(1))
        .toInput(request);
    Mockito.verify(restaurantOpeningHoursMapper, Mockito.times(1))
        .toResponse(out);

    Mockito.verifyNoMoreInteractions(updateRestaurantOpeningHourUseCase, restaurantOpeningHoursMapper);
    Mockito.verifyNoInteractions(
        searchUseCase,
        findUseCase,
        createUseCase,
        updateUseCase,
        removeUseCase,
        mapper,
        createRestaurantOpeningHourUseCase,
        removeRestaurantOpeningHourUseCase,
        createRestaurantAddressUseCase,
        updateRestaurantAddressUseCase,
        removeRestaurantAddressUseCase,
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
    Mockito.verify(removeRestaurantOpeningHourUseCase, Mockito.times(1))
        .execute(requestUserCaptor.capture(), Mockito.eq(openingHoursUuid));
    Assertions.assertEquals(requestUserUuid, requestUserCaptor.getValue()
        .uuid()
    );

    Mockito.verifyNoMoreInteractions(removeRestaurantOpeningHourUseCase);
    Mockito.verifyNoInteractions(
        searchUseCase,
        findUseCase,
        createUseCase,
        updateUseCase,
        removeUseCase,
        mapper,
        createRestaurantOpeningHourUseCase,
        updateRestaurantOpeningHourUseCase,
        restaurantOpeningHoursMapper,
        createRestaurantAddressUseCase,
        updateRestaurantAddressUseCase,
        removeRestaurantAddressUseCase,
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
    Mockito.when(createRestaurantAddressUseCase.execute(Mockito.any(RequestUser.class), Mockito.eq(restaurantUuid),
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
    Mockito.verify(createRestaurantAddressUseCase, Mockito.times(1))
        .execute(requestUserCaptor.capture(), Mockito.eq(restaurantUuid), Mockito.eq(addressInput));
    Assertions.assertEquals(requestUserUuid, requestUserCaptor.getValue()
        .uuid()
    );

    Mockito.verify(addressMapper, Mockito.times(1))
        .toInput(request);
    Mockito.verify(addressMapper, Mockito.times(1))
        .toResponse(addressOutput);

    Mockito.verifyNoMoreInteractions(createRestaurantAddressUseCase, addressMapper);
    Mockito.verifyNoInteractions(
        searchUseCase,
        findUseCase,
        createUseCase,
        updateUseCase,
        removeUseCase,
        mapper,
        createRestaurantOpeningHourUseCase,
        updateRestaurantOpeningHourUseCase,
        removeRestaurantOpeningHourUseCase,
        restaurantOpeningHoursMapper,
        updateRestaurantAddressUseCase,
        removeRestaurantAddressUseCase
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
    Mockito.when(updateRestaurantAddressUseCase.execute(Mockito.any(RequestUser.class), Mockito.eq(restaurantUuid),
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
    Mockito.verify(updateRestaurantAddressUseCase, Mockito.times(1))
        .execute(requestUserCaptor.capture(), Mockito.eq(restaurantUuid), Mockito.eq(addressInput));
    Assertions.assertEquals(requestUserUuid, requestUserCaptor.getValue()
        .uuid()
    );

    Mockito.verify(addressMapper, Mockito.times(1))
        .toInput(request);
    Mockito.verify(addressMapper, Mockito.times(1))
        .toResponse(addressOutput);

    Mockito.verifyNoMoreInteractions(updateRestaurantAddressUseCase, addressMapper);
    Mockito.verifyNoInteractions(
        searchUseCase,
        findUseCase,
        createUseCase,
        updateUseCase,
        removeUseCase,
        mapper,
        createRestaurantOpeningHourUseCase,
        updateRestaurantOpeningHourUseCase,
        removeRestaurantOpeningHourUseCase,
        restaurantOpeningHoursMapper,
        createRestaurantAddressUseCase,
        removeRestaurantAddressUseCase
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
    Mockito.verify(removeRestaurantAddressUseCase, Mockito.times(1))
        .execute(requestUserCaptor.capture(), Mockito.eq(restaurantUuid));
    Assertions.assertEquals(requestUserUuid, requestUserCaptor.getValue()
        .uuid()
    );

    Mockito.verifyNoMoreInteractions(removeRestaurantAddressUseCase);
    Mockito.verifyNoInteractions(
        searchUseCase,
        findUseCase,
        createUseCase,
        updateUseCase,
        removeUseCase,
        mapper,
        createRestaurantOpeningHourUseCase,
        updateRestaurantOpeningHourUseCase,
        removeRestaurantOpeningHourUseCase,
        restaurantOpeningHoursMapper,
        createRestaurantAddressUseCase,
        updateRestaurantAddressUseCase,
        addressMapper
    );
  }
}
