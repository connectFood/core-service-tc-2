package com.connectfood.core.infrastructure.persistence.specification;

import java.util.UUID;

import com.connectfood.core.infrastructure.persistence.entity.UsersEntity;

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
class UsersSpecificationTest {

  @Test
  @DisplayName("Deve criar specification para filtro por nome (fullName)")
  void shouldCreateSpecificationForNameContains() {
    final var specification = UsersSpecification.nameContains("Lucas");
    Assertions.assertNotNull(specification);
  }

  @Test
  @DisplayName("Deve criar specification para filtro por email")
  void shouldCreateSpecificationForEmailContains() {
    final var specification = UsersSpecification.emailContains("gmail");
    Assertions.assertNotNull(specification);
  }

  @Test
  @DisplayName("Deve retornar null quando usersTypeUuid for null")
  void shouldReturnNullWhenUsersTypeUuidIsNull() {
    final Specification<UsersEntity> specification =
        UsersSpecification.hasUsersTypeUuid(null);

    Assertions.assertNull(specification);
  }

  @Test
  @DisplayName("Deve criar specification quando usersTypeUuid for informado")
  void shouldCreateSpecificationWhenUsersTypeUuidIsProvided() {
    final var uuid = UUID.randomUUID();

    final Specification<UsersEntity> specification =
        UsersSpecification.hasUsersTypeUuid(uuid);

    Assertions.assertNotNull(specification);
  }

  @Test
  @DisplayName("Deve executar o predicate corretamente quando usersTypeUuid for informado")
  void shouldExecutePredicateWhenUsersTypeUuidIsProvided() {
    final var uuid = UUID.randomUUID();

    final Specification<UsersEntity> specification =
        UsersSpecification.hasUsersTypeUuid(uuid);

    final Root<UsersEntity> root = Mockito.mock(Root.class);
    final CriteriaQuery<?> query = Mockito.mock(CriteriaQuery.class);
    final CriteriaBuilder cb = Mockito.mock(CriteriaBuilder.class);

    final Path<Object> usersTypePath = Mockito.mock(Path.class);
    final Path<Object> uuidPath = Mockito.mock(Path.class);
    final Predicate predicate = Mockito.mock(Predicate.class);

    Mockito.when(root.get("usersType"))
        .thenReturn(usersTypePath);
    Mockito.when(usersTypePath.get("uuid"))
        .thenReturn(uuidPath);
    Mockito.when(cb.equal(uuidPath, uuid))
        .thenReturn(predicate);

    final Predicate result = specification.toPredicate(root, query, cb);

    Assertions.assertEquals(predicate, result);
  }
}
