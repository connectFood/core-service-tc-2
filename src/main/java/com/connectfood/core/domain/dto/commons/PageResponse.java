package com.connectfood.core.domain.dto.commons;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PageResponse<T> implements Serializable {

  private Boolean success;

  private Long totalElements;

  private Integer page;

  private Integer size;

  private T content;

  public PageResponse(T content, Long totalElements, Integer page, Integer size) {
    this.success = true;
    this.totalElements = totalElements;
    this.page = page;
    this.size = size;
    this.content = content;
  }
}
