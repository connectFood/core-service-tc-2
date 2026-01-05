package com.connectfood.core.entrypoint.rest.controller;

import java.util.List;
import java.util.UUID;

import com.connectfood.core.application.restaurantopeninghours.usecase.FindRestaurantOpeningHoursUseCase;
import com.connectfood.core.application.restaurantopeninghours.usecase.RemoveRestaurantOpeningHoursUseCase;
import com.connectfood.core.application.restaurantopeninghours.usecase.SearchRestaurantOpeningHoursUseCase;
import com.connectfood.core.application.restaurantopeninghours.usecase.UpdateRestaurantOpeningHoursUseCase;
import com.connectfood.core.entrypoint.rest.dto.commons.BaseResponse;
import com.connectfood.core.entrypoint.rest.dto.commons.PageResponse;
import com.connectfood.core.entrypoint.rest.dto.restaurantopeninghours.RestaurantOpeningHoursRequest;
import com.connectfood.core.entrypoint.rest.dto.restaurantopeninghours.RestaurantOpeningHoursResponse;
import com.connectfood.core.entrypoint.rest.mappers.RestaurantOpeningHoursEntryMapper;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/v1/restaurant_opening_hours")
@Tag(name = "Restaurant Opening Hours Controller", description = "Operations related to restaurant opening hours "
    + "management")
public class RestaurantOpeningHoursController {

  private final SearchRestaurantOpeningHoursUseCase searchUseCase;
  private final FindRestaurantOpeningHoursUseCase findUseCase;
  private final UpdateRestaurantOpeningHoursUseCase updateUseCase;
  private final RemoveRestaurantOpeningHoursUseCase removeUseCase;
  private final RestaurantOpeningHoursEntryMapper mapper;

  public RestaurantOpeningHoursController(
      final SearchRestaurantOpeningHoursUseCase searchUseCase,
      final FindRestaurantOpeningHoursUseCase findUseCase,
      final UpdateRestaurantOpeningHoursUseCase updateUseCase,
      final RemoveRestaurantOpeningHoursUseCase removeUseCase,
      final RestaurantOpeningHoursEntryMapper mapper
  ) {
    this.searchUseCase = searchUseCase;
    this.findUseCase = findUseCase;
    this.updateUseCase = updateUseCase;
    this.removeUseCase = removeUseCase;
    this.mapper = mapper;
  }

  @GetMapping
  @Operation(
      summary = "Search restaurant opening hours with filters and pagination",
      description = "Returns paginated list of restaurant opening hours filtered by the given parameters"
  )
  public ResponseEntity<PageResponse<List<RestaurantOpeningHoursResponse>>> search(
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
      summary = "Find restaurant opening hours by UUID",
      description = "Returns a restaurant opening hours for the given UUID"
  )
  public ResponseEntity<BaseResponse<RestaurantOpeningHoursResponse>> findByUuid(@PathVariable final UUID uuid) {
    final var result = findUseCase.execute(uuid);
    final var response = mapper.toResponse(result);

    return ResponseEntity.ok()
        .body(new BaseResponse<>(response));
  }

  @PutMapping(path = "/{uuid}")
  @Operation(
      summary = "Update an existing restaurant opening hours",
      description = "Updates an existing restaurant opening hours identified by UUID"
  )
  public ResponseEntity<BaseResponse<RestaurantOpeningHoursResponse>> update(
      @PathVariable final UUID uuid,
      @Valid @RequestBody final RestaurantOpeningHoursRequest request
  ) {
    final var result = updateUseCase.execute(uuid, mapper.toInput(request));
    final var response = mapper.toResponse(result);

    return ResponseEntity.ok()
        .body(new BaseResponse<>(response));
  }

  @DeleteMapping(path = "/{uuid}")
  @Operation(
      summary = "Delete an existing restaurant opening hours",
      description = "Deletes an existing restaurant opening hours identified by UUID"
  )
  public ResponseEntity<Void> delete(@PathVariable final UUID uuid) {
    removeUseCase.execute(uuid);

    return ResponseEntity.noContent()
        .build();
  }
}
