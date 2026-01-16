package com.connectfood.infrastructure.rest.handlers;

import java.util.Set;

import com.connectfood.core.domain.exception.BadRequestException;
import com.connectfood.core.domain.exception.ConflictException;
import com.connectfood.core.domain.exception.NotFoundException;
import com.connectfood.infrastructure.rest.dto.commons.FieldErrorResponse;
import com.connectfood.infrastructure.rest.dto.commons.ProblemDetailsResponse;
import com.connectfood.infrastructure.rest.handlers.GlobalExceptionHandler;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Path;

@ExtendWith(MockitoExtension.class)
class GlobalExceptionHandlerTest {

  private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

  @Test
  @DisplayName("Deve tratar NotFoundException e retornar ProblemDetails com status 404")
  void shouldReturnNotFoundWhenNotFoundExceptionOccurs() {
    final var request = Mockito.mock(HttpServletRequest.class);
    Mockito.when(request.getRequestURI())
        .thenReturn("/restaurants-items/123");

    final var exception = new NotFoundException("Restaurant Items not found");

    final var response = handler.handleNotFoundException(exception, request);

    Assertions.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    Assertions.assertNotNull(response.getBody());

    final ProblemDetailsResponse body = response.getBody();
    Assertions.assertEquals("https://httpstatuses.com/404", body.type());
    Assertions.assertEquals("Not Found", body.title());
    Assertions.assertEquals(404, body.status());
    Assertions.assertEquals("Restaurant Items not found", body.detail());
    Assertions.assertEquals("/restaurants-items/123", body.instance());
    Assertions.assertNull(body.errors());
  }

  @Test
  @DisplayName("Deve tratar BadRequestException e retornar ProblemDetails com status 400")
  void shouldReturnBadRequestWhenBadRequestExceptionOccurs() {
    final var request = Mockito.mock(HttpServletRequest.class);
    Mockito.when(request.getRequestURI())
        .thenReturn("/restaurants-items");

    final var exception = new BadRequestException("Invalid data");

    final var response = handler.handleBadRequestException(exception, request);

    Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    Assertions.assertNotNull(response.getBody());

    final ProblemDetailsResponse body = response.getBody();
    Assertions.assertEquals("https://httpstatuses.com/400", body.type());
    Assertions.assertEquals("Bad Request", body.title());
    Assertions.assertEquals(400, body.status());
    Assertions.assertEquals("Invalid data", body.detail());
    Assertions.assertEquals("/restaurants-items", body.instance());
    Assertions.assertNull(body.errors());
  }

  @Test
  @DisplayName("Deve tratar ConflictException e retornar ProblemDetails com status 409")
  void shouldReturnConflictWhenConflictExceptionOccurs() {
    final var request = Mockito.mock(HttpServletRequest.class);
    Mockito.when(request.getRequestURI())
        .thenReturn("/restaurants-items");

    final var exception = new ConflictException("Conflict");

    final var response = handler.handleConflictException(exception, request);

    Assertions.assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
    Assertions.assertNotNull(response.getBody());

    final ProblemDetailsResponse body = response.getBody();
    Assertions.assertEquals("https://httpstatuses.com/409", body.type());
    Assertions.assertEquals("Conflict", body.title());
    Assertions.assertEquals(409, body.status());
    Assertions.assertEquals("Conflict", body.detail());
    Assertions.assertEquals("/restaurants-items", body.instance());
    Assertions.assertNull(body.errors());
  }

  @Test
  @DisplayName("Deve tratar MethodArgumentNotValidException e retornar ProblemDetails com errors e status 400")
  void shouldReturnBadRequestWhenMethodArgumentNotValidExceptionOccurs() {
    final var request = Mockito.mock(HttpServletRequest.class);
    Mockito.when(request.getRequestURI())
        .thenReturn("/restaurants-items");

    final var bindingResult = Mockito.mock(BindingResult.class);
    Mockito.when(bindingResult.getFieldErrors())
        .thenReturn(
            java.util.List.of(
                new FieldError("RestaurantItemsRequest", "name", "Name is required"),
                new FieldError("RestaurantItemsRequest", "value", "Value is required")
            )
        );

    final var exception = Mockito.mock(MethodArgumentNotValidException.class);
    Mockito.when(exception.getBindingResult())
        .thenReturn(bindingResult);

    final var response = handler.handleValidation(exception, request);

    Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    Assertions.assertNotNull(response.getBody());

    final var body = response.getBody();
    Assertions.assertEquals("https://httpstatuses.com/400", body.type());
    Assertions.assertEquals("Bad Request", body.title());
    Assertions.assertEquals(400, body.status());
    Assertions.assertEquals("Invalid input data", body.detail());
    Assertions.assertEquals("/restaurants-items", body.instance());

    Assertions.assertNotNull(body.errors());
    Assertions.assertEquals(2, body.errors()
        .size()
    );
    Assertions.assertEquals("name", body.errors()
        .get(0)
        .field()
    );
    Assertions.assertEquals("Name is required", body.errors()
        .get(0)
        .message()
    );
    Assertions.assertEquals("value", body.errors()
        .get(1)
        .field()
    );
    Assertions.assertEquals("Value is required", body.errors()
        .get(1)
        .message()
    );
  }

  @Test
  @DisplayName("Deve tratar ConstraintViolationException e retornar ProblemDetails com errors e status 400")
  void shouldReturnBadRequestWhenConstraintViolationExceptionOccurs() {
    final var request = Mockito.mock(HttpServletRequest.class);
    Mockito.when(request.getRequestURI())
        .thenReturn("/restaurants-items");

    final ConstraintViolation<?> violation1 = Mockito.mock(ConstraintViolation.class);
    final ConstraintViolation<?> violation2 = Mockito.mock(ConstraintViolation.class);

    final Path path1 = Mockito.mock(Path.class);
    final Path path2 = Mockito.mock(Path.class);

    Mockito.when(path1.toString())
        .thenReturn("name");
    Mockito.when(path2.toString())
        .thenReturn("requestType");

    Mockito.when(violation1.getPropertyPath())
        .thenReturn(path1);
    Mockito.when(violation1.getMessage())
        .thenReturn("Name is required");

    Mockito.when(violation2.getPropertyPath())
        .thenReturn(path2);
    Mockito.when(violation2.getMessage())
        .thenReturn("Request Type is required");

    final var exception = new ConstraintViolationException(Set.of(violation1, violation2));

    final var response = handler.handleConstraintViolationException(exception, request);

    Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    Assertions.assertNotNull(response.getBody());

    final var body = response.getBody();
    Assertions.assertEquals("https://httpstatuses.com/400", body.type());
    Assertions.assertEquals("Bad Request", body.title());
    Assertions.assertEquals(400, body.status());
    Assertions.assertEquals("Invalid input data", body.detail());
    Assertions.assertEquals("/restaurants-items", body.instance());

    Assertions.assertNotNull(body.errors());
    Assertions.assertEquals(2, body.errors()
        .size()
    );

    final var fields = body.errors()
        .stream()
        .map(FieldErrorResponse::field)
        .toList();
    final var messages = body.errors()
        .stream()
        .map(FieldErrorResponse::message)
        .toList();

    Assertions.assertTrue(fields.contains("name"));
    Assertions.assertTrue(fields.contains("requestType"));

    Assertions.assertTrue(messages.contains("Name is required"));
    Assertions.assertTrue(messages.contains("Request Type is required"));
  }

  @Test
  @DisplayName("Deve tratar Exception genérica e retornar ProblemDetails com status 500")
  void shouldReturnInternalServerErrorWhenGenericExceptionOccurs() {
    final var request = Mockito.mock(HttpServletRequest.class);
    Mockito.when(request.getRequestURI())
        .thenReturn("/restaurants-items");

    final var exception = new RuntimeException("Unexpected error");

    final var response = handler.handleGeneric(exception, request);

    Assertions.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    Assertions.assertNotNull(response.getBody());

    final var body = response.getBody();
    Assertions.assertEquals("https://httpstatuses.com/500", body.type());
    Assertions.assertEquals("Internal Server Error", body.title());
    Assertions.assertEquals(500, body.status());
    Assertions.assertEquals("Unexpected error", body.detail());
    Assertions.assertEquals("/restaurants-items", body.instance());
    Assertions.assertNull(body.errors());
  }
}
