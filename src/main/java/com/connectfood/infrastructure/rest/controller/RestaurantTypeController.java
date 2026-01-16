package com.connectfood.infrastructure.rest.controller;

import java.util.List;
import java.util.UUID;

import com.connectfood.core.application.restauranttype.usecase.CreateRestaurantTypeUseCase;
import com.connectfood.core.application.restauranttype.usecase.FindRestaurantTypeUseCase;
import com.connectfood.core.application.restauranttype.usecase.RemoveRestaurantTypeUseCase;
import com.connectfood.core.application.restauranttype.usecase.SearchRestaurantsTypeUseCase;
import com.connectfood.core.application.restauranttype.usecase.UpdateRestaurantTypeUseCase;
import com.connectfood.infrastructure.rest.controller.docs.RestaurantTypeControllerApi;
import com.connectfood.infrastructure.rest.dto.commons.BaseResponse;
import com.connectfood.infrastructure.rest.dto.commons.PageResponse;
import com.connectfood.infrastructure.rest.dto.restauranttype.RestaurantTypeRequest;
import com.connectfood.infrastructure.rest.dto.restauranttype.RestaurantTypeResponse;
import com.connectfood.infrastructure.rest.mappers.RestaurantTypeEntryMapper;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
public class RestaurantTypeController implements RestaurantTypeControllerApi {

  private final SearchRestaurantsTypeUseCase searchUseCase;
  private final FindRestaurantTypeUseCase findUseCase;
  private final CreateRestaurantTypeUseCase createUseCase;
  private final UpdateRestaurantTypeUseCase updateUseCase;
  private final RemoveRestaurantTypeUseCase removeUseCase;
  private final RestaurantTypeEntryMapper mapper;

  @Override
  public ResponseEntity<PageResponse<List<RestaurantTypeResponse>>> search(
      final String name,
      final Integer page,
      final Integer size,
      final String sort,
      final String direction
  ) {
    final var result = searchUseCase.execute(name, page, size, sort, direction);

    final var response = result.content()
        .stream()
        .map(mapper::toResponse)
        .toList();

    return ResponseEntity.ok()
        .body(new PageResponse<>(response, result.total(), page, size));
  }

  @Override
  public ResponseEntity<BaseResponse<RestaurantTypeResponse>> find(final UUID uuid) {
    final var result = findUseCase.execute(uuid);
    final var response = mapper.toResponse(result);

    return ResponseEntity.ok()
        .body(new BaseResponse<>(response));
  }

  @Override
  public ResponseEntity<BaseResponse<RestaurantTypeResponse>> create(
      final RestaurantTypeRequest request) {
    final var result = createUseCase.execute(mapper.toInput(request));
    final var response = mapper.toResponse(result);

    return ResponseEntity.status(HttpStatus.CREATED).
        body(new BaseResponse<>(response));
  }

  @Override
  public ResponseEntity<BaseResponse<RestaurantTypeResponse>> update(
      final UUID uuid,
      final RestaurantTypeRequest request
  ) {
    final var result = updateUseCase.execute(uuid, mapper.toInput(request));
    final var response = mapper.toResponse(result);

    return ResponseEntity.ok()
        .body(new BaseResponse<>(response));
  }

  @Override
  public ResponseEntity<Void> delete(final UUID uuid) {
    removeUseCase.execute(uuid);

    return ResponseEntity.noContent()
        .build();
  }
}
