package com.connectfood.core.application.address.usecase;

import java.util.Optional;
import java.util.UUID;

import com.connectfood.core.application.address.dto.AddressInput;
import com.connectfood.core.application.address.mapper.AddressAppMapper;
import com.connectfood.core.application.users.mapper.UsersAddressAppMapper;
import com.connectfood.core.application.users.usecase.CreateUsersAddressUseCase;
import com.connectfood.core.domain.exception.NotFoundException;
import com.connectfood.core.domain.model.Address;
import com.connectfood.core.domain.model.Users;
import com.connectfood.core.domain.model.UsersAddress;
import com.connectfood.core.domain.repository.AddressRepository;
import com.connectfood.core.domain.repository.UsersAddressRepository;
import com.connectfood.core.domain.repository.UsersRepository;

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
  private AddressRepository repository;

  @Mock
  private AddressAppMapper mapper;

  @Mock
  private UsersRepository usersRepository;

  @Mock
  private UsersAddressRepository usersAddressRepository;

  @Mock
  private UsersAddressAppMapper usersAddressMapper;

  @InjectMocks
  private CreateUsersAddressUseCase useCase;

  @Test
  @DisplayName("Não deve criar endereço quando o usuário não for encontrado")
  void shouldThrowNotFoundExceptionWhenUserIsNotFound() {
    final var userUuid = UUID.randomUUID();
    final AddressInput input = Mockito.mock(AddressInput.class);

    Mockito.when(usersRepository.findByUuid(userUuid))
        .thenReturn(Optional.empty());

    final var ex = Assertions.assertThrows(NotFoundException.class,
        () -> useCase.execute(userUuid, input)
    );

    Assertions.assertEquals("User not found", ex.getMessage());
    Mockito.verify(usersRepository, Mockito.times(1))
        .findByUuid(userUuid);
    Mockito.verifyNoInteractions(repository, mapper, usersAddressRepository, usersAddressMapper);
  }

  @Test
  @DisplayName("Deve criar endereço, vincular ao usuário e retornar o output")
  void shouldCreateUsersAddressAndReturnOutput() {
    final var userUuid = UUID.randomUUID();
    final AddressInput input = Mockito.mock(AddressInput.class);

    final Users users = Mockito.mock(Users.class);
    Mockito.when(usersRepository.findByUuid(userUuid))
        .thenReturn(Optional.of(users));

    final Address addressToSave = Mockito.mock(Address.class);
    Mockito.when(mapper.toDomain(input))
        .thenReturn(addressToSave);

    final Address savedAddress = Mockito.mock(Address.class);
    Mockito.when(repository.save(addressToSave))
        .thenReturn(savedAddress);

    final UsersAddress usersAddressToSave = Mockito.mock(UsersAddress.class);
    Mockito.when(usersAddressMapper.toDomain(users, savedAddress))
        .thenReturn(usersAddressToSave);

    final UsersAddress savedUsersAddress = Mockito.mock(UsersAddress.class);
    Mockito.when(usersAddressRepository.save(usersAddressToSave))
        .thenReturn(savedUsersAddress);

    final UsersAddressOutput expectedOutput = Mockito.mock(UsersAddressOutput.class);
//    Mockito.when(usersAddressMapper.toOutput(savedUsersAddress))
//        .thenReturn(expectedOutput);

    final var result = useCase.execute(userUuid, input);

    Assertions.assertNotNull(result);
    Assertions.assertSame(expectedOutput, result);

    Mockito.verify(usersRepository, Mockito.times(1))
        .findByUuid(userUuid);
    Mockito.verify(mapper, Mockito.times(1))
        .toDomain(input);
    Mockito.verify(repository, Mockito.times(1))
        .save(addressToSave);
    Mockito.verify(usersAddressMapper, Mockito.times(1))
        .toDomain(users, savedAddress);
    Mockito.verify(usersAddressRepository, Mockito.times(1))
        .save(usersAddressToSave);
//    Mockito.verify(usersAddressMapper, Mockito.times(1))
//        .toOutput(savedUsersAddress);
  }
}
