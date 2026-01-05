package com.connectfood.core.entrypoint.rest.controller;

import java.util.UUID;

import com.connectfood.core.application.address.usecase.CreateUsersAddressUseCase;
import com.connectfood.core.entrypoint.rest.dto.address.AddressRequest;
import com.connectfood.core.entrypoint.rest.dto.address.UsersAddressResponse;
import com.connectfood.core.entrypoint.rest.dto.commons.BaseResponse;
import com.connectfood.core.entrypoint.rest.mappers.AddressEntryMapper;
import com.connectfood.core.entrypoint.rest.mappers.UsersAddressEntryMapper;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
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

  public AddressController(
      final CreateUsersAddressUseCase createUsersAddressUseCase,
      final AddressEntryMapper mapper,
      final UsersAddressEntryMapper usersAddressMapper
  ) {
    this.createUsersAddressUseCase = createUsersAddressUseCase;
    this.mapper = mapper;
    this.usersAddressMapper = usersAddressMapper;
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
}
