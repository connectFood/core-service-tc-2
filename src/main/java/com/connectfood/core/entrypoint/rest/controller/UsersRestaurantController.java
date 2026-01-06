package com.connectfood.core.entrypoint.rest.controller;

import java.util.UUID;

import com.connectfood.core.application.userrestaurant.usecase.CreateUsersRestaurantUseCase;
import com.connectfood.core.application.userrestaurant.usecase.FindUsersRestaurantUseCase;
import com.connectfood.core.application.userrestaurant.usecase.RemoveUsersRestaurantUseCase;
import com.connectfood.core.entrypoint.rest.dto.commons.BaseResponse;
import com.connectfood.core.entrypoint.rest.dto.userrestaurant.UsersRestaurantRequest;
import com.connectfood.core.entrypoint.rest.dto.userrestaurant.UsersRestaurantResponse;
import com.connectfood.core.entrypoint.rest.mappers.UsersRestaurantEntryMapper;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/v1/users-restaurant")
@Tag(name = "UsersRestaurant Controller", description = "Operations related to users and restaurants association")
public class UsersRestaurantController {

  private final CreateUsersRestaurantUseCase createUseCase;
  private final FindUsersRestaurantUseCase findUseCase;
  private final RemoveUsersRestaurantUseCase removeUseCase;
  private final UsersRestaurantEntryMapper mapper;

  public UsersRestaurantController(
      final CreateUsersRestaurantUseCase createUseCase,
      final FindUsersRestaurantUseCase findUseCase,
      final RemoveUsersRestaurantUseCase removeUseCase,
      final UsersRestaurantEntryMapper mapper) {
    this.createUseCase = createUseCase;
    this.findUseCase = findUseCase;
    this.removeUseCase = removeUseCase;
    this.mapper = mapper;
  }

  @GetMapping(path = "/users/{usersUuid}")
  @Operation(
      summary = "Find users-restaurant association by user UUID",
      description = "Returns the restaurant association for the given user UUID"
  )
  public ResponseEntity<BaseResponse<UsersRestaurantResponse>> findByUsersUuid(@PathVariable final UUID usersUuid) {
    final var result = findUseCase.execute(usersUuid);
    final var response = mapper.toResponse(result);

    return ResponseEntity.ok()
        .body(new BaseResponse<>(response));
  }

  @PostMapping
  @Operation(
      summary = "Create a new users-restaurant association",
      description = "Creates a new association between a user and a restaurant"
  )
  public ResponseEntity<BaseResponse<UsersRestaurantResponse>> create(
      @Valid @RequestBody final UsersRestaurantRequest request) {

    final var result = createUseCase.execute(mapper.toInput(request));
    final var response = mapper.toResponse(result);

    return ResponseEntity.status(HttpStatus.CREATED)
        .body(new BaseResponse<>(response));
  }

  @DeleteMapping(path = "/{uuid}")
  @Operation(
      summary = "Delete an existing users-restaurant association",
      description = "Deletes an association identified by UUID"
  )
  public ResponseEntity<Void> delete(@PathVariable final UUID uuid) {
    removeUseCase.execute(uuid);

    return ResponseEntity.noContent()
        .build();
  }
}

