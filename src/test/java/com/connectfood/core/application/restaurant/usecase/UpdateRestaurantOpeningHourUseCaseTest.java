package com.connectfood.core.application.restaurant.usecase;

import java.util.Optional;
import java.util.UUID;

import com.connectfood.core.application.restaurant.dto.RestaurantOpeningHourInput;
import com.connectfood.core.application.restaurant.dto.RestaurantOpeningHourOutput;
import com.connectfood.core.application.restaurant.mapper.RestaurantOpeningHourAppMapper;
import com.connectfood.core.application.security.RequestUser;
import com.connectfood.core.application.security.RequestUserGuard;
import com.connectfood.core.domain.exception.NotFoundException;
import com.connectfood.core.domain.model.RestaurantOpeningHour;
import com.connectfood.core.domain.model.enums.UsersType;
import com.connectfood.core.domain.repository.RestaurantOpeningHourGateway;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class UpdateRestaurantOpeningHourUseCaseTest {

  @Mock
  private RestaurantOpeningHourGateway repository;

  @Mock
  private RestaurantOpeningHourAppMapper mapper;

  @Mock
  private RequestUserGuard guard;

  @InjectMocks
  private UpdateRestaurantOpeningHourUseCase useCase;

  @Test
  @DisplayName("Deve atualizar horário de funcionamento quando registro existir")
  void shouldUpdateOpeningHoursWhenExists() {
    final var requestUserUuid = UUID.randomUUID();
    final var requestUser = new RequestUser(requestUserUuid);

    final var uuid = UUID.randomUUID();
    final var input = Mockito.mock(RestaurantOpeningHourInput.class);

    final RestaurantOpeningHour existing = Mockito.mock(RestaurantOpeningHour.class);
    Mockito.when(repository.findByUuid(uuid))
        .thenReturn(Optional.of(existing));

    final RestaurantOpeningHour domainToUpdate = Mockito.mock(RestaurantOpeningHour.class);
    Mockito.when(mapper.toDomain(uuid, input))
        .thenReturn(domainToUpdate);

    final RestaurantOpeningHour updated = Mockito.mock(RestaurantOpeningHour.class);
    Mockito.when(repository.update(uuid, domainToUpdate))
        .thenReturn(updated);

    final RestaurantOpeningHourOutput output = Mockito.mock(RestaurantOpeningHourOutput.class);
    Mockito.when(mapper.toOutput(updated))
        .thenReturn(output);

    final var result = useCase.execute(requestUser, uuid, input);

    Assertions.assertNotNull(result);
    Assertions.assertSame(output, result);

    Mockito.verify(guard, Mockito.times(1))
        .requireRole(requestUser, UsersType.OWNER.name());

    Mockito.verify(repository, Mockito.times(1))
        .findByUuid(uuid);

    Mockito.verify(mapper, Mockito.times(1))
        .toDomain(uuid, input);

    Mockito.verify(repository, Mockito.times(1))
        .update(uuid, domainToUpdate);

    Mockito.verify(mapper, Mockito.times(1))
        .toOutput(updated);

    Mockito.verifyNoMoreInteractions(guard, repository, mapper);
  }

  @Test
  @DisplayName("Não deve atualizar horário de funcionamento quando registro não existir")
  void shouldThrowNotFoundExceptionWhenDoesNotExist() {
    final var requestUserUuid = UUID.randomUUID();
    final var requestUser = new RequestUser(requestUserUuid);

    final var uuid = UUID.randomUUID();
    final var input = Mockito.mock(RestaurantOpeningHourInput.class);

    Mockito.when(repository.findByUuid(uuid))
        .thenReturn(Optional.empty());

    final var exception = Assertions.assertThrows(
        NotFoundException.class,
        () -> useCase.execute(requestUser, uuid, input)
    );

    Assertions.assertEquals("Restaurant opening hours not found", exception.getMessage());

    Mockito.verify(guard, Mockito.times(1))
        .requireRole(requestUser, UsersType.OWNER.name());

    Mockito.verify(repository, Mockito.times(1))
        .findByUuid(uuid);

    Mockito.verifyNoInteractions(mapper);
    Mockito.verifyNoMoreInteractions(guard, repository);
  }
}
