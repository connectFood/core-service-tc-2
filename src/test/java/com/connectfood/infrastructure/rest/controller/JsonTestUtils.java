package com.connectfood.infrastructure.rest.controller;

import java.util.UUID;

import com.jayway.jsonpath.JsonPath;

public final class JsonTestUtils {

  private JsonTestUtils() {
  }

  public static UUID readUuid(final String json, final String path) {
    final String value = JsonPath.read(json, path);
    return UUID.fromString(value);
  }
}
