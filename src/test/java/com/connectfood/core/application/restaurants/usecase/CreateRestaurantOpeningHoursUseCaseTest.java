package com.connectfood.core.application.restaurants.usecase;

import java.util.UUID;

import com.connectfood.core.application.restaurants.dto.RestaurantOpeningHoursInput;
import com.connectfood.core.application.restaurants.dto.RestaurantOpeningHoursOutput;
import com.connectfood.core.application.restaurants.mapper.RestaurantOpeningHoursAppMapper;
import com.connectfood.core.domain.model.RestaurantOpeningHours;
import com.connectfood.core.domain.repository.RestaurantOpeningHoursRepository;

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
  private RestaurantOpeningHoursRepository repository;

  @Mock
  private RestaurantOpeningHoursAppMapper mapper;

  @InjectMocks
  private CreateRestaurantOpeningHoursUseCase useCase;

  @Test
  @DisplayName("Deve criar horário de funcionamento e retornar output mapeado")
  void shouldCreateOpeningHoursAndReturnMappedOutput() {
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

    final var result = useCase.execute(restaurantUuid, input);

    Assertions.assertNotNull(result);
    Assertions.assertSame(output, result);

    Mockito.verify(mapper, Mockito.times(1))
        .toDomain(input);
    Mockito.verify(repository, Mockito.times(1))
        .save(domain, restaurantUuid);
    Mockito.verify(mapper, Mockito.times(1))
        .toOutput(saved);
    Mockito.verifyNoMoreInteractions(repository, mapper);
  }
}
