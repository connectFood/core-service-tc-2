package com.connectfood.infrastructure.rest.controller;

import java.util.List;
import java.util.UUID;

import com.connectfood.core.application.restaurantitems.usecase.CreateRestaurantItemsUseCase;
import com.connectfood.core.application.restaurantitems.usecase.FindRestaurantItemsUseCase;
import com.connectfood.core.application.restaurantitems.usecase.RemoveRestaurantItemsUseCase;
import com.connectfood.core.application.restaurantitems.usecase.SearchRestaurantItemsUseCase;
import com.connectfood.core.application.restaurantitems.usecase.UpdateRestaurantItemsUseCase;
import com.connectfood.core.application.security.RequestUser;
import com.connectfood.infrastructure.rest.controller.docs.RestaurantItemsControllerApi;
import com.connectfood.infrastructure.rest.dto.commons.BaseResponse;
import com.connectfood.infrastructure.rest.dto.commons.PageResponse;
import com.connectfood.infrastructure.rest.dto.restaurantitems.RestaurantItemsRequest;
import com.connectfood.infrastructure.rest.dto.restaurantitems.RestaurantItemsResponse;
import com.connectfood.infrastructure.rest.mappers.RestaurantItemsEntryMapper;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RestaurantItemsController implements RestaurantItemsControllerApi {

  private final SearchRestaurantItemsUseCase searchUseCase;
  private final FindRestaurantItemsUseCase findUseCase;
  private final CreateRestaurantItemsUseCase createUseCase;
  private final UpdateRestaurantItemsUseCase updateUseCase;
  private final RemoveRestaurantItemsUseCase removeUseCase;
  private final RestaurantItemsEntryMapper mapper;

  public RestaurantItemsController(
      final SearchRestaurantItemsUseCase searchUseCase,
      final FindRestaurantItemsUseCase findUseCase,
      final CreateRestaurantItemsUseCase createUseCase,
      final UpdateRestaurantItemsUseCase updateUseCase,
      final RemoveRestaurantItemsUseCase removeUseCase,
      final RestaurantItemsEntryMapper mapper
  ) {
    this.searchUseCase = searchUseCase;
    this.findUseCase = findUseCase;
    this.createUseCase = createUseCase;
    this.updateUseCase = updateUseCase;
    this.removeUseCase = removeUseCase;
    this.mapper = mapper;
  }

  @Override
  public ResponseEntity<PageResponse<List<RestaurantItemsResponse>>> search(final UUID restaurantUuid,
      final Integer page, final Integer size, final String sort, final String direction) {
    final var result = searchUseCase.execute(restaurantUuid, page, size, sort, direction);

    final var response = result.content()
        .stream()
        .map(mapper::toResponse)
        .toList();

    return ResponseEntity.ok()
        .body(new PageResponse<>(response, result.total(), page, size));
  }

  @Override
  public ResponseEntity<BaseResponse<RestaurantItemsResponse>> findByUuid(final UUID uuid) {
    final var result = findUseCase.execute(uuid);
    final var response = mapper.toResponse(result);

    return ResponseEntity.ok()
        .body(new BaseResponse<>(response));
  }

  @Override
  public ResponseEntity<BaseResponse<RestaurantItemsResponse>> create(final UUID requestUserUuid,
      final RestaurantItemsRequest request) {
    final var result = createUseCase.execute(new RequestUser(requestUserUuid), mapper.toInput(request));
    final var response = mapper.toResponse(result);

    return ResponseEntity.status(HttpStatus.CREATED)
        .body(new BaseResponse<>(response));
  }

  @Override
  public ResponseEntity<BaseResponse<RestaurantItemsResponse>> update(final UUID requestUserUuid, final UUID uuid,
      final RestaurantItemsRequest request
  ) {
    final var result = updateUseCase.execute(new RequestUser(requestUserUuid), uuid, mapper.toInput(request));
    final var response = mapper.toResponse(result);

    return ResponseEntity.ok()
        .body(new BaseResponse<>(response));
  }

  @Override
  public ResponseEntity<Void> delete(final UUID requestUserUuid, final UUID uuid) {
    removeUseCase.execute(new RequestUser(requestUserUuid), uuid);

    return ResponseEntity.noContent()
        .build();
  }
}
