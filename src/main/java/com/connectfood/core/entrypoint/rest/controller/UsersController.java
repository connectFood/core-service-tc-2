package com.connectfood.core.entrypoint.rest.controller;

import java.util.List;
import java.util.UUID;

import com.connectfood.core.application.users.usecase.CreateUsersUseCase;
import com.connectfood.core.application.users.usecase.FindUsersUseCase;
import com.connectfood.core.application.users.usecase.RemoveUsersUseCase;
import com.connectfood.core.application.users.usecase.SearchUsersUseCase;
import com.connectfood.core.application.users.usecase.UpdateUsersUseCase;
import com.connectfood.core.entrypoint.rest.dto.commons.BaseResponse;
import com.connectfood.core.entrypoint.rest.dto.commons.PageResponse;
import com.connectfood.core.entrypoint.rest.dto.users.UsersRequest;
import com.connectfood.core.entrypoint.rest.dto.users.UsersResponse;
import com.connectfood.core.entrypoint.rest.mappers.UsersEntryMapper;

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
@RequestMapping("/v1/users")
@Tag(name = "Users Controller", description = "Operations related to users management")
public class UsersController {

  private final SearchUsersUseCase searchUseCase;
  private final FindUsersUseCase findUseCase;
  private final CreateUsersUseCase createUseCase;
  private final UpdateUsersUseCase updateUseCase;
  private final RemoveUsersUseCase removeUseCase;
  private final UsersEntryMapper mapper;

  public UsersController(
      final SearchUsersUseCase searchUseCase,
      final FindUsersUseCase findUseCase,
      final CreateUsersUseCase createUseCase,
      final UpdateUsersUseCase updateUseCase,
      final RemoveUsersUseCase removeUseCase,
      final UsersEntryMapper mapper) {
    this.searchUseCase = searchUseCase;
    this.findUseCase = findUseCase;
    this.createUseCase = createUseCase;
    this.updateUseCase = updateUseCase;
    this.removeUseCase = removeUseCase;
    this.mapper = mapper;
  }

  @GetMapping
  @Operation(
      summary = "Search users with filters and pagination",
      description = "Returns a paginated list of users filtered by the given parameters"
  )
  public ResponseEntity<PageResponse<List<UsersResponse>>> search(
      @RequestParam(required = false) final String fullName,
      @RequestParam(required = false) final String email,
      @RequestParam(required = false) final UUID usersTypeUuid,
      @RequestParam(defaultValue = "0") final Integer page,
      @RequestParam(defaultValue = "10") final Integer size,
      @RequestParam(required = false) final String sort,
      @RequestParam(required = false) final String direction) {

    final var result = searchUseCase.execute(fullName, email, usersTypeUuid, page, size, sort, direction);

    final var response = result.content()
        .stream()
        .map(mapper::toResponse)
        .toList();

    return ResponseEntity.ok()
        .body(new PageResponse<>(response, result.total(), page, size));
  }

  @GetMapping(path = "/{uuid}")
  @Operation(
      summary = "Find user by UUID",
      description = "Returns a user for the given UUID"
  )
  public ResponseEntity<BaseResponse<UsersResponse>> findByUuid(@PathVariable final UUID uuid) {
    final var result = findUseCase.execute(uuid);
    final var response = mapper.toResponse(result);

    return ResponseEntity.ok()
        .body(new BaseResponse<>(response));
  }

  @PostMapping
  @Operation(
      summary = "Create a new user",
      description = "Creates a new user and returns the created resource"
  )
  public ResponseEntity<BaseResponse<UsersResponse>> create(@Valid @RequestBody final UsersRequest request) {
    final var result = createUseCase.execute(mapper.toInput(request));
    final var response = mapper.toResponse(result);

    return ResponseEntity.status(HttpStatus.CREATED)
        .body(new BaseResponse<>(response));
  }

  @PutMapping(path = "/{uuid}")
  @Operation(
      summary = "Update an existing user",
      description = "Updates an existing user identified by UUID"
  )
  public ResponseEntity<BaseResponse<UsersResponse>> update(
      @PathVariable final UUID uuid,
      @Valid @RequestBody final UsersRequest request) {

    final var result = updateUseCase.execute(uuid, mapper.toInput(request));
    final var response = mapper.toResponse(result);

    return ResponseEntity.ok()
        .body(new BaseResponse<>(response));
  }

  @DeleteMapping(path = "/{uuid}")
  @Operation(
      summary = "Delete an existing user",
      description = "Deletes an existing user identified by UUID"
  )
  public ResponseEntity<Void> delete(@PathVariable final UUID uuid) {
    removeUseCase.execute(uuid);

    return ResponseEntity.noContent()
        .build();
  }
}
