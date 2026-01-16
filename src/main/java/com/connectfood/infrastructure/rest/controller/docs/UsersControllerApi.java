package com.connectfood.infrastructure.rest.controller.docs;

import java.util.List;
import java.util.UUID;

import com.connectfood.infrastructure.rest.dto.commons.BaseResponse;
import com.connectfood.infrastructure.rest.dto.commons.PageResponse;
import com.connectfood.infrastructure.rest.dto.users.UsersRequest;
import com.connectfood.infrastructure.rest.dto.users.UsersResponse;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RequestMapping("/v1/users")
@Tag(name = "Users Controller", description = "Operations related to users management")
public interface UsersControllerApi {

  @GetMapping
  @Operation(
      summary = "Search users with filters and pagination",
      description = "Returns a paginated list of users filtered by the given parameters"
  )
  ResponseEntity<PageResponse<List<UsersResponse>>> search(
      @RequestParam(required = false) final String fullName,
      @RequestParam(required = false) final String email,
      @RequestParam(required = false) final UUID usersTypeUuid,
      @RequestParam(defaultValue = "0") final Integer page,
      @RequestParam(defaultValue = "10") final Integer size,
      @RequestParam(required = false) final String sort,
      @RequestParam(required = false) final String direction);

  @GetMapping(path = "/{uuid}")
  @Operation(
      summary = "Find user by UUID",
      description = "Returns a user for the given UUID"
  )
  ResponseEntity<BaseResponse<UsersResponse>> findByUuid(@PathVariable final UUID uuid);

  @PostMapping
  @Operation(
      summary = "Create a new user",
      description = "Creates a new user and returns the created resource"
  )
  ResponseEntity<BaseResponse<UsersResponse>> create(@Valid @RequestBody final UsersRequest request);

  @PutMapping(path = "/{uuid}")
  @Operation(
      summary = "Update an existing user",
      description = "Updates an existing user identified by UUID"
  )
  ResponseEntity<BaseResponse<UsersResponse>> update(
      @PathVariable final UUID uuid, @Valid @RequestBody final UsersRequest request);

  @DeleteMapping(path = "/{uuid}")
  @Operation(
      summary = "Delete an existing user",
      description = "Deletes an existing user identified by UUID"
  )
  ResponseEntity<Void> delete(@PathVariable final UUID uuid);
}
