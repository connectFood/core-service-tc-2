package com.connectfood.infrastructure.persistence.adapter;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

import com.connectfood.core.domain.model.RestaurantType;
import com.connectfood.core.domain.model.commons.PageModel;
import com.connectfood.infrastructure.persistence.entity.RestaurantsTypeEntity;
import com.connectfood.infrastructure.persistence.jpa.JpaRestaurantsTypeRepository;
import com.connectfood.infrastructure.persistence.mappers.RestaurantsTypeInfraMapper;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

@ExtendWith(MockitoExtension.class)
class RestaurantTypeGatewayAdapterTest {

  @Mock
  private JpaRestaurantsTypeRepository repository;

  @Mock
  private RestaurantsTypeInfraMapper mapper;

  @InjectMocks
  private RestaurantTypeGatewayAdapter adapter;

  @Test
  @DisplayName("Deve salvar e retornar o model mapeado")
  void saveShouldPersistAndReturnMappedModel() {
    final RestaurantType model = Mockito.mock(RestaurantType.class);

    final RestaurantsTypeEntity entityToSave = Mockito.mock(RestaurantsTypeEntity.class);
    final RestaurantsTypeEntity savedEntity = Mockito.mock(RestaurantsTypeEntity.class);

    final RestaurantType mappedDomain = Mockito.mock(RestaurantType.class);

    Mockito.when(mapper.toEntity(model))
        .thenReturn(entityToSave);
    Mockito.when(repository.save(entityToSave))
        .thenReturn(savedEntity);
    Mockito.when(mapper.toDomain(savedEntity))
        .thenReturn(mappedDomain);

    final var result = adapter.save(model);

    Assertions.assertSame(mappedDomain, result);

    Mockito.verify(mapper, Mockito.times(1))
        .toEntity(model);
    Mockito.verify(repository, Mockito.times(1))
        .save(entityToSave);
    Mockito.verify(mapper, Mockito.times(1))
        .toDomain(savedEntity);
    Mockito.verifyNoMoreInteractions(repository, mapper);
  }

  @Test
  @DisplayName("Deve atualizar e retornar o model mapeado")
  void updateShouldPersistAndReturnMappedModel() {
    final var uuid = UUID.randomUUID();

    final RestaurantType model = Mockito.mock(RestaurantType.class);

    final RestaurantsTypeEntity foundEntity = Mockito.mock(RestaurantsTypeEntity.class);
    final RestaurantsTypeEntity entityToSave = Mockito.mock(RestaurantsTypeEntity.class);
    final RestaurantsTypeEntity savedEntity = Mockito.mock(RestaurantsTypeEntity.class);

    final RestaurantType mappedDomain = Mockito.mock(RestaurantType.class);

    Mockito.when(repository.findByUuid(uuid))
        .thenReturn(Optional.of(foundEntity));
    Mockito.when(mapper.toEntity(model, foundEntity))
        .thenReturn(entityToSave);
    Mockito.when(repository.save(entityToSave))
        .thenReturn(savedEntity);
    Mockito.when(mapper.toDomain(savedEntity))
        .thenReturn(mappedDomain);

    final var result = adapter.update(uuid, model);

    Assertions.assertSame(mappedDomain, result);

    Mockito.verify(repository, Mockito.times(1))
        .findByUuid(uuid);
    Mockito.verify(mapper, Mockito.times(1))
        .toEntity(model, foundEntity);
    Mockito.verify(repository, Mockito.times(1))
        .save(entityToSave);
    Mockito.verify(mapper, Mockito.times(1))
        .toDomain(savedEntity);
    Mockito.verifyNoMoreInteractions(repository, mapper);
  }

  @Test
  @DisplayName("Deve lançar exceção quando uuid não existir ao atualizar")
  void updateShouldThrowWhenNotFound() {
    final var uuid = UUID.randomUUID();
    final RestaurantType model = Mockito.mock(RestaurantType.class);

    Mockito.when(repository.findByUuid(uuid))
        .thenReturn(Optional.empty());

    Assertions.assertThrows(NoSuchElementException.class, () -> adapter.update(uuid, model));

    Mockito.verify(repository, Mockito.times(1))
        .findByUuid(uuid);
    Mockito.verifyNoInteractions(mapper);
    Mockito.verifyNoMoreInteractions(repository);
  }

  @Test
  @DisplayName("Deve retornar Optional vazio quando não encontrar por uuid")
  void findByIdShouldReturnEmptyWhenNotFound() {
    final var uuid = UUID.randomUUID();

    Mockito.when(repository.findByUuid(uuid))
        .thenReturn(Optional.empty());

    final var result = adapter.findById(uuid);

    Assertions.assertNotNull(result);
    Assertions.assertTrue(result.isEmpty());

    Mockito.verify(repository, Mockito.times(1))
        .findByUuid(uuid);
    Mockito.verifyNoInteractions(mapper);
    Mockito.verifyNoMoreInteractions(repository);
  }

  @Test
  @DisplayName("Deve retornar model quando encontrar por uuid")
  void findByIdShouldReturnMappedModelWhenFound() {
    final var uuid = UUID.randomUUID();

    final RestaurantsTypeEntity foundEntity = Mockito.mock(RestaurantsTypeEntity.class);
    final RestaurantType mappedDomain = Mockito.mock(RestaurantType.class);

    Mockito.when(repository.findByUuid(uuid))
        .thenReturn(Optional.of(foundEntity));
    Mockito.when(mapper.toDomain(foundEntity))
        .thenReturn(mappedDomain);

    final var result = adapter.findById(uuid);

    Assertions.assertTrue(result.isPresent());
    Assertions.assertSame(mappedDomain, result.get());

    Mockito.verify(repository, Mockito.times(1))
        .findByUuid(uuid);
    Mockito.verify(mapper, Mockito.times(1))
        .toDomain(foundEntity);
    Mockito.verifyNoMoreInteractions(repository, mapper);
  }

  @Test
  @DisplayName("Deve listar paginado com defaults quando sort e direction forem null")
  void findAllShouldReturnPagedResultWithDefaultsWhenSortAndDirectionAreNull() {
    final RestaurantsTypeEntity entity1 = Mockito.mock(RestaurantsTypeEntity.class);
    final RestaurantsTypeEntity entity2 = Mockito.mock(RestaurantsTypeEntity.class);

    final RestaurantType domain1 = Mockito.mock(RestaurantType.class);
    final RestaurantType domain2 = Mockito.mock(RestaurantType.class);

    Mockito.when(mapper.toDomain(entity1))
        .thenReturn(domain1);
    Mockito.when(mapper.toDomain(entity2))
        .thenReturn(domain2);

    final Page<RestaurantsTypeEntity> pageResult =
        new PageImpl<>(List.of(entity1, entity2), Pageable.unpaged(), 2);

    Mockito.when(repository.findAll(
            ArgumentMatchers.<Specification<RestaurantsTypeEntity>>any(),
            ArgumentMatchers.any(Pageable.class)
        ))
        .thenReturn(pageResult);

    final PageModel<List<RestaurantType>> result =
        adapter.findAll("abc", 0, 10, null, null);

    Assertions.assertNotNull(result);
    Assertions.assertEquals(2, result.content().size());
    Assertions.assertEquals(List.of(domain1, domain2), result.content());
    Assertions.assertEquals(2L, result.total());

    Mockito.verify(repository, Mockito.times(1))
        .findAll(ArgumentMatchers.<Specification<RestaurantsTypeEntity>>any(), ArgumentMatchers.any(Pageable.class));
    Mockito.verify(mapper, Mockito.times(1))
        .toDomain(entity1);
    Mockito.verify(mapper, Mockito.times(1))
        .toDomain(entity2);
    Mockito.verifyNoMoreInteractions(repository, mapper);
  }

  @Test
  @DisplayName("Deve listar paginado respeitando sort e direction informados")
  void findAllShouldReturnPagedResultUsingProvidedSortAndDirection() {
    final RestaurantsTypeEntity entity = Mockito.mock(RestaurantsTypeEntity.class);
    final RestaurantType domain = Mockito.mock(RestaurantType.class);

    Mockito.when(mapper.toDomain(entity))
        .thenReturn(domain);

    final Page<RestaurantsTypeEntity> pageResult =
        new PageImpl<>(List.of(entity), Pageable.unpaged(), 1);

    Mockito.when(repository.findAll(
            ArgumentMatchers.<Specification<RestaurantsTypeEntity>>any(),
            ArgumentMatchers.any(Pageable.class)
        ))
        .thenReturn(pageResult);

    final PageModel<List<RestaurantType>> result =
        adapter.findAll(null, 1, 5, "name", "DESC");

    Assertions.assertNotNull(result);
    Assertions.assertEquals(1, result.content().size());
    Assertions.assertEquals(List.of(domain), result.content());
    Assertions.assertEquals(1L, result.total());

    Mockito.verify(repository, Mockito.times(1))
        .findAll(ArgumentMatchers.<Specification<RestaurantsTypeEntity>>any(), ArgumentMatchers.any(Pageable.class));
    Mockito.verify(mapper, Mockito.times(1))
        .toDomain(entity);
    Mockito.verifyNoMoreInteractions(repository, mapper);
  }

  @Test
  @DisplayName("Deve deletar pelo uuid")
  void deleteShouldDeleteByUuid() {
    final var uuid = UUID.randomUUID();

    adapter.delete(uuid);

    Mockito.verify(repository, Mockito.times(1))
        .deleteByUuid(uuid);
    Mockito.verifyNoInteractions(mapper);
    Mockito.verifyNoMoreInteractions(repository);
  }
}
