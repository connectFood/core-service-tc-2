package com.connectfood.infrastructure.rest.controller.docs;

import java.util.List;
import java.util.UUID;

import com.connectfood.infrastructure.rest.dto.commons.BaseResponse;
import com.connectfood.infrastructure.rest.dto.commons.PageResponse;
import com.connectfood.infrastructure.rest.dto.userstype.UsersTypeRequest;
import com.connectfood.infrastructure.rest.dto.userstype.UsersTypeResponse;

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

@RequestMapping("/v1/users-types")
@Tag(name = "Users Type Controller", description = "Operations related to user types management")
public interface UsersTypeControllerApi {

  @GetMapping
  @Operation(
      summary = "Search user types with filters and pagination",
      description = "Returns a paginated list of user types filtered by given parameters"
  )
  ResponseEntity<PageResponse<List<UsersTypeResponse>>> search(
      @RequestParam(required = false) final String name,
      @RequestParam(defaultValue = "0") final Integer page,
      @RequestParam(defaultValue = "10") final Integer size,
      @RequestParam(required = false) final String sort,
      @RequestParam(required = false) final String direction);

  @GetMapping(path = "/{uuid}")
  @Operation(
      summary = "Find user type by UUID",
      description = "Returns a user type for the given UUID"
  )
  ResponseEntity<BaseResponse<UsersTypeResponse>> findByUuid(@PathVariable final UUID uuid);

  @PostMapping
  @Operation(
      summary = "Create a new user type",
      description = "Creates a new user type and returns the created resource"
  )
  ResponseEntity<BaseResponse<UsersTypeResponse>> create(@Valid @RequestBody final UsersTypeRequest request);

  @PutMapping(path = "/{uuid}")
  @Operation(
      summary = "Update an existing user type",
      description = "Updates an existing user type identified by UUID"
  )
  ResponseEntity<BaseResponse<UsersTypeResponse>> update(
      @PathVariable final UUID uuid, @Valid @RequestBody final UsersTypeRequest request);

  @DeleteMapping(path = "/{uuid}")
  @Operation(
      summary = "Delete an existing user type",
      description = "Deletes an existing user type identified by UUID"
  )
  ResponseEntity<Void> delete(@PathVariable final UUID uuid);
}
