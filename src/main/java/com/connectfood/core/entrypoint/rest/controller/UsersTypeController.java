package com.connectfood.core.entrypoint.rest.controller;

import java.util.List;

import com.connectfood.core.application.usertype.usecase.SearchUserTypeUseCase;
import com.connectfood.core.entrypoint.rest.dto.commons.BaseResponse;
import com.connectfood.core.entrypoint.rest.dto.userstype.UsersTypeResponse;
import com.connectfood.core.entrypoint.rest.mappers.UsersTypeEntryMapper;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;


@RestController
@RequestMapping("/v1/users_types")
@Tag(name = "Users Type Controller", description = "Operations related to user types management")
public class UsersTypeController {

  private final SearchUserTypeUseCase searchUseCase;
  private final UsersTypeEntryMapper mapper;

  public UsersTypeController(
      final SearchUserTypeUseCase searchUseCase,
      final UsersTypeEntryMapper mapper) {
    this.searchUseCase = searchUseCase;
    this.mapper = mapper;
  }

  @GetMapping
  @Operation(
      summary = "Search all user types",
      description = "Returns a list of all registered user types"
  )
  public ResponseEntity<BaseResponse<List<UsersTypeResponse>>> search() {
    final var response = searchUseCase.execute()
        .stream()
        .map(mapper::toResponse)
        .toList();

    return ResponseEntity.ok()
        .body(new BaseResponse<>(response));
  }
}
