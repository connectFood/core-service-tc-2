package com.connectfood.infrastructure.rest.dto.commons;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ProblemDetailsResponse(
    String type,
    String title,
    Integer status,
    String detail,
    String instance,
    List<FieldErrorResponse> errors
) {

  public ProblemDetailsResponse(
      String type,
      String title,
      Integer status,
      String detail,
      String instance
  ) {
    this(type, title, status, detail, instance, null);
  }
}
