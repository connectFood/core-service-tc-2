package com.connectfood.infrastructure.persistence.adapter;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

import com.connectfood.core.domain.model.Restaurant;
import com.connectfood.core.domain.model.RestaurantType;
import com.connectfood.core.domain.model.commons.PageModel;
import com.connectfood.infrastructure.persistence.entity.RestaurantEntity;
import com.connectfood.infrastructure.persistence.entity.RestaurantTypeEntity;
import com.connectfood.infrastructure.persistence.jpa.JpaRestaurantRepository;
import com.connectfood.infrastructure.persistence.jpa.JpaRestaurantTypeRepository;
import com.connectfood.infrastructure.persistence.mappers.RestaurantInfraMapper;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

@ExtendWith(MockitoExtension.class)
class RestaurantGatewayAdapterTest {

  @Mock
  private JpaRestaurantRepository repository;

  @Mock
  private RestaurantInfraMapper mapper;

  @Mock
  private JpaRestaurantTypeRepository restaurantsTypeRepository;

  @InjectMocks
  private RestaurantGatewayAdapter adapter;

  @Test
  @DisplayName("Deve salvar e retornar o model mapeado")
  void saveShouldPersistAndReturnMappedModel() {
    final Restaurant restaurant = Mockito.mock(Restaurant.class);
    final RestaurantType restaurantTypeDomain = Mockito.mock(RestaurantType.class);

    final var restaurantsTypeUuid = UUID.randomUUID();
    Mockito.when(restaurantTypeDomain.getUuid())
        .thenReturn(restaurantsTypeUuid);
    Mockito.when(restaurant.getRestaurantType())
        .thenReturn(restaurantTypeDomain);

    final RestaurantTypeEntity restaurantTypeEntity = Mockito.mock(RestaurantTypeEntity.class);

    final RestaurantEntity entityToSave = Mockito.mock(RestaurantEntity.class);
    final RestaurantEntity savedEntity = Mockito.mock(RestaurantEntity.class);

    final Restaurant mappedDomain = Mockito.mock(Restaurant.class);

    Mockito.when(restaurantsTypeRepository.findByUuid(restaurantsTypeUuid))
        .thenReturn(Optional.of(restaurantTypeEntity));
    Mockito.when(mapper.toEntity(restaurant, restaurantTypeEntity))
        .thenReturn(entityToSave);
    Mockito.when(repository.save(entityToSave))
        .thenReturn(savedEntity);
    Mockito.when(mapper.toDomain(savedEntity))
        .thenReturn(mappedDomain);

    final var result = adapter.save(restaurant);

    Assertions.assertSame(mappedDomain, result);

    Mockito.verify(restaurantsTypeRepository, Mockito.times(1))
        .findByUuid(restaurantsTypeUuid);
    Mockito.verify(mapper, Mockito.times(1))
        .toEntity(restaurant, restaurantTypeEntity);
    Mockito.verify(repository, Mockito.times(1))
        .save(entityToSave);
    Mockito.verify(mapper, Mockito.times(1))
        .toDomain(savedEntity);
    Mockito.verifyNoMoreInteractions(repository, mapper, restaurantsTypeRepository);
  }

  @Test
  @DisplayName("Deve lançar exceção quando tipo de restaurante não existir ao salvar")
  void saveShouldThrowWhenRestaurantsTypeDoesNotExist() {
    final Restaurant restaurant = Mockito.mock(Restaurant.class);
    final RestaurantType restaurantTypeDomain = Mockito.mock(RestaurantType.class);

    final var restaurantsTypeUuid = UUID.randomUUID();
    Mockito.when(restaurantTypeDomain.getUuid())
        .thenReturn(restaurantsTypeUuid);
    Mockito.when(restaurant.getRestaurantType())
        .thenReturn(restaurantTypeDomain);

    Mockito.when(restaurantsTypeRepository.findByUuid(restaurantsTypeUuid))
        .thenReturn(Optional.empty());

    Assertions.assertThrows(NoSuchElementException.class, () -> adapter.save(restaurant));

    Mockito.verify(restaurantsTypeRepository, Mockito.times(1))
        .findByUuid(restaurantsTypeUuid);
    Mockito.verifyNoInteractions(repository, mapper);
    Mockito.verifyNoMoreInteractions(restaurantsTypeRepository);
  }

  @Test
  @DisplayName("Deve atualizar usando o tipo já existente quando o uuid for o mesmo")
  void updateShouldKeepExistingTypeWhenUuidIsSame() {
    final var uuid = UUID.randomUUID();

    final Restaurant restaurant = Mockito.mock(Restaurant.class);
    final RestaurantType restaurantTypeDomain = Mockito.mock(RestaurantType.class);

    final var restaurantsTypeUuid = UUID.randomUUID();
    Mockito.when(restaurantTypeDomain.getUuid())
        .thenReturn(restaurantsTypeUuid);
    Mockito.when(restaurant.getRestaurantType())
        .thenReturn(restaurantTypeDomain);

    final RestaurantEntity foundEntity = Mockito.mock(RestaurantEntity.class);

    final RestaurantTypeEntity existingTypeEntity = Mockito.mock(RestaurantTypeEntity.class);
    Mockito.when(existingTypeEntity.getUuid())
        .thenReturn(restaurantsTypeUuid);

    Mockito.when(foundEntity.getRestaurantsType())
        .thenReturn(existingTypeEntity);

    final RestaurantTypeEntity fetchedTypeEntity = Mockito.mock(RestaurantTypeEntity.class);

    Mockito.when(repository.findByUuid(uuid))
        .thenReturn(Optional.of(foundEntity));
    Mockito.when(restaurantsTypeRepository.findByUuid(restaurantsTypeUuid))
        .thenReturn(Optional.of(fetchedTypeEntity));

    final RestaurantEntity entityToSave = Mockito.mock(RestaurantEntity.class);
    final RestaurantEntity savedEntity = Mockito.mock(RestaurantEntity.class);
    final Restaurant mappedDomain = Mockito.mock(Restaurant.class);

    Mockito.when(mapper.toEntity(restaurant, foundEntity, existingTypeEntity))
        .thenReturn(entityToSave);
    Mockito.when(repository.save(entityToSave))
        .thenReturn(savedEntity);
    Mockito.when(mapper.toDomain(savedEntity))
        .thenReturn(mappedDomain);

    final var result = adapter.update(uuid, restaurant);

    Assertions.assertSame(mappedDomain, result);

    Mockito.verify(repository, Mockito.times(1))
        .findByUuid(uuid);
    Mockito.verify(restaurantsTypeRepository, Mockito.times(1))
        .findByUuid(restaurantsTypeUuid);
    Mockito.verify(mapper, Mockito.times(1))
        .toEntity(restaurant, foundEntity, existingTypeEntity);
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

    final Restaurant restaurant = Mockito.mock(Restaurant.class);
    final RestaurantType restaurantTypeDomain = Mockito.mock(RestaurantType.class);

    final var newTypeUuid = UUID.randomUUID();
    Mockito.when(restaurantTypeDomain.getUuid())
        .thenReturn(newTypeUuid);
    Mockito.when(restaurant.getRestaurantType())
        .thenReturn(restaurantTypeDomain);

    final RestaurantEntity foundEntity = Mockito.mock(RestaurantEntity.class);

    final var existingTypeUuid = UUID.randomUUID();
    final RestaurantTypeEntity existingTypeEntity = Mockito.mock(RestaurantTypeEntity.class);
    Mockito.when(existingTypeEntity.getUuid())
        .thenReturn(existingTypeUuid);

    Mockito.when(foundEntity.getRestaurantsType())
        .thenReturn(existingTypeEntity);

    final RestaurantTypeEntity fetchedTypeEntity = Mockito.mock(RestaurantTypeEntity.class);

    Mockito.when(repository.findByUuid(uuid))
        .thenReturn(Optional.of(foundEntity));
    Mockito.when(restaurantsTypeRepository.findByUuid(newTypeUuid))
        .thenReturn(Optional.of(fetchedTypeEntity));

    final RestaurantEntity entityToSave = Mockito.mock(RestaurantEntity.class);
    final RestaurantEntity savedEntity = Mockito.mock(RestaurantEntity.class);
    final Restaurant mappedDomain = Mockito.mock(Restaurant.class);

    Mockito.when(mapper.toEntity(restaurant, foundEntity, fetchedTypeEntity))
        .thenReturn(entityToSave);
    Mockito.when(repository.save(entityToSave))
        .thenReturn(savedEntity);
    Mockito.when(mapper.toDomain(savedEntity))
        .thenReturn(mappedDomain);

    final var result = adapter.update(uuid, restaurant);

    Assertions.assertSame(mappedDomain, result);

    Mockito.verify(repository, Mockito.times(1))
        .findByUuid(uuid);
    Mockito.verify(restaurantsTypeRepository, Mockito.times(1))
        .findByUuid(newTypeUuid);
    Mockito.verify(mapper, Mockito.times(1))
        .toEntity(restaurant, foundEntity, fetchedTypeEntity);
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
    final Restaurant restaurant = Mockito.mock(Restaurant.class);

    Mockito.when(repository.findByUuid(uuid))
        .thenReturn(Optional.empty());

    Assertions.assertThrows(NoSuchElementException.class, () -> adapter.update(uuid, restaurant));

    Mockito.verify(repository, Mockito.times(1))
        .findByUuid(uuid);
    Mockito.verifyNoInteractions(mapper, restaurantsTypeRepository);
    Mockito.verifyNoMoreInteractions(repository);
  }

  @Test
  @DisplayName("Deve lançar exceção quando tipo não existir ao atualizar")
  void updateShouldThrowWhenRestaurantsTypeDoesNotExist() {
    final var uuid = UUID.randomUUID();

    final Restaurant restaurant = Mockito.mock(Restaurant.class);
    final RestaurantType restaurantTypeDomain = Mockito.mock(RestaurantType.class);

    final var typeUuid = UUID.randomUUID();
    Mockito.when(restaurantTypeDomain.getUuid())
        .thenReturn(typeUuid);
    Mockito.when(restaurant.getRestaurantType())
        .thenReturn(restaurantTypeDomain);

    final RestaurantEntity foundEntity = Mockito.mock(RestaurantEntity.class);

    Mockito.when(repository.findByUuid(uuid))
        .thenReturn(Optional.of(foundEntity));
    Mockito.when(restaurantsTypeRepository.findByUuid(typeUuid))
        .thenReturn(Optional.empty());

    Assertions.assertThrows(NoSuchElementException.class, () -> adapter.update(uuid, restaurant));

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

    final RestaurantEntity foundEntity = Mockito.mock(RestaurantEntity.class);
    final Restaurant mappedDomain = Mockito.mock(Restaurant.class);

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

    final RestaurantEntity entity1 = Mockito.mock(RestaurantEntity.class);
    final RestaurantEntity entity2 = Mockito.mock(RestaurantEntity.class);

    final Restaurant domain1 = Mockito.mock(Restaurant.class);
    final Restaurant domain2 = Mockito.mock(Restaurant.class);

    Mockito.when(mapper.toDomain(entity1))
        .thenReturn(domain1);
    Mockito.when(mapper.toDomain(entity2))
        .thenReturn(domain2);

    final Page<RestaurantEntity> pageResult =
        new PageImpl<>(List.of(entity1, entity2), Pageable.unpaged(), 2);

    final ArgumentCaptor<Pageable> pageableCaptor = ArgumentCaptor.forClass(Pageable.class);

    Mockito.when(repository.findAll(
            ArgumentMatchers.<Specification<RestaurantEntity>>any(),
            ArgumentMatchers.any(Pageable.class)
        ))
        .thenReturn(pageResult);

    final PageModel<List<Restaurant>> result =
        adapter.findAll(
            "abc",
            uuidType,
            "Rua A",
            "São Paulo",
            "SP",
            0,
            10,
            null,
            null
        );

    Assertions.assertNotNull(result);
    Assertions.assertEquals(2, result.content()
        .size()
    );
    Assertions.assertEquals(List.of(domain1, domain2), result.content());
    Assertions.assertEquals(2L, result.total());

    Mockito.verify(repository, Mockito.times(1))
        .findAll(ArgumentMatchers.<Specification<RestaurantEntity>>any(), pageableCaptor.capture());

    final Pageable usedPageable = pageableCaptor.getValue();
    Assertions.assertEquals(0, usedPageable.getPageNumber());
    Assertions.assertEquals(10, usedPageable.getPageSize());
    Assertions.assertEquals(Sort.by(Sort.Direction.ASC, "id"), usedPageable.getSort());

    Mockito.verify(mapper, Mockito.times(1))
        .toDomain(entity1);
    Mockito.verify(mapper, Mockito.times(1))
        .toDomain(entity2);
    Mockito.verifyNoMoreInteractions(repository, mapper, restaurantsTypeRepository);
  }

  @Test
  @DisplayName("Deve listar paginado respeitando sort e direction informados")
  void findAllShouldReturnPagedResultUsingProvidedSortAndDirection() {
    final RestaurantEntity entity = Mockito.mock(RestaurantEntity.class);
    final Restaurant domain = Mockito.mock(Restaurant.class);

    Mockito.when(mapper.toDomain(entity))
        .thenReturn(domain);

    final Page<RestaurantEntity> pageResult =
        new PageImpl<>(List.of(entity), Pageable.unpaged(), 1);

    final ArgumentCaptor<Pageable> pageableCaptor = ArgumentCaptor.forClass(Pageable.class);

    Mockito.when(repository.findAll(
            ArgumentMatchers.<Specification<RestaurantEntity>>any(),
            ArgumentMatchers.any(Pageable.class)
        ))
        .thenReturn(pageResult);

    final PageModel<List<Restaurant>> result =
        adapter.findAll(
            null,
            null,
            null,
            null,
            null,
            1,
            5,
            "name",
            "DESC"
        );

    Assertions.assertNotNull(result);
    Assertions.assertEquals(1, result.content()
        .size()
    );
    Assertions.assertEquals(List.of(domain), result.content());
    Assertions.assertEquals(1L, result.total());

    Mockito.verify(repository, Mockito.times(1))
        .findAll(ArgumentMatchers.<Specification<RestaurantEntity>>any(), pageableCaptor.capture());

    final Pageable usedPageable = pageableCaptor.getValue();
    Assertions.assertEquals(1, usedPageable.getPageNumber());
    Assertions.assertEquals(5, usedPageable.getPageSize());
    Assertions.assertEquals(Sort.by(Sort.Direction.DESC, "name"), usedPageable.getSort());

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
