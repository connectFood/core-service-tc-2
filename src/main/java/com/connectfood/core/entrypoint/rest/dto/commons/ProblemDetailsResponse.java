package com.connectfood.core.entrypoint.rest.dto.commons;

import java.util.List;

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
