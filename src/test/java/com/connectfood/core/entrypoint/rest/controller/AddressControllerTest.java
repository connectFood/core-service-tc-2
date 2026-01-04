package com.connectfood.core.entrypoint.rest.controller;

import java.util.UUID;

import com.connectfood.core.application.address.dto.AddressInput;
import com.connectfood.core.application.address.dto.UsersAddressOutput;
import com.connectfood.core.application.address.usecase.CreateUsersAddressUseCase;
import com.connectfood.core.entrypoint.rest.dto.address.AddressRequest;
import com.connectfood.core.entrypoint.rest.dto.address.UsersAddressResponse;
import com.connectfood.core.entrypoint.rest.dto.commons.BaseResponse;
import com.connectfood.core.entrypoint.rest.mappers.AddressEntryMapper;
import com.connectfood.core.entrypoint.rest.mappers.UsersAddressEntryMapper;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

@ExtendWith(MockitoExtension.class)
class AddressControllerTest {

  @Mock
  private CreateUsersAddressUseCase createUsersAddressUseCase;

  @Mock
  private AddressEntryMapper mapper;

  @Mock
  private UsersAddressEntryMapper usersAddressMapper;

  @InjectMocks
  private AddressController controller;

  @Test
  @DisplayName("Deve retornar status 201 e BaseResponse com o endereço criado")
  void shouldReturnCreatedWithBaseResponseWhenAddressIsCreated() {
    final var userUuid = UUID.randomUUID();

    final AddressRequest request = Mockito.mock(AddressRequest.class);

    final AddressInput input = Mockito.mock(AddressInput.class);
    Mockito.when(mapper.toInput(request))
        .thenReturn(input);

    final UsersAddressOutput output = Mockito.mock(UsersAddressOutput.class);
    Mockito.when(createUsersAddressUseCase.execute(userUuid, input))
        .thenReturn(output);

    final UsersAddressResponse response = Mockito.mock(UsersAddressResponse.class);
    Mockito.when(usersAddressMapper.toResponse(output))
        .thenReturn(response);

    final var result = controller.createUsersAddress(userUuid, request);

    Assertions.assertNotNull(result);
    Assertions.assertEquals(HttpStatus.CREATED, result.getStatusCode());
    Assertions.assertNotNull(result.getBody());

    final BaseResponse<UsersAddressResponse> body = result.getBody();
    Assertions.assertNotNull(body.content());
    Assertions.assertSame(response, body.content());

    Mockito.verify(mapper, Mockito.times(1))
        .toInput(request);
    Mockito.verify(createUsersAddressUseCase, Mockito.times(1))
        .execute(userUuid, input);
    Mockito.verify(usersAddressMapper, Mockito.times(1))
        .toResponse(output);
    Mockito.verifyNoMoreInteractions(createUsersAddressUseCase, mapper, usersAddressMapper);
  }
}
