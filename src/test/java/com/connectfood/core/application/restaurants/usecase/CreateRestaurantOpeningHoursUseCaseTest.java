package com.connectfood.core.application.restaurants.usecase;

import java.util.UUID;

import com.connectfood.core.application.restaurants.dto.RestaurantOpeningHoursInput;
import com.connectfood.core.application.restaurants.dto.RestaurantOpeningHoursOutput;
import com.connectfood.core.application.restaurants.mapper.RestaurantOpeningHoursAppMapper;
import com.connectfood.core.application.security.RequestUser;
import com.connectfood.core.application.security.RequestUserGuard;
import com.connectfood.core.domain.model.RestaurantOpeningHours;
import com.connectfood.core.domain.model.enums.UsersType;
import com.connectfood.core.domain.repository.RestaurantOpeningHoursGateway;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CreateRestaurantOpeningHoursUseCaseTest {

  @Mock
  private RestaurantOpeningHoursGateway repository;

  @Mock
  private RestaurantOpeningHoursAppMapper mapper;

  @Mock
  private RequestUserGuard guard;

  @InjectMocks
  private CreateRestaurantOpeningHoursUseCase useCase;

  @Test
  @DisplayName("Deve criar horário de funcionamento e retornar output mapeado")
  void shouldCreateOpeningHoursAndReturnMappedOutput() {
    final var requestUserUuid = UUID.randomUUID();
    final var requestUser = new RequestUser(requestUserUuid);

    final var restaurantUuid = UUID.randomUUID();
    final RestaurantOpeningHoursInput input = Mockito.mock(RestaurantOpeningHoursInput.class);

    final RestaurantOpeningHours domain = Mockito.mock(RestaurantOpeningHours.class);
    Mockito.when(mapper.toDomain(input))
        .thenReturn(domain);

    final RestaurantOpeningHours saved = Mockito.mock(RestaurantOpeningHours.class);
    Mockito.when(repository.save(domain, restaurantUuid))
        .thenReturn(saved);

    final RestaurantOpeningHoursOutput output = Mockito.mock(RestaurantOpeningHoursOutput.class);
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
