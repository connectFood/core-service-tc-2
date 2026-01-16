package com.connectfood.infrastructure.persistence.adapter;

import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

import com.connectfood.core.domain.model.RestaurantOpeningHour;
import com.connectfood.infrastructure.persistence.entity.RestaurantOpeningHourEntity;
import com.connectfood.infrastructure.persistence.entity.RestaurantEntity;
import com.connectfood.infrastructure.persistence.jpa.JpaRestaurantOpeningHourRepository;
import com.connectfood.infrastructure.persistence.jpa.JpaRestaurantRepository;
import com.connectfood.infrastructure.persistence.mappers.RestaurantOpeningHourInfraMapper;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class RestaurantOpeningHourGatewayAdapterTest {

  @Mock
  private JpaRestaurantOpeningHourRepository repository;

  @Mock
  private RestaurantOpeningHourInfraMapper mapper;

  @Mock
  private JpaRestaurantRepository restaurantsRepository;

  @InjectMocks
  private RestaurantOpeningHourGatewayAdapter adapter;

  @Test
  @DisplayName("Deve salvar e retornar o model mapeado")
  void saveShouldPersistAndReturnMappedModel() {
    final var restaurantUuid = UUID.randomUUID();
    final RestaurantOpeningHour model = Mockito.mock(RestaurantOpeningHour.class);

    final RestaurantEntity restaurantEntity = Mockito.mock(RestaurantEntity.class);

    final RestaurantOpeningHourEntity entityToSave = Mockito.mock(RestaurantOpeningHourEntity.class);
    final RestaurantOpeningHourEntity savedEntity = Mockito.mock(RestaurantOpeningHourEntity.class);

    final RestaurantOpeningHour mappedDomain = Mockito.mock(RestaurantOpeningHour.class);

    Mockito.when(restaurantsRepository.findByUuid(restaurantUuid))
        .thenReturn(Optional.of(restaurantEntity));
    Mockito.when(mapper.toEntity(model, restaurantEntity))
        .thenReturn(entityToSave);
    Mockito.when(repository.save(entityToSave))
        .thenReturn(savedEntity);
    Mockito.when(mapper.toDomain(savedEntity))
        .thenReturn(mappedDomain);

    final var result = adapter.save(model, restaurantUuid);

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
    final var restaurantUuid = UUID.randomUUID();
    final RestaurantOpeningHour model = Mockito.mock(RestaurantOpeningHour.class);

    Mockito.when(restaurantsRepository.findByUuid(restaurantUuid))
        .thenReturn(Optional.empty());

    Assertions.assertThrows(NoSuchElementException.class, () -> adapter.save(model, restaurantUuid));

    Mockito.verify(restaurantsRepository, Mockito.times(1))
        .findByUuid(restaurantUuid);
    Mockito.verifyNoInteractions(repository, mapper);
    Mockito.verifyNoMoreInteractions(restaurantsRepository);
  }

  @Test
  @DisplayName("Deve atualizar e retornar o model mapeado")
  void updateShouldPersistAndReturnMappedModel() {
    final var uuid = UUID.randomUUID();
    final RestaurantOpeningHour model = Mockito.mock(RestaurantOpeningHour.class);

    final RestaurantOpeningHourEntity foundEntity = Mockito.mock(RestaurantOpeningHourEntity.class);
    final RestaurantOpeningHourEntity entityToSave = Mockito.mock(RestaurantOpeningHourEntity.class);
    final RestaurantOpeningHourEntity savedEntity = Mockito.mock(RestaurantOpeningHourEntity.class);

    final RestaurantOpeningHour mappedDomain = Mockito.mock(RestaurantOpeningHour.class);

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
    final RestaurantOpeningHour model = Mockito.mock(RestaurantOpeningHour.class);

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

    final Optional<RestaurantOpeningHour> result = adapter.findByUuid(uuid);

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

    final RestaurantOpeningHourEntity foundEntity = Mockito.mock(RestaurantOpeningHourEntity.class);
    final RestaurantOpeningHour mappedDomain = Mockito.mock(RestaurantOpeningHour.class);

    Mockito.when(repository.findByUuid(uuid))
        .thenReturn(Optional.of(foundEntity));
    Mockito.when(mapper.toDomain(foundEntity))
        .thenReturn(mappedDomain);

    final Optional<RestaurantOpeningHour> result = adapter.findByUuid(uuid);

    Assertions.assertTrue(result.isPresent());
    Assertions.assertSame(mappedDomain, result.get());

    Mockito.verify(repository, Mockito.times(1))
        .findByUuid(uuid);
    Mockito.verify(mapper, Mockito.times(1))
        .toDomain(foundEntity);
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
