package com.connectfood.core.entrypoint.rest.handler;

import java.util.ArrayList;
import java.util.List;

import com.connectfood.core.domain.dto.commons.ProblemDetailsErrorsResponse;
import com.connectfood.core.domain.dto.commons.ProblemDetailsResponse;
import com.connectfood.core.domain.exception.BadRequestException;
import com.connectfood.core.domain.exception.ConflictException;
import com.connectfood.core.domain.exception.NotFoundException;
import com.connectfood.core.domain.exception.UnauthorizedException;
import com.connectfood.core.domain.factory.ProblemDetailsFactory;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import lombok.RequiredArgsConstructor;

@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {

  private final ProblemDetailsFactory problemDetailsFactory;

  @ExceptionHandler(NotFoundException.class)
  public ResponseEntity<ProblemDetailsResponse> handleNotFoundException(
      final NotFoundException exception, final HttpServletRequest request) {
    return buildApiErrorResponse(
        exception.getMessage(), HttpStatus.NOT_FOUND, request.getRequestURI());
  }

  @ExceptionHandler(BadRequestException.class)
  public ResponseEntity<ProblemDetailsResponse> handleBadRequestException(
      final BadRequestException exception, final HttpServletRequest request) {
    return buildApiErrorResponse(
        exception.getMessage(), HttpStatus.BAD_REQUEST, request.getRequestURI());
  }

  @ExceptionHandler(ConflictException.class)
  public ResponseEntity<ProblemDetailsResponse> handleConflictException(
      final ConflictException exception, final HttpServletRequest request) {
    return buildApiErrorResponse(
        exception.getMessage(), HttpStatus.CONFLICT, request.getRequestURI());
  }

  @ExceptionHandler(UnauthorizedException.class)
  public ResponseEntity<ProblemDetailsResponse> handleUnauthorizedException(
      final UnauthorizedException exception, final HttpServletRequest request) {
    return buildApiErrorResponse(
        exception.getMessage(), HttpStatus.UNAUTHORIZED, request.getRequestURI());
  }

  @ExceptionHandler(InternalAuthenticationServiceException.class)
  public ResponseEntity<ProblemDetailsResponse> handleInternalAuthenticationServiceException(
      final UnauthorizedException exception, final HttpServletRequest request) {
    return buildApiErrorResponse(
        exception.getMessage(), HttpStatus.UNAUTHORIZED, request.getRequestURI());
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ProblemDetailsResponse> handleValidation(
      final MethodArgumentNotValidException exception, final HttpServletRequest request) {
    List<ProblemDetailsErrorsResponse> errors = new ArrayList<>();
    exception
        .getBindingResult()
        .getFieldErrors()
        .forEach(error -> errors.add(new ProblemDetailsErrorsResponse(error.getField(), error.getDefaultMessage())));

    return buildApiErrorResponse(
        "Invalid input data", HttpStatus.BAD_REQUEST, request.getRequestURI(), errors);
  }

  @ExceptionHandler(ConstraintViolationException.class)
  public ResponseEntity<ProblemDetailsResponse> handleConstraintViolationException(
      final ConstraintViolationException exception, final HttpServletRequest request) {
    List<ProblemDetailsErrorsResponse> errors = new ArrayList<>();
    exception
        .getConstraintViolations()
        .forEach(error -> errors.add(new ProblemDetailsErrorsResponse(error.getPropertyPath()
            .toString(), error.getMessage()
        )));

    return buildApiErrorResponse(
        "Invalid input data", HttpStatus.BAD_REQUEST, request.getRequestURI(), errors);
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ProblemDetailsResponse> handleGeneric(
      final Exception exception, final HttpServletRequest request) {
    return buildApiErrorResponse(
        "Unexpected error", HttpStatus.INTERNAL_SERVER_ERROR, request.getRequestURI());
  }

  private ResponseEntity<ProblemDetailsResponse> buildApiErrorResponse(
      final String message, final HttpStatus status, final String path) {
    return buildApiErrorResponse(message, status, path, null);
  }

  private ResponseEntity<ProblemDetailsResponse> buildApiErrorResponse(
      final String message,
      final HttpStatus status,
      final String path,
      final List<ProblemDetailsErrorsResponse> errors) {

    String type = "https://httpstatuses.com/" + status.value();
    final var response = new ProblemDetailsResponse(type, status.getReasonPhrase(), status.value(), message, path,
        errors
    );
    return ResponseEntity.status(status)
        .body(response);
  }
}
