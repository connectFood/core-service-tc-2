package com.connectfood.core.infrastructure.persistence.specification.commons;

import java.time.LocalDateTime;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.data.jpa.domain.Specification;

@ExtendWith(MockitoExtension.class)
class SpecificationCommonsTest {

  @Test
  @DisplayName("Deve retornar null quando value for null no likeIgnoreCase")
  void shouldReturnNullWhenValueIsNullOnLikeIgnoreCase() {
    final Specification<Object> specification = SpecificationCommons.likeIgnoreCase("name", null);

    Assertions.assertNull(specification);
  }

  @Test
  @DisplayName("Deve retornar null quando value for blank no likeIgnoreCase")
  void shouldReturnNullWhenValueIsBlankOnLikeIgnoreCase() {
    final Specification<Object> specification = SpecificationCommons.likeIgnoreCase("name", "   ");

    Assertions.assertNull(specification);
  }

  @Test
  @DisplayName("Deve executar o predicate corretamente no likeIgnoreCase")
  @SuppressWarnings({ "unchecked", "rawtypes" })
  void shouldExecutePredicateCorrectlyOnLikeIgnoreCase() {
    final Specification<Object> specification = SpecificationCommons.likeIgnoreCase("name", "  Pizza  ");

    final Root<Object> root = Mockito.mock(Root.class);
    final CriteriaQuery<?> query = Mockito.mock(CriteriaQuery.class);
    final CriteriaBuilder cb = Mockito.mock(CriteriaBuilder.class);

    final Path<String> fieldPath = Mockito.mock(Path.class);
    final Expression<String> lowerExpression = Mockito.mock(Expression.class);
    final Predicate predicate = Mockito.mock(Predicate.class);

    Mockito.when(root.get("name")).thenReturn((Path) fieldPath);
    Mockito.when(cb.lower(fieldPath)).thenReturn(lowerExpression);
    Mockito.when(cb.like(lowerExpression, "%pizza%")).thenReturn(predicate);

    final Predicate result = specification.toPredicate(root, query, cb);

    Assertions.assertEquals(predicate, result);
  }

  @Test
  @DisplayName("Deve retornar null quando value for null no eq")
  void shouldReturnNullWhenValueIsNullOnEq() {
    final Specification<Object> specification = SpecificationCommons.eq("active", null);

    Assertions.assertNull(specification);
  }

  @Test
  @DisplayName("Deve executar o predicate corretamente no eq")
  void shouldExecutePredicateCorrectlyOnEq() {
    final var value = "ACTIVE";
    final Specification<Object> specification = SpecificationCommons.eq("status", value);

    final Root<Object> root = Mockito.mock(Root.class);
    final CriteriaQuery<?> query = Mockito.mock(CriteriaQuery.class);
    final CriteriaBuilder cb = Mockito.mock(CriteriaBuilder.class);

    final Path<Object> fieldPath = Mockito.mock(Path.class);
    final Predicate predicate = Mockito.mock(Predicate.class);

    Mockito.when(root.get("status")).thenReturn(fieldPath);
    Mockito.when(cb.equal(fieldPath, value)).thenReturn(predicate);

    final Predicate result = specification.toPredicate(root, query, cb);

    Assertions.assertEquals(predicate, result);
  }

  @Test
  @DisplayName("Deve executar o predicate corretamente no isTrue")
  @SuppressWarnings({ "unchecked", "rawtypes" })
  void shouldExecutePredicateCorrectlyOnIsTrue() {
    final Specification<Object> specification = SpecificationCommons.isTrue("enabled");

    final Root<Object> root = Mockito.mock(Root.class);
    final CriteriaQuery<?> query = Mockito.mock(CriteriaQuery.class);
    final CriteriaBuilder cb = Mockito.mock(CriteriaBuilder.class);

    final Path<Boolean> booleanPath = Mockito.mock(Path.class);
    final Predicate predicate = Mockito.mock(Predicate.class);

    Mockito.when(root.get("enabled")).thenReturn((Path) booleanPath);
    Mockito.when(cb.isTrue(booleanPath)).thenReturn(predicate);

    final Predicate result = specification.toPredicate(root, query, cb);

    Assertions.assertEquals(predicate, result);
  }

  @Test
  @DisplayName("Deve executar o predicate corretamente no isFalse")
  @SuppressWarnings({ "unchecked", "rawtypes" })
  void shouldExecutePredicateCorrectlyOnIsFalse() {
    final Specification<Object> specification = SpecificationCommons.isFalse("enabled");

    final Root<Object> root = Mockito.mock(Root.class);
    final CriteriaQuery<?> query = Mockito.mock(CriteriaQuery.class);
    final CriteriaBuilder cb = Mockito.mock(CriteriaBuilder.class);

    final Path<Boolean> booleanPath = Mockito.mock(Path.class);
    final Predicate predicate = Mockito.mock(Predicate.class);

    Mockito.when(root.get("enabled")).thenReturn((Path) booleanPath);
    Mockito.when(cb.isFalse(booleanPath)).thenReturn(predicate);

    final Predicate result = specification.toPredicate(root, query, cb);

    Assertions.assertEquals(predicate, result);
  }

  @Test
  @DisplayName("Deve retornar null quando start e end forem null no between")
  void shouldReturnNullWhenStartAndEndAreNullOnBetween() {
    final Specification<LocalDateTime> specification = SpecificationCommons.between("createdAt", null, null);

    Assertions.assertNull(specification);
  }

  @Test
  @DisplayName("Deve executar between quando start e end forem informados no between")
  @SuppressWarnings({ "unchecked", "rawtypes" })
  void shouldExecuteBetweenWhenStartAndEndAreProvided() {
    final var start = LocalDateTime.now().minusDays(1);
    final var end = LocalDateTime.now();

    final Specification<LocalDateTime> specification = SpecificationCommons.between("createdAt", start, end);

    final Root<LocalDateTime> root = Mockito.mock(Root.class);
    final CriteriaQuery<?> query = Mockito.mock(CriteriaQuery.class);
    final CriteriaBuilder cb = Mockito.mock(CriteriaBuilder.class);

    final Path<LocalDateTime> fieldPath = Mockito.mock(Path.class);
    final Predicate predicate = Mockito.mock(Predicate.class);

    Mockito.when(root.get("createdAt")).thenReturn((Path) fieldPath);
    Mockito.when(cb.between(fieldPath, start, end)).thenReturn(predicate);

    final Predicate result = specification.toPredicate(root, query, cb);

    Assertions.assertEquals(predicate, result);
  }

  @Test
  @DisplayName("Deve executar greaterThanOrEqualTo quando apenas start for informado no between")
  @SuppressWarnings({ "unchecked", "rawtypes" })
  void shouldExecuteGreaterThanOrEqualToWhenOnlyStartIsProvided() {
    final var start = LocalDateTime.now().minusDays(1);

    final Specification<LocalDateTime> specification = SpecificationCommons.between("createdAt", start, null);

    final Root<LocalDateTime> root = Mockito.mock(Root.class);
    final CriteriaQuery<?> query = Mockito.mock(CriteriaQuery.class);
    final CriteriaBuilder cb = Mockito.mock(CriteriaBuilder.class);

    final Path<LocalDateTime> fieldPath = Mockito.mock(Path.class);
    final Predicate predicate = Mockito.mock(Predicate.class);

    Mockito.when(root.get("createdAt")).thenReturn((Path) fieldPath);
    Mockito.when(cb.greaterThanOrEqualTo(fieldPath, start)).thenReturn(predicate);

    final Predicate result = specification.toPredicate(root, query, cb);

    Assertions.assertEquals(predicate, result);
  }

  @Test
  @DisplayName("Deve executar lessThanOrEqualTo quando apenas end for informado no between")
  @SuppressWarnings({ "unchecked", "rawtypes" })
  void shouldExecuteLessThanOrEqualToWhenOnlyEndIsProvided() {
    final var end = LocalDateTime.now();

    final Specification<LocalDateTime> specification = SpecificationCommons.between("createdAt", null, end);

    final Root<LocalDateTime> root = Mockito.mock(Root.class);
    final CriteriaQuery<?> query = Mockito.mock(CriteriaQuery.class);
    final CriteriaBuilder cb = Mockito.mock(CriteriaBuilder.class);

    final Path<LocalDateTime> fieldPath = Mockito.mock(Path.class);
    final Predicate predicate = Mockito.mock(Predicate.class);

    Mockito.when(root.get("createdAt")).thenReturn((Path) fieldPath);
    Mockito.when(cb.lessThanOrEqualTo(fieldPath, end)).thenReturn(predicate);

    final Predicate result = specification.toPredicate(root, query, cb);

    Assertions.assertEquals(predicate, result);
  }
}
