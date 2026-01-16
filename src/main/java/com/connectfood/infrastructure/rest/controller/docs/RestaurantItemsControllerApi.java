package com.connectfood.infrastructure.rest.controller.docs;

import java.util.List;
import java.util.UUID;

import com.connectfood.infrastructure.rest.dto.commons.BaseResponse;
import com.connectfood.infrastructure.rest.dto.commons.PageResponse;
import com.connectfood.infrastructure.rest.dto.restaurantitems.RestaurantItemsRequest;
import com.connectfood.infrastructure.rest.dto.restaurantitems.RestaurantItemsResponse;

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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RequestMapping("/v1/restaurant-items")
@Tag(name = "Restaurant Items Controller", description = "Operations related to restaurant items management")
public interface RestaurantItemsControllerApi {

  @GetMapping
  @Operation(
      summary = "Search restaurant items with filters and pagination",
      description = "Returns paginated list of restaurant items filtered by the given parameters"
  )
  ResponseEntity<PageResponse<List<RestaurantItemsResponse>>> search(
      @RequestParam(required = false) final UUID restaurantUuid,
      @RequestParam(defaultValue = "0") final Integer page,
      @RequestParam(defaultValue = "10") final Integer size,
      @RequestParam(required = false) final String sort,
      @RequestParam(required = false) final String direction);

  @GetMapping(path = "/{uuid}")
  @Operation(
      summary = "Find restaurant item by UUID",
      description = "Returns a restaurant item for the given UUID"
  )
  ResponseEntity<BaseResponse<RestaurantItemsResponse>> findByUuid(@PathVariable final UUID uuid);

  @PostMapping
  @Operation(
      summary = "Create a new restaurant item",
      description = "Creates a new restaurant item and returns the created resource"
  )
  ResponseEntity<BaseResponse<RestaurantItemsResponse>> create(
      @RequestHeader(name = "Request-User-Uuid") final UUID requestUserUuid,
      @Valid @RequestBody final RestaurantItemsRequest request);

  @PutMapping(path = "/{uuid}")
  @Operation(
      summary = "Update an existing restaurant item",
      description = "Updates an existing restaurant item identified by UUID"
  )
  ResponseEntity<BaseResponse<RestaurantItemsResponse>> update(
      @RequestHeader(name = "Request-User-Uuid") final UUID requestUserUuid,
      @PathVariable final UUID uuid, @Valid @RequestBody final RestaurantItemsRequest request);

  @DeleteMapping(path = "/{uuid}")
  @Operation(
      summary = "Delete an existing restaurant item",
      description = "Deletes an existing restaurant item identified by UUID"
  )
  ResponseEntity<Void> delete(
      @RequestHeader(name = "Request-User-Uuid") final UUID requestUserUuid, @PathVariable final UUID uuid);
}
