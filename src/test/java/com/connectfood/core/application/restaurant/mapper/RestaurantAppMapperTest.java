package com.connectfood.core.application.restaurant.mapper;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

import com.connectfood.core.application.address.dto.AddressInput;
import com.connectfood.core.application.address.dto.AddressOutput;
import com.connectfood.core.application.address.mapper.AddressAppMapper;
import com.connectfood.core.application.restaurant.dto.RestaurantOpeningHourInput;
import com.connectfood.core.application.restaurant.dto.RestaurantOpeningHourOutput;
import com.connectfood.core.application.restaurant.dto.RestaurantInput;
import com.connectfood.core.application.restaurant.dto.RestaurantOutput;
import com.connectfood.core.application.restauranttype.dto.RestaurantTypeOutput;
import com.connectfood.core.application.restauranttype.mapper.RestaurantTypeAppMapper;
import com.connectfood.core.application.user.dto.UserOutput;
import com.connectfood.core.application.user.mapper.UserAppMapper;
import com.connectfood.core.domain.model.Address;
import com.connectfood.core.domain.model.RestaurantOpeningHour;
import com.connectfood.core.domain.model.Restaurant;
import com.connectfood.core.domain.model.RestaurantType;
import com.connectfood.core.domain.model.User;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class RestaurantAppMapperTest {

  @Mock
  private RestaurantTypeAppMapper restaurantsTypeMapper;

  @Mock
  private RestaurantOpeningHourAppMapper restaurantOpeningHoursMapper;

  @Mock
  private AddressAppMapper addressMapper;

  @Mock
  private UserAppMapper usersMapper;

  @InjectMocks
  private RestaurantAppMapper mapper;

  @Test
  @DisplayName("toDomain(input,type) deve retornar null quando input for null")
  void toDomainShouldReturnNullWhenInputIsNull() {
    final RestaurantType restaurantType = Mockito.mock(RestaurantType.class);

    final var result = mapper.toDomain((RestaurantInput) null, restaurantType);

    Assertions.assertNull(result);
    Mockito.verifyNoInteractions(restaurantsTypeMapper, restaurantOpeningHoursMapper, addressMapper, usersMapper);
  }

  @Test
  @DisplayName("toDomain(input,type) deve criar domínio a partir do input")
  void toDomainShouldCreateDomainFromInput() {
    final var input = new RestaurantInput(
        "Restaurant A",
        UUID.randomUUID(),
        List.of(new RestaurantOpeningHourInput(DayOfWeek.MONDAY, LocalTime.of(9, 0), LocalTime.of(18, 0))),
        Mockito.mock(AddressInput.class),
        UUID.randomUUID()
    );

    final RestaurantType restaurantType = Mockito.mock(RestaurantType.class);

    final var result = mapper.toDomain(input, restaurantType);

    Assertions.assertNotNull(result);
    Assertions.assertNotNull(result.getUuid());
    Assertions.assertEquals("Restaurant A", result.getName());
    Assertions.assertSame(restaurantType, result.getRestaurantType());

    Mockito.verifyNoInteractions(restaurantsTypeMapper, restaurantOpeningHoursMapper, addressMapper, usersMapper);
  }

  @Test
  @DisplayName("toDomain(uuid,input,type) deve retornar null quando input for null")
  void toDomainWithUuidShouldReturnNullWhenInputIsNull() {
    final RestaurantType restaurantType = Mockito.mock(RestaurantType.class);

    final var result = mapper.toDomain(UUID.randomUUID(), null, restaurantType);

    Assertions.assertNull(result);
    Mockito.verifyNoInteractions(restaurantsTypeMapper, restaurantOpeningHoursMapper, addressMapper, usersMapper);
  }

  @Test
  @DisplayName("toDomain(uuid,input,type) deve criar domínio com uuid explícito a partir do input")
  void toDomainWithUuidShouldCreateDomainWithExplicitUuid() {
    final var uuid = UUID.randomUUID();

    final var input = new RestaurantInput(
        "Restaurant B",
        UUID.randomUUID(),
        null,
        null,
        null
    );

    final RestaurantType restaurantType = Mockito.mock(RestaurantType.class);

    final var result = mapper.toDomain(uuid, input, restaurantType);

    Assertions.assertNotNull(result);
    Assertions.assertEquals(uuid, result.getUuid());
    Assertions.assertEquals("Restaurant B", result.getName());
    Assertions.assertSame(restaurantType, result.getRestaurantType());

    Mockito.verifyNoInteractions(restaurantsTypeMapper, restaurantOpeningHoursMapper, addressMapper, usersMapper);
  }

  @Test
  @DisplayName("toOutputAll deve retornar null quando model for null")
  void toOutputAllShouldReturnNullWhenModelIsNull() {
    final var result = mapper.toOutputAll(null);

    Assertions.assertNull(result);
    Mockito.verifyNoInteractions(restaurantsTypeMapper, restaurantOpeningHoursMapper, addressMapper, usersMapper);
  }

  @Test
  @DisplayName("toOutputAll deve mapear tipo quando model.getRestaurantsType() estiver presente")
  void toOutputAllShouldMapRestaurantsTypeWhenPresent() {
    final var uuid = UUID.randomUUID();

    final RestaurantType type = Mockito.mock(RestaurantType.class);
    final RestaurantTypeOutput typeOutput = Mockito.mock(RestaurantTypeOutput.class);

    Mockito.when(restaurantsTypeMapper.toOutput(type))
        .thenReturn(typeOutput);

    final Restaurant model = Mockito.mock(Restaurant.class);
    Mockito.when(model.getUuid())
        .thenReturn(uuid);
    Mockito.when(model.getName())
        .thenReturn("Restaurant C");
    Mockito.when(model.getRestaurantType())
        .thenReturn(type);

    final RestaurantOutput result = mapper.toOutputAll(model);

    Assertions.assertNotNull(result);
    Assertions.assertEquals(uuid, result.getUuid());
    Assertions.assertEquals("Restaurant C", result.getName());
    Assertions.assertSame(typeOutput, result.getRestaurantsType());
    Assertions.assertNull(result.getOpeningHours());
    Assertions.assertNull(result.getAddress());
    Assertions.assertNull(result.getUsers());

    Mockito.verify(restaurantsTypeMapper, Mockito.times(1))
        .toOutput(type);
    Mockito.verifyNoInteractions(restaurantOpeningHoursMapper, addressMapper, usersMapper);
  }

  @Test
  @DisplayName("toOutputAll deve setar restaurantsType null quando model.getRestaurantsType() for null")
  void toOutputAllShouldSetNullRestaurantsTypeWhenNull() {
    final Restaurant model = Mockito.mock(Restaurant.class);
    Mockito.when(model.getUuid())
        .thenReturn(UUID.randomUUID());
    Mockito.when(model.getName())
        .thenReturn("Restaurant D");
    Mockito.when(model.getRestaurantType())
        .thenReturn(null);

    final RestaurantOutput result = mapper.toOutputAll(model);

    Assertions.assertNotNull(result);
    Assertions.assertNull(result.getRestaurantsType());

    Mockito.verifyNoInteractions(restaurantsTypeMapper, restaurantOpeningHoursMapper, addressMapper, usersMapper);
  }

  @Test
  @DisplayName("toOutput deve retornar null quando model for null")
  void toOutputShouldReturnNullWhenModelIsNull() {
    final var result = mapper.toOutput(null);

    Assertions.assertNull(result);
    Mockito.verifyNoInteractions(restaurantsTypeMapper, restaurantOpeningHoursMapper, addressMapper, usersMapper);
  }

  @Test
  @DisplayName("toOutput deve mapear todos os campos quando presentes")
  void toOutputShouldMapAllFieldsWhenPresent() {
    final var uuid = UUID.randomUUID();

    final RestaurantType type = Mockito.mock(RestaurantType.class);
    final RestaurantTypeOutput typeOutput = Mockito.mock(RestaurantTypeOutput.class);
    Mockito.when(restaurantsTypeMapper.toOutput(type))
        .thenReturn(typeOutput);

    final var openingEntity1 = Mockito.mock(RestaurantOpeningHour.class);
    final var openingEntity2 = Mockito.mock(RestaurantOpeningHour.class);
    final var openingOut1 = Mockito.mock(RestaurantOpeningHourOutput.class);
    final var openingOut2 = Mockito.mock(RestaurantOpeningHourOutput.class);

    Mockito.when(restaurantOpeningHoursMapper.toOutput(openingEntity1))
        .thenReturn(openingOut1);
    Mockito.when(restaurantOpeningHoursMapper.toOutput(openingEntity2))
        .thenReturn(openingOut2);

    final Address address = Mockito.mock(Address.class);
    final AddressOutput addressOutput = Mockito.mock(AddressOutput.class);
    Mockito.when(addressMapper.toOutput(address))
        .thenReturn(addressOutput);

    final User user = Mockito.mock(User.class);
    final UserOutput userOutput = Mockito.mock(UserOutput.class);
    Mockito.when(usersMapper.toOutput(user))
        .thenReturn(userOutput);

    final Restaurant model = Mockito.mock(Restaurant.class);
    Mockito.when(model.getUuid())
        .thenReturn(uuid);
    Mockito.when(model.getName())
        .thenReturn("Restaurant E");
    Mockito.when(model.getRestaurantType())
        .thenReturn(type);
    Mockito.when(model.getOpeningHours())
        .thenReturn(List.of(openingEntity1, openingEntity2));
    Mockito.when(model.getAddress())
        .thenReturn(address);
    Mockito.when(model.getUser())
        .thenReturn(user);

    final RestaurantOutput result = mapper.toOutput(model);

    Assertions.assertNotNull(result);
    Assertions.assertEquals(uuid, result.getUuid());
    Assertions.assertEquals("Restaurant E", result.getName());
    Assertions.assertSame(typeOutput, result.getRestaurantsType());
    Assertions.assertEquals(List.of(openingOut1, openingOut2), result.getOpeningHours());
    Assertions.assertSame(addressOutput, result.getAddress());
    Assertions.assertSame(userOutput, result.getUsers());

    Mockito.verify(restaurantsTypeMapper, Mockito.times(1))
        .toOutput(type);
    Mockito.verify(restaurantOpeningHoursMapper, Mockito.times(1))
        .toOutput(openingEntity1);
    Mockito.verify(restaurantOpeningHoursMapper, Mockito.times(1))
        .toOutput(openingEntity2);
    Mockito.verify(addressMapper, Mockito.times(1))
        .toOutput(address);
    Mockito.verify(usersMapper, Mockito.times(1))
        .toOutput(user);
    Mockito.verifyNoMoreInteractions(restaurantsTypeMapper, restaurantOpeningHoursMapper, addressMapper, usersMapper);
  }

  @Test
  @DisplayName("toOutput deve mapear campos nulos corretamente (type, address, users) e mapear openingHours")
  void toOutputShouldHandleNullTypeAddressUsersAndMapOpeningHours() {
    final var uuid = UUID.randomUUID();

    final var openingEntity = Mockito.mock(RestaurantOpeningHour.class);
    final var openingOut = Mockito.mock(RestaurantOpeningHourOutput.class);
    Mockito.when(restaurantOpeningHoursMapper.toOutput(openingEntity))
        .thenReturn(openingOut);

    final Restaurant model = Mockito.mock(Restaurant.class);
    Mockito.when(model.getUuid())
        .thenReturn(uuid);
    Mockito.when(model.getName())
        .thenReturn("Restaurant F");
    Mockito.when(model.getRestaurantType())
        .thenReturn(null);
    Mockito.when(model.getOpeningHours())
        .thenReturn(List.of(openingEntity));
    Mockito.when(model.getAddress())
        .thenReturn(null);
    Mockito.when(model.getUser())
        .thenReturn(null);

    final RestaurantOutput result = mapper.toOutput(model);

    Assertions.assertNotNull(result);
    Assertions.assertEquals(uuid, result.getUuid());
    Assertions.assertEquals("Restaurant F", result.getName());
    Assertions.assertNull(result.getRestaurantsType());
    Assertions.assertEquals(List.of(openingOut), result.getOpeningHours());
    Assertions.assertNull(result.getAddress());
    Assertions.assertNull(result.getUsers());

    Mockito.verifyNoInteractions(restaurantsTypeMapper, addressMapper, usersMapper);
    Mockito.verify(restaurantOpeningHoursMapper, Mockito.times(1))
        .toOutput(openingEntity);
    Mockito.verifyNoMoreInteractions(restaurantOpeningHoursMapper);
  }

  @Test
  @DisplayName("toOutput(model,openingHours,address,users) deve retornar null quando model for null")
  void toOutputWithParamsShouldReturnNullWhenModelIsNull() {
    final var result = mapper.toOutput(null, List.of(), Mockito.mock(AddressOutput.class),
        Mockito.mock(UserOutput.class)
    );

    Assertions.assertNull(result);
    Mockito.verifyNoInteractions(restaurantsTypeMapper, restaurantOpeningHoursMapper, addressMapper, usersMapper);
  }

  @Test
  @DisplayName("toOutput(model,openingHours,address,users) deve montar output com os parâmetros informados")
  void toOutputWithParamsShouldBuildOutputUsingProvidedArgs() {
    final var uuid = UUID.randomUUID();

    final RestaurantType type = Mockito.mock(RestaurantType.class);
    final RestaurantTypeOutput typeOutput = Mockito.mock(RestaurantTypeOutput.class);
    Mockito.when(restaurantsTypeMapper.toOutput(type))
        .thenReturn(typeOutput);

    final var openingOut1 = Mockito.mock(RestaurantOpeningHourOutput.class);
    final var openingOut2 = Mockito.mock(RestaurantOpeningHourOutput.class);
    final var openingHours = List.of(openingOut1, openingOut2);

    final AddressOutput addressOutput = Mockito.mock(AddressOutput.class);
    final UserOutput userOutput = Mockito.mock(UserOutput.class);

    final Restaurant model = Mockito.mock(Restaurant.class);
    Mockito.when(model.getUuid())
        .thenReturn(uuid);
    Mockito.when(model.getName())
        .thenReturn("Restaurant G");
    Mockito.when(model.getRestaurantType())
        .thenReturn(type);

    final RestaurantOutput result = mapper.toOutput(model, openingHours, addressOutput, userOutput);

    Assertions.assertNotNull(result);
    Assertions.assertEquals(uuid, result.getUuid());
    Assertions.assertEquals("Restaurant G", result.getName());
    Assertions.assertSame(typeOutput, result.getRestaurantsType());
    Assertions.assertSame(openingHours, result.getOpeningHours());
    Assertions.assertSame(addressOutput, result.getAddress());
    Assertions.assertSame(userOutput, result.getUsers());

    Mockito.verify(restaurantsTypeMapper, Mockito.times(1))
        .toOutput(type);
    Mockito.verifyNoInteractions(restaurantOpeningHoursMapper, addressMapper, usersMapper);
    Mockito.verifyNoMoreInteractions(restaurantsTypeMapper);
  }

  @Test
  @DisplayName(
      "toOutput(model,openingHours,address,users) deve setar restaurantsType null quando model.getRestaurantsType() "
          + "for null")
  void toOutputWithParamsShouldSetNullRestaurantsTypeWhenNull() {
    final Restaurant model = Mockito.mock(Restaurant.class);
    Mockito.when(model.getUuid())
        .thenReturn(UUID.randomUUID());
    Mockito.when(model.getName())
        .thenReturn("Restaurant H");
    Mockito.when(model.getRestaurantType())
        .thenReturn(null);

    final var openingHours = List.of(Mockito.mock(RestaurantOpeningHourOutput.class));
    final AddressOutput addressOutput = Mockito.mock(AddressOutput.class);
    final UserOutput userOutput = Mockito.mock(UserOutput.class);

    final RestaurantOutput result = mapper.toOutput(model, openingHours, addressOutput, userOutput);

    Assertions.assertNotNull(result);
    Assertions.assertNull(result.getRestaurantsType());
    Assertions.assertSame(openingHours, result.getOpeningHours());
    Assertions.assertSame(addressOutput, result.getAddress());
    Assertions.assertSame(userOutput, result.getUsers());

    Mockito.verifyNoInteractions(restaurantsTypeMapper, restaurantOpeningHoursMapper, addressMapper, usersMapper);
  }
}
