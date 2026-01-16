package com.connectfood.infrastructure.rest.controller;

import java.util.List;
import java.util.UUID;

import com.connectfood.core.application.usertype.usecase.CreateUserTypeUseCase;
import com.connectfood.core.application.usertype.usecase.FindUserTypeUseCase;
import com.connectfood.core.application.usertype.usecase.RemoveUserTypeUseCase;
import com.connectfood.core.application.usertype.usecase.SearchUserTypeUseCase;
import com.connectfood.core.application.usertype.usecase.UpdateUserTypeUseCase;
import com.connectfood.infrastructure.rest.controller.docs.UsersTypeControllerApi;
import com.connectfood.infrastructure.rest.dto.commons.BaseResponse;
import com.connectfood.infrastructure.rest.dto.commons.PageResponse;
import com.connectfood.infrastructure.rest.dto.usertype.UserTypeRequest;
import com.connectfood.infrastructure.rest.dto.usertype.UserTypeResponse;
import com.connectfood.infrastructure.rest.mappers.UserTypeEntryMapper;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class UserTypeController implements UsersTypeControllerApi {

  private final SearchUserTypeUseCase searchUseCase;
  private final FindUserTypeUseCase findUseCase;
  private final CreateUserTypeUseCase createUseCase;
  private final UpdateUserTypeUseCase updateUseCase;
  private final RemoveUserTypeUseCase removeUseCase;
  private final UserTypeEntryMapper mapper;

  public UserTypeController(
      final SearchUserTypeUseCase searchUseCase,
      final FindUserTypeUseCase findUseCase,
      final CreateUserTypeUseCase createUseCase,
      final UpdateUserTypeUseCase updateUseCase,
      final RemoveUserTypeUseCase removeUseCase,
      final UserTypeEntryMapper mapper) {
    this.searchUseCase = searchUseCase;
    this.findUseCase = findUseCase;
    this.createUseCase = createUseCase;
    this.updateUseCase = updateUseCase;
    this.removeUseCase = removeUseCase;
    this.mapper = mapper;
  }

  @Override
  public ResponseEntity<PageResponse<List<UserTypeResponse>>> search(
      final String name,
      final Integer page,
      final Integer size,
      final String sort,
      final String direction) {
    final var result = searchUseCase.execute(name, page, size, sort, direction);

    final var response = result.content()
        .stream()
        .map(mapper::toResponse)
        .toList();

    return ResponseEntity.ok()
        .body(new PageResponse<>(response, result.total(), page, size));
  }

  @Override
  public ResponseEntity<BaseResponse<UserTypeResponse>> findByUuid(final UUID uuid) {
    final var result = findUseCase.execute(uuid);
    final var response = mapper.toResponse(result);

    return ResponseEntity.ok()
        .body(new BaseResponse<>(response));
  }

  @Override
  public ResponseEntity<BaseResponse<UserTypeResponse>> create(final UserTypeRequest request) {
    final var result = createUseCase.execute(mapper.toInput(request));
    final var response = mapper.toResponse(result);

    return ResponseEntity.status(HttpStatus.CREATED)
        .body(new BaseResponse<>(response));
  }

  @Override
  public ResponseEntity<BaseResponse<UserTypeResponse>> update(
      final UUID uuid, final UserTypeRequest request) {
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
