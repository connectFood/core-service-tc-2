package com.connectfood.core.entrypoint.rest.controller;

import java.util.List;
import java.util.UUID;

import com.connectfood.core.application.restaurantitems.usecase.CreateRestaurantItemsUseCase;
import com.connectfood.core.application.restaurantitems.usecase.FindRestaurantItemsUseCase;
import com.connectfood.core.application.restaurantitems.usecase.RemoveRestaurantItemsUseCase;
import com.connectfood.core.application.restaurantitems.usecase.SearchRestaurantItemsUseCase;
import com.connectfood.core.application.restaurantitems.usecase.UpdateRestaurantItemsUseCase;
import com.connectfood.core.application.security.RequestUser;
import com.connectfood.core.entrypoint.rest.dto.commons.BaseResponse;
import com.connectfood.core.entrypoint.rest.dto.commons.PageResponse;
import com.connectfood.core.entrypoint.rest.dto.restaurantitems.RestaurantItemsRequest;
import com.connectfood.core.entrypoint.rest.dto.restaurantitems.RestaurantItemsResponse;
import com.connectfood.core.entrypoint.rest.mappers.RestaurantItemsEntryMapper;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/v1/restaurant-items")
@Tag(name = "Restaurant Items Controller", description = "Operations related to restaurant items management")
public class RestaurantItemsController {

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

  @GetMapping
  @Operation(
      summary = "Search restaurant items with filters and pagination",
      description = "Returns paginated list of restaurant items filtered by the given parameters"
  )
  public ResponseEntity<PageResponse<List<RestaurantItemsResponse>>> search(
      @RequestParam(required = false) final UUID restaurantUuid,
      @RequestParam(defaultValue = "0") final Integer page,
      @RequestParam(defaultValue = "10") final Integer size,
      @RequestParam(required = false) final String sort,
      @RequestParam(required = false) final String direction
  ) {
    final var result = searchUseCase.execute(restaurantUuid, page, size, sort, direction);

    final var response = result.content()
        .stream()
        .map(mapper::toResponse)
        .toList();

    return ResponseEntity.ok()
        .body(new PageResponse<>(response, result.total(), page, size));
  }

  @GetMapping(path = "/{uuid}")
  @Operation(
      summary = "Find restaurant item by UUID",
      description = "Returns a restaurant item for the given UUID"
  )
  public ResponseEntity<BaseResponse<RestaurantItemsResponse>> findByUuid(@PathVariable final UUID uuid) {
    final var result = findUseCase.execute(uuid);
    final var response = mapper.toResponse(result);

    return ResponseEntity.ok()
        .body(new BaseResponse<>(response));
  }

  @PostMapping
  @Operation(
      summary = "Create a new restaurant item",
      description = "Creates a new restaurant item and returns the created resource"
  )
  public ResponseEntity<BaseResponse<RestaurantItemsResponse>> create(
      @RequestHeader(name = "Request-User-Uuid") final UUID requestUserUuid,
      @Valid @RequestBody final RestaurantItemsRequest request) {
    final var result = createUseCase.execute(new RequestUser(requestUserUuid), mapper.toInput(request));
    final var response = mapper.toResponse(result);

    return ResponseEntity.status(HttpStatus.CREATED)
        .body(new BaseResponse<>(response));
  }

  @PutMapping(path = "/{uuid}")
  @Operation(
      summary = "Update an existing restaurant item",
      description = "Updates an existing restaurant item identified by UUID"
  )
  public ResponseEntity<BaseResponse<RestaurantItemsResponse>> update(
      @RequestHeader(name = "Request-User-Uuid") final UUID requestUserUuid,
      @PathVariable final UUID uuid,
      @Valid @RequestBody final RestaurantItemsRequest request
  ) {
    final var result = updateUseCase.execute(new RequestUser(requestUserUuid), uuid, mapper.toInput(request));
    final var response = mapper.toResponse(result);

    return ResponseEntity.ok()
        .body(new BaseResponse<>(response));
  }

  @DeleteMapping(path = "/{uuid}")
  @Operation(
      summary = "Delete an existing restaurant item",
      description = "Deletes an existing restaurant item identified by UUID"
  )
  public ResponseEntity<Void> delete(
      @RequestHeader(name = "Request-User-Uuid") final UUID requestUserUuid,
      @PathVariable final UUID uuid) {
    removeUseCase.execute(new RequestUser(requestUserUuid), uuid);

    return ResponseEntity.noContent()
        .build();
  }
}
