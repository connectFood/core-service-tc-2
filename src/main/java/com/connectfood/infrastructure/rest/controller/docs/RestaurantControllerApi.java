package com.connectfood.infrastructure.rest.controller.docs;

import java.util.List;
import java.util.UUID;

import com.connectfood.infrastructure.rest.dto.address.AddressRequest;
import com.connectfood.infrastructure.rest.dto.address.AddressResponse;
import com.connectfood.infrastructure.rest.dto.commons.BaseResponse;
import com.connectfood.infrastructure.rest.dto.commons.PageResponse;
import com.connectfood.infrastructure.rest.dto.restaurantopeninghour.RestaurantOpeningHourRequest;
import com.connectfood.infrastructure.rest.dto.restaurantopeninghour.RestaurantOpeningHourResponse;
import com.connectfood.infrastructure.rest.dto.restaurant.RestaurantRequest;
import com.connectfood.infrastructure.rest.dto.restaurant.RestaurantResponse;

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

@RequestMapping("/v1/restaurants")
@Tag(name = "Restaurants Controller", description = "Operations related to restaurants management")
public interface RestaurantControllerApi {

  @GetMapping
  @Operation(
      summary = "Search Restaurants with filters and pagination",
      description = "Returns paginated list of restaurants filtered by the given parameters"
  )
  ResponseEntity<PageResponse<List<RestaurantResponse>>> search(
      @RequestParam(required = false) final String name,
      @RequestParam(required = false) final UUID restaurantsTypeUuid,
      @RequestParam(required = false) final String street,
      @RequestParam(required = false) final String city,
      @RequestParam(required = false) final String state,
      @RequestParam(defaultValue = "0") final Integer page,
      @RequestParam(defaultValue = "10") final Integer size,
      @RequestParam(required = false) final String sort,
      @RequestParam(required = false) final String direction);

  @GetMapping(path = "/{uuid}")
  @Operation(
      summary = "Find restaurant by UUID",
      description = "Returns a restaurants for the given UUID"
  )
  ResponseEntity<BaseResponse<RestaurantResponse>> findByUuid(@PathVariable final UUID uuid);

  @PostMapping
  @Operation(
      summary = "Create a new restaurant",
      description = "Create a new restaurant and returns the created resource"
  )
  ResponseEntity<BaseResponse<RestaurantResponse>> create(
      @RequestHeader(name = "Request-User-Uuid") final UUID requestUserUuid,
      @Valid @RequestBody final RestaurantRequest request);

  @PostMapping(path = "/{uuid}/opening-hours")
  @Operation(
      summary = "Create a new restaurant opening hours",
      description = "Create a new restaurant opening hours and returns the created resource"
  )
  ResponseEntity<BaseResponse<RestaurantOpeningHourResponse>> createOpeningHours(
      @RequestHeader(name = "Request-User-Uuid") final UUID requestUserUuid,
      @PathVariable final UUID uuid, @Valid @RequestBody final RestaurantOpeningHourRequest request);

  @PostMapping(path = "/{uuid}/address")
  @Operation(
      summary = "Create a new restaurant address",
      description = "Create a new restaurant address and returns the created resource"
  )
  ResponseEntity<BaseResponse<AddressResponse>> createAddress(
      @RequestHeader(name = "Request-User-Uuid") final UUID requestUserUuid,
      @PathVariable final UUID uuid, @Valid @RequestBody final AddressRequest request);

  @PutMapping(path = "/{uuid}")
  @Operation(
      summary = "Update an existing restaurant",
      description = "Updates an existing restaurants identified by UUID"
  )
  ResponseEntity<BaseResponse<RestaurantResponse>> update(
      @RequestHeader(name = "Request-User-Uuid") final UUID requestUserUuid,
      @PathVariable final UUID uuid, @Valid @RequestBody final RestaurantRequest request);

  @PutMapping(path = "/opening-hours/{openingHoursUuid}")
  @Operation(
      summary = "Update an existing restaurant opening hours",
      description = "Updates an existing restaurant opening hours identified by UUID"
  )
  ResponseEntity<BaseResponse<RestaurantOpeningHourResponse>> update(
      @RequestHeader(name = "Request-User-Uuid") final UUID requestUserUuid,
      @PathVariable final UUID openingHoursUuid, @Valid @RequestBody final RestaurantOpeningHourRequest request);

  @PutMapping(path = "/{uuid}/address")
  @Operation(
      summary = "Update an existing restaurant address",
      description = "Updates an existing restaurants address identified by UUID"
  )
  ResponseEntity<BaseResponse<AddressResponse>> updateAddress(
      @RequestHeader(name = "Request-User-Uuid") final UUID requestUserUuid,
      @PathVariable final UUID uuid, @Valid @RequestBody final AddressRequest request);

  @DeleteMapping(path = "/{uuid}")
  @Operation(
      summary = "Delete an existing restaurant",
      description = "Delete and existing restaurant identified by UUID"
  )
  ResponseEntity<Void> delete(
      @RequestHeader(name = "Request-User-Uuid") final UUID requestUserUuid, @PathVariable final UUID uuid);

  @DeleteMapping(path = "/opening-hours/{openingHoursUuid}")
  @Operation(
      summary = "Delete an existing restaurant opening hours",
      description = "Deletes an existing restaurant opening hours identified by UUID"
  )
  ResponseEntity<Void> deleteOpeningHoursUuid(
      @RequestHeader(name = "Request-User-Uuid") final UUID requestUserUuid, @PathVariable final UUID openingHoursUuid);

  @DeleteMapping(path = "/{uuid}/address")
  @Operation(
      summary = "Delete an existing restaurant address",
      description = "Deletes an existing restaurant address identified by UUID"
  )
  ResponseEntity<Void> deleteAddress(
      @RequestHeader(name = "Request-User-Uuid") final UUID requestUserUuid, @PathVariable final UUID uuid);
}
