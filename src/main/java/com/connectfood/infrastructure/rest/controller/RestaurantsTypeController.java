package com.connectfood.infrastructure.rest.controller;

import java.util.List;
import java.util.UUID;

import com.connectfood.core.application.restaurantstype.usecase.CreateRestaurantTypeUseCase;
import com.connectfood.core.application.restaurantstype.usecase.FindRestaurantTypeUseCase;
import com.connectfood.core.application.restaurantstype.usecase.RemoveRestaurantTypeUseCase;
import com.connectfood.core.application.restaurantstype.usecase.SearchRestaurantTypeUseCase;
import com.connectfood.core.application.restaurantstype.usecase.UpdateRestaurantTypeUseCase;
import com.connectfood.infrastructure.rest.controller.docs.RestaurantsTypeControllerApi;
import com.connectfood.infrastructure.rest.dto.commons.BaseResponse;
import com.connectfood.infrastructure.rest.dto.commons.PageResponse;
import com.connectfood.infrastructure.rest.dto.restaurantstype.RestaurantsTypeRequest;
import com.connectfood.infrastructure.rest.dto.restaurantstype.RestaurantsTypeResponse;
import com.connectfood.infrastructure.rest.mappers.RestaurantsTypeEntryMapper;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
public class RestaurantsTypeController implements RestaurantsTypeControllerApi {

  private final SearchRestaurantTypeUseCase searchUseCase;
  private final FindRestaurantTypeUseCase findUseCase;
  private final CreateRestaurantTypeUseCase createUseCase;
  private final UpdateRestaurantTypeUseCase updateUseCase;
  private final RemoveRestaurantTypeUseCase removeUseCase;
  private final RestaurantsTypeEntryMapper mapper;

  @Override
  public ResponseEntity<PageResponse<List<RestaurantsTypeResponse>>> search(
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
  public ResponseEntity<BaseResponse<RestaurantsTypeResponse>> find(final UUID uuid) {
    final var result = findUseCase.execute(uuid);
    final var response = mapper.toResponse(result);

    return ResponseEntity.ok()
        .body(new BaseResponse<>(response));
  }

  @Override
  public ResponseEntity<BaseResponse<RestaurantsTypeResponse>> create(
      final RestaurantsTypeRequest request) {
    final var result = createUseCase.execute(mapper.toInput(request));
    final var response = mapper.toResponse(result);

    return ResponseEntity.status(HttpStatus.CREATED).
        body(new BaseResponse<>(response));
  }

  @Override
  public ResponseEntity<BaseResponse<RestaurantsTypeResponse>> update(
      final UUID uuid,
      final RestaurantsTypeRequest request
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
