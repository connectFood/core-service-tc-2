package com.connectfood.infrastructure.persistence.specification;

import java.util.UUID;

import com.connectfood.infrastructure.persistence.entity.RestaurantItemsEntity;
import com.connectfood.infrastructure.persistence.specification.RestaurantItemsSpecification;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.jpa.domain.Specification;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

@ExtendWith(MockitoExtension.class)
class RestaurantItemsSpecificationTest {

  @Test
  @DisplayName("Deve retornar null quando o restaurantUuid for null")
  void shouldReturnNullWhenRestaurantUuidIsNull() {
    final Specification<RestaurantItemsEntity> spec =
        RestaurantItemsSpecification.hasRestaurantUuid(null);

    Assertions.assertNull(spec);
  }

  @Test
  @DisplayName("Deve criar o predicate corretamente quando o restaurantUuid for informado")
  @SuppressWarnings("unchecked")
  void shouldCreatePredicateWhenRestaurantUuidIsProvided() {
    final var restaurantUuid = UUID.randomUUID();

    final Root<RestaurantItemsEntity> root = Mockito.mock(Root.class);
    final CriteriaQuery<?> query = Mockito.mock(CriteriaQuery.class);
    final CriteriaBuilder cb = Mockito.mock(CriteriaBuilder.class);

    final Path<Object> restaurantPath = Mockito.mock(Path.class);
    final Path<Object> uuidPath = Mockito.mock(Path.class);
    final Predicate predicate = Mockito.mock(Predicate.class);

    Mockito.when(root.get("restaurant"))
        .thenReturn(restaurantPath);
    Mockito.when(restaurantPath.get("uuid"))
        .thenReturn(uuidPath);
    Mockito.when(cb.equal(uuidPath, restaurantUuid))
        .thenReturn(predicate);

    final Specification<RestaurantItemsEntity> spec =
        RestaurantItemsSpecification.hasRestaurantUuid(restaurantUuid);

    final Predicate result = spec.toPredicate(root, query, cb);

    Assertions.assertNotNull(result);
    Assertions.assertEquals(predicate, result);

    Mockito.verify(root, Mockito.times(1))
        .get("restaurant");
    Mockito.verify(restaurantPath, Mockito.times(1))
        .get("uuid");
    Mockito.verify(cb, Mockito.times(1))
        .equal(uuidPath, restaurantUuid);
  }
}
