package com.connectfood.core.entrypoint.rest.controller;

import java.util.List;
import java.util.UUID;

import com.connectfood.core.application.usertype.usecase.CreateUserTypeUseCase;
import com.connectfood.core.application.usertype.usecase.FindUserTypeUseCase;
import com.connectfood.core.application.usertype.usecase.RemoveUserTypeUseCase;
import com.connectfood.core.application.usertype.usecase.SearchUserTypeUseCase;
import com.connectfood.core.application.usertype.usecase.UpdateUserTypeUseCase;
import com.connectfood.core.entrypoint.rest.dto.commons.BaseResponse;
import com.connectfood.core.entrypoint.rest.dto.commons.PageResponse;
import com.connectfood.core.entrypoint.rest.dto.userstype.UsersTypeRequest;
import com.connectfood.core.entrypoint.rest.dto.userstype.UsersTypeResponse;
import com.connectfood.core.entrypoint.rest.mappers.UsersTypeEntryMapper;

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
@RequestMapping("/v1/users_types")
@Tag(name = "Users Type Controller", description = "Operations related to user types management")
public class UsersTypeController {

  private final SearchUserTypeUseCase searchUseCase;
  private final FindUserTypeUseCase findUseCase;
  private final CreateUserTypeUseCase createUseCase;
  private final UpdateUserTypeUseCase updateUseCase;
  private final RemoveUserTypeUseCase removeUseCase;
  private final UsersTypeEntryMapper mapper;

  public UsersTypeController(
      final SearchUserTypeUseCase searchUseCase,
      final FindUserTypeUseCase findUseCase,
      final CreateUserTypeUseCase createUseCase,
      final UpdateUserTypeUseCase updateUseCase,
      final RemoveUserTypeUseCase removeUseCase,
      final UsersTypeEntryMapper mapper) {
    this.searchUseCase = searchUseCase;
    this.findUseCase = findUseCase;
    this.createUseCase = createUseCase;
    this.updateUseCase = updateUseCase;
    this.removeUseCase = removeUseCase;
    this.mapper = mapper;
  }

  @GetMapping
  @Operation(
      summary = "Search user types with filters and pagination",
      description = "Returns a paginated list of user types filtered by given parameters"
  )
  public ResponseEntity<PageResponse<List<UsersTypeResponse>>> search(
      @RequestParam(required = false) final String name,
      @RequestParam(defaultValue = "0") final Integer page,
      @RequestParam(defaultValue = "10") final Integer size,
      @RequestParam(required = false) final String sort,
      @RequestParam(required = false) final String direction) {
    final var result = searchUseCase.execute(name, page, size, sort, direction);

    final var response = result.content()
        .stream()
        .map(mapper::toResponse)
        .toList();

    return ResponseEntity.ok()
        .body(new PageResponse<>(response, result.total(), page, size));
  }

  @GetMapping(path = "/{uuid}")
  @Operation(
      summary = "Find user type by UUID",
      description = "Returns a user type for the given UUID"
  )
  public ResponseEntity<BaseResponse<UsersTypeResponse>> findByUuid(@PathVariable("uuid") final UUID uuid) {
    final var result = findUseCase.execute(uuid);
    final var response = mapper.toResponse(result);

    return ResponseEntity.ok()
        .body(new BaseResponse<>(response));
  }

  @PostMapping
  @Operation(
      summary = "Create a new user type",
      description = "Creates a new user type and returns the created resource"
  )
  public ResponseEntity<BaseResponse<UsersTypeResponse>> create(@Valid @RequestBody final UsersTypeRequest request) {
    final var result = createUseCase.execute(mapper.toInput(request));
    final var response = mapper.toResponse(result);

    return ResponseEntity.status(HttpStatus.CREATED)
        .body(new BaseResponse<>(response));
  }

  @PutMapping(path = "/{uuid}")
  @Operation(
      summary = "Update an existing user type",
      description = "Updates an existing user type identified by UUID"
  )
  public ResponseEntity<BaseResponse<UsersTypeResponse>> update(
      @PathVariable("uuid") final UUID uuid, @Valid @RequestBody final UsersTypeRequest request) {
    final var result = updateUseCase.execute(uuid, mapper.toInput(request));
    final var response = mapper.toResponse(result);

    return ResponseEntity.ok()
        .body(new BaseResponse<>(response));
  }

  @DeleteMapping(path = "/{uuid}")
  @Operation(
      summary = "Delete an existing user type",
      description = "Deletes an existing user type identified by UUID"
  )
  public ResponseEntity<Void> delete(@PathVariable("uuid") final UUID uuid) {
    removeUseCase.execute(uuid);

    return ResponseEntity.noContent()
        .build();
  }
}
