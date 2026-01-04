package com.connectfood.core.application.restaurantopeninghours.usecase;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.connectfood.core.application.dto.commons.PageOutput;
import com.connectfood.core.application.restaurantopeninghours.dto.RestaurantOpeningHoursOutput;
import com.connectfood.core.application.restaurantopeninghours.mapper.RestaurantOpeningHoursAppMapper;
import com.connectfood.core.domain.exception.NotFoundException;
import com.connectfood.core.domain.model.RestaurantOpeningHours;
import com.connectfood.core.domain.model.Restaurants;
import com.connectfood.core.domain.model.commons.PageModel;
import com.connectfood.core.domain.repository.RestaurantOpeningHoursRepository;
import com.connectfood.core.domain.repository.RestaurantsRepository;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class SearchRestaurantOpeningHoursUseCaseTest {

  @Mock
  private RestaurantOpeningHoursRepository repository;

  @Mock
  private RestaurantOpeningHoursAppMapper mapper;

  @Mock
  private RestaurantsRepository restaurantsRepository;

  @InjectMocks
  private SearchRestaurantOpeningHoursUseCase useCase;

  @Test
  @DisplayName("Deve retornar PageOutput com resultados mapeados quando restaurant existir e houver conteúdo")
  void shouldReturnPageOutputWithMappedResultsWhenRestaurantExistsAndHasContent() {
    final var restaurantUuid = UUID.randomUUID();
    final var page = 0;
    final var size = 10;
    final var sort = "dayOfWeek";
    final var direction = "ASC";

    final Restaurants restaurants = Mockito.mock(Restaurants.class);
    Mockito.when(restaurantsRepository.findByUuid(restaurantUuid))
        .thenReturn(Optional.of(restaurants));

    final RestaurantOpeningHours model1 = Mockito.mock(RestaurantOpeningHours.class);
    final RestaurantOpeningHours model2 = Mockito.mock(RestaurantOpeningHours.class);

    final RestaurantOpeningHoursOutput out1 = Mockito.mock(RestaurantOpeningHoursOutput.class);
    final RestaurantOpeningHoursOutput out2 = Mockito.mock(RestaurantOpeningHoursOutput.class);

    Mockito.when(mapper.toOutput(model1)).thenReturn(out1);
    Mockito.when(mapper.toOutput(model2)).thenReturn(out2);

    final var pageModel = new PageModel<List<RestaurantOpeningHours>>(
        List.of(model1, model2),
        2L
    );

    Mockito.when(repository.findAll(restaurantUuid, page, size, sort, direction))
        .thenReturn(pageModel);

    final PageOutput<List<RestaurantOpeningHoursOutput>> result =
        useCase.execute(restaurantUuid, page, size, sort, direction);

    Assertions.assertNotNull(result);
    Assertions.assertNotNull(result.content());
    Assertions.assertEquals(2, result.content().size());
    Assertions.assertSame(out1, result.content().get(0));
    Assertions.assertSame(out2, result.content().get(1));
    Assertions.assertEquals(2L, result.total());

    Mockito.verify(restaurantsRepository, Mockito.times(1))
        .findByUuid(restaurantUuid);
    Mockito.verify(repository, Mockito.times(1))
        .findAll(restaurantUuid, page, size, sort, direction);
    Mockito.verify(mapper, Mockito.times(1))
        .toOutput(model1);
    Mockito.verify(mapper, Mockito.times(1))
        .toOutput(model2);
    Mockito.verifyNoMoreInteractions(restaurantsRepository, repository, mapper);
  }

  @Test
  @DisplayName("Deve retornar PageOutput com lista vazia quando restaurant existir e não houver conteúdo")
  void shouldReturnPageOutputWithEmptyListWhenRestaurantExistsAndNoContent() {
    final var restaurantUuid = UUID.randomUUID();
    final var page = 1;
    final var size = 5;
    final var sort = "startTime";
    final var direction = "DESC";

    final Restaurants restaurants = Mockito.mock(Restaurants.class);
    Mockito.when(restaurantsRepository.findByUuid(restaurantUuid))
        .thenReturn(Optional.of(restaurants));

    final var pageModel = new PageModel<List<RestaurantOpeningHours>>(
        List.of(),
        0L
    );

    Mockito.when(repository.findAll(restaurantUuid, page, size, sort, direction))
        .thenReturn(pageModel);

    final PageOutput<List<RestaurantOpeningHoursOutput>> result =
        useCase.execute(restaurantUuid, page, size, sort, direction);

    Assertions.assertNotNull(result);
    Assertions.assertNotNull(result.content());
    Assertions.assertTrue(result.content().isEmpty());
    Assertions.assertEquals(0L, result.total());

    Mockito.verify(restaurantsRepository, Mockito.times(1))
        .findByUuid(restaurantUuid);
    Mockito.verify(repository, Mockito.times(1))
        .findAll(restaurantUuid, page, size, sort, direction);
    Mockito.verifyNoInteractions(mapper);
    Mockito.verifyNoMoreInteractions(restaurantsRepository, repository);
  }

  @Test
  @DisplayName("Não deve buscar horários de funcionamento quando restaurant não existir")
  void shouldThrowNotFoundExceptionWhenRestaurantDoesNotExist() {
    final var restaurantUuid = UUID.randomUUID();

    Mockito.when(restaurantsRepository.findByUuid(restaurantUuid))
        .thenReturn(Optional.empty());

    final var exception = Assertions.assertThrows(
        NotFoundException.class,
        () -> useCase.execute(restaurantUuid, 0, 10, "dayOfWeek", "ASC")
    );

    Assertions.assertEquals("Restaurant not found", exception.getMessage());

    Mockito.verify(restaurantsRepository, Mockito.times(1))
        .findByUuid(restaurantUuid);
    Mockito.verifyNoInteractions(repository, mapper);
    Mockito.verifyNoMoreInteractions(restaurantsRepository);
  }
}
