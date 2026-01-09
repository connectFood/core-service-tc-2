package com.connectfood.core.entrypoint.rest.controller;

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
import com.connectfood.core.entrypoint.rest.dto.address.AddressRequest;
import com.connectfood.core.entrypoint.rest.dto.address.AddressResponse;
import com.connectfood.core.entrypoint.rest.dto.commons.BaseResponse;
import com.connectfood.core.entrypoint.rest.dto.commons.PageResponse;
import com.connectfood.core.entrypoint.rest.dto.restaurantopeninghours.RestaurantOpeningHoursRequest;
import com.connectfood.core.entrypoint.rest.dto.restaurantopeninghours.RestaurantOpeningHoursResponse;
import com.connectfood.core.entrypoint.rest.dto.restaurants.RestaurantsRequest;
import com.connectfood.core.entrypoint.rest.dto.restaurants.RestaurantsResponse;
import com.connectfood.core.entrypoint.rest.mappers.AddressEntryMapper;
import com.connectfood.core.entrypoint.rest.mappers.RestaurantOpeningHoursEntryMapper;
import com.connectfood.core.entrypoint.rest.mappers.RestaurantsEntryMapper;

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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/v1/restaurants")
@Tag(name = "Restaurants Controller", description = "Operations related to restaurants management")
public class RestaurantsController {

  private final SearchRestaurantsUseCase searchUseCase;
  private final FindRestaurantsUseCase findUseCase;
  private final CreateRestaurantsUseCase createUseCase;
  private final UpdateRestaurantsUseCase updateUseCase;
  private final RemoveRestaurantsUseCase removeUseCase;
  private final RestaurantsEntryMapper mapper;
  private final CreateRestaurantOpeningHoursUseCase createRestaurantOpeningHoursUseCase;
  private final UpdateRestaurantOpeningHoursUseCase updateRestaurantOpeningHoursUseCase;
  private final RemoveRestaurantOpeningHoursUseCase removeRestaurantOpeningHoursUseCase;
  private final RestaurantOpeningHoursEntryMapper restaurantOpeningHoursMapper;
  private final CreateRestaurantsAddressUseCase createRestaurantsAddressUseCase;
  private final UpdateRestaurantsAddressUseCase updateRestaurantsAddressUseCase;
  private final RemoveRestaurantsAddressUseCase removeRestaurantsAddressUseCase;
  private final AddressEntryMapper addressMapper;

  public RestaurantsController(
      final SearchRestaurantsUseCase searchUseCase,
      final FindRestaurantsUseCase findUseCase,
      final CreateRestaurantsUseCase createUseCase,
      final UpdateRestaurantsUseCase updateUseCase,
      final RemoveRestaurantsUseCase removeUseCase,
      final RestaurantsEntryMapper mapper,
      final CreateRestaurantOpeningHoursUseCase createRestaurantOpeningHoursUseCase,
      final UpdateRestaurantOpeningHoursUseCase updateRestaurantOpeningHoursUseCase,
      final RemoveRestaurantOpeningHoursUseCase removeRestaurantOpeningHoursUseCase,
      final RestaurantOpeningHoursEntryMapper restaurantOpeningHoursMapper,
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

  @GetMapping
  @Operation(
      summary = "Search Restaurants with filters and pagination",
      description = "Returns paginated list of restaurants filtered by the given parameters"
  )
  public ResponseEntity<PageResponse<List<RestaurantsResponse>>> search(
      @RequestParam(required = false) final String name,
      @RequestParam(required = false) final UUID restaurantsTypeUuid,
      @RequestParam(required = false) final String street,
      @RequestParam(required = false) final String city,
      @RequestParam(required = false) final String state,
      @RequestParam(defaultValue = "0") final Integer page,
      @RequestParam(defaultValue = "10") final Integer size,
      @RequestParam(required = false) final String sort,
      @RequestParam(required = false) final String direction
  ) {
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

  @GetMapping(path = "/{uuid}")
  @Operation(
      summary = "Find restaurant by UUID",
      description = "Returns a restaurants for the given UUID"
  )
  public ResponseEntity<BaseResponse<RestaurantsResponse>> findByUuid(@PathVariable("uuid") final UUID uuid) {

    final var result = findUseCase.execute(uuid);
    final var response = mapper.toResponse(result);

    return ResponseEntity.ok()
        .body(new BaseResponse<>(response));
  }

  @PostMapping
  @Operation(
      summary = "Create a new restaurant",
      description = "Create a new restaurant and returns the created resource"
  )
  public ResponseEntity<BaseResponse<RestaurantsResponse>> create(
      @Valid @RequestBody final RestaurantsRequest request) {

    final var result = createUseCase.execute(mapper.toInput(request));
    final var response = mapper.toResponse(result);

    return ResponseEntity.status(HttpStatus.CREATED)
        .body(new BaseResponse<>(response));
  }

  @PostMapping(path = "/{uuid}/opening_hours")
  @Operation(
      summary = "Create a new restaurant opening hours",
      description = "Create a new restaurant opening hours and returns the created resource"
  )
  public ResponseEntity<BaseResponse<RestaurantOpeningHoursResponse>> createOpeningHours(
      @PathVariable final UUID uuid,
      @Valid @RequestBody final RestaurantOpeningHoursRequest request) {

    final var result = createRestaurantOpeningHoursUseCase.execute(uuid, restaurantOpeningHoursMapper.toInput(request));
    final var response = restaurantOpeningHoursMapper.toResponse(result);

    return ResponseEntity.status(HttpStatus.CREATED)
        .body(new BaseResponse<>(response));
  }

  @PostMapping(path = "/{uuid}/address")
  @Operation(
      summary = "Create a new restaurant address",
      description = "Create a new restaurant address and returns the created resource"
  )
  public ResponseEntity<BaseResponse<AddressResponse>> createAddress(
      @PathVariable final UUID uuid,
      @Valid @RequestBody final AddressRequest request) {

    final var result = createRestaurantsAddressUseCase.execute(uuid, addressMapper.toInput(request));
    final var response = addressMapper.toResponse(result);

    return ResponseEntity.status(HttpStatus.CREATED)
        .body(new BaseResponse<>(response));
  }

  @PutMapping(path = "/{uuid}")
  @Operation(
      summary = "Update an existing restaurant",
      description = "Updates an existing restaurants identified by UUID"
  )
  public ResponseEntity<BaseResponse<RestaurantsResponse>> update(
      @PathVariable final UUID uuid,
      @Valid @RequestBody final RestaurantsRequest request
  ) {

    final var result = updateUseCase.execute(uuid, mapper.toInput(request));
    final var response = mapper.toResponse(result);

    return ResponseEntity.ok()
        .body(new BaseResponse<>(response));
  }

  @PutMapping(path = "/opening_hours/{openingHoursUuid}")
  @Operation(
      summary = "Update an existing restaurant opening hours",
      description = "Updates an existing restaurant opening hours identified by UUID"
  )
  public ResponseEntity<BaseResponse<RestaurantOpeningHoursResponse>> update(
      @PathVariable final UUID openingHoursUuid,
      @Valid @RequestBody final RestaurantOpeningHoursRequest request
  ) {
    final var result = updateRestaurantOpeningHoursUseCase.execute(openingHoursUuid,
        restaurantOpeningHoursMapper.toInput(request)
    );
    final var response = restaurantOpeningHoursMapper.toResponse(result);

    return ResponseEntity.ok()
        .body(new BaseResponse<>(response));
  }

  @PutMapping(path = "/{uuid}/address")
  @Operation(
      summary = "Update an existing restaurant address",
      description = "Updates an existing restaurants address identified by UUID"
  )
  public ResponseEntity<BaseResponse<AddressResponse>> updateAddress(
      @PathVariable final UUID uuid,
      @Valid @RequestBody final AddressRequest request) {

    final var result = updateRestaurantsAddressUseCase.execute(uuid, addressMapper.toInput(request));
    final var response = addressMapper.toResponse(result);

    return ResponseEntity.ok()
        .body(new BaseResponse<>(response));
  }

  @DeleteMapping(path = "/{uuid}")
  @Operation(
      summary = "Delete an existing restaurant",
      description = "Delete and existing restaurant identified by UUID"
  )
  public ResponseEntity<Void> delete(@PathVariable("uuid") final UUID uuid) {
    removeUseCase.execute(uuid);

    return ResponseEntity.noContent()
        .build();
  }

  @DeleteMapping(path = "/opening_hours/{openingHoursUuid}")
  @Operation(
      summary = "Delete an existing restaurant opening hours",
      description = "Deletes an existing restaurant opening hours identified by UUID"
  )
  public ResponseEntity<Void> deleteOpeningHoursUuid(@PathVariable final UUID openingHoursUuid) {
    removeRestaurantOpeningHoursUseCase.execute(openingHoursUuid);

    return ResponseEntity.noContent()
        .build();
  }

  @DeleteMapping(path = "/{uuid}/address")
  @Operation(
      summary = "Delete an existing restaurant address",
      description = "Deletes an existing restaurant address identified by UUID"
  )
  public ResponseEntity<Void> deleteAddress(@PathVariable final UUID uuid) {
    removeRestaurantsAddressUseCase.execute(uuid);

    return ResponseEntity.noContent()
        .build();
  }
}
