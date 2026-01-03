package com.connectfood.core.entrypoint.rest.controller;

import java.util.List;
import java.util.UUID;

import com.connectfood.core.application.address.usecase.CreateRestaurantsAddressUseCase;
import com.connectfood.core.application.address.usecase.CreateUsersAddressUseCase;
import com.connectfood.core.application.address.usecase.FindRestaurantsAddressUseCase;
import com.connectfood.core.application.address.usecase.RemoveRestaurantsAddressUseCase;
import com.connectfood.core.application.address.usecase.SearchRestaurantsAddressUseCase;
import com.connectfood.core.application.address.usecase.UpdateRestaurantsAddressUseCase;
import com.connectfood.core.application.restaurants.useCase.CreateRestaurantsUseCase;
import com.connectfood.core.entrypoint.rest.dto.address.AddressRequest;
import com.connectfood.core.entrypoint.rest.dto.address.RestaurantsAddressResponse;
import com.connectfood.core.entrypoint.rest.dto.address.UsersAddressResponse;
import com.connectfood.core.entrypoint.rest.dto.commons.BaseResponse;
import com.connectfood.core.entrypoint.rest.dto.commons.PageResponse;
import com.connectfood.core.entrypoint.rest.mappers.AddressEntryMapper;
import com.connectfood.core.entrypoint.rest.mappers.RestaurantsAddressEntryMapper;
import com.connectfood.core.entrypoint.rest.mappers.UsersAddressEntryMapper;

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
@RequestMapping("/v1/address")
@Tag(
    name = "Address",
    description = "Operations related to address management for system entities (users, restaurants, etc.)"
)
public class AddressController {

  private final CreateUsersAddressUseCase createUsersAddressUseCase;
  private final AddressEntryMapper mapper;
  private final UsersAddressEntryMapper usersAddressMapper;

  private final CreateRestaurantsAddressUseCase createRestaurantsAddressUseCase;
  private final FindRestaurantsAddressUseCase findRestaurantsAddressUseCase;
  private final RemoveRestaurantsAddressUseCase removeRestaurantsAddressUseCase;
  private SearchRestaurantsAddressUseCase searchRestaurantsAddressUseCase;
  private UpdateRestaurantsAddressUseCase updateRestaurantsAddressUseCase;
  private RestaurantsAddressEntryMapper restaurantsAddressMapper;

  public AddressController(
      final CreateUsersAddressUseCase createUsersAddressUseCase,
      final AddressEntryMapper mapper,
      final UsersAddressEntryMapper usersAddressMapper,
      final CreateRestaurantsAddressUseCase createRestaurantsAddressUseCase,
      final FindRestaurantsAddressUseCase findRestaurantsAddressUseCase,
      final RemoveRestaurantsAddressUseCase removeRestaurantsAddressUseCase,
      final SearchRestaurantsAddressUseCase searchRestaurantsAddressUseCase,
      final UpdateRestaurantsAddressUseCase updateRestaurantsAddressUseCase,
      final RestaurantsAddressEntryMapper restaurantsAddressMapper
  ) {
    this.createUsersAddressUseCase = createUsersAddressUseCase;
    this.mapper = mapper;
    this.usersAddressMapper = usersAddressMapper;
    this.createRestaurantsAddressUseCase = createRestaurantsAddressUseCase;
    this.findRestaurantsAddressUseCase = findRestaurantsAddressUseCase;
    this.removeRestaurantsAddressUseCase = removeRestaurantsAddressUseCase;
    this.searchRestaurantsAddressUseCase = searchRestaurantsAddressUseCase;
    this.updateRestaurantsAddressUseCase = updateRestaurantsAddressUseCase;
    this.restaurantsAddressMapper = restaurantsAddressMapper;
  }

  // METHODS BY USERS ADDRESS
  @PostMapping(path = "/{uuid}/users")
  @Operation(
      summary = "Create address for user",
      description = "Creates a new address associated with a user and returns the created address"
  )
  public ResponseEntity<BaseResponse<UsersAddressResponse>> createUsersAddress(
      @PathVariable final UUID uuid,
      @Valid @RequestBody final AddressRequest request
  ) {
    final var result = createUsersAddressUseCase.execute(uuid, mapper.toInput(request));
    final var response = usersAddressMapper.toResponse(result);

    return ResponseEntity.status(HttpStatus.CREATED)
        .body(new BaseResponse<>(response));
  }

  // METHODS BY RESTAURANTS ADDRESS
  @PostMapping(path = "/{uuid}/restaurants")
  public ResponseEntity<BaseResponse<RestaurantsAddressResponse>> createRestaurantsAddress(
      @PathVariable final UUID uuid,
      @Valid @RequestBody final AddressRequest request
  ) {
    final var result = createRestaurantsAddressUseCase.execute(uuid, mapper.toInput(request));
    final var response = restaurantsAddressMapper.toResponse(result);

    return ResponseEntity.status(HttpStatus.CREATED)
        .body(new BaseResponse<>(response));
  }

  @GetMapping
  @Operation(
      summary = "Search Restaurants Address with filters and pagination",
      description = "Returns paginated list of restaurants address filtered by the given parameters"
  )
  public ResponseEntity<PageResponse<List<RestaurantsAddressResponse>>> search(
      @RequestParam(required = false) final String city,
      @RequestParam(required = false) final String state,
      @RequestParam(required = false) final String country,
      @RequestParam(required = false) final UUID restaurantsUuid,
      @RequestParam(defaultValue = "0") final Integer page,
      @RequestParam(defaultValue = "10") final Integer size,
      @RequestParam(required = false) final String sort,
      @RequestParam(required = false) final String direction
  ) {
    final var result = searchRestaurantsAddressUseCase.execute(city, state, country, restaurantsUuid, page, size,
        sort, direction);

    final var response = result.content()
        .stream()
        .map(restaurantsAddressMapper::toResponse)
        .toList();

    return ResponseEntity.ok()
        .body(new PageResponse<>(response, result.total(), page, size));
  }

  @GetMapping("/{uuid}/restaurants")
  @Operation(
      summary = "Find restaurant address by UUID",
      description = "Returns a restaurants for the given UUID"
  )
  public ResponseEntity<BaseResponse<RestaurantsAddressResponse>> findByUuid(@PathVariable("uuid") final UUID uuid) {

    final var result = findRestaurantsAddressUseCase.execute(uuid);
    final var response = restaurantsAddressMapper.toResponse(result);

    return ResponseEntity.ok()
        .body(new BaseResponse<>(response));
  }

  @PutMapping(path = "/{uuid}/restaurants")
  @Operation(
      summary = "Update an existing restaurant address",
      description = "Updates and existing restaurants identified by UUID"
  )
  public ResponseEntity<BaseResponse<RestaurantsAddressResponse>> update(
      @PathVariable("uuid") final UUID uuid,
      @Valid @RequestBody final AddressRequest request
  ) {

    final var result = updateRestaurantsAddressUseCase.execute(uuid, mapper.toInput(request));
    final var response = restaurantsAddressMapper.toResponse(result);

    return ResponseEntity.ok()
        .body(new BaseResponse<>(response));
  }

  @DeleteMapping(path = "/{uuid}/restaurants")
  @Operation(
      summary = "Delete an existing restaurant address",
      description = "Delete and existing restaurant address identified by UUID"
  )
  public ResponseEntity<Void> delete(@PathVariable("uuid") final UUID uuid) {

    removeRestaurantsAddressUseCase.execute(uuid);
    return ResponseEntity.noContent().build();
  }
}
