package com.connectfood.infrastructure.persistence.mappers;

import java.util.UUID;

import com.connectfood.core.domain.model.Address;
import com.connectfood.core.domain.model.User;
import com.connectfood.core.domain.model.UserAddress;
import com.connectfood.infrastructure.persistence.entity.AddressEntity;
import com.connectfood.infrastructure.persistence.entity.UsersAddressEntity;
import com.connectfood.infrastructure.persistence.entity.UsersEntity;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class UsersAddressInfraMapperTest {

  @Mock
  private UsersInfraMapper usersMapper;

  @Mock
  private AddressInfraMapper addressMapper;

  @InjectMocks
  private UsersAddressInfraMapper mapper;

  @Test
  @DisplayName("Deve retornar null quando entity for null")
  void toDomainShouldReturnNullWhenEntityIsNull() {
    final UserAddress result = mapper.toDomain(null);

    Assertions.assertNull(result);
    Mockito.verifyNoInteractions(usersMapper, addressMapper);
  }

  @Test
  @DisplayName("Deve mapear entity para domain corretamente")
  void toDomainShouldMapEntityToDomainCorrectly() {
    final var uuid = UUID.randomUUID();

    final var usersEntity = Mockito.mock(UsersEntity.class);
    final var addressEntity = Mockito.mock(AddressEntity.class);

    final User userDomain = Mockito.mock(User.class);
    final Address addressDomain = Mockito.mock(Address.class);

    Mockito.when(usersMapper.toDomain(usersEntity))
        .thenReturn(userDomain);
    Mockito.when(addressMapper.toDomain(addressEntity))
        .thenReturn(addressDomain);

    final var entity = Mockito.mock(UsersAddressEntity.class);
    Mockito.when(entity.getUuid())
        .thenReturn(uuid);
    Mockito.when(entity.getUsers())
        .thenReturn(usersEntity);
    Mockito.when(entity.getAddress())
        .thenReturn(addressEntity);

    final UserAddress result = mapper.toDomain(entity);

    Assertions.assertNotNull(result);
    Assertions.assertEquals(uuid, result.getUuid());
    Assertions.assertSame(userDomain, result.getUser());
    Assertions.assertSame(addressDomain, result.getAddress());

    Mockito.verify(usersMapper, Mockito.times(1))
        .toDomain(usersEntity);
    Mockito.verify(addressMapper, Mockito.times(1))
        .toDomain(addressEntity);
  }

  @Test
  @DisplayName("Deve retornar null quando usersEntity for null")
  void toEntityShouldReturnNullWhenUsersEntityIsNull() {
    final var addressEntity = Mockito.mock(AddressEntity.class);

    final UsersAddressEntity result = mapper.toEntity(UUID.randomUUID(), null, addressEntity);

    Assertions.assertNull(result);
    Mockito.verifyNoInteractions(usersMapper, addressMapper);
  }

  @Test
  @DisplayName("Deve retornar null quando addressEntity for null")
  void toEntityShouldReturnNullWhenAddressEntityIsNull() {
    final var usersEntity = Mockito.mock(UsersEntity.class);

    final UsersAddressEntity result = mapper.toEntity(UUID.randomUUID(), usersEntity, null);

    Assertions.assertNull(result);
    Mockito.verifyNoInteractions(usersMapper, addressMapper);
  }

  @Test
  @DisplayName("Deve mapear para entity corretamente quando dados forem válidos")
  void toEntityShouldMapToEntityCorrectlyWhenDataIsValid() {
    final var uuid = UUID.randomUUID();

    final var usersEntity = Mockito.mock(UsersEntity.class);
    final var addressEntity = Mockito.mock(AddressEntity.class);

    final UsersAddressEntity result = mapper.toEntity(uuid, usersEntity, addressEntity);

    Assertions.assertNotNull(result);
    Assertions.assertEquals(uuid, result.getUuid());
    Assertions.assertSame(usersEntity, result.getUsers());
    Assertions.assertSame(addressEntity, result.getAddress());

    Mockito.verifyNoInteractions(usersMapper, addressMapper);
  }
}
