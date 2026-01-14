package com.connectfood.core.application.restaurants.usecase;

import java.util.Optional;
import java.util.UUID;

import com.connectfood.core.application.restaurants.dto.RestaurantOpeningHoursInput;
import com.connectfood.core.application.restaurants.dto.RestaurantOpeningHoursOutput;
import com.connectfood.core.application.restaurants.mapper.RestaurantOpeningHoursAppMapper;
import com.connectfood.core.domain.exception.NotFoundException;
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
class UpdateRestaurantOpeningHoursUseCaseTest {

  @Mock
  private RestaurantOpeningHoursRepository repository;

  @Mock
  private RestaurantOpeningHoursAppMapper mapper;

  @InjectMocks
  private UpdateRestaurantOpeningHoursUseCase useCase;

  @Test
  @DisplayName("Deve atualizar horário de funcionamento quando registro existir")
  void shouldUpdateOpeningHoursWhenExists() {
    final var uuid = UUID.randomUUID();

    final var input = Mockito.mock(RestaurantOpeningHoursInput.class);

    final RestaurantOpeningHours existing = Mockito.mock(RestaurantOpeningHours.class);
    Mockito.when(repository.findByUuid(uuid))
        .thenReturn(Optional.of(existing));

    final RestaurantOpeningHours domainToUpdate = Mockito.mock(RestaurantOpeningHours.class);
    Mockito.when(mapper.toDomain(uuid, input))
        .thenReturn(domainToUpdate);

    final RestaurantOpeningHours updated = Mockito.mock(RestaurantOpeningHours.class);
    Mockito.when(repository.update(uuid, domainToUpdate))
        .thenReturn(updated);

    final RestaurantOpeningHoursOutput output = Mockito.mock(RestaurantOpeningHoursOutput.class);
    Mockito.when(mapper.toOutput(updated))
        .thenReturn(output);

    final var result = useCase.execute(uuid, input);

    Assertions.assertNotNull(result);
    Assertions.assertSame(output, result);

    Mockito.verify(repository, Mockito.times(1))
        .findByUuid(uuid);
    Mockito.verify(mapper, Mockito.times(1))
        .toDomain(uuid, input);
    Mockito.verify(repository, Mockito.times(1))
        .update(uuid, domainToUpdate);
    Mockito.verify(mapper, Mockito.times(1))
        .toOutput(updated);
    Mockito.verifyNoMoreInteractions(repository, mapper);
  }

  @Test
  @DisplayName("Não deve atualizar horário de funcionamento quando registro não existir")
  void shouldThrowNotFoundExceptionWhenDoesNotExist() {
    final var uuid = UUID.randomUUID();
    final var input = Mockito.mock(RestaurantOpeningHoursInput.class);

    Mockito.when(repository.findByUuid(uuid))
        .thenReturn(Optional.empty());

    final var exception = Assertions.assertThrows(
        NotFoundException.class,
        () -> useCase.execute(uuid, input)
    );

    Assertions.assertEquals("Restaurant opening hours not found", exception.getMessage());

    Mockito.verify(repository, Mockito.times(1))
        .findByUuid(uuid);
    Mockito.verifyNoInteractions(mapper);
    Mockito.verifyNoMoreInteractions(repository);
  }
}
