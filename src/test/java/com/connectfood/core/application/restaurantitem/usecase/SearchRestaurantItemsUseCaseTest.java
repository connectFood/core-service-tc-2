package com.connectfood.core.application.restaurantitem.usecase;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.connectfood.core.application.dto.commons.PageOutput;
import com.connectfood.core.application.restaurantitem.dto.RestaurantItemOutput;
import com.connectfood.core.application.restaurantitem.mapper.RestaurantItemAppMapper;
import com.connectfood.core.domain.exception.NotFoundException;
import com.connectfood.core.domain.model.RestaurantItem;
import com.connectfood.core.domain.model.Restaurant;
import com.connectfood.core.domain.model.commons.PageModel;
import com.connectfood.core.domain.repository.RestaurantItemGateway;
import com.connectfood.core.domain.repository.RestaurantGateway;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class SearchRestaurantItemsUseCaseTest {

  @Mock
  private RestaurantItemGateway repository;

  @Mock
  private RestaurantItemAppMapper mapper;

  @Mock
  private RestaurantGateway restaurantGateway;

  @InjectMocks
  private SearchRestaurantItemsUseCase useCase;

  @Test
  @DisplayName("Deve retornar PageOutput com lista mapeada e total quando houver resultados")
  void shouldReturnPageOutputWithMappedResultsAndTotal() {
    final var restaurantUuid = UUID.randomUUID();
    final var page = 0;
    final var size = 10;
    final var sort = "id";
    final var direction = "ASC";

    final Restaurant restaurant = Mockito.mock(Restaurant.class);
    Mockito.when(restaurantGateway.findByUuid(restaurantUuid))
        .thenReturn(Optional.of(restaurant));

    final RestaurantItem model1 = Mockito.mock(RestaurantItem.class);
    final RestaurantItem model2 = Mockito.mock(RestaurantItem.class);

    final var pageModel = new PageModel<>(List.of(model1, model2), 25L);

    Mockito.when(repository.findAll(restaurantUuid, page, size, sort, direction))
        .thenReturn(pageModel);

    final RestaurantItemOutput output1 = Mockito.mock(RestaurantItemOutput.class);
    final RestaurantItemOutput output2 = Mockito.mock(RestaurantItemOutput.class);

    Mockito.when(mapper.toOutput(model1)).thenReturn(output1);
    Mockito.when(mapper.toOutput(model2)).thenReturn(output2);

    final PageOutput<List<RestaurantItemOutput>> result =
        useCase.execute(restaurantUuid, page, size, sort, direction);

    Assertions.assertNotNull(result);
    Assertions.assertEquals(25L, result.total());
    Assertions.assertEquals(List.of(output1, output2), result.content());

    Mockito.verify(restaurantGateway, Mockito.times(1)).findByUuid(restaurantUuid);
    Mockito.verify(repository, Mockito.times(1)).findAll(restaurantUuid, page, size, sort, direction);
    Mockito.verify(mapper, Mockito.times(1)).toOutput(model1);
    Mockito.verify(mapper, Mockito.times(1)).toOutput(model2);
    Mockito.verifyNoMoreInteractions(restaurantGateway, repository, mapper);
  }

  @Test
  @DisplayName("Deve retornar PageOutput vazio quando repository retornar lista vazia")
  void shouldReturnEmptyPageOutputWhenNoResults() {
    final var restaurantUuid = UUID.randomUUID();
    final var page = 0;
    final var size = 10;

    final Restaurant restaurant = Mockito.mock(Restaurant.class);
    Mockito.when(restaurantGateway.findByUuid(restaurantUuid))
        .thenReturn(Optional.of(restaurant));

    final var pageModel = new PageModel<>(List.<RestaurantItem>of(), 0L);

    Mockito.when(repository.findAll(restaurantUuid, page, size, null, null))
        .thenReturn(pageModel);

    final var result = useCase.execute(restaurantUuid, page, size, null, null);

    Assertions.assertNotNull(result);
    Assertions.assertEquals(0L, result.total());
    Assertions.assertTrue(result.content().isEmpty());

    Mockito.verify(restaurantGateway, Mockito.times(1)).findByUuid(restaurantUuid);
    Mockito.verify(repository, Mockito.times(1)).findAll(restaurantUuid, page, size, null, null);
    Mockito.verifyNoInteractions(mapper);
    Mockito.verifyNoMoreInteractions(restaurantGateway, repository);
  }

  @Test
  @DisplayName("Deve lançar NotFoundException quando restaurante não existir")
  void shouldThrowNotFoundExceptionWhenRestaurantDoesNotExist() {
    final var restaurantUuid = UUID.randomUUID();

    Mockito.when(restaurantGateway.findByUuid(restaurantUuid))
        .thenReturn(Optional.empty());

    final var exception = Assertions.assertThrows(
        NotFoundException.class,
        () -> useCase.execute(restaurantUuid, 0, 10, "id", "ASC")
    );

    Assertions.assertEquals("Restaurant not found", exception.getMessage());

    Mockito.verify(restaurantGateway, Mockito.times(1)).findByUuid(restaurantUuid);
    Mockito.verifyNoInteractions(repository, mapper);
    Mockito.verifyNoMoreInteractions(restaurantGateway);
  }
}
