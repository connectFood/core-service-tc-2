package com.connectfood.core.entrypoint.rest.dto.commons;

public record PageResponse<T>(
    T content,
    Long total,
    Integer page,
    Integer size
) {
}
