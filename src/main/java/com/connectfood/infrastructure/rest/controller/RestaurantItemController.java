package com.connectfood.infrastructure.rest.controller;

import java.util.List;
import java.util.UUID;

import com.connectfood.core.application.restaurantitem.usecase.CreateRestaurantItemUseCase;
import com.connectfood.core.application.restaurantitem.usecase.FindRestaurantItemUseCase;
import com.connectfood.core.application.restaurantitem.usecase.RemoveRestaurantItemUseCase;
import com.connectfood.core.application.restaurantitem.usecase.SearchRestaurantItemsUseCase;
import com.connectfood.core.application.restaurantitem.usecase.UpdateRestaurantItemUseCase;
import com.connectfood.core.application.security.RequestUser;
import com.connectfood.infrastructure.rest.controller.docs.RestaurantItemControllerApi;
import com.connectfood.infrastructure.rest.dto.commons.BaseResponse;
import com.connectfood.infrastructure.rest.dto.commons.PageResponse;
import com.connectfood.infrastructure.rest.dto.restaurantitem.RestaurantItemRequest;
import com.connectfood.infrastructure.rest.dto.restaurantitem.RestaurantItemResponse;
import com.connectfood.infrastructure.rest.mappers.RestaurantItemEntryMapper;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RestaurantItemController implements RestaurantItemControllerApi {

  private final SearchRestaurantItemsUseCase searchUseCase;
  private final FindRestaurantItemUseCase findUseCase;
  private final CreateRestaurantItemUseCase createUseCase;
  private final UpdateRestaurantItemUseCase updateUseCase;
  private final RemoveRestaurantItemUseCase removeUseCase;
  private final RestaurantItemEntryMapper mapper;

  public RestaurantItemController(
      final SearchRestaurantItemsUseCase searchUseCase,
      final FindRestaurantItemUseCase findUseCase,
      final CreateRestaurantItemUseCase createUseCase,
      final UpdateRestaurantItemUseCase updateUseCase,
      final RemoveRestaurantItemUseCase removeUseCase,
      final RestaurantItemEntryMapper mapper
  ) {
    this.searchUseCase = searchUseCase;
    this.findUseCase = findUseCase;
    this.createUseCase = createUseCase;
    this.updateUseCase = updateUseCase;
    this.removeUseCase = removeUseCase;
    this.mapper = mapper;
  }

  @Override
  public ResponseEntity<PageResponse<List<RestaurantItemResponse>>> search(final UUID restaurantUuid,
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
  public ResponseEntity<BaseResponse<RestaurantItemResponse>> findByUuid(final UUID uuid) {
    final var result = findUseCase.execute(uuid);
    final var response = mapper.toResponse(result);

    return ResponseEntity.ok()
        .body(new BaseResponse<>(response));
  }

  @Override
  public ResponseEntity<BaseResponse<RestaurantItemResponse>> create(final UUID requestUserUuid,
      final RestaurantItemRequest request) {
    final var result = createUseCase.execute(new RequestUser(requestUserUuid), mapper.toInput(request));
    final var response = mapper.toResponse(result);

    return ResponseEntity.status(HttpStatus.CREATED)
        .body(new BaseResponse<>(response));
  }

  @Override
  public ResponseEntity<BaseResponse<RestaurantItemResponse>> update(final UUID requestUserUuid, final UUID uuid,
      final RestaurantItemRequest request
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
