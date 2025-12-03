package com.connectfood.core.application.dto.commons;

public record PageOutput<T>(T content, Long total) {
}
