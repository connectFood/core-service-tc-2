package com.connectfood.core.entrypoint.rest.controller;

import com.connectfood.core.application.usercase.users.CreateUserUseCase;
import com.connectfood.core.application.usercase.users.DeleteUserUseCase;
import com.connectfood.core.application.usercase.users.GetUserUseCase;
import com.connectfood.core.application.usercase.users.ListUsersUseCase;
import com.connectfood.core.application.usercase.users.UpdateUserUseCase;
import com.connectfood.core.domain.dto.commons.BaseResponse;
import com.connectfood.core.domain.dto.commons.PageResponse;
import com.connectfood.core.domain.dto.users.UserCreateRequest;
import com.connectfood.core.domain.dto.users.UserResponse;
import com.connectfood.core.domain.dto.users.UserUpdateRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Validated
@RestController
@RequestMapping("/v1/users")
@RequiredArgsConstructor
public class UsersController {

  private final CreateUserUseCase createUserUseCase;
  private final ListUsersUseCase listUsersUseCase;
  private final GetUserUseCase getUserUseCase;
  private final UpdateUserUseCase updateUserUseCase;
  private final DeleteUserUseCase deleteUserUseCase;

  @GetMapping
  public ResponseEntity<PageResponse<UserResponse>> listUsers(
      @Valid @RequestParam(value = "name", required = false) String name,
      @Valid @RequestParam(value = "page", required = false, defaultValue = "0") Integer page,
      @Valid @RequestParam(value = "size", required = false, defaultValue = "10") Integer size) {
//    final var result = listUsersUseCase.execute(name, page, size);

    return ResponseEntity.ok()
        .body(new PageResponse<>(null, 0L, page, size));
  }

  @GetMapping("/{uuid}")
  public ResponseEntity<BaseResponse<UserResponse>> getUserByUuid(@PathVariable("uuid") String uuid) {
//    final var result = getUserUseCase.execute(uuid);
    return ResponseEntity.ok()
        .body(new BaseResponse<>(null));
  }

  @PostMapping
  public ResponseEntity<BaseResponse<UserResponse>> createUser(@Valid @RequestBody UserCreateRequest request) {
//    final var result = createUserUseCase.execute(request);
    return ResponseEntity.status(HttpStatus.CREATED)
        .body(new BaseResponse<>(null));
  }

  @PutMapping("/{uuid}")
  public ResponseEntity<BaseResponse<UserResponse>> updateUser(@PathVariable("uuid") String uuid,
      @Valid @RequestBody UserUpdateRequest request) {
//    final var result = updateUserUseCase.execute(uuid, request);
    return ResponseEntity.ok()
        .body(new BaseResponse<>(null));
  }

  @DeleteMapping("/{uuid}")
  public ResponseEntity<Void> deleteUser(@PathVariable("uuid") String uuid) {
    deleteUserUseCase.execute(uuid);
    return ResponseEntity.noContent()
        .build();
  }
}
