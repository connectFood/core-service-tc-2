package com.connectfood.infrastructure.rest.dto.commons;

public record FieldErrorResponse(
    String field,
    String message
) {
}
