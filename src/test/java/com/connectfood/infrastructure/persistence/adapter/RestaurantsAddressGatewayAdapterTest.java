package com.connectfood.infrastructure.persistence.adapter;

import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

import com.connectfood.core.domain.model.Address;
import com.connectfood.core.domain.model.Restaurant;
import com.connectfood.core.domain.model.RestaurantsAddress;
import com.connectfood.infrastructure.persistence.entity.AddressEntity;
import com.connectfood.infrastructure.persistence.entity.RestaurantsAddressEntity;
import com.connectfood.infrastructure.persistence.entity.RestaurantsEntity;
import com.connectfood.infrastructure.persistence.jpa.JpaAddressRepository;
import com.connectfood.infrastructure.persistence.jpa.JpaRestaurantsAddressRepository;
import com.connectfood.infrastructure.persistence.jpa.JpaRestaurantsRepository;
import com.connectfood.infrastructure.persistence.mappers.RestaurantsAddressInfraMapper;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class RestaurantsAddressGatewayAdapterTest {

  @Mock
  private JpaRestaurantsAddressRepository repository;

  @Mock
  private RestaurantsAddressInfraMapper mapper;

  @Mock
  private JpaRestaurantsRepository restaurantsRepository;

  @Mock
  private JpaAddressRepository addressRepository;

  @InjectMocks
  private RestaurantsAddressGatewayAdapter adapter;

  @Test
  @DisplayName("Deve salvar e retornar RestaurantsAddress mapeado")
  void saveShouldPersistAndReturnMappedModel() {
    final var restaurantsAddressUuid = UUID.randomUUID();
    final var restaurantsUuid = UUID.randomUUID();
    final var addressUuid = UUID.randomUUID();

    final Restaurant restaurantDomain = Mockito.mock(Restaurant.class);
    Mockito.when(restaurantDomain.getUuid())
        .thenReturn(restaurantsUuid);

    final Address addressDomain = Mockito.mock(Address.class);
    Mockito.when(addressDomain.getUuid())
        .thenReturn(addressUuid);

    final RestaurantsAddress restaurantsAddress = Mockito.mock(RestaurantsAddress.class);
    Mockito.when(restaurantsAddress.getUuid())
        .thenReturn(restaurantsAddressUuid);
    Mockito.when(restaurantsAddress.getRestaurant())
        .thenReturn(restaurantDomain);
    Mockito.when(restaurantsAddress.getAddress())
        .thenReturn(addressDomain);

    final RestaurantsEntity restaurantsEntity = Mockito.mock(RestaurantsEntity.class);
    final AddressEntity addressEntity = Mockito.mock(AddressEntity.class);

    final RestaurantsAddressEntity entityToSave = Mockito.mock(RestaurantsAddressEntity.class);
    final RestaurantsAddressEntity savedEntity = Mockito.mock(RestaurantsAddressEntity.class);

    final RestaurantsAddress mappedDomain = Mockito.mock(RestaurantsAddress.class);

    Mockito.when(restaurantsRepository.findByUuid(restaurantsUuid))
        .thenReturn(Optional.of(restaurantsEntity));
    Mockito.when(addressRepository.findByUuid(addressUuid))
        .thenReturn(Optional.of(addressEntity));
    Mockito.when(mapper.toEntity(restaurantsAddressUuid, restaurantsEntity, addressEntity))
        .thenReturn(entityToSave);
    Mockito.when(repository.save(entityToSave))
        .thenReturn(savedEntity);
    Mockito.when(mapper.toDomain(savedEntity))
        .thenReturn(mappedDomain);

    final RestaurantsAddress result = adapter.save(restaurantsAddress);

    Assertions.assertSame(mappedDomain, result);

    Mockito.verify(restaurantsRepository, Mockito.times(1))
        .findByUuid(restaurantsUuid);
    Mockito.verify(addressRepository, Mockito.times(1))
        .findByUuid(addressUuid);
    Mockito.verify(mapper, Mockito.times(1))
        .toEntity(restaurantsAddressUuid, restaurantsEntity, addressEntity);
    Mockito.verify(repository, Mockito.times(1))
        .save(entityToSave);
    Mockito.verify(mapper, Mockito.times(1))
        .toDomain(savedEntity);
    Mockito.verifyNoMoreInteractions(repository, mapper, restaurantsRepository, addressRepository);
  }

  @Test
  @DisplayName("Deve lançar exceção quando restaurant não existir ao salvar")
  void saveShouldThrowWhenRestaurantDoesNotExist() {
    final var restaurantsUuid = UUID.randomUUID();

    final Restaurant restaurantDomain = Mockito.mock(Restaurant.class);
    Mockito.when(restaurantDomain.getUuid())
        .thenReturn(restaurantsUuid);

    final RestaurantsAddress restaurantsAddress = Mockito.mock(RestaurantsAddress.class);
    Mockito.when(restaurantsAddress.getRestaurant())
        .thenReturn(restaurantDomain);

    Mockito.when(restaurantsRepository.findByUuid(restaurantsUuid))
        .thenReturn(Optional.empty());

    Assertions.assertThrows(NoSuchElementException.class, () -> adapter.save(restaurantsAddress));

    Mockito.verify(restaurantsRepository, Mockito.times(1))
        .findByUuid(restaurantsUuid);
    Mockito.verifyNoInteractions(addressRepository, repository, mapper);
    Mockito.verifyNoMoreInteractions(restaurantsRepository);
  }

  @Test
  @DisplayName("Deve lançar exceção quando address não existir ao salvar")
  void saveShouldThrowWhenAddressDoesNotExist() {
    final var restaurantsAddressUuid = UUID.randomUUID();
    final var restaurantsUuid = UUID.randomUUID();
    final var addressUuid = UUID.randomUUID();

    final Restaurant restaurantDomain = Mockito.mock(Restaurant.class);
    Mockito.when(restaurantDomain.getUuid())
        .thenReturn(restaurantsUuid);

    final Address addressDomain = Mockito.mock(Address.class);
    Mockito.when(addressDomain.getUuid())
        .thenReturn(addressUuid);

    final RestaurantsAddress restaurantsAddress = Mockito.mock(RestaurantsAddress.class);
    Mockito.when(restaurantsAddress.getRestaurant())
        .thenReturn(restaurantDomain);
    Mockito.when(restaurantsAddress.getAddress())
        .thenReturn(addressDomain);

    final RestaurantsEntity restaurantsEntity = Mockito.mock(RestaurantsEntity.class);

    Mockito.when(restaurantsRepository.findByUuid(restaurantsUuid))
        .thenReturn(Optional.of(restaurantsEntity));
    Mockito.when(addressRepository.findByUuid(addressUuid))
        .thenReturn(Optional.empty());

    Assertions.assertThrows(NoSuchElementException.class, () -> adapter.save(restaurantsAddress));

    Mockito.verify(restaurantsRepository, Mockito.times(1))
        .findByUuid(restaurantsUuid);
    Mockito.verify(addressRepository, Mockito.times(1))
        .findByUuid(addressUuid);
    Mockito.verifyNoInteractions(repository, mapper);
    Mockito.verifyNoMoreInteractions(restaurantsRepository, addressRepository);
  }

  @Test
  @DisplayName("Deve retornar Optional vazio quando não encontrar por restaurantsUuid")
  void findByRestaurantsUuidShouldReturnEmptyWhenNotFound() {
    final var restaurantsUuid = UUID.randomUUID();

    Mockito.when(repository.findByRestaurantsUuid(restaurantsUuid))
        .thenReturn(Optional.empty());

    final Optional<RestaurantsAddress> result = adapter.findByRestaurantsUuid(restaurantsUuid);

    Assertions.assertNotNull(result);
    Assertions.assertTrue(result.isEmpty());

    Mockito.verify(repository, Mockito.times(1))
        .findByRestaurantsUuid(restaurantsUuid);
    Mockito.verifyNoInteractions(mapper, restaurantsRepository, addressRepository);
    Mockito.verifyNoMoreInteractions(repository);
  }

  @Test
  @DisplayName("Deve retornar RestaurantsAddress mapeado quando encontrar por restaurantsUuid")
  void findByRestaurantsUuidShouldReturnMappedModelWhenFound() {
    final var restaurantsUuid = UUID.randomUUID();

    final RestaurantsAddressEntity foundEntity = Mockito.mock(RestaurantsAddressEntity.class);
    final RestaurantsAddress mappedDomain = Mockito.mock(RestaurantsAddress.class);

    Mockito.when(repository.findByRestaurantsUuid(restaurantsUuid))
        .thenReturn(Optional.of(foundEntity));
    Mockito.when(mapper.toDomain(foundEntity))
        .thenReturn(mappedDomain);

    final Optional<RestaurantsAddress> result = adapter.findByRestaurantsUuid(restaurantsUuid);

    Assertions.assertTrue(result.isPresent());
    Assertions.assertSame(mappedDomain, result.get());

    Mockito.verify(repository, Mockito.times(1))
        .findByRestaurantsUuid(restaurantsUuid);
    Mockito.verify(mapper, Mockito.times(1))
        .toDomain(foundEntity);
    Mockito.verifyNoMoreInteractions(repository, mapper, restaurantsRepository, addressRepository);
  }

  @Test
  @DisplayName("Deve deletar pelo uuid")
  void deleteShouldDeleteByUuid() {
    final var uuid = UUID.randomUUID();

    adapter.delete(uuid);

    Mockito.verify(repository, Mockito.times(1))
        .deleteByUuid(uuid);
    Mockito.verifyNoInteractions(mapper, restaurantsRepository, addressRepository);
    Mockito.verifyNoMoreInteractions(repository);
  }
}
