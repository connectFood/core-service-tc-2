package com.connectfood.core.application.usertype.dto.commons;

public record PageOutput<T>(T content, Long total) {
}
