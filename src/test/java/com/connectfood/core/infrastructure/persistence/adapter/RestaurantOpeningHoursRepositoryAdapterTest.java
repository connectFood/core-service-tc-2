package com.connectfood.core.infrastructure.persistence.adapter;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

import com.connectfood.core.domain.model.RestaurantOpeningHours;
import com.connectfood.core.domain.model.Restaurants;
import com.connectfood.core.domain.model.commons.PageModel;
import com.connectfood.core.infrastructure.persistence.entity.RestaurantOpeningHoursEntity;
import com.connectfood.core.infrastructure.persistence.entity.RestaurantsEntity;
import com.connectfood.core.infrastructure.persistence.jpa.JpaRestaurantOpeningHoursRepository;
import com.connectfood.core.infrastructure.persistence.jpa.JpaRestaurantsRepository;
import com.connectfood.core.infrastructure.persistence.mappers.RestaurantOpeningHoursInfraMapper;

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
class RestaurantOpeningHoursRepositoryAdapterTest {

  @Mock
  private JpaRestaurantOpeningHoursRepository repository;

  @Mock
  private RestaurantOpeningHoursInfraMapper mapper;

  @Mock
  private JpaRestaurantsRepository restaurantsRepository;

  @InjectMocks
  private RestaurantOpeningHoursRepositoryAdapter adapter;

  @Test
  @DisplayName("Deve salvar e retornar o model mapeado")
  void saveShouldPersistAndReturnMappedModel() {
    final RestaurantOpeningHours model = Mockito.mock(RestaurantOpeningHours.class);
    final Restaurants restaurantDomain = Mockito.mock(Restaurants.class);

    final var restaurantUuid = UUID.randomUUID();
    Mockito.when(restaurantDomain.getUuid())
        .thenReturn(restaurantUuid);
    Mockito.when(model.getRestaurant())
        .thenReturn(restaurantDomain);

    final RestaurantsEntity restaurantEntity = Mockito.mock(RestaurantsEntity.class);

    final RestaurantOpeningHoursEntity entityToSave = Mockito.mock(RestaurantOpeningHoursEntity.class);
    final RestaurantOpeningHoursEntity savedEntity = Mockito.mock(RestaurantOpeningHoursEntity.class);

    final RestaurantOpeningHours mappedDomain = Mockito.mock(RestaurantOpeningHours.class);

    Mockito.when(restaurantsRepository.findByUuid(restaurantUuid))
        .thenReturn(Optional.of(restaurantEntity));
    Mockito.when(mapper.toEntity(model, restaurantEntity))
        .thenReturn(entityToSave);
    Mockito.when(repository.save(entityToSave))
        .thenReturn(savedEntity);
    Mockito.when(mapper.toDomain(savedEntity))
        .thenReturn(mappedDomain);

    final var result = adapter.save(model);

    Assertions.assertSame(mappedDomain, result);

    Mockito.verify(restaurantsRepository, Mockito.times(1))
        .findByUuid(restaurantUuid);
    Mockito.verify(mapper, Mockito.times(1))
        .toEntity(model, restaurantEntity);
    Mockito.verify(repository, Mockito.times(1))
        .save(entityToSave);
    Mockito.verify(mapper, Mockito.times(1))
        .toDomain(savedEntity);
    Mockito.verifyNoMoreInteractions(repository, mapper, restaurantsRepository);
  }

  @Test
  @DisplayName("Deve lançar exceção quando restaurante não existir ao salvar")
  void saveShouldThrowWhenRestaurantDoesNotExist() {
    final RestaurantOpeningHours model = Mockito.mock(RestaurantOpeningHours.class);
    final Restaurants restaurantDomain = Mockito.mock(Restaurants.class);

    final var restaurantUuid = UUID.randomUUID();
    Mockito.when(restaurantDomain.getUuid())
        .thenReturn(restaurantUuid);
    Mockito.when(model.getRestaurant())
        .thenReturn(restaurantDomain);

    Mockito.when(restaurantsRepository.findByUuid(restaurantUuid))
        .thenReturn(Optional.empty());

    Assertions.assertThrows(NoSuchElementException.class, () -> adapter.save(model));

    Mockito.verify(restaurantsRepository, Mockito.times(1))
        .findByUuid(restaurantUuid);
    Mockito.verifyNoInteractions(repository, mapper);
    Mockito.verifyNoMoreInteractions(restaurantsRepository);
  }

  @Test
  @DisplayName("Deve atualizar e retornar o model mapeado")
  void updateShouldPersistAndReturnMappedModel() {
    final var uuid = UUID.randomUUID();
    final RestaurantOpeningHours model = Mockito.mock(RestaurantOpeningHours.class);

    final RestaurantOpeningHoursEntity foundEntity = Mockito.mock(RestaurantOpeningHoursEntity.class);
    final RestaurantOpeningHoursEntity entityToSave = Mockito.mock(RestaurantOpeningHoursEntity.class);
    final RestaurantOpeningHoursEntity savedEntity = Mockito.mock(RestaurantOpeningHoursEntity.class);

    final RestaurantOpeningHours mappedDomain = Mockito.mock(RestaurantOpeningHours.class);

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
    Mockito.verifyNoMoreInteractions(repository, mapper, restaurantsRepository);
  }

  @Test
  @DisplayName("Deve lançar exceção quando uuid não existir ao atualizar")
  void updateShouldThrowWhenUuidDoesNotExist() {
    final var uuid = UUID.randomUUID();
    final RestaurantOpeningHours model = Mockito.mock(RestaurantOpeningHours.class);

    Mockito.when(repository.findByUuid(uuid))
        .thenReturn(Optional.empty());

    Assertions.assertThrows(NoSuchElementException.class, () -> adapter.update(uuid, model));

    Mockito.verify(repository, Mockito.times(1))
        .findByUuid(uuid);
    Mockito.verifyNoInteractions(mapper, restaurantsRepository);
    Mockito.verifyNoMoreInteractions(repository);
  }

  @Test
  @DisplayName("Deve retornar Optional vazio quando não encontrar por uuid")
  void findByUuidShouldReturnEmptyWhenNotFound() {
    final var uuid = UUID.randomUUID();

    Mockito.when(repository.findByUuid(uuid))
        .thenReturn(Optional.empty());

    final Optional<RestaurantOpeningHours> result = adapter.findByUuid(uuid);

    Assertions.assertNotNull(result);
    Assertions.assertTrue(result.isEmpty());

    Mockito.verify(repository, Mockito.times(1))
        .findByUuid(uuid);
    Mockito.verifyNoInteractions(mapper, restaurantsRepository);
    Mockito.verifyNoMoreInteractions(repository);
  }

  @Test
  @DisplayName("Deve retornar model quando encontrar por uuid")
  void findByUuidShouldReturnMappedModelWhenFound() {
    final var uuid = UUID.randomUUID();

    final RestaurantOpeningHoursEntity foundEntity = Mockito.mock(RestaurantOpeningHoursEntity.class);
    final RestaurantOpeningHours mappedDomain = Mockito.mock(RestaurantOpeningHours.class);

    Mockito.when(repository.findByUuid(uuid))
        .thenReturn(Optional.of(foundEntity));
    Mockito.when(mapper.toDomain(foundEntity))
        .thenReturn(mappedDomain);

    final Optional<RestaurantOpeningHours> result = adapter.findByUuid(uuid);

    Assertions.assertTrue(result.isPresent());
    Assertions.assertSame(mappedDomain, result.get());

    Mockito.verify(repository, Mockito.times(1))
        .findByUuid(uuid);
    Mockito.verify(mapper, Mockito.times(1))
        .toDomain(foundEntity);
    Mockito.verifyNoMoreInteractions(repository, mapper, restaurantsRepository);
  }

  @Test
  @DisplayName("Deve listar paginado com defaults quando sort e direction forem null")
  void findAllShouldReturnPagedResultWithDefaultsWhenSortAndDirectionAreNull() {
    final var restaurantUuid = UUID.randomUUID();

    final var entity1 = Mockito.mock(RestaurantOpeningHoursEntity.class);
    final var entity2 = Mockito.mock(RestaurantOpeningHoursEntity.class);

    final RestaurantOpeningHours domain1 = Mockito.mock(RestaurantOpeningHours.class);
    final RestaurantOpeningHours domain2 = Mockito.mock(RestaurantOpeningHours.class);

    Mockito.when(mapper.toDomainAll(entity1))
        .thenReturn(domain1);
    Mockito.when(mapper.toDomainAll(entity2))
        .thenReturn(domain2);

    final Page<RestaurantOpeningHoursEntity> pageResult =
        new PageImpl<>(List.of(entity1, entity2), Pageable.unpaged(), 2);

    Mockito.when(repository.findAll(
            ArgumentMatchers.<Specification<RestaurantOpeningHoursEntity>>any(),
            ArgumentMatchers.any(Pageable.class)
        ))
        .thenReturn(pageResult);

    final PageModel<List<RestaurantOpeningHours>> result =
        adapter.findAll(restaurantUuid, 0, 10, null, null);

    Assertions.assertNotNull(result);
    Assertions.assertEquals(2, result.content().size());
    Assertions.assertEquals(List.of(domain1, domain2), result.content());
    Assertions.assertEquals(2L, result.total());

    Mockito.verify(repository, Mockito.times(1))
        .findAll(ArgumentMatchers.<Specification<RestaurantOpeningHoursEntity>>any(), ArgumentMatchers.any(Pageable.class));
    Mockito.verify(mapper, Mockito.times(1))
        .toDomainAll(entity1);
    Mockito.verify(mapper, Mockito.times(1))
        .toDomainAll(entity2);
    Mockito.verifyNoMoreInteractions(repository, mapper, restaurantsRepository);
  }

  @Test
  @DisplayName("Deve listar paginado respeitando sort e direction informados")
  void findAllShouldReturnPagedResultUsingProvidedSortAndDirection() {
    final var restaurantUuid = UUID.randomUUID();

    final var entity = Mockito.mock(RestaurantOpeningHoursEntity.class);
    final RestaurantOpeningHours domain = Mockito.mock(RestaurantOpeningHours.class);

    Mockito.when(mapper.toDomainAll(entity))
        .thenReturn(domain);

    final Page<RestaurantOpeningHoursEntity> pageResult =
        new PageImpl<>(List.of(entity), Pageable.unpaged(), 1);

    Mockito.when(repository.findAll(
            ArgumentMatchers.<Specification<RestaurantOpeningHoursEntity>>any(),
            ArgumentMatchers.any(Pageable.class)
        ))
        .thenReturn(pageResult);

    final PageModel<List<RestaurantOpeningHours>> result =
        adapter.findAll(restaurantUuid, 1, 5, "dayWeek", "DESC");

    Assertions.assertNotNull(result);
    Assertions.assertEquals(1, result.content().size());
    Assertions.assertEquals(List.of(domain), result.content());
    Assertions.assertEquals(1L, result.total());

    Mockito.verify(repository, Mockito.times(1))
        .findAll(ArgumentMatchers.<Specification<RestaurantOpeningHoursEntity>>any(), ArgumentMatchers.any(Pageable.class));
    Mockito.verify(mapper, Mockito.times(1))
        .toDomainAll(entity);
    Mockito.verifyNoMoreInteractions(repository, mapper, restaurantsRepository);
  }

  @Test
  @DisplayName("Deve deletar pelo uuid")
  void deleteShouldDeleteByUuid() {
    final var uuid = UUID.randomUUID();

    adapter.delete(uuid);

    Mockito.verify(repository, Mockito.times(1))
        .deleteByUuid(uuid);
    Mockito.verifyNoInteractions(mapper, restaurantsRepository);
    Mockito.verifyNoMoreInteractions(repository);
  }
}
