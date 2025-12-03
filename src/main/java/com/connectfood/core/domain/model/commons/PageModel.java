package com.connectfood.core.domain.model.commons;

public record PageModel<T>(T content, Long total) {
}
