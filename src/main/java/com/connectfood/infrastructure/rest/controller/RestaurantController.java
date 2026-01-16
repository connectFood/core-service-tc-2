package com.connectfood.infrastructure.rest.controller;

import java.util.List;
import java.util.UUID;

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
  private final FindRestaurantsUseCase findUseCase;
  private final CreateRestaurantsUseCase createUseCase;
  private final UpdateRestaurantsUseCase updateUseCase;
  private final RemoveRestaurantsUseCase removeUseCase;
  private final RestaurantEntryMapper mapper;
  private final CreateRestaurantOpeningHoursUseCase createRestaurantOpeningHoursUseCase;
  private final UpdateRestaurantOpeningHoursUseCase updateRestaurantOpeningHoursUseCase;
  private final RemoveRestaurantOpeningHoursUseCase removeRestaurantOpeningHoursUseCase;
  private final RestaurantOpeningHourEntryMapper restaurantOpeningHoursMapper;
  private final CreateRestaurantsAddressUseCase createRestaurantsAddressUseCase;
  private final UpdateRestaurantsAddressUseCase updateRestaurantsAddressUseCase;
  private final RemoveRestaurantsAddressUseCase removeRestaurantsAddressUseCase;
  private final AddressEntryMapper addressMapper;

  public RestaurantController(
      final SearchRestaurantsUseCase searchUseCase,
      final FindRestaurantsUseCase findUseCase,
      final CreateRestaurantsUseCase createUseCase,
      final UpdateRestaurantsUseCase updateUseCase,
      final RemoveRestaurantsUseCase removeUseCase,
      final RestaurantEntryMapper mapper,
      final CreateRestaurantOpeningHoursUseCase createRestaurantOpeningHoursUseCase,
      final UpdateRestaurantOpeningHoursUseCase updateRestaurantOpeningHoursUseCase,
      final RemoveRestaurantOpeningHoursUseCase removeRestaurantOpeningHoursUseCase,
      final RestaurantOpeningHourEntryMapper restaurantOpeningHoursMapper,
      final CreateRestaurantsAddressUseCase createRestaurantsAddressUseCase,
      final UpdateRestaurantsAddressUseCase updateRestaurantsAddressUseCase,
      final RemoveRestaurantsAddressUseCase removeRestaurantsAddressUseCase,
      final AddressEntryMapper addressMapper
  ) {
    this.searchUseCase = searchUseCase;
    this.findUseCase = findUseCase;
    this.createUseCase = createUseCase;
    this.updateUseCase = updateUseCase;
    this.removeUseCase = removeUseCase;
    this.mapper = mapper;
    this.createRestaurantOpeningHoursUseCase = createRestaurantOpeningHoursUseCase;
    this.updateRestaurantOpeningHoursUseCase = updateRestaurantOpeningHoursUseCase;
    this.removeRestaurantOpeningHoursUseCase = removeRestaurantOpeningHoursUseCase;
    this.restaurantOpeningHoursMapper = restaurantOpeningHoursMapper;
    this.createRestaurantsAddressUseCase = createRestaurantsAddressUseCase;
    this.updateRestaurantsAddressUseCase = updateRestaurantsAddressUseCase;
    this.removeRestaurantsAddressUseCase = removeRestaurantsAddressUseCase;
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

    final var result = createRestaurantOpeningHoursUseCase.execute(new RequestUser(requestUserUuid), uuid,
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

    final var result = createRestaurantsAddressUseCase.execute(new RequestUser(requestUserUuid), uuid,
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
    final var result = updateRestaurantOpeningHoursUseCase.execute(new RequestUser(requestUserUuid), openingHoursUuid,
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

    final var result = updateRestaurantsAddressUseCase.execute(new RequestUser(requestUserUuid), uuid,
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
    removeRestaurantOpeningHoursUseCase.execute(new RequestUser(requestUserUuid), openingHoursUuid);

    return ResponseEntity.noContent()
        .build();
  }

  @Override
  public ResponseEntity<Void> deleteAddress(
      final UUID requestUserUuid,
      final UUID uuid
  ) {
    removeRestaurantsAddressUseCase.execute(new RequestUser(requestUserUuid), uuid);

    return ResponseEntity.noContent()
        .build();
  }
}
