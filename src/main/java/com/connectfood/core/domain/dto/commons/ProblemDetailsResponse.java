package com.connectfood.core.domain.dto.commons;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ProblemDetailsResponse {

  private String type;

  private String title;

  private Integer status;

  private String detail;

  private String instance;

  private List<ProblemDetailsErrorsResponse> errors;

}
