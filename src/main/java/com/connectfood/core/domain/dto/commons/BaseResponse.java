package com.connectfood.core.domain.dto.commons;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class BaseResponse<T> implements Serializable {

  private Boolean success;

  private T content;

  public BaseResponse(T content) {
    this.success = true;
    this.content = content;
  }
}
