package com.connectfood.core.entrypoint.rest.dto.commons;

public record FieldErrorResponse(
    String field,
    String message
) {
}
