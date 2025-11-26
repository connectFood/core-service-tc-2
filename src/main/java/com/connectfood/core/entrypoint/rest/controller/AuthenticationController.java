package com.connectfood.core.entrypoint.rest.controller;

import com.connectfood.core.application.usercase.authentication.AuthenticationUseCase;
import com.connectfood.core.domain.dto.authentication.AuthenticationRequest;
import com.connectfood.core.domain.dto.authentication.AuthenticationResponse;
import com.connectfood.core.domain.dto.commons.BaseResponse;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Validated
@RestController
@RequestMapping(value = "/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {

  private final AuthenticationUseCase authenticationUseCase;

  @PostMapping("/login")
  public ResponseEntity<BaseResponse<AuthenticationResponse>> login(@Valid @RequestBody AuthenticationRequest request) {
//    final var response = authenticationUseCase.execute(request);
    return ResponseEntity.ok(new BaseResponse<>(null));
  }
}
