package com.connectfood.core.infrastructure.persistence.entity.commons;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class BaseEntityTest {

  private static class TestEntity extends BaseEntity {
  }

  @Test
  @DisplayName("Deve preencher createdAt e updatedAt no prePersist")
  void shouldSetCreatedAtAndUpdatedAtOnPrePersist() {
    final var entity = new TestEntity();

    Assertions.assertNull(entity.getCreatedAt());
    Assertions.assertNull(entity.getUpdatedAt());

    entity.prePersist();

    Assertions.assertNotNull(entity.getCreatedAt());
    Assertions.assertNotNull(entity.getUpdatedAt());
  }

  @Test
  @DisplayName("Deve atualizar updatedAt no preUpdate")
  void shouldUpdateUpdatedAtOnPreUpdate() {
    final var entity = new TestEntity();

    entity.prePersist();
    final LocalDateTime previousUpdatedAt = entity.getUpdatedAt();

    entity.preUpdate();

    Assertions.assertNotNull(entity.getUpdatedAt());
    Assertions.assertTrue(entity.getUpdatedAt().isAfter(previousUpdatedAt) || entity.getUpdatedAt().isEqual(previousUpdatedAt));
  }
}
