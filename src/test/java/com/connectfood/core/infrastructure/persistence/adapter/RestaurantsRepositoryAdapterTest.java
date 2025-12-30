package com.connectfood.core.infrastructure.persistence.adapter;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

import com.connectfood.core.domain.model.Restaurants;
import com.connectfood.core.domain.model.RestaurantsType;
import com.connectfood.core.domain.model.commons.PageModel;
import com.connectfood.core.infrastructure.persistence.entity.RestaurantsEntity;
import com.connectfood.core.infrastructure.persistence.entity.RestaurantsTypeEntity;
import com.connectfood.core.infrastructure.persistence.jpa.JpaRestaurantsRepository;
import com.connectfood.core.infrastructure.persistence.jpa.JpaRestaurantsTypeRepository;
import com.connectfood.core.infrastructure.persistence.mappers.RestaurantsInfraMapper;

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
class RestaurantsRepositoryAdapterTest {

  @Mock
  private JpaRestaurantsRepository repository;

  @Mock
  private RestaurantsInfraMapper mapper;

  @Mock
  private JpaRestaurantsTypeRepository restaurantsTypeRepository;

  @InjectMocks
  private RestaurantsRepositoryAdapter adapter;

  @Test
  @DisplayName("Deve salvar e retornar o model mapeado")
  void saveShouldPersistAndReturnMappedModel() {
    final Restaurants restaurants = Mockito.mock(Restaurants.class);
    final RestaurantsType restaurantsTypeDomain = Mockito.mock(RestaurantsType.class);

    final var restaurantsTypeUuid = UUID.randomUUID();
    Mockito.when(restaurantsTypeDomain.getUuid())
        .thenReturn(restaurantsTypeUuid);
    Mockito.when(restaurants.getRestaurantsType())
        .thenReturn(restaurantsTypeDomain);

    final RestaurantsTypeEntity restaurantsTypeEntity = Mockito.mock(RestaurantsTypeEntity.class);

    final RestaurantsEntity entityToSave = Mockito.mock(RestaurantsEntity.class);
    final RestaurantsEntity savedEntity = Mockito.mock(RestaurantsEntity.class);

    final Restaurants mappedDomain = Mockito.mock(Restaurants.class);

    Mockito.when(restaurantsTypeRepository.findByUuid(restaurantsTypeUuid))
        .thenReturn(Optional.of(restaurantsTypeEntity));
    Mockito.when(mapper.toEntity(restaurants, restaurantsTypeEntity))
        .thenReturn(entityToSave);
    Mockito.when(repository.save(entityToSave))
        .thenReturn(savedEntity);
    Mockito.when(mapper.toDomain(savedEntity))
        .thenReturn(mappedDomain);

    final var result = adapter.save(restaurants);

    Assertions.assertSame(mappedDomain, result);

    Mockito.verify(restaurantsTypeRepository, Mockito.times(1))
        .findByUuid(restaurantsTypeUuid);
    Mockito.verify(mapper, Mockito.times(1))
        .toEntity(restaurants, restaurantsTypeEntity);
    Mockito.verify(repository, Mockito.times(1))
        .save(entityToSave);
    Mockito.verify(mapper, Mockito.times(1))
        .toDomain(savedEntity);
    Mockito.verifyNoMoreInteractions(repository, mapper, restaurantsTypeRepository);
  }

  @Test
  @DisplayName("Deve lançar exceção quando tipo de restaurante não existir ao salvar")
  void saveShouldThrowWhenRestaurantsTypeDoesNotExist() {
    final Restaurants restaurants = Mockito.mock(Restaurants.class);
    final RestaurantsType restaurantsTypeDomain = Mockito.mock(RestaurantsType.class);

    final var restaurantsTypeUuid = UUID.randomUUID();
    Mockito.when(restaurantsTypeDomain.getUuid())
        .thenReturn(restaurantsTypeUuid);
    Mockito.when(restaurants.getRestaurantsType())
        .thenReturn(restaurantsTypeDomain);

    Mockito.when(restaurantsTypeRepository.findByUuid(restaurantsTypeUuid))
        .thenReturn(Optional.empty());

    Assertions.assertThrows(NoSuchElementException.class, () -> adapter.save(restaurants));

    Mockito.verify(restaurantsTypeRepository, Mockito.times(1))
        .findByUuid(restaurantsTypeUuid);
    Mockito.verifyNoInteractions(repository, mapper);
    Mockito.verifyNoMoreInteractions(restaurantsTypeRepository);
  }

  @Test
  @DisplayName("Deve atualizar usando o tipo já existente quando o uuid for o mesmo")
  void updateShouldKeepExistingTypeWhenUuidIsSame() {
    final var uuid = UUID.randomUUID();

    final Restaurants restaurants = Mockito.mock(Restaurants.class);
    final RestaurantsType restaurantsTypeDomain = Mockito.mock(RestaurantsType.class);

    final var restaurantsTypeUuid = UUID.randomUUID();
    Mockito.when(restaurantsTypeDomain.getUuid())
        .thenReturn(restaurantsTypeUuid);
    Mockito.when(restaurants.getRestaurantsType())
        .thenReturn(restaurantsTypeDomain);

    final RestaurantsEntity foundEntity = Mockito.mock(RestaurantsEntity.class);

    final RestaurantsTypeEntity existingTypeEntity = Mockito.mock(RestaurantsTypeEntity.class);
    Mockito.when(existingTypeEntity.getUuid())
        .thenReturn(restaurantsTypeUuid);

    Mockito.when(foundEntity.getRestaurantsType())
        .thenReturn(existingTypeEntity);

    final RestaurantsTypeEntity fetchedTypeEntity = Mockito.mock(RestaurantsTypeEntity.class);

    Mockito.when(repository.findByUuid(uuid))
        .thenReturn(Optional.of(foundEntity));
    Mockito.when(restaurantsTypeRepository.findByUuid(restaurantsTypeUuid))
        .thenReturn(Optional.of(fetchedTypeEntity));

    final RestaurantsEntity entityToSave = Mockito.mock(RestaurantsEntity.class);
    final RestaurantsEntity savedEntity = Mockito.mock(RestaurantsEntity.class);
    final Restaurants mappedDomain = Mockito.mock(Restaurants.class);

    Mockito.when(mapper.toEntity(restaurants, foundEntity, existingTypeEntity))
        .thenReturn(entityToSave);
    Mockito.when(repository.save(entityToSave))
        .thenReturn(savedEntity);
    Mockito.when(mapper.toDomain(savedEntity))
        .thenReturn(mappedDomain);

    final var result = adapter.update(uuid, restaurants);

    Assertions.assertSame(mappedDomain, result);

    Mockito.verify(repository, Mockito.times(1))
        .findByUuid(uuid);
    Mockito.verify(restaurantsTypeRepository, Mockito.times(1))
        .findByUuid(restaurantsTypeUuid);
    Mockito.verify(mapper, Mockito.times(1))
        .toEntity(restaurants, foundEntity, existingTypeEntity);
    Mockito.verify(repository, Mockito.times(1))
        .save(entityToSave);
    Mockito.verify(mapper, Mockito.times(1))
        .toDomain(savedEntity);
    Mockito.verifyNoMoreInteractions(repository, mapper, restaurantsTypeRepository);
  }

  @Test
  @DisplayName("Deve atualizar trocando o tipo quando o uuid for diferente")
  void updateShouldReplaceTypeWhenUuidIsDifferent() {
    final var uuid = UUID.randomUUID();

    final Restaurants restaurants = Mockito.mock(Restaurants.class);
    final RestaurantsType restaurantsTypeDomain = Mockito.mock(RestaurantsType.class);

    final var newTypeUuid = UUID.randomUUID();
    Mockito.when(restaurantsTypeDomain.getUuid())
        .thenReturn(newTypeUuid);
    Mockito.when(restaurants.getRestaurantsType())
        .thenReturn(restaurantsTypeDomain);

    final RestaurantsEntity foundEntity = Mockito.mock(RestaurantsEntity.class);

    final var existingTypeUuid = UUID.randomUUID();
    final RestaurantsTypeEntity existingTypeEntity = Mockito.mock(RestaurantsTypeEntity.class);
    Mockito.when(existingTypeEntity.getUuid())
        .thenReturn(existingTypeUuid);

    Mockito.when(foundEntity.getRestaurantsType())
        .thenReturn(existingTypeEntity);

    final RestaurantsTypeEntity fetchedTypeEntity = Mockito.mock(RestaurantsTypeEntity.class);

    Mockito.when(repository.findByUuid(uuid))
        .thenReturn(Optional.of(foundEntity));
    Mockito.when(restaurantsTypeRepository.findByUuid(newTypeUuid))
        .thenReturn(Optional.of(fetchedTypeEntity));

    final RestaurantsEntity entityToSave = Mockito.mock(RestaurantsEntity.class);
    final RestaurantsEntity savedEntity = Mockito.mock(RestaurantsEntity.class);
    final Restaurants mappedDomain = Mockito.mock(Restaurants.class);

    Mockito.when(mapper.toEntity(restaurants, foundEntity, fetchedTypeEntity))
        .thenReturn(entityToSave);
    Mockito.when(repository.save(entityToSave))
        .thenReturn(savedEntity);
    Mockito.when(mapper.toDomain(savedEntity))
        .thenReturn(mappedDomain);

    final var result = adapter.update(uuid, restaurants);

    Assertions.assertSame(mappedDomain, result);

    Mockito.verify(repository, Mockito.times(1))
        .findByUuid(uuid);
    Mockito.verify(restaurantsTypeRepository, Mockito.times(1))
        .findByUuid(newTypeUuid);
    Mockito.verify(mapper, Mockito.times(1))
        .toEntity(restaurants, foundEntity, fetchedTypeEntity);
    Mockito.verify(repository, Mockito.times(1))
        .save(entityToSave);
    Mockito.verify(mapper, Mockito.times(1))
        .toDomain(savedEntity);
    Mockito.verifyNoMoreInteractions(repository, mapper, restaurantsTypeRepository);
  }

  @Test
  @DisplayName("Deve lançar exceção quando uuid não existir ao atualizar")
  void updateShouldThrowWhenRestaurantDoesNotExist() {
    final var uuid = UUID.randomUUID();
    final Restaurants restaurants = Mockito.mock(Restaurants.class);

    Mockito.when(repository.findByUuid(uuid))
        .thenReturn(Optional.empty());

    Assertions.assertThrows(NoSuchElementException.class, () -> adapter.update(uuid, restaurants));

    Mockito.verify(repository, Mockito.times(1))
        .findByUuid(uuid);
    Mockito.verifyNoInteractions(mapper, restaurantsTypeRepository);
    Mockito.verifyNoMoreInteractions(repository);
  }

  @Test
  @DisplayName("Deve lançar exceção quando tipo não existir ao atualizar")
  void updateShouldThrowWhenRestaurantsTypeDoesNotExist() {
    final var uuid = UUID.randomUUID();

    final Restaurants restaurants = Mockito.mock(Restaurants.class);
    final RestaurantsType restaurantsTypeDomain = Mockito.mock(RestaurantsType.class);

    final var typeUuid = UUID.randomUUID();
    Mockito.when(restaurantsTypeDomain.getUuid())
        .thenReturn(typeUuid);
    Mockito.when(restaurants.getRestaurantsType())
        .thenReturn(restaurantsTypeDomain);

    final RestaurantsEntity foundEntity = Mockito.mock(RestaurantsEntity.class);

    Mockito.when(repository.findByUuid(uuid))
        .thenReturn(Optional.of(foundEntity));
    Mockito.when(restaurantsTypeRepository.findByUuid(typeUuid))
        .thenReturn(Optional.empty());

    Assertions.assertThrows(NoSuchElementException.class, () -> adapter.update(uuid, restaurants));

    Mockito.verify(repository, Mockito.times(1))
        .findByUuid(uuid);
    Mockito.verify(restaurantsTypeRepository, Mockito.times(1))
        .findByUuid(typeUuid);
    Mockito.verifyNoInteractions(mapper);
    Mockito.verifyNoMoreInteractions(repository, restaurantsTypeRepository);
  }

  @Test
  @DisplayName("Deve retornar Optional vazio quando não encontrar por uuid")
  void findByUuidShouldReturnEmptyWhenNotFound() {
    final var uuid = UUID.randomUUID();

    Mockito.when(repository.findByUuid(uuid))
        .thenReturn(Optional.empty());

    final var result = adapter.findByUuid(uuid);

    Assertions.assertNotNull(result);
    Assertions.assertTrue(result.isEmpty());

    Mockito.verify(repository, Mockito.times(1))
        .findByUuid(uuid);
    Mockito.verifyNoInteractions(mapper, restaurantsTypeRepository);
    Mockito.verifyNoMoreInteractions(repository);
  }

  @Test
  @DisplayName("Deve retornar model quando encontrar por uuid")
  void findByUuidShouldReturnMappedModelWhenFound() {
    final var uuid = UUID.randomUUID();

    final RestaurantsEntity foundEntity = Mockito.mock(RestaurantsEntity.class);
    final Restaurants mappedDomain = Mockito.mock(Restaurants.class);

    Mockito.when(repository.findByUuid(uuid))
        .thenReturn(Optional.of(foundEntity));
    Mockito.when(mapper.toDomain(foundEntity))
        .thenReturn(mappedDomain);

    final var result = adapter.findByUuid(uuid);

    Assertions.assertTrue(result.isPresent());
    Assertions.assertSame(mappedDomain, result.get());

    Mockito.verify(repository, Mockito.times(1))
        .findByUuid(uuid);
    Mockito.verify(mapper, Mockito.times(1))
        .toDomain(foundEntity);
    Mockito.verifyNoMoreInteractions(repository, mapper, restaurantsTypeRepository);
  }

  @Test
  @DisplayName("Deve listar paginado com defaults quando sort e direction forem null")
  void findAllShouldReturnPagedResultWithDefaultsWhenSortAndDirectionAreNull() {
    final var uuidType = UUID.randomUUID();

    final RestaurantsEntity entity1 = Mockito.mock(RestaurantsEntity.class);
    final RestaurantsEntity entity2 = Mockito.mock(RestaurantsEntity.class);

    final Restaurants domain1 = Mockito.mock(Restaurants.class);
    final Restaurants domain2 = Mockito.mock(Restaurants.class);

    Mockito.when(mapper.toDomain(entity1))
        .thenReturn(domain1);
    Mockito.when(mapper.toDomain(entity2))
        .thenReturn(domain2);

    final Page<RestaurantsEntity> pageResult =
        new PageImpl<>(List.of(entity1, entity2), Pageable.unpaged(), 2);

    Mockito.when(repository.findAll(
            ArgumentMatchers.<Specification<RestaurantsEntity>>any(),
            ArgumentMatchers.any(Pageable.class)
        ))
        .thenReturn(pageResult);

    final PageModel<List<Restaurants>> result =
        adapter.findAll("abc", uuidType, 0, 10, null, null);

    Assertions.assertNotNull(result);
    Assertions.assertEquals(2, result.content()
        .size()
    );
    Assertions.assertEquals(List.of(domain1, domain2), result.content());
    Assertions.assertEquals(2L, result.total());

    Mockito.verify(repository, Mockito.times(1))
        .findAll(ArgumentMatchers.<Specification<RestaurantsEntity>>any(), ArgumentMatchers.any(Pageable.class));
    Mockito.verify(mapper, Mockito.times(1))
        .toDomain(entity1);
    Mockito.verify(mapper, Mockito.times(1))
        .toDomain(entity2);
    Mockito.verifyNoMoreInteractions(repository, mapper, restaurantsTypeRepository);
  }

  @Test
  @DisplayName("Deve listar paginado respeitando sort e direction informados")
  void findAllShouldReturnPagedResultUsingProvidedSortAndDirection() {
    final RestaurantsEntity entity = Mockito.mock(RestaurantsEntity.class);
    final Restaurants domain = Mockito.mock(Restaurants.class);

    Mockito.when(mapper.toDomain(entity))
        .thenReturn(domain);

    final Page<RestaurantsEntity> pageResult =
        new PageImpl<>(List.of(entity), Pageable.unpaged(), 1);

    Mockito.when(repository.findAll(
            ArgumentMatchers.<Specification<RestaurantsEntity>>any(),
            ArgumentMatchers.any(Pageable.class)
        ))
        .thenReturn(pageResult);

    final PageModel<List<Restaurants>> result =
        adapter.findAll(null, null, 1, 5, "name", "DESC");

    Assertions.assertNotNull(result);
    Assertions.assertEquals(1, result.content()
        .size()
    );
    Assertions.assertEquals(List.of(domain), result.content());
    Assertions.assertEquals(1L, result.total());

    Mockito.verify(repository, Mockito.times(1))
        .findAll(ArgumentMatchers.<Specification<RestaurantsEntity>>any(), ArgumentMatchers.any(Pageable.class));
    Mockito.verify(mapper, Mockito.times(1))
        .toDomain(entity);
    Mockito.verifyNoMoreInteractions(repository, mapper, restaurantsTypeRepository);
  }

  @Test
  @DisplayName("Deve deletar pelo uuid")
  void deleteShouldDeleteByUuid() {
    final var uuid = UUID.randomUUID();

    adapter.delete(uuid);

    Mockito.verify(repository, Mockito.times(1))
        .deleteByUuid(uuid);
    Mockito.verifyNoInteractions(mapper, restaurantsTypeRepository);
    Mockito.verifyNoMoreInteractions(repository);
  }
}
