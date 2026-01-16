package com.connectfood.infrastructure.rest.mappers;

import java.util.List;
import java.util.UUID;

import com.connectfood.core.application.address.dto.AddressInput;
import com.connectfood.core.application.address.dto.AddressOutput;
import com.connectfood.core.application.restaurants.dto.RestaurantOpeningHoursInput;
import com.connectfood.core.application.restaurants.dto.RestaurantOpeningHoursOutput;
import com.connectfood.core.application.restaurants.dto.RestaurantsInput;
import com.connectfood.core.application.restaurants.dto.RestaurantsOutput;
import com.connectfood.core.application.restaurantstype.dto.RestaurantsTypeOutput;
import com.connectfood.core.application.users.dto.UsersOutput;
import com.connectfood.infrastructure.rest.dto.address.AddressRequest;
import com.connectfood.infrastructure.rest.dto.address.AddressResponse;
import com.connectfood.infrastructure.rest.dto.restaurantopeninghour.RestaurantOpeningHourRequest;
import com.connectfood.infrastructure.rest.dto.restaurantopeninghour.RestaurantOpeningHourResponse;
import com.connectfood.infrastructure.rest.dto.restaurant.RestaurantRequest;
import com.connectfood.infrastructure.rest.dto.restaurant.RestaurantResponse;
import com.connectfood.infrastructure.rest.dto.restauranttype.RestaurantTypeResponse;
import com.connectfood.infrastructure.rest.dto.user.UserResponse;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class RestaurantEntryMapperTest {

  @Mock
  private RestaurantTypeEntryMapper restaurantsTypeMapper;

  @Mock
  private RestaurantOpeningHourEntryMapper restaurantOpeningHoursMapper;

  @Mock
  private AddressEntryMapper addressMapper;

  @Mock
  private UserEntryMapper usersMapper;

  @InjectMocks
  private RestaurantEntryMapper mapper;

  @Test
  @DisplayName("Não deve converter para input quando request for null")
  void shouldReturnNullWhenRequestIsNull() {
    final RestaurantsInput result = mapper.toInput(null);

    Assertions.assertNull(result);
    Mockito.verifyNoInteractions(restaurantsTypeMapper, restaurantOpeningHoursMapper, addressMapper, usersMapper);
  }

  @Test
  @DisplayName("Deve converter para input quando request estiver preenchido")
  void shouldConvertToInputWhenRequestIsProvided() {
    final var restaurantsTypeUuid = UUID.randomUUID();
    final var usersUuid = UUID.randomUUID();

    final RestaurantOpeningHourRequest opening1 = Mockito.mock(RestaurantOpeningHourRequest.class);
    final RestaurantOpeningHourRequest opening2 = Mockito.mock(RestaurantOpeningHourRequest.class);

    final RestaurantOpeningHoursInput openingInput1 = Mockito.mock(RestaurantOpeningHoursInput.class);
    final RestaurantOpeningHoursInput openingInput2 = Mockito.mock(RestaurantOpeningHoursInput.class);

    Mockito.when(restaurantOpeningHoursMapper.toInput(opening1))
        .thenReturn(openingInput1);
    Mockito.when(restaurantOpeningHoursMapper.toInput(opening2))
        .thenReturn(openingInput2);

    final AddressRequest addressRequest = Mockito.mock(AddressRequest.class);
    final AddressInput addressInput = Mockito.mock(AddressInput.class);
    Mockito.when(addressMapper.toInput(addressRequest))
        .thenReturn(addressInput);

    final RestaurantRequest request = Mockito.mock(RestaurantRequest.class);
    Mockito.when(request.getName())
        .thenReturn("Restaurant A");
    Mockito.when(request.getRestaurantsTypeUuid())
        .thenReturn(restaurantsTypeUuid);
    Mockito.when(request.getOpeningHours())
        .thenReturn(List.of(opening1, opening2));
    Mockito.when(request.getAddress())
        .thenReturn(addressRequest);
    Mockito.when(request.getUsersUuid())
        .thenReturn(usersUuid);

    final RestaurantsInput result = mapper.toInput(request);

    Assertions.assertNotNull(result);
    Assertions.assertEquals("Restaurant A", result.getName());
    Assertions.assertEquals(restaurantsTypeUuid, result.getRestaurantsTypeUuid());
    Assertions.assertEquals(List.of(openingInput1, openingInput2), result.getOpeningHours());
    Assertions.assertSame(addressInput, result.getAddress());
    Assertions.assertEquals(usersUuid, result.getUsersUuid());

    Mockito.verify(restaurantOpeningHoursMapper, Mockito.times(1))
        .toInput(opening1);
    Mockito.verify(restaurantOpeningHoursMapper, Mockito.times(1))
        .toInput(opening2);
    Mockito.verify(addressMapper, Mockito.times(1))
        .toInput(addressRequest);
    Mockito.verifyNoInteractions(restaurantsTypeMapper, usersMapper);
  }

  @Test
  @DisplayName("Deve converter para input quando openingHours estiver vazio")
  void shouldConvertToInputWhenOpeningHoursIsEmpty() {
    final var restaurantsTypeUuid = UUID.randomUUID();
    final var usersUuid = UUID.randomUUID();

    final AddressRequest addressRequest = Mockito.mock(AddressRequest.class);
    final AddressInput addressInput = Mockito.mock(AddressInput.class);
    Mockito.when(addressMapper.toInput(addressRequest))
        .thenReturn(addressInput);

    final RestaurantRequest request = Mockito.mock(RestaurantRequest.class);
    Mockito.when(request.getName())
        .thenReturn("Restaurant A");
    Mockito.when(request.getRestaurantsTypeUuid())
        .thenReturn(restaurantsTypeUuid);
    Mockito.when(request.getOpeningHours())
        .thenReturn(List.of());
    Mockito.when(request.getAddress())
        .thenReturn(addressRequest);
    Mockito.when(request.getUsersUuid())
        .thenReturn(usersUuid);

    final RestaurantsInput result = mapper.toInput(request);

    Assertions.assertNotNull(result);
    Assertions.assertEquals("Restaurant A", result.getName());
    Assertions.assertEquals(restaurantsTypeUuid, result.getRestaurantsTypeUuid());
    Assertions.assertNotNull(result.getOpeningHours());
    Assertions.assertTrue(result.getOpeningHours()
        .isEmpty());
    Assertions.assertSame(addressInput, result.getAddress());
    Assertions.assertEquals(usersUuid, result.getUsersUuid());

    Mockito.verify(addressMapper, Mockito.times(1))
        .toInput(addressRequest);
    Mockito.verifyNoInteractions(restaurantOpeningHoursMapper, restaurantsTypeMapper, usersMapper);
  }

  @Test
  @DisplayName("Não deve converter para response quando output for null")
  void shouldReturnNullWhenOutputIsNull() {
    final RestaurantResponse result = mapper.toResponse(null);

    Assertions.assertNull(result);
    Mockito.verifyNoInteractions(restaurantsTypeMapper, restaurantOpeningHoursMapper, addressMapper, usersMapper);
  }

  @Test
  @DisplayName("Deve converter para response com todos os campos quando todos estiverem presentes")
  void shouldConvertToResponseWhenAllFieldsArePresent() {
    final var uuid = UUID.randomUUID();

    final RestaurantsTypeOutput typeOutput = Mockito.mock(RestaurantsTypeOutput.class);
    final RestaurantTypeResponse typeResponse = Mockito.mock(RestaurantTypeResponse.class);
    Mockito.when(restaurantsTypeMapper.toResponse(typeOutput))
        .thenReturn(typeResponse);

    final RestaurantOpeningHoursOutput openingOut1 = Mockito.mock(RestaurantOpeningHoursOutput.class);
    final RestaurantOpeningHoursOutput openingOut2 = Mockito.mock(RestaurantOpeningHoursOutput.class);

    final RestaurantOpeningHourResponse openingResp1 = Mockito.mock(RestaurantOpeningHourResponse.class);
    final RestaurantOpeningHourResponse openingResp2 = Mockito.mock(RestaurantOpeningHourResponse.class);

    Mockito.when(restaurantOpeningHoursMapper.toResponse(openingOut1))
        .thenReturn(openingResp1);
    Mockito.when(restaurantOpeningHoursMapper.toResponse(openingOut2))
        .thenReturn(openingResp2);

    final AddressOutput addressOutput = Mockito.mock(AddressOutput.class);
    final AddressResponse addressResponse = Mockito.mock(AddressResponse.class);
    Mockito.when(addressMapper.toResponse(addressOutput))
        .thenReturn(addressResponse);

    final UsersOutput usersOutput = Mockito.mock(UsersOutput.class);
    final UserResponse userResponse = Mockito.mock(UserResponse.class);
    Mockito.when(usersMapper.toResponse(usersOutput))
        .thenReturn(userResponse);

    final RestaurantsOutput output = Mockito.mock(RestaurantsOutput.class);
    Mockito.when(output.getUuid())
        .thenReturn(uuid);
    Mockito.when(output.getName())
        .thenReturn("Restaurant B");
    Mockito.when(output.getRestaurantsType())
        .thenReturn(typeOutput);
    Mockito.when(output.getOpeningHours())
        .thenReturn(List.of(openingOut1, openingOut2));
    Mockito.when(output.getAddress())
        .thenReturn(addressOutput);
    Mockito.when(output.getUsers())
        .thenReturn(usersOutput);

    final RestaurantResponse result = mapper.toResponse(output);

    Assertions.assertNotNull(result);
    Assertions.assertEquals(uuid, result.getUuid());
    Assertions.assertEquals("Restaurant B", result.getName());
    Assertions.assertSame(typeResponse, result.getRestaurantsType());
    Assertions.assertEquals(List.of(openingResp1, openingResp2), result.getOpeningHours());
    Assertions.assertSame(addressResponse, result.getAddress());
    Assertions.assertSame(userResponse, result.getUsers());

    Mockito.verify(restaurantsTypeMapper, Mockito.times(1))
        .toResponse(typeOutput);
    Mockito.verify(restaurantOpeningHoursMapper, Mockito.times(1))
        .toResponse(openingOut1);
    Mockito.verify(restaurantOpeningHoursMapper, Mockito.times(1))
        .toResponse(openingOut2);
    Mockito.verify(addressMapper, Mockito.times(1))
        .toResponse(addressOutput);
    Mockito.verify(usersMapper, Mockito.times(1))
        .toResponse(usersOutput);
    Mockito.verifyNoMoreInteractions(restaurantsTypeMapper, restaurantOpeningHoursMapper, addressMapper, usersMapper);
  }

  @Test
  @DisplayName("Deve converter para response com nulos quando campos opcionais forem null")
  void shouldConvertToResponseWithNullOptionalFieldsWhenTheyAreNull() {
    final var uuid = UUID.randomUUID();

    final RestaurantsOutput output = Mockito.mock(RestaurantsOutput.class);
    Mockito.when(output.getUuid())
        .thenReturn(uuid);
    Mockito.when(output.getName())
        .thenReturn("Restaurant C");
    Mockito.when(output.getRestaurantsType())
        .thenReturn(null);
    Mockito.when(output.getOpeningHours())
        .thenReturn(null);
    Mockito.when(output.getAddress())
        .thenReturn(null);
    Mockito.when(output.getUsers())
        .thenReturn(null);

    final RestaurantResponse result = mapper.toResponse(output);

    Assertions.assertNotNull(result);
    Assertions.assertEquals(uuid, result.getUuid());
    Assertions.assertEquals("Restaurant C", result.getName());
    Assertions.assertNull(result.getRestaurantsType());
    Assertions.assertNull(result.getOpeningHours());
    Assertions.assertNull(result.getAddress());
    Assertions.assertNull(result.getUsers());

    Mockito.verifyNoInteractions(restaurantsTypeMapper, restaurantOpeningHoursMapper, addressMapper, usersMapper);
  }

  @Test
  @DisplayName("Deve converter para response com lista vazia quando openingHours estiver vazio")
  void shouldConvertToResponseWithEmptyOpeningHoursList() {
    final var uuid = UUID.randomUUID();

    final RestaurantsOutput output = Mockito.mock(RestaurantsOutput.class);
    Mockito.when(output.getUuid())
        .thenReturn(uuid);
    Mockito.when(output.getName())
        .thenReturn("Restaurant D");
    Mockito.when(output.getRestaurantsType())
        .thenReturn(null);
    Mockito.when(output.getOpeningHours())
        .thenReturn(List.of());
    Mockito.when(output.getAddress())
        .thenReturn(null);
    Mockito.when(output.getUsers())
        .thenReturn(null);

    final RestaurantResponse result = mapper.toResponse(output);

    Assertions.assertNotNull(result);
    Assertions.assertEquals(uuid, result.getUuid());
    Assertions.assertEquals("Restaurant D", result.getName());
    Assertions.assertNull(result.getRestaurantsType());
    Assertions.assertNotNull(result.getOpeningHours());
    Assertions.assertTrue(result.getOpeningHours()
        .isEmpty());
    Assertions.assertNull(result.getAddress());
    Assertions.assertNull(result.getUsers());

    Mockito.verifyNoInteractions(restaurantsTypeMapper, addressMapper, usersMapper);
    Mockito.verifyNoInteractions(restaurantOpeningHoursMapper);
  }
}
