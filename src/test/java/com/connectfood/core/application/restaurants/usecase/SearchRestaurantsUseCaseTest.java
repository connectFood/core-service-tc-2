package com.connectfood.core.application.restaurants.usecase;

import java.util.List;
import java.util.UUID;

import com.connectfood.core.application.dto.commons.PageOutput;
import com.connectfood.core.application.restaurants.dto.RestaurantsOutput;
import com.connectfood.core.application.restaurants.mapper.RestaurantsAppMapper;
import com.connectfood.core.domain.model.Restaurants;
import com.connectfood.core.domain.model.commons.PageModel;
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
class SearchRestaurantsUseCaseTest {

  @Mock
  private RestaurantsRepository repository;

  @Mock
  private RestaurantsAppMapper mapper;

  @InjectMocks
  private SearchRestaurantsUseCase useCase;

  @Test
  @DisplayName("Deve retornar PageOutput com lista mapeada e total quando houver resultados")
  void shouldReturnPageOutputWithMappedResultsAndTotal() {
    final String name = "Pizza";
    final UUID restaurantsTypeUuid = UUID.randomUUID();
    final String street = "Paulista";
    final String city = "São Paulo";
    final String state = "SP";
    final Integer page = 0;
    final Integer size = 10;
    final String sort = "id";
    final String direction = "ASC";

    final Restaurants restaurant1 = Mockito.mock(Restaurants.class);
    final Restaurants restaurant2 = Mockito.mock(Restaurants.class);

    final PageModel<List<Restaurants>> model = new PageModel<>(List.of(restaurant1, restaurant2), 2L);

    Mockito.when(repository.findAll(name, restaurantsTypeUuid, street, city, state, page, size, sort, direction))
        .thenReturn(model);

    final RestaurantsOutput output1 = Mockito.mock(RestaurantsOutput.class);
    final RestaurantsOutput output2 = Mockito.mock(RestaurantsOutput.class);

    Mockito.when(mapper.toOutputAll(restaurant1))
        .thenReturn(output1);
    Mockito.when(mapper.toOutputAll(restaurant2))
        .thenReturn(output2);

    final PageOutput<List<RestaurantsOutput>> result = useCase.execute(
        name,
        restaurantsTypeUuid,
        street,
        city,
        state,
        page,
        size,
        sort,
        direction
    );

    Assertions.assertNotNull(result);
    Assertions.assertNotNull(result.content());
    Assertions.assertEquals(2, result.content()
        .size()
    );
    Assertions.assertEquals(List.of(output1, output2), result.content());
    Assertions.assertEquals(2L, result.total());

    Mockito.verify(repository, Mockito.times(1))
        .findAll(name, restaurantsTypeUuid, street, city, state, page, size, sort, direction);

    Mockito.verify(mapper, Mockito.times(1))
        .toOutputAll(restaurant1);
    Mockito.verify(mapper, Mockito.times(1))
        .toOutputAll(restaurant2);

    Mockito.verifyNoMoreInteractions(repository, mapper);
  }

  @Test
  @DisplayName("Deve retornar PageOutput com lista vazia quando não houver resultados")
  void shouldReturnPageOutputWithEmptyListWhenNoResults() {
    final String name = null;
    final UUID restaurantsTypeUuid = null;
    final String street = null;
    final String city = null;
    final String state = null;
    final Integer page = 0;
    final Integer size = 10;
    final String sort = null;
    final String direction = null;

    final PageModel<List<Restaurants>> model = new PageModel<>(List.of(), 0L);

    Mockito.when(repository.findAll(name, restaurantsTypeUuid, street, city, state, page, size, sort, direction))
        .thenReturn(model);

    final PageOutput<List<RestaurantsOutput>> result = useCase.execute(
        name,
        restaurantsTypeUuid,
        street,
        city,
        state,
        page,
        size,
        sort,
        direction
    );

    Assertions.assertNotNull(result);
    Assertions.assertNotNull(result.content());
    Assertions.assertTrue(result.content()
        .isEmpty());
    Assertions.assertEquals(0L, result.total());

    Mockito.verify(repository, Mockito.times(1))
        .findAll(name, restaurantsTypeUuid, street, city, state, page, size, sort, direction);

    Mockito.verifyNoInteractions(mapper);
    Mockito.verifyNoMoreInteractions(repository);
  }
}
