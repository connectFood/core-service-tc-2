package com.connectfood.core.entrypoint.rest.controller;

import com.connectfood.core.application.restaurantstype.usecase.CreateRestaurantTypeUseCase;
import com.connectfood.core.application.restaurantstype.usecase.FindRestaurantTypeUseCase;
import com.connectfood.core.application.restaurantstype.usecase.RemoveRestaurantTypeUseCase;
import com.connectfood.core.application.restaurantstype.usecase.SearchRestaurantTypeUseCase;

import com.connectfood.core.application.restaurantstype.usecase.UpdateRestaurantTypeUseCase;

import com.connectfood.core.entrypoint.rest.dto.commons.BaseResponse;
import com.connectfood.core.entrypoint.rest.dto.commons.PageResponse;
import com.connectfood.core.entrypoint.rest.dto.restaurantstype.RestaurantsTypeRequest;
import com.connectfood.core.entrypoint.rest.dto.restaurantstype.RestaurantsTypeResponse;
import com.connectfood.core.entrypoint.rest.mappers.RestaurantsTypeEntryMapper;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/v1/restaurants_types")
@AllArgsConstructor
public class RestaurantsTypeController {

  private final SearchRestaurantTypeUseCase searchUseCase;
  private final FindRestaurantTypeUseCase findUseCase;
  private final CreateRestaurantTypeUseCase createUseCase;
  private final UpdateRestaurantTypeUseCase updateUseCase;
  private final RemoveRestaurantTypeUseCase removeUseCase;
  private final RestaurantsTypeEntryMapper mapper;

  @GetMapping
  public ResponseEntity<PageResponse<List<RestaurantsTypeResponse>>> search(
      @RequestParam(required = false) final String name,
      @RequestParam(defaultValue = "0") final Integer page,
      @RequestParam(defaultValue = "10") final Integer size,
      @RequestParam(required = false) final String sort,
      @RequestParam(required = false) final String direction
  ) {
    final var result =  searchUseCase.execute(name, page, size, sort, direction);

    final var response = result.content()
        .stream()
        .map(entity -> mapper.toResponse(entity))
        .toList();

    return ResponseEntity.ok()
        .body(new PageResponse<>(response, result.total(), page, size));
  }

  @GetMapping("/{uuid}")
  public ResponseEntity<BaseResponse<RestaurantsTypeResponse>> find(@PathVariable("uuid") final UUID uuid) {
    final var result = findUseCase.execute(uuid);
    final var response = mapper.toResponse(result);

    return ResponseEntity.ok().body(new BaseResponse<>(response));
  }

  @PostMapping
  public ResponseEntity<BaseResponse<RestaurantsTypeResponse>> create(@Valid @RequestBody final RestaurantsTypeRequest request) {
    final var result = createUseCase.execute(mapper.toInput(request));
    final var response = mapper.toResponse(result);

    return ResponseEntity.status(HttpStatus.CREATED).
        body(new BaseResponse<>(response));
  }

  @PutMapping(path = "/{uuid}")
  public ResponseEntity<BaseResponse<RestaurantsTypeResponse>> update(
      @PathVariable("uuid") final UUID uuid,
      @Valid @RequestBody final RestaurantsTypeRequest request
  ) {
    final var result = updateUseCase.execute(uuid, mapper.toInput(request));
    final var response = mapper.toResponse(result);

    return ResponseEntity.ok().body(new BaseResponse<>(response));
  }

  @DeleteMapping(path = "/{uuid}")
  public ResponseEntity<Void> delete(@PathVariable("uuid") final UUID uuid) {
    removeUseCase.execute(uuid);

    return ResponseEntity.noContent().build();
  }
}
