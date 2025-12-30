package com.connectfood.core.infrastructure.persistence.adapter;

import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

import com.connectfood.core.domain.model.Address;
import com.connectfood.core.domain.model.Users;
import com.connectfood.core.domain.model.UsersAddress;
import com.connectfood.core.infrastructure.persistence.entity.AddressEntity;
import com.connectfood.core.infrastructure.persistence.entity.UsersAddressEntity;
import com.connectfood.core.infrastructure.persistence.entity.UsersEntity;
import com.connectfood.core.infrastructure.persistence.jpa.JpaAddressRepository;
import com.connectfood.core.infrastructure.persistence.jpa.JpaUsersAddressRepository;
import com.connectfood.core.infrastructure.persistence.jpa.JpaUsersRepository;
import com.connectfood.core.infrastructure.persistence.mappers.UsersAddressInfraMapper;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class UsersAddressRepositoryAdapterTest {

  @Mock
  private JpaUsersAddressRepository repository;

  @Mock
  private UsersAddressInfraMapper mapper;

  @Mock
  private JpaUsersRepository usersRepository;

  @Mock
  private JpaAddressRepository addressRepository;

  @InjectMocks
  private UsersAddressRepositoryAdapter adapter;

  @Test
  @DisplayName("Deve salvar e retornar o model mapeado")
  void saveShouldPersistAndReturnMappedModel() {
    final var usersUuid = UUID.randomUUID();
    final var addressUuid = UUID.randomUUID();
    final var usersAddressUuid = UUID.randomUUID();

    final Users users = Mockito.mock(Users.class);
    final Address address = Mockito.mock(Address.class);

    Mockito.when(users.getUuid())
        .thenReturn(usersUuid);
    Mockito.when(address.getUuid())
        .thenReturn(addressUuid);

    final UsersAddress usersAddress = Mockito.mock(UsersAddress.class);
    Mockito.when(usersAddress.getUuid())
        .thenReturn(usersAddressUuid);
    Mockito.when(usersAddress.getUsers())
        .thenReturn(users);
    Mockito.when(usersAddress.getAddress())
        .thenReturn(address);

    final UsersEntity usersEntity = Mockito.mock(UsersEntity.class);
    final AddressEntity addressEntity = Mockito.mock(AddressEntity.class);

    Mockito.when(usersRepository.findByUuid(usersUuid))
        .thenReturn(Optional.of(usersEntity));
    Mockito.when(addressRepository.findByUuid(addressUuid))
        .thenReturn(Optional.of(addressEntity));

    final UsersAddressEntity entityToSave = Mockito.mock(UsersAddressEntity.class);
    Mockito.when(mapper.toEntity(usersAddressUuid, usersEntity, addressEntity))
        .thenReturn(entityToSave);

    final UsersAddressEntity savedEntity = Mockito.mock(UsersAddressEntity.class);
    Mockito.when(repository.save(entityToSave))
        .thenReturn(savedEntity);

    final UsersAddress mappedDomain = Mockito.mock(UsersAddress.class);
    Mockito.when(mapper.toDomain(savedEntity))
        .thenReturn(mappedDomain);

    final var result = adapter.save(usersAddress);

    Assertions.assertSame(mappedDomain, result);

    Mockito.verify(usersRepository, Mockito.times(1))
        .findByUuid(usersUuid);
    Mockito.verify(addressRepository, Mockito.times(1))
        .findByUuid(addressUuid);
    Mockito.verify(mapper, Mockito.times(1))
        .toEntity(usersAddressUuid, usersEntity, addressEntity);
    Mockito.verify(repository, Mockito.times(1))
        .save(entityToSave);
    Mockito.verify(mapper, Mockito.times(1))
        .toDomain(savedEntity);
    Mockito.verifyNoMoreInteractions(repository, mapper, usersRepository, addressRepository);
  }

  @Test
  @DisplayName("Deve lançar exceção quando usuário não existir ao salvar")
  void saveShouldThrowWhenUsersNotFound() {
    final var usersUuid = UUID.randomUUID();

    final Users users = Mockito.mock(Users.class);
    Mockito.when(users.getUuid())
        .thenReturn(usersUuid);

    final UsersAddress usersAddress = Mockito.mock(UsersAddress.class);
    Mockito.when(usersAddress.getUsers())
        .thenReturn(users);

    Mockito.when(usersRepository.findByUuid(usersUuid))
        .thenReturn(Optional.empty());

    Assertions.assertThrows(NoSuchElementException.class, () -> adapter.save(usersAddress));

    Mockito.verify(usersRepository, Mockito.times(1))
        .findByUuid(usersUuid);
    Mockito.verifyNoInteractions(addressRepository, mapper, repository);
    Mockito.verifyNoMoreInteractions(usersRepository);
  }

  @Test
  @DisplayName("Deve lançar exceção quando endereço não existir ao salvar")
  void saveShouldThrowWhenAddressNotFound() {
    final var usersUuid = UUID.randomUUID();
    final var addressUuid = UUID.randomUUID();

    final Users users = Mockito.mock(Users.class);
    final Address address = Mockito.mock(Address.class);

    Mockito.when(users.getUuid())
        .thenReturn(usersUuid);
    Mockito.when(address.getUuid())
        .thenReturn(addressUuid);

    final UsersAddress usersAddress = Mockito.mock(UsersAddress.class);
    Mockito.when(usersAddress.getUsers())
        .thenReturn(users);
    Mockito.when(usersAddress.getAddress())
        .thenReturn(address);

    final UsersEntity usersEntity = Mockito.mock(UsersEntity.class);

    Mockito.when(usersRepository.findByUuid(usersUuid))
        .thenReturn(Optional.of(usersEntity));
    Mockito.when(addressRepository.findByUuid(addressUuid))
        .thenReturn(Optional.empty());

    Assertions.assertThrows(NoSuchElementException.class, () -> adapter.save(usersAddress));

    Mockito.verify(usersRepository, Mockito.times(1))
        .findByUuid(usersUuid);
    Mockito.verify(addressRepository, Mockito.times(1))
        .findByUuid(addressUuid);
    Mockito.verifyNoInteractions(mapper, repository);
    Mockito.verifyNoMoreInteractions(usersRepository, addressRepository);
  }

  @Test
  @DisplayName("Deve retornar Optional vazio quando não encontrar por uuid do usuário")
  void findByUsersUuidShouldReturnEmptyWhenNotFound() {
    final var usersUuid = UUID.randomUUID();

    Mockito.when(repository.findByUsersUuid(usersUuid))
        .thenReturn(Optional.empty());

    final var result = adapter.findByUsersUuid(usersUuid);

    Assertions.assertNotNull(result);
    Assertions.assertTrue(result.isEmpty());

    Mockito.verify(repository, Mockito.times(1))
        .findByUsersUuid(usersUuid);
    Mockito.verifyNoInteractions(mapper, usersRepository, addressRepository);
    Mockito.verifyNoMoreInteractions(repository);
  }

  @Test
  @DisplayName("Deve retornar model quando encontrar por uuid do usuário")
  void findByUsersUuidShouldReturnMappedModelWhenFound() {
    final var usersUuid = UUID.randomUUID();

    final UsersAddressEntity entity = Mockito.mock(UsersAddressEntity.class);
    final UsersAddress mappedDomain = Mockito.mock(UsersAddress.class);

    Mockito.when(repository.findByUsersUuid(usersUuid))
        .thenReturn(Optional.of(entity));
    Mockito.when(mapper.toDomain(entity))
        .thenReturn(mappedDomain);

    final var result = adapter.findByUsersUuid(usersUuid);

    Assertions.assertTrue(result.isPresent());
    Assertions.assertSame(mappedDomain, result.get());

    Mockito.verify(repository, Mockito.times(1))
        .findByUsersUuid(usersUuid);
    Mockito.verify(mapper, Mockito.times(1))
        .toDomain(entity);
    Mockito.verifyNoMoreInteractions(repository, mapper);
    Mockito.verifyNoInteractions(usersRepository, addressRepository);
  }

  @Test
  @DisplayName("Deve deletar pelo uuid")
  void deleteShouldDeleteByUuid() {
    final var uuid = UUID.randomUUID();

    adapter.delete(uuid);

    Mockito.verify(repository, Mockito.times(1))
        .deleteByUuid(uuid);
    Mockito.verifyNoInteractions(mapper, usersRepository, addressRepository);
    Mockito.verifyNoMoreInteractions(repository);
  }
}
