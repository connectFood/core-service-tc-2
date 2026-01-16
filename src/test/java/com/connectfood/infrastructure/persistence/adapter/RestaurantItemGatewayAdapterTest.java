package com.connectfood.infrastructure.persistence.adapter;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.connectfood.core.domain.model.RestaurantItem;
import com.connectfood.core.domain.model.Restaurant;
import com.connectfood.infrastructure.persistence.entity.RestaurantItemsEntity;
import com.connectfood.infrastructure.persistence.entity.RestaurantsEntity;
import com.connectfood.infrastructure.persistence.jpa.JpaRestaurantItemsRepository;
import com.connectfood.infrastructure.persistence.jpa.JpaRestaurantsRepository;
import com.connectfood.infrastructure.persistence.mappers.RestaurantItemsInfraMapper;

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
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

@ExtendWith(MockitoExtension.class)
class RestaurantItemGatewayAdapterTest {

  @Mock
  private JpaRestaurantItemsRepository repository;

  @Mock
  private RestaurantItemsInfraMapper mapper;

  @Mock
  private JpaRestaurantsRepository restaurantsRepository;

  @InjectMocks
  private RestaurantItemGatewayAdapter adapter;

  @Test
  @DisplayName("Deve salvar o item quando o restaurant existir e retornar o domain mapeado")
  void shouldSaveItemWhenRestaurantExistsAndReturnMappedDomain() {
    final var restaurantUuid = UUID.randomUUID();

    final Restaurant restaurantDomain = Mockito.mock(Restaurant.class);
    Mockito.when(restaurantDomain.getUuid())
        .thenReturn(restaurantUuid);

    final RestaurantItem model = Mockito.mock(RestaurantItem.class);
    Mockito.when(model.getRestaurant())
        .thenReturn(restaurantDomain);

    final var restaurantEntity = new RestaurantsEntity();
    Mockito.when(restaurantsRepository.findByUuid(restaurantUuid))
        .thenReturn(Optional.of(restaurantEntity));

    final var toSaveEntity = new RestaurantItemsEntity();
    Mockito.when(mapper.toEntity(model, restaurantEntity))
        .thenReturn(toSaveEntity);

    final var savedEntity = new RestaurantItemsEntity();
    Mockito.when(repository.save(toSaveEntity))
        .thenReturn(savedEntity);

    final RestaurantItem expectedDomain = Mockito.mock(RestaurantItem.class);
    Mockito.when(mapper.toDomain(savedEntity))
        .thenReturn(expectedDomain);

    final var result = adapter.save(model);

    Assertions.assertSame(expectedDomain, result);

    Mockito.verify(restaurantsRepository, Mockito.times(1))
        .findByUuid(restaurantUuid);
    Mockito.verify(mapper, Mockito.times(1))
        .toEntity(model, restaurantEntity);
    Mockito.verify(repository, Mockito.times(1))
        .save(toSaveEntity);
    Mockito.verify(mapper, Mockito.times(1))
        .toDomain(savedEntity);
  }

  @Test
  @DisplayName("Deve lançar exceção quando o restaurant não existir")
  void shouldThrowExceptionWhenRestaurantDoesNotExist() {
    final var restaurantUuid = UUID.randomUUID();

    final Restaurant restaurantDomain = Mockito.mock(Restaurant.class);
    Mockito.when(restaurantDomain.getUuid())
        .thenReturn(restaurantUuid);

    final RestaurantItem model = Mockito.mock(RestaurantItem.class);
    Mockito.when(model.getRestaurant())
        .thenReturn(restaurantDomain);

    Mockito.when(restaurantsRepository.findByUuid(restaurantUuid))
        .thenReturn(Optional.empty());

    Assertions.assertThrows(RuntimeException.class, () -> adapter.save(model));

    Mockito.verify(restaurantsRepository, Mockito.times(1))
        .findByUuid(restaurantUuid);
    Mockito.verifyNoInteractions(mapper);
    Mockito.verify(repository, Mockito.never())
        .save(Mockito.any());
  }

  @Test
  @DisplayName("Deve atualizar o item existente e retornar o domain mapeado")
  void shouldUpdateExistingItemAndReturnMappedDomain() {
    final var uuid = UUID.randomUUID();

    final RestaurantItem model = Mockito.mock(RestaurantItem.class);

    final var existingEntity = new RestaurantItemsEntity();
    Mockito.when(repository.findByUuid(uuid))
        .thenReturn(Optional.of(existingEntity));

    final var updatedEntity = new RestaurantItemsEntity();
    Mockito.when(mapper.toEntity(model, existingEntity))
        .thenReturn(updatedEntity);

    final var savedEntity = new RestaurantItemsEntity();
    Mockito.when(repository.save(updatedEntity))
        .thenReturn(savedEntity);

    final RestaurantItem expectedDomain = Mockito.mock(RestaurantItem.class);
    Mockito.when(mapper.toDomain(savedEntity))
        .thenReturn(expectedDomain);

    final var result = adapter.update(uuid, model);

    Assertions.assertSame(expectedDomain, result);

    Mockito.verify(repository, Mockito.times(1))
        .findByUuid(uuid);
    Mockito.verify(mapper, Mockito.times(1))
        .toEntity(model, existingEntity);
    Mockito.verify(repository, Mockito.times(1))
        .save(updatedEntity);
    Mockito.verify(mapper, Mockito.times(1))
        .toDomain(savedEntity);
  }

  @Test
  @DisplayName("Deve retornar Optional vazio quando não existir")
  void shouldReturnEmptyOptionalWhenNotFound() {
    final var uuid = UUID.randomUUID();
    Mockito.when(repository.findByUuid(uuid))
        .thenReturn(Optional.empty());

    final var result = adapter.findByUuid(uuid);

    Assertions.assertTrue(result.isEmpty());
    Mockito.verify(repository, Mockito.times(1))
        .findByUuid(uuid);
    Mockito.verifyNoInteractions(mapper);
  }

  @Test
  @DisplayName("Deve retornar Optional com domain quando existir")
  void shouldReturnOptionalWithDomainWhenFound() {
    final var uuid = UUID.randomUUID();

    final var entity = new RestaurantItemsEntity();
    Mockito.when(repository.findByUuid(uuid))
        .thenReturn(Optional.of(entity));

    final RestaurantItem domain = Mockito.mock(RestaurantItem.class);
    Mockito.when(mapper.toDomain(entity))
        .thenReturn(domain);

    final var result = adapter.findByUuid(uuid);

    Assertions.assertTrue(result.isPresent());
    Assertions.assertSame(domain, result.get());

    Mockito.verify(repository, Mockito.times(1))
        .findByUuid(uuid);
    Mockito.verify(mapper, Mockito.times(1))
        .toDomain(entity);
  }

  @Test
  @DisplayName("Deve retornar PageModel com lista mapeada e totalElements")
  void shouldReturnPageModelWithMappedListAndTotalElements() {
    final var restaurantUuid = UUID.randomUUID();
    final var page = 0;
    final var size = 10;
    final var sort = "id";
    final var direction = "ASC";

    final var entity1 = new RestaurantItemsEntity();
    final var entity2 = new RestaurantItemsEntity();

    final var entitiesPage = new PageImpl<>(
        List.of(entity1, entity2),
        PageRequest.of(page, size),
        25L
    );

    Mockito.when(repository.findAll(
            ArgumentMatchers.<Specification<RestaurantItemsEntity>>any(),
            ArgumentMatchers.any(Pageable.class)
        ))
        .thenReturn(entitiesPage);

    final RestaurantItem domain1 = Mockito.mock(RestaurantItem.class);
    final RestaurantItem domain2 = Mockito.mock(RestaurantItem.class);

    Mockito.when(mapper.toDomainAll(entity1))
        .thenReturn(domain1);
    Mockito.when(mapper.toDomainAll(entity2))
        .thenReturn(domain2);

    final var result = adapter.findAll(restaurantUuid, page, size, sort, direction);

    Assertions.assertNotNull(result);
    Assertions.assertEquals(2, result.content()
        .size()
    );
    Assertions.assertEquals(25L, result.total());
    Assertions.assertEquals(List.of(domain1, domain2), result.content());

    final var pageableCaptor = ArgumentCaptor.forClass(Pageable.class);
    Mockito.verify(repository)
        .findAll(
            ArgumentMatchers.<Specification<RestaurantItemsEntity>>any(),
            pageableCaptor.capture()
        );

    final var pageableUsed = pageableCaptor.getValue();
    Assertions.assertEquals(page, pageableUsed.getPageNumber());
    Assertions.assertEquals(size, pageableUsed.getPageSize());

    final var order = pageableUsed.getSort()
        .getOrderFor(sort);
    Assertions.assertNotNull(order);
    Assertions.assertEquals(Sort.Direction.ASC, order.getDirection());

    Mockito.verify(mapper, Mockito.times(1))
        .toDomainAll(entity1);
    Mockito.verify(mapper, Mockito.times(1))
        .toDomainAll(entity2);
  }

  @Test
  @DisplayName("Deve usar defaults quando sort/direction forem null")
  void shouldUseDefaultsWhenSortAndDirectionAreNull() {
    final var restaurantUuid = UUID.randomUUID();

    final var entitiesPage = new PageImpl<RestaurantItemsEntity>(
        List.of(),
        PageRequest.of(0, 10),
        0L
    );

    Mockito.when(repository.findAll(
            ArgumentMatchers.<Specification<RestaurantItemsEntity>>any(),
            ArgumentMatchers.any(Pageable.class)
        ))
        .thenReturn(entitiesPage);

    final var result = adapter.findAll(restaurantUuid, 0, 10, null, null);

    Assertions.assertNotNull(result);
    Assertions.assertTrue(result.content()
        .isEmpty());
    Assertions.assertEquals(0L, result.total());

    final var pageableCaptor = ArgumentCaptor.forClass(Pageable.class);
    Mockito.verify(repository)
        .findAll(
            ArgumentMatchers.<Specification<RestaurantItemsEntity>>any(),
            pageableCaptor.capture()
        );

    final var pageableUsed = pageableCaptor.getValue();

    final var order = pageableUsed.getSort()
        .getOrderFor("id");
    Assertions.assertNotNull(order);
    Assertions.assertEquals(Sort.Direction.ASC, order.getDirection());

    Mockito.verifyNoInteractions(mapper);
  }

  @Test
  @DisplayName("Deve delegar deleteByUuid para o repository")
  void shouldDelegateDeleteByUuidToRepository() {
    final var uuid = UUID.randomUUID();

    adapter.delete(uuid);

    Mockito.verify(repository, Mockito.times(1))
        .deleteByUuid(uuid);
    Mockito.verifyNoInteractions(mapper, restaurantsRepository);
  }
}
