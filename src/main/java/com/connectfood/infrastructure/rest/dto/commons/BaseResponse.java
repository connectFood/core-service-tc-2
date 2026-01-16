package com.connectfood.infrastructure.rest.dto.commons;

public record BaseResponse<T>(
    Boolean success,
    T content
) {
  public BaseResponse(T content) {
    this(true, content);
  }
}
