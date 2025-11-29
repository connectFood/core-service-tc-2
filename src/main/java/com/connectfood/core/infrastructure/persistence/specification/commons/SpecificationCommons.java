package com.connectfood.core.infrastructure.persistence.specification.commons;

import org.springframework.data.jpa.domain.Specification;

public final class SpecificationCommons {

  private SpecificationCommons() {
  }

  public static <T> Specification<T> likeIgnoreCase(String field, String value) {
    if (value == null || value.isBlank()) {
      return null;
    }

    final String pattern = "%" + value.trim()
        .toLowerCase() + "%";

    return (root, query, cb) ->
        cb.like(cb.lower(root.get(field)), pattern);
  }

  public static <T, Y> Specification<T> eq(String field, Y value) {
    if (value == null) {
      return null;
    }

    return (root, query, cb) ->
        cb.equal(root.get(field), value);
  }

  public static <T> Specification<T> isTrue(String field) {
    return (root, query, cb) -> cb.isTrue(root.get(field));
  }

  public static <T> Specification<T> isFalse(String field) {
    return (root, query, cb) -> cb.isFalse(root.get(field));
  }

  public static <T extends Comparable<? super T>> Specification<T> between(
      String field,
      T start,
      T end
  ) {
    if (start == null && end == null) {
      return null;
    }
    return (root, query, cb) -> {
      if (start != null && end != null) {
        return cb.between(root.get(field), start, end);
      }
      if (start != null) {
        return cb.greaterThanOrEqualTo(root.get(field), start);
      }
      return cb.lessThanOrEqualTo(root.get(field), end);
    };
  }
}
