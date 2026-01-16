package com.connectfood.core.application.users.usecase;

import java.util.Optional;
import java.util.UUID;

import com.connectfood.core.application.address.dto.AddressInput;
import com.connectfood.core.application.address.dto.AddressOutput;
import com.connectfood.core.application.address.mapper.AddressAppMapper;
import com.connectfood.core.application.users.mapper.UsersAddressAppMapper;
import com.connectfood.core.domain.exception.NotFoundException;
import com.connectfood.core.domain.model.Address;
import com.connectfood.core.domain.model.User;
import com.connectfood.core.domain.model.UserAddress;
import com.connectfood.core.domain.repository.AddressGateway;
import com.connectfood.core.domain.repository.UserAddressGateway;
import com.connectfood.core.domain.repository.UserGateway;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CreateUsersAddressUseCaseTest {

  @Mock
  private AddressGateway repository;

  @Mock
  private AddressAppMapper mapper;

  @Mock
  private UserGateway userGateway;

  @Mock
  private UserAddressGateway userAddressGateway;

  @Mock
  private UsersAddressAppMapper usersAddressMapper;

  @InjectMocks
  private CreateUsersAddressUseCase useCase;

  @Test
  @DisplayName("Não deve criar endereço quando o usuário não for encontrado")
  void shouldThrowNotFoundExceptionWhenUserIsNotFound() {
    final var userUuid = UUID.randomUUID();
    final AddressInput input = Mockito.mock(AddressInput.class);

    Mockito.when(userGateway.findByUuid(userUuid))
        .thenReturn(Optional.empty());

    final var ex = Assertions.assertThrows(
        NotFoundException.class,
        () -> useCase.execute(userUuid, input)
    );

    Assertions.assertEquals("User not found", ex.getMessage());

    Mockito.verify(userGateway, Mockito.times(1))
        .findByUuid(userUuid);
    Mockito.verifyNoInteractions(repository, mapper, userAddressGateway, usersAddressMapper);
    Mockito.verifyNoMoreInteractions(userGateway);
  }

  @Test
  @DisplayName("Deve criar endereço, vincular ao usuário e retornar o output")
  void shouldCreateUsersAddressAndReturnOutput() {
    final var userUuid = UUID.randomUUID();
    final AddressInput input = Mockito.mock(AddressInput.class);

    final User user = Mockito.mock(User.class);
    Mockito.when(userGateway.findByUuid(userUuid))
        .thenReturn(Optional.of(user));

    final Address addressToSave = Mockito.mock(Address.class);
    Mockito.when(mapper.toDomain(input))
        .thenReturn(addressToSave);

    final Address savedAddress = Mockito.mock(Address.class);
    Mockito.when(repository.save(addressToSave))
        .thenReturn(savedAddress);

    final UserAddress userAddressToSave = Mockito.mock(UserAddress.class);
    Mockito.when(usersAddressMapper.toDomain(user, savedAddress))
        .thenReturn(userAddressToSave);

    final UserAddress savedUserAddress = Mockito.mock(UserAddress.class);
    Mockito.when(userAddressGateway.save(userAddressToSave))
        .thenReturn(savedUserAddress);

    final AddressOutput expectedOutput = Mockito.mock(AddressOutput.class);
    Mockito.when(savedUserAddress.getAddress())
        .thenReturn(savedAddress);
    Mockito.when(mapper.toOutput(savedAddress))
        .thenReturn(expectedOutput);

    final var result = useCase.execute(userUuid, input);

    Assertions.assertNotNull(result);
    Assertions.assertSame(expectedOutput, result);

    Mockito.verify(userGateway, Mockito.times(1))
        .findByUuid(userUuid);
    Mockito.verify(mapper, Mockito.times(1))
        .toDomain(input);
    Mockito.verify(repository, Mockito.times(1))
        .save(addressToSave);
    Mockito.verify(usersAddressMapper, Mockito.times(1))
        .toDomain(user, savedAddress);
    Mockito.verify(userAddressGateway, Mockito.times(1))
        .save(userAddressToSave);
    Mockito.verify(savedUserAddress, Mockito.times(1))
        .getAddress();
    Mockito.verify(mapper, Mockito.times(1))
        .toOutput(savedAddress);

    Mockito.verifyNoMoreInteractions(
        userGateway,
        mapper,
        repository,
        usersAddressMapper,
        userAddressGateway,
        savedUserAddress
    );
  }
}
