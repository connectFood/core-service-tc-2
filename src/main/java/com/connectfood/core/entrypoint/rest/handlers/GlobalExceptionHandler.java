package com.connectfood.core.entrypoint.rest.handlers;

import java.util.ArrayList;
import java.util.List;

import com.connectfood.core.domain.exception.BadRequestException;
import com.connectfood.core.domain.exception.ConflictException;
import com.connectfood.core.domain.exception.ForbiddenException;
import com.connectfood.core.domain.exception.NotFoundException;
import com.connectfood.core.entrypoint.rest.dto.commons.FieldErrorResponse;
import com.connectfood.core.entrypoint.rest.dto.commons.ProblemDetailsResponse;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;

@RestControllerAdvice
public class GlobalExceptionHandler {
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

  @ExceptionHandler(ForbiddenException.class)
  public ResponseEntity<ProblemDetailsResponse> handleForbiddenException(
      final ConflictException exception, final HttpServletRequest request) {
    return buildApiErrorResponse(
        exception.getMessage(), HttpStatus.FORBIDDEN, request.getRequestURI());
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ProblemDetailsResponse> handleValidation(
      final MethodArgumentNotValidException exception, final HttpServletRequest request) {
    List<FieldErrorResponse> errors = new ArrayList<>();
    exception
        .getBindingResult()
        .getFieldErrors()
        .forEach(error -> errors.add(new FieldErrorResponse(error.getField(), error.getDefaultMessage())));

    return buildApiErrorResponse(
        "Invalid input data", HttpStatus.BAD_REQUEST, request.getRequestURI(), errors);
  }

  @ExceptionHandler(ConstraintViolationException.class)
  public ResponseEntity<ProblemDetailsResponse> handleConstraintViolationException(
      final ConstraintViolationException exception, final HttpServletRequest request) {
    List<FieldErrorResponse> errors = new ArrayList<>();
    exception
        .getConstraintViolations()
        .forEach(error -> errors.add(new FieldErrorResponse(error.getPropertyPath()
            .toString(), error.getMessage()
        )));

    return buildApiErrorResponse(
        "Invalid input data", HttpStatus.BAD_REQUEST, request.getRequestURI(), errors);
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ProblemDetailsResponse> handleGeneric(
      final Exception exception, final HttpServletRequest request) {
    return buildApiErrorResponse(
        exception.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, request.getRequestURI());
  }

  private ResponseEntity<ProblemDetailsResponse> buildApiErrorResponse(
      final String message, final HttpStatus status, final String path) {
    final var type = "https://httpstatuses.com/" + status.value();
    return ResponseEntity.status(status)
        .body(new ProblemDetailsResponse(
            type,
            status.getReasonPhrase(),
            status.value(),
            message,
            path
        ));
  }

  private ResponseEntity<ProblemDetailsResponse> buildApiErrorResponse(
      final String message,
      final HttpStatus status,
      final String path,
      final List<FieldErrorResponse> errors) {

    final var type = "https://httpstatuses.com/" + status.value();
    return ResponseEntity.status(status)
        .body(new ProblemDetailsResponse(
            type,
            status.getReasonPhrase(),
            status.value(),
            message,
            path,
            errors
        ));
  }
}
