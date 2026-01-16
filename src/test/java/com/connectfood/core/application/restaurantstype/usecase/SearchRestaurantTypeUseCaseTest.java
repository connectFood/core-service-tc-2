package com.connectfood.core.application.restaurantstype.usecase;

import java.util.Collections;
import java.util.List;

import com.connectfood.core.application.dto.commons.PageOutput;
import com.connectfood.core.application.restaurantstype.dto.RestaurantsTypeOutput;
import com.connectfood.core.application.restaurantstype.mapper.RestaurantsTypeAppMapper;
import com.connectfood.core.domain.model.RestaurantType;
import com.connectfood.core.domain.model.commons.PageModel;
import com.connectfood.core.domain.repository.RestaurantsTypeGateway;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class SearchRestaurantTypeUseCaseTest {

  @Mock
  private RestaurantsTypeGateway repository;

  @Mock
  private RestaurantsTypeAppMapper mapper;

  @InjectMocks
  private SearchRestaurantTypeUseCase useCase;

  @Test
  @DisplayName("Deve retornar PageOutput com resultados mapeados quando houver conteúdo")
  void shouldReturnPageOutputWithMappedResultsWhenHasContent() {
    final var name = "pizza";
    final var page = 0;
    final var size = 10;
    final var sort = "name";
    final var direction = "ASC";

    final var model1 = Mockito.mock(RestaurantType.class);
    final var model2 = Mockito.mock(RestaurantType.class);

    final var out1 = Mockito.mock(RestaurantsTypeOutput.class);
    final var out2 = Mockito.mock(RestaurantsTypeOutput.class);

    Mockito.when(mapper.toOutput(model1))
        .thenReturn(out1);
    Mockito.when(mapper.toOutput(model2))
        .thenReturn(out2);

    final var pageModel = new PageModel<>(List.of(model1, model2), 2L);

    Mockito.when(repository.findAll(name, page, size, sort, direction))
        .thenReturn(pageModel);

    final PageOutput<List<RestaurantsTypeOutput>> result =
        useCase.execute(name, page, size, sort, direction);

    Assertions.assertNotNull(result);
    Assertions.assertNotNull(result.content());
    Assertions.assertEquals(2, result.content()
        .size()
    );
    Assertions.assertSame(out1, result.content()
        .get(0)
    );
    Assertions.assertSame(out2, result.content()
        .get(1)
    );
    Assertions.assertEquals(2L, result.total());

    Mockito.verify(repository, Mockito.times(1))
        .findAll(name, page, size, sort, direction);

    Mockito.verify(mapper, Mockito.times(1))
        .toOutput(model1);
    Mockito.verify(mapper, Mockito.times(1))
        .toOutput(model2);
    Mockito.verifyNoMoreInteractions(repository, mapper);
  }

  @Test
  @DisplayName("Deve retornar PageOutput com lista vazia quando não houver conteúdo")
  void shouldReturnPageOutputWithEmptyListWhenNoContent() {
    final var name = "burger";
    final var page = 1;
    final var size = 5;
    final var sort = "createdAt";
    final var direction = "DESC";

    final var pageModel = new PageModel<>(Collections.<RestaurantType>emptyList(), 0L);

    Mockito.when(repository.findAll(name, page, size, sort, direction))
        .thenReturn(pageModel);

    final PageOutput<List<RestaurantsTypeOutput>> result =
        useCase.execute(name, page, size, sort, direction);

    Assertions.assertNotNull(result);
    Assertions.assertNotNull(result.content());
    Assertions.assertTrue(result.content()
        .isEmpty());
    Assertions.assertEquals(0L, result.total());

    Mockito.verify(repository, Mockito.times(1))
        .findAll(name, page, size, sort, direction);

    Mockito.verifyNoInteractions(mapper);
    Mockito.verifyNoMoreInteractions(repository);
  }
}
