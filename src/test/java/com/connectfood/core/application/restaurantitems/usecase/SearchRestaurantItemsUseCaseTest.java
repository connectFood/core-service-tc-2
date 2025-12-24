package com.connectfood.core.application.restaurantitems.usecase;

import java.util.List;
import java.util.UUID;

import com.connectfood.core.application.dto.commons.PageOutput;
import com.connectfood.core.application.restaurantitems.dto.RestaurantItemsOutput;
import com.connectfood.core.application.restaurantitems.mapper.RestaurantItemsAppMapper;
import com.connectfood.core.domain.model.RestaurantItems;
import com.connectfood.core.domain.model.commons.PageModel;
import com.connectfood.core.domain.repository.RestaurantItemsRepository;

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
  private RestaurantItemsRepository repository;

  @Mock
  private RestaurantItemsAppMapper mapper;

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

    final RestaurantItems model1 = Mockito.mock(RestaurantItems.class);
    final RestaurantItems model2 = Mockito.mock(RestaurantItems.class);

    final var pageModel = new PageModel<List<RestaurantItems>>(List.of(model1, model2), 25L);

    Mockito.when(repository.findAll(restaurantUuid, page, size, sort, direction))
        .thenReturn(pageModel);

    final RestaurantItemsOutput output1 = Mockito.mock(RestaurantItemsOutput.class);
    final RestaurantItemsOutput output2 = Mockito.mock(RestaurantItemsOutput.class);

    Mockito.when(mapper.toOutput(model1))
        .thenReturn(output1);
    Mockito.when(mapper.toOutput(model2))
        .thenReturn(output2);

    final PageOutput<List<RestaurantItemsOutput>> result =
        useCase.execute(restaurantUuid, page, size, sort, direction);

    Assertions.assertNotNull(result);
    Assertions.assertEquals(25L, result.total());
    Assertions.assertEquals(List.of(output1, output2), result.content());

    Mockito.verify(repository, Mockito.times(1))
        .findAll(restaurantUuid, page, size, sort, direction);
    Mockito.verify(mapper, Mockito.times(1))
        .toOutput(model1);
    Mockito.verify(mapper, Mockito.times(1))
        .toOutput(model2);
    Mockito.verifyNoMoreInteractions(repository, mapper);
  }

  @Test
  @DisplayName("Deve retornar PageOutput vazio quando repository retornar lista vazia")
  void shouldReturnEmptyPageOutputWhenNoResults() {
    final var restaurantUuid = UUID.randomUUID();
    final var page = 0;
    final var size = 10;

    final var pageModel = new PageModel<List<RestaurantItems>>(List.of(), 0L);

    Mockito.when(repository.findAll(restaurantUuid, page, size, null, null))
        .thenReturn(pageModel);

    final var result = useCase.execute(restaurantUuid, page, size, null, null);

    Assertions.assertNotNull(result);
    Assertions.assertEquals(0L, result.total());
    Assertions.assertTrue(result.content()
        .isEmpty());

    Mockito.verify(repository, Mockito.times(1))
        .findAll(restaurantUuid, page, size, null, null);
    Mockito.verifyNoInteractions(mapper);
    Mockito.verifyNoMoreInteractions(repository);
  }
}
