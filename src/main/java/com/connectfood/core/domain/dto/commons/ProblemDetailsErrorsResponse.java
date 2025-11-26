package com.connectfood.core.domain.dto.commons;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ProblemDetailsErrorsResponse {

  private String field;

  private String message;
}
