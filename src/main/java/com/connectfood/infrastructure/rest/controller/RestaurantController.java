package com.connectfood.infrastructure.rest.controller;

import java.util.List;
import java.util.UUID;

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
import com.connectfood.infrastructure.rest.controller.docs.RestaurantControllerApi;
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

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RestaurantController implements RestaurantControllerApi {

  private final SearchRestaurantsUseCase searchUseCase;
  private final FindRestaurantUseCase findUseCase;
  private final CreateRestaurantUseCase createUseCase;
  private final UpdateRestaurantUseCase updateUseCase;
  private final RemoveRestaurantUseCase removeUseCase;
  private final RestaurantEntryMapper mapper;
  private final CreateRestaurantOpeningHourUseCase createRestaurantOpeningHourUseCase;
  private final UpdateRestaurantOpeningHourUseCase updateRestaurantOpeningHourUseCase;
  private final RemoveRestaurantOpeningHourUseCase removeRestaurantOpeningHourUseCase;
  private final RestaurantOpeningHourEntryMapper restaurantOpeningHoursMapper;
  private final CreateRestaurantAddressUseCase createRestaurantAddressUseCase;
  private final UpdateRestaurantAddressUseCase updateRestaurantAddressUseCase;
  private final RemoveRestaurantAddressUseCase removeRestaurantAddressUseCase;
  private final AddressEntryMapper addressMapper;

  public RestaurantController(
      final SearchRestaurantsUseCase searchUseCase,
      final FindRestaurantUseCase findUseCase,
      final CreateRestaurantUseCase createUseCase,
      final UpdateRestaurantUseCase updateUseCase,
      final RemoveRestaurantUseCase removeUseCase,
      final RestaurantEntryMapper mapper,
      final CreateRestaurantOpeningHourUseCase createRestaurantOpeningHourUseCase,
      final UpdateRestaurantOpeningHourUseCase updateRestaurantOpeningHourUseCase,
      final RemoveRestaurantOpeningHourUseCase removeRestaurantOpeningHourUseCase,
      final RestaurantOpeningHourEntryMapper restaurantOpeningHoursMapper,
      final CreateRestaurantAddressUseCase createRestaurantAddressUseCase,
      final UpdateRestaurantAddressUseCase updateRestaurantAddressUseCase,
      final RemoveRestaurantAddressUseCase removeRestaurantAddressUseCase,
      final AddressEntryMapper addressMapper
  ) {
    this.searchUseCase = searchUseCase;
    this.findUseCase = findUseCase;
    this.createUseCase = createUseCase;
    this.updateUseCase = updateUseCase;
    this.removeUseCase = removeUseCase;
    this.mapper = mapper;
    this.createRestaurantOpeningHourUseCase = createRestaurantOpeningHourUseCase;
    this.updateRestaurantOpeningHourUseCase = updateRestaurantOpeningHourUseCase;
    this.removeRestaurantOpeningHourUseCase = removeRestaurantOpeningHourUseCase;
    this.restaurantOpeningHoursMapper = restaurantOpeningHoursMapper;
    this.createRestaurantAddressUseCase = createRestaurantAddressUseCase;
    this.updateRestaurantAddressUseCase = updateRestaurantAddressUseCase;
    this.removeRestaurantAddressUseCase = removeRestaurantAddressUseCase;
    this.addressMapper = addressMapper;
  }

  @Override
  public ResponseEntity<PageResponse<List<RestaurantResponse>>> search(
      final String name,
      final UUID restaurantsTypeUuid,
      final String street,
      final String city,
      final String state,
      final Integer page,
      final Integer size,
      final String sort,
      final String direction) {
    final var result = searchUseCase.execute(name, restaurantsTypeUuid, street, city, state, page, size, sort,
        direction
    );

    final var response = result.content()
        .stream()
        .map(mapper::toResponse)
        .toList();

    return ResponseEntity.ok()
        .body(new PageResponse<>(response, result.total(), page, size));
  }

  @Override
  public ResponseEntity<BaseResponse<RestaurantResponse>> findByUuid(final UUID uuid) {

    final var result = findUseCase.execute(uuid);
    final var response = mapper.toResponse(result);

    return ResponseEntity.ok()
        .body(new BaseResponse<>(response));
  }

  @Override
  public ResponseEntity<BaseResponse<RestaurantResponse>> create(
      final UUID requestUserUuid,
      final RestaurantRequest request) {

    final var result = createUseCase.execute(new RequestUser(requestUserUuid), mapper.toInput(request));
    final var response = mapper.toResponse(result);

    return ResponseEntity.status(HttpStatus.CREATED)
        .body(new BaseResponse<>(response));
  }

  @Override
  public ResponseEntity<BaseResponse<RestaurantOpeningHourResponse>> createOpeningHours(
      final UUID requestUserUuid,
      final UUID uuid,
      final RestaurantOpeningHourRequest request) {

    final var result = createRestaurantOpeningHourUseCase.execute(new RequestUser(requestUserUuid), uuid,
        restaurantOpeningHoursMapper.toInput(request)
    );
    final var response = restaurantOpeningHoursMapper.toResponse(result);

    return ResponseEntity.status(HttpStatus.CREATED)
        .body(new BaseResponse<>(response));
  }

  @Override
  public ResponseEntity<BaseResponse<AddressResponse>> createAddress(
      final UUID requestUserUuid,
      final UUID uuid,
      final AddressRequest request) {

    final var result = createRestaurantAddressUseCase.execute(new RequestUser(requestUserUuid), uuid,
        addressMapper.toInput(request)
    );
    final var response = addressMapper.toResponse(result);

    return ResponseEntity.status(HttpStatus.CREATED)
        .body(new BaseResponse<>(response));
  }

  @Override
  public ResponseEntity<BaseResponse<RestaurantResponse>> update(
      final UUID requestUserUuid,
      final UUID uuid,
      final RestaurantRequest request
  ) {

    final var result = updateUseCase.execute(new RequestUser(requestUserUuid), uuid, mapper.toInput(request));
    final var response = mapper.toResponse(result);

    return ResponseEntity.ok()
        .body(new BaseResponse<>(response));
  }

  @Override
  public ResponseEntity<BaseResponse<RestaurantOpeningHourResponse>> update(
      final UUID requestUserUuid,
      final UUID openingHoursUuid,
      final RestaurantOpeningHourRequest request
  ) {
    final var result = updateRestaurantOpeningHourUseCase.execute(new RequestUser(requestUserUuid), openingHoursUuid,
        restaurantOpeningHoursMapper.toInput(request)
    );
    final var response = restaurantOpeningHoursMapper.toResponse(result);

    return ResponseEntity.ok()
        .body(new BaseResponse<>(response));
  }

  @Override
  public ResponseEntity<BaseResponse<AddressResponse>> updateAddress(
      final UUID requestUserUuid,
      final UUID uuid,
      final AddressRequest request
  ) {

    final var result = updateRestaurantAddressUseCase.execute(new RequestUser(requestUserUuid), uuid,
        addressMapper.toInput(request)
    );
    final var response = addressMapper.toResponse(result);

    return ResponseEntity.ok()
        .body(new BaseResponse<>(response));
  }

  @Override
  public ResponseEntity<Void> delete(
      final UUID requestUserUuid,
      final UUID uuid
  ) {
    removeUseCase.execute(new RequestUser(requestUserUuid), uuid);

    return ResponseEntity.noContent()
        .build();
  }

  @Override
  public ResponseEntity<Void> deleteOpeningHoursUuid(
      final UUID requestUserUuid,
      final UUID openingHoursUuid
  ) {
    removeRestaurantOpeningHourUseCase.execute(new RequestUser(requestUserUuid), openingHoursUuid);

    return ResponseEntity.noContent()
        .build();
  }

  @Override
  public ResponseEntity<Void> deleteAddress(
      final UUID requestUserUuid,
      final UUID uuid
  ) {
    removeRestaurantAddressUseCase.execute(new RequestUser(requestUserUuid), uuid);

    return ResponseEntity.noContent()
        .build();
  }
}
