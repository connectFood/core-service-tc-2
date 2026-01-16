package com.connectfood.infrastructure.persistence.adapter;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

import com.connectfood.core.domain.model.RestaurantItemImage;
import com.connectfood.core.domain.model.commons.PageModel;
import com.connectfood.infrastructure.persistence.entity.RestaurantItemsEntity;
import com.connectfood.infrastructure.persistence.entity.RestaurantItemsImagesEntity;
import com.connectfood.infrastructure.persistence.jpa.JpaRestaurantItemsImagesRepository;
import com.connectfood.infrastructure.persistence.jpa.JpaRestaurantItemsRepository;
import com.connectfood.infrastructure.persistence.mappers.RestaurantItemsImageInfraMapper;

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
class RestaurantItemsImagesGatewayAdapterTest {

  @Mock
  private JpaRestaurantItemsImagesRepository repository;

  @Mock
  private RestaurantItemsImageInfraMapper mapper;

  @Mock
  private JpaRestaurantItemsRepository restaurantItemsRepository;

  @InjectMocks
  private RestaurantItemsImagesGatewayAdapter adapter;

  @Test
  @DisplayName("Deve salvar e retornar o model mapeado")
  void saveShouldPersistAndReturnMappedModel() {
    final var restaurantItemsUuid = UUID.randomUUID();
    final RestaurantItemImage model = Mockito.mock(RestaurantItemImage.class);

    final RestaurantItemsEntity restaurantItemsEntity = Mockito.mock(RestaurantItemsEntity.class);

    final RestaurantItemsImagesEntity entityToSave = Mockito.mock(RestaurantItemsImagesEntity.class);
    final RestaurantItemsImagesEntity savedEntity = Mockito.mock(RestaurantItemsImagesEntity.class);

    final RestaurantItemImage mappedDomain = Mockito.mock(RestaurantItemImage.class);

    Mockito.when(restaurantItemsRepository.findByUuid(restaurantItemsUuid))
        .thenReturn(Optional.of(restaurantItemsEntity));
    Mockito.when(mapper.toEntity(model, restaurantItemsEntity))
        .thenReturn(entityToSave);
    Mockito.when(repository.save(entityToSave))
        .thenReturn(savedEntity);
    Mockito.when(mapper.toDomain(savedEntity))
        .thenReturn(mappedDomain);

    final var result = adapter.save(restaurantItemsUuid, model);

    Assertions.assertSame(mappedDomain, result);

    Mockito.verify(restaurantItemsRepository, Mockito.times(1))
        .findByUuid(restaurantItemsUuid);
    Mockito.verify(mapper, Mockito.times(1))
        .toEntity(model, restaurantItemsEntity);
    Mockito.verify(repository, Mockito.times(1))
        .save(entityToSave);
    Mockito.verify(mapper, Mockito.times(1))
        .toDomain(savedEntity);
    Mockito.verifyNoMoreInteractions(repository, mapper, restaurantItemsRepository);
  }

  @Test
  @DisplayName("Deve lançar exceção quando restaurantItemsUuid não existir ao salvar")
  void saveShouldThrowWhenRestaurantItemsDoesNotExist() {
    final var restaurantItemsUuid = UUID.randomUUID();
    final RestaurantItemImage model = Mockito.mock(RestaurantItemImage.class);

    Mockito.when(restaurantItemsRepository.findByUuid(restaurantItemsUuid))
        .thenReturn(Optional.empty());

    Assertions.assertThrows(NoSuchElementException.class, () -> adapter.save(restaurantItemsUuid, model));

    Mockito.verify(restaurantItemsRepository, Mockito.times(1))
        .findByUuid(restaurantItemsUuid);
    Mockito.verifyNoInteractions(repository, mapper);
    Mockito.verifyNoMoreInteractions(restaurantItemsRepository);
  }

  @Test
  @DisplayName("Deve atualizar e retornar o model mapeado")
  void updateShouldPersistAndReturnMappedModel() {
    final var uuid = UUID.randomUUID();
    final RestaurantItemImage model = Mockito.mock(RestaurantItemImage.class);

    final RestaurantItemsImagesEntity foundEntity = Mockito.mock(RestaurantItemsImagesEntity.class);
    final RestaurantItemsImagesEntity entityToSave = Mockito.mock(RestaurantItemsImagesEntity.class);
    final RestaurantItemsImagesEntity savedEntity = Mockito.mock(RestaurantItemsImagesEntity.class);

    final RestaurantItemImage mappedDomain = Mockito.mock(RestaurantItemImage.class);

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
    Mockito.verifyNoMoreInteractions(repository, mapper, restaurantItemsRepository);
  }

  @Test
  @DisplayName("Deve lançar exceção quando uuid não existir ao atualizar")
  void updateShouldThrowWhenUuidDoesNotExist() {
    final var uuid = UUID.randomUUID();
    final RestaurantItemImage model = Mockito.mock(RestaurantItemImage.class);

    Mockito.when(repository.findByUuid(uuid))
        .thenReturn(Optional.empty());

    Assertions.assertThrows(NoSuchElementException.class, () -> adapter.update(uuid, model));

    Mockito.verify(repository, Mockito.times(1))
        .findByUuid(uuid);
    Mockito.verifyNoInteractions(mapper, restaurantItemsRepository);
    Mockito.verifyNoMoreInteractions(repository);
  }

  @Test
  @DisplayName("Deve retornar Optional vazio quando não encontrar por uuid")
  void findByUuidShouldReturnEmptyWhenNotFound() {
    final var uuid = UUID.randomUUID();

    Mockito.when(repository.findByUuid(uuid))
        .thenReturn(Optional.empty());

    final Optional<RestaurantItemImage> result = adapter.findByUuid(uuid);

    Assertions.assertNotNull(result);
    Assertions.assertTrue(result.isEmpty());

    Mockito.verify(repository, Mockito.times(1))
        .findByUuid(uuid);
    Mockito.verifyNoInteractions(mapper, restaurantItemsRepository);
    Mockito.verifyNoMoreInteractions(repository);
  }

  @Test
  @DisplayName("Deve retornar model quando encontrar por uuid")
  void findByUuidShouldReturnMappedModelWhenFound() {
    final var uuid = UUID.randomUUID();

    final RestaurantItemsImagesEntity foundEntity = Mockito.mock(RestaurantItemsImagesEntity.class);
    final RestaurantItemImage mappedDomain = Mockito.mock(RestaurantItemImage.class);

    Mockito.when(repository.findByUuid(uuid))
        .thenReturn(Optional.of(foundEntity));
    Mockito.when(mapper.toDomain(foundEntity))
        .thenReturn(mappedDomain);

    final Optional<RestaurantItemImage> result = adapter.findByUuid(uuid);

    Assertions.assertTrue(result.isPresent());
    Assertions.assertSame(mappedDomain, result.get());

    Mockito.verify(repository, Mockito.times(1))
        .findByUuid(uuid);
    Mockito.verify(mapper, Mockito.times(1))
        .toDomain(foundEntity);
    Mockito.verifyNoMoreInteractions(repository, mapper, restaurantItemsRepository);
  }

  @Test
  @DisplayName("Deve listar paginado com defaults quando sort e direction forem null")
  void findAllShouldReturnPagedResultWithDefaultsWhenSortAndDirectionAreNull() {
    final var restaurantItemsUuid = UUID.randomUUID();

    final var entity1 = Mockito.mock(RestaurantItemsImagesEntity.class);
    final var entity2 = Mockito.mock(RestaurantItemsImagesEntity.class);

    final RestaurantItemImage domain1 = Mockito.mock(RestaurantItemImage.class);
    final RestaurantItemImage domain2 = Mockito.mock(RestaurantItemImage.class);

    Mockito.when(mapper.toDomain(entity1))
        .thenReturn(domain1);
    Mockito.when(mapper.toDomain(entity2))
        .thenReturn(domain2);

    final Page<RestaurantItemsImagesEntity> pageResult = new PageImpl<>(List.of(entity1, entity2), Pageable.unpaged(),
        2
    );

    Mockito.when(repository.findAll(
            ArgumentMatchers.<Specification<RestaurantItemsImagesEntity>>any(),
            ArgumentMatchers.any(Pageable.class)
        ))
        .thenReturn(pageResult);

    final PageModel<List<RestaurantItemImage>> result =
        adapter.findAll(restaurantItemsUuid, 0, 10, null, null);

    Assertions.assertNotNull(result);
    Assertions.assertEquals(2, result.content()
        .size()
    );
    Assertions.assertEquals(List.of(domain1, domain2), result.content());
    Assertions.assertEquals(2L, result.total());

    Mockito.verify(repository, Mockito.times(1))
        .findAll(ArgumentMatchers.<Specification<RestaurantItemsImagesEntity>>any(),
            ArgumentMatchers.any(Pageable.class)
        );
    Mockito.verify(mapper, Mockito.times(1))
        .toDomain(entity1);
    Mockito.verify(mapper, Mockito.times(1))
        .toDomain(entity2);
    Mockito.verifyNoMoreInteractions(repository, mapper, restaurantItemsRepository);
  }

  @Test
  @DisplayName("Deve listar paginado respeitando sort e direction informados")
  void findAllShouldReturnPagedResultUsingProvidedSortAndDirection() {
    final var restaurantItemsUuid = UUID.randomUUID();

    final var entity = Mockito.mock(RestaurantItemsImagesEntity.class);
    final RestaurantItemImage domain = Mockito.mock(RestaurantItemImage.class);

    Mockito.when(mapper.toDomain(entity))
        .thenReturn(domain);

    final Page<RestaurantItemsImagesEntity> pageResult = new PageImpl<>(List.of(entity), Pageable.unpaged(), 1);

    Mockito.when(repository.findAll(
            ArgumentMatchers.<Specification<RestaurantItemsImagesEntity>>any(),
            ArgumentMatchers.any(Pageable.class)
        ))
        .thenReturn(pageResult);

    final PageModel<List<RestaurantItemImage>> result =
        adapter.findAll(restaurantItemsUuid, 1, 5, "name", "DESC");

    Assertions.assertNotNull(result);
    Assertions.assertEquals(1, result.content()
        .size()
    );
    Assertions.assertEquals(List.of(domain), result.content());
    Assertions.assertEquals(1L, result.total());

    Mockito.verify(repository, Mockito.times(1))
        .findAll(ArgumentMatchers.<Specification<RestaurantItemsImagesEntity>>any(),
            ArgumentMatchers.any(Pageable.class)
        );
    Mockito.verify(mapper, Mockito.times(1))
        .toDomain(entity);
    Mockito.verifyNoMoreInteractions(repository, mapper, restaurantItemsRepository);
  }

  @Test
  @DisplayName("Deve deletar pelo uuid")
  void deleteShouldDeleteByUuid() {
    final var uuid = UUID.randomUUID();

    adapter.delete(uuid);

    Mockito.verify(repository, Mockito.times(1))
        .deleteByUuid(uuid);
    Mockito.verifyNoInteractions(mapper, restaurantItemsRepository);
    Mockito.verifyNoMoreInteractions(repository);
  }
}
