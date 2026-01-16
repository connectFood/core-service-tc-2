package com.connectfood.core.application.restaurant.usecase;

import java.util.UUID;

import com.connectfood.core.application.restaurant.dto.RestaurantOpeningHourInput;
import com.connectfood.core.application.restaurant.dto.RestaurantOpeningHourOutput;
import com.connectfood.core.application.restaurant.mapper.RestaurantOpeningHourAppMapper;
import com.connectfood.core.application.security.RequestUser;
import com.connectfood.core.application.security.RequestUserGuard;
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
class CreateRestaurantOpeningHourUseCaseTest {

  @Mock
  private RestaurantOpeningHourGateway repository;

  @Mock
  private RestaurantOpeningHourAppMapper mapper;

  @Mock
  private RequestUserGuard guard;

  @InjectMocks
  private CreateRestaurantOpeningHourUseCase useCase;

  @Test
  @DisplayName("Deve criar horário de funcionamento e retornar output mapeado")
  void shouldCreateOpeningHoursAndReturnMappedOutput() {
    final var requestUserUuid = UUID.randomUUID();
    final var requestUser = new RequestUser(requestUserUuid);

    final var restaurantUuid = UUID.randomUUID();
    final RestaurantOpeningHourInput input = Mockito.mock(RestaurantOpeningHourInput.class);

    final RestaurantOpeningHour domain = Mockito.mock(RestaurantOpeningHour.class);
    Mockito.when(mapper.toDomain(input))
        .thenReturn(domain);

    final RestaurantOpeningHour saved = Mockito.mock(RestaurantOpeningHour.class);
    Mockito.when(repository.save(domain, restaurantUuid))
        .thenReturn(saved);

    final RestaurantOpeningHourOutput output = Mockito.mock(RestaurantOpeningHourOutput.class);
    Mockito.when(mapper.toOutput(saved))
        .thenReturn(output);

    final var result = useCase.execute(requestUser, restaurantUuid, input);

    Assertions.assertNotNull(result);
    Assertions.assertSame(output, result);

    Mockito.verify(guard, Mockito.times(1))
        .requireRole(requestUser, UsersType.OWNER.name());

    Mockito.verify(mapper, Mockito.times(1))
        .toDomain(input);

    Mockito.verify(repository, Mockito.times(1))
        .save(domain, restaurantUuid);

    Mockito.verify(mapper, Mockito.times(1))
        .toOutput(saved);

    Mockito.verifyNoMoreInteractions(guard, repository, mapper);
  }
}
