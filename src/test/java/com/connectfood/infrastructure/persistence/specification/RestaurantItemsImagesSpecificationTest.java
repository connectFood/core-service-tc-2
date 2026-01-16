package com.connectfood.infrastructure.persistence.specification;

import java.util.UUID;

import com.connectfood.infrastructure.persistence.entity.RestaurantItemsImagesEntity;
import com.connectfood.infrastructure.persistence.specification.RestaurantItemsImagesSpecification;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
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
class RestaurantItemsImagesSpecificationTest {

  @Test
  @DisplayName("Deve retornar null quando restaurantItemsUuid for null")
  void shouldReturnNullWhenRestaurantItemsUuidIsNull() {
    final Specification<RestaurantItemsImagesEntity> specification =
        RestaurantItemsImagesSpecification.hasRestaurantItemsUuid(null);

    Assertions.assertNull(specification);
  }

  @Test
  @DisplayName("Deve criar specification quando restaurantItemsUuid for informado")
  void shouldCreateSpecificationWhenRestaurantItemsUuidIsProvided() {
    final var uuid = UUID.randomUUID();

    final Specification<RestaurantItemsImagesEntity> specification =
        RestaurantItemsImagesSpecification.hasRestaurantItemsUuid(uuid);

    Assertions.assertNotNull(specification);
  }

  @Test
  @DisplayName("Deve executar o predicate corretamente quando restaurantItemsUuid for informado")
  void shouldExecutePredicateWhenRestaurantItemsUuidIsProvided() {
    final var uuid = UUID.randomUUID();

    final Specification<RestaurantItemsImagesEntity> specification =
        RestaurantItemsImagesSpecification.hasRestaurantItemsUuid(uuid);

    final Root<RestaurantItemsImagesEntity> root = Mockito.mock(Root.class);
    final CriteriaQuery<?> query = Mockito.mock(CriteriaQuery.class);
    final CriteriaBuilder cb = Mockito.mock(CriteriaBuilder.class);

    final Path<Object> restaurantItemsPath = Mockito.mock(Path.class);
    final Path<Object> uuidPath = Mockito.mock(Path.class);
    final Predicate predicate = Mockito.mock(Predicate.class);

    Mockito.when(root.get("restaurantItems")).thenReturn(restaurantItemsPath);
    Mockito.when(restaurantItemsPath.get("uuid")).thenReturn(uuidPath);
    Mockito.when(cb.equal(uuidPath, uuid)).thenReturn(predicate);

    final Predicate result = specification.toPredicate(root, query, cb);

    Assertions.assertEquals(predicate, result);
  }
}
