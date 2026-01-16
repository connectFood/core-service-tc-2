package com.connectfood.infrastructure.rest.controller;

import java.util.List;
import java.util.UUID;

import com.connectfood.core.application.users.usecase.CreateUsersUseCase;
import com.connectfood.core.application.users.usecase.FindUsersUseCase;
import com.connectfood.core.application.users.usecase.RemoveUsersUseCase;
import com.connectfood.core.application.users.usecase.SearchUsersUseCase;
import com.connectfood.core.application.users.usecase.UpdateUsersUseCase;
import com.connectfood.infrastructure.rest.controller.docs.UsersControllerApi;
import com.connectfood.infrastructure.rest.dto.commons.BaseResponse;
import com.connectfood.infrastructure.rest.dto.commons.PageResponse;
import com.connectfood.infrastructure.rest.dto.users.UsersRequest;
import com.connectfood.infrastructure.rest.dto.users.UsersResponse;
import com.connectfood.infrastructure.rest.mappers.UsersEntryMapper;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UsersController implements UsersControllerApi {

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

  @Override
  public ResponseEntity<PageResponse<List<UsersResponse>>> search(
      final String fullName,
      final String email,
      final UUID usersTypeUuid,
      final Integer page,
      final Integer size,
      final String sort,
      final String direction) {

    final var result = searchUseCase.execute(fullName, email, usersTypeUuid, page, size, sort, direction);

    final var response = result.content()
        .stream()
        .map(mapper::toResponse)
        .toList();

    return ResponseEntity.ok()
        .body(new PageResponse<>(response, result.total(), page, size));
  }

  @Override
  public ResponseEntity<BaseResponse<UsersResponse>> findByUuid(final UUID uuid) {
    final var result = findUseCase.execute(uuid);
    final var response = mapper.toResponse(result);

    return ResponseEntity.ok()
        .body(new BaseResponse<>(response));
  }

  @Override
  public ResponseEntity<BaseResponse<UsersResponse>> create(final UsersRequest request) {
    final var result = createUseCase.execute(mapper.toInput(request));
    final var response = mapper.toResponse(result);

    return ResponseEntity.status(HttpStatus.CREATED)
        .body(new BaseResponse<>(response));
  }

  @Override
  public ResponseEntity<BaseResponse<UsersResponse>> update(
      final UUID uuid,
      final UsersRequest request) {

    final var result = updateUseCase.execute(uuid, mapper.toInput(request));
    final var response = mapper.toResponse(result);

    return ResponseEntity.ok()
        .body(new BaseResponse<>(response));
  }

  @Override
  public ResponseEntity<Void> delete(final UUID uuid) {
    removeUseCase.execute(uuid);

    return ResponseEntity.noContent()
        .build();
  }
}
