package com.connectfood.core.infrastructure.persistence.specification;

import java.util.UUID;

import com.connectfood.core.infrastructure.persistence.entity.RestaurantOpeningHoursEntity;

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
class RestaurantOpeningHoursSpecificationTest {

  @Test
  @DisplayName("Deve retornar null quando restaurantUuid for null")
  void shouldReturnNullWhenRestaurantUuidIsNull() {
    final Specification<RestaurantOpeningHoursEntity> specification =
        RestaurantOpeningHoursSpecification.hasRestaurantUuid(null);

    Assertions.assertNull(specification);
  }

  @Test
  @DisplayName("Deve criar specification quando restaurantUuid for informado")
  void shouldCreateSpecificationWhenRestaurantUuidIsProvided() {
    final var uuid = UUID.randomUUID();

    final Specification<RestaurantOpeningHoursEntity> specification =
        RestaurantOpeningHoursSpecification.hasRestaurantUuid(uuid);

    Assertions.assertNotNull(specification);
  }

  @Test
  @DisplayName("Deve executar o predicate corretamente quando restaurantUuid for informado")
  void shouldExecutePredicateWhenRestaurantUuidIsProvided() {
    final var uuid = UUID.randomUUID();

    final Specification<RestaurantOpeningHoursEntity> specification =
        RestaurantOpeningHoursSpecification.hasRestaurantUuid(uuid);

    final Root<RestaurantOpeningHoursEntity> root = Mockito.mock(Root.class);
    final CriteriaQuery<?> query = Mockito.mock(CriteriaQuery.class);
    final CriteriaBuilder cb = Mockito.mock(CriteriaBuilder.class);

    final Path<Object> restaurantPath = Mockito.mock(Path.class);
    final Path<Object> uuidPath = Mockito.mock(Path.class);
    final Predicate predicate = Mockito.mock(Predicate.class);

    Mockito.when(root.get("restaurant"))
        .thenReturn(restaurantPath);
    Mockito.when(restaurantPath.get("uuid"))
        .thenReturn(uuidPath);
    Mockito.when(cb.equal(uuidPath, uuid))
        .thenReturn(predicate);

    final Predicate result = specification.toPredicate(root, query, cb);

    Assertions.assertEquals(predicate, result);
  }
}
