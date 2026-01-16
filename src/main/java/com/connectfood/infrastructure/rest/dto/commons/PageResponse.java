package com.connectfood.infrastructure.rest.dto.commons;

public record PageResponse<T>(
    T content,
    Long total,
    Integer page,
    Integer size
) {
}
