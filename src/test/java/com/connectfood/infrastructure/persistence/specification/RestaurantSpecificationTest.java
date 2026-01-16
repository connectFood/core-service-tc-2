package com.connectfood.infrastructure.persistence.specification;

import java.util.UUID;

import com.connectfood.infrastructure.persistence.entity.RestaurantEntity;

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
class RestaurantSpecificationTest {

  @Test
  @DisplayName("Deve criar specification para filtro por nome")
  void shouldCreateSpecificationForNameContains() {
    final var specification = RestaurantSpecification.nameContains("Pizza");

    Assertions.assertNotNull(specification);
  }

  @Test
  @DisplayName("Deve retornar null quando restaurantsTypeUuid for null")
  void shouldReturnNullWhenRestaurantsTypeUuidIsNull() {
    final Specification<RestaurantEntity> specification =
        RestaurantSpecification.hasRestaurantsTypeUuid(null);

    Assertions.assertNull(specification);
  }

  @Test
  @DisplayName("Deve criar specification quando restaurantsTypeUuid for informado")
  void shouldCreateSpecificationWhenRestaurantsTypeUuidIsProvided() {
    final var uuid = UUID.randomUUID();

    final Specification<RestaurantEntity> specification =
        RestaurantSpecification.hasRestaurantsTypeUuid(uuid);

    Assertions.assertNotNull(specification);
  }

  @Test
  @DisplayName("Deve executar o predicate corretamente quando restaurantsTypeUuid for informado")
  void shouldExecutePredicateWhenRestaurantsTypeUuidIsProvided() {
    final var uuid = UUID.randomUUID();

    final Specification<RestaurantEntity> specification =
        RestaurantSpecification.hasRestaurantsTypeUuid(uuid);

    final Root<RestaurantEntity> root = Mockito.mock(Root.class);
    final CriteriaQuery<?> query = Mockito.mock(CriteriaQuery.class);
    final CriteriaBuilder cb = Mockito.mock(CriteriaBuilder.class);

    final Path<Object> restaurantsTypePath = Mockito.mock(Path.class);
    final Path<Object> uuidPath = Mockito.mock(Path.class);
    final Predicate predicate = Mockito.mock(Predicate.class);

    Mockito.when(root.get("restaurantsType"))
        .thenReturn(restaurantsTypePath);
    Mockito.when(restaurantsTypePath.get("uuid"))
        .thenReturn(uuidPath);
    Mockito.when(cb.equal(uuidPath, uuid))
        .thenReturn(predicate);

    final Predicate result = specification.toPredicate(root, query, cb);

    Assertions.assertEquals(predicate, result);
  }
}
