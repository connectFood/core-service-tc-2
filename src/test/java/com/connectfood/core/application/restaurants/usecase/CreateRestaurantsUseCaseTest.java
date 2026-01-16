package com.connectfood.core.application.restaurants.usecase;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.connectfood.core.application.address.dto.AddressInput;
import com.connectfood.core.application.address.dto.AddressOutput;
import com.connectfood.core.application.restaurants.dto.RestaurantOpeningHoursInput;
import com.connectfood.core.application.restaurants.dto.RestaurantOpeningHoursOutput;
import com.connectfood.core.application.restaurants.dto.RestaurantsInput;
import com.connectfood.core.application.restaurants.dto.RestaurantsOutput;
import com.connectfood.core.application.restaurants.mapper.RestaurantsAppMapper;
import com.connectfood.core.application.security.RequestUser;
import com.connectfood.core.application.security.RequestUserGuard;
import com.connectfood.core.application.users.dto.UsersOutput;
import com.connectfood.core.domain.exception.NotFoundException;
import com.connectfood.core.domain.model.Restaurant;
import com.connectfood.core.domain.model.RestaurantType;
import com.connectfood.core.domain.model.User;
import com.connectfood.core.domain.model.enums.UsersType;
import com.connectfood.core.domain.repository.RestaurantGateway;
import com.connectfood.core.domain.repository.RestaurantTypeGateway;
import com.connectfood.core.domain.repository.UserGateway;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CreateRestaurantsUseCaseTest {

  @Mock
  private RestaurantGateway repository;

  @Mock
  private RestaurantsAppMapper mapper;

  @Mock
  private RequestUserGuard guard;

  @Mock
  private RestaurantTypeGateway restaurantTypeGateway;

  @Mock
  private CreateRestaurantsAddressUseCase createRestaurantsAddressUseCase;

  @Mock
  private CreateRestaurantOpeningHoursUseCase createRestaurantOpeningHoursUseCase;

  @Mock
  private UserGateway userGateway;

  @Mock
  private CreateUsersRestaurantUseCase createUsersRestaurantUseCase;

  @InjectMocks
  private CreateRestaurantsUseCase useCase;

  @Test
  @DisplayName("Não deve criar restaurante quando usuário não existir")
  void shouldThrowExceptionWhenUserNotFound() {
    final var requestUserUuid = UUID.randomUUID();
    final var requestUser = new RequestUser(requestUserUuid);

    final var usersUuid = UUID.randomUUID();

    final RestaurantsInput input = Mockito.mock(RestaurantsInput.class);
    Mockito.when(input.getUsersUuid())
        .thenReturn(usersUuid);

    Mockito.when(userGateway.findByUuid(usersUuid))
        .thenReturn(Optional.empty());

    final var ex = Assertions.assertThrows(
        NotFoundException.class,
        () -> useCase.execute(requestUser, input)
    );

    Assertions.assertEquals("User not found", ex.getMessage());

    Mockito.verify(guard, Mockito.times(1))
        .requireRole(requestUser, UsersType.OWNER.name());

    Mockito.verify(userGateway, Mockito.times(1))
        .findByUuid(usersUuid);

    Mockito.verifyNoInteractions(
        restaurantTypeGateway,
        repository,
        mapper,
        createRestaurantsAddressUseCase,
        createRestaurantOpeningHoursUseCase,
        createUsersRestaurantUseCase
    );

    Mockito.verifyNoMoreInteractions(guard, userGateway);
  }

  @Test
  @DisplayName("Não deve criar restaurante quando tipo de restaurante não existir")
  void shouldThrowExceptionWhenRestaurantsTypeNotFound() {
    final var requestUserUuid = UUID.randomUUID();
    final var requestUser = new RequestUser(requestUserUuid);

    final var usersUuid = UUID.randomUUID();
    final var restaurantsTypeUuid = UUID.randomUUID();

    final RestaurantsInput input = Mockito.mock(RestaurantsInput.class);
    Mockito.when(input.getUsersUuid())
        .thenReturn(usersUuid);
    Mockito.when(input.getRestaurantsTypeUuid())
        .thenReturn(restaurantsTypeUuid);

    final User user = Mockito.mock(User.class);
    Mockito.when(userGateway.findByUuid(usersUuid))
        .thenReturn(Optional.of(user));

    Mockito.when(restaurantTypeGateway.findById(restaurantsTypeUuid))
        .thenReturn(Optional.empty());

    final var ex = Assertions.assertThrows(
        NotFoundException.class,
        () -> useCase.execute(requestUser, input)
    );

    Assertions.assertEquals("Restaurants Type not found", ex.getMessage());

    Mockito.verify(guard, Mockito.times(1))
        .requireRole(requestUser, UsersType.OWNER.name());

    Mockito.verify(userGateway, Mockito.times(1))
        .findByUuid(usersUuid);

    Mockito.verify(restaurantTypeGateway, Mockito.times(1))
        .findById(restaurantsTypeUuid);

    Mockito.verifyNoInteractions(
        repository,
        mapper,
        createRestaurantsAddressUseCase,
        createRestaurantOpeningHoursUseCase,
        createUsersRestaurantUseCase
    );

    Mockito.verifyNoMoreInteractions(guard, userGateway, restaurantTypeGateway);
  }

  @Test
  @DisplayName("Deve criar restaurante, endereço, vínculo usuário-restaurante e horários, retornando output final")
  void shouldCreateRestaurantWhenDataIsValid() {
    final var requestUserUuid = UUID.randomUUID();
    final var requestUser = new RequestUser(requestUserUuid);

    final var usersUuid = UUID.randomUUID();
    final var restaurantsTypeUuid = UUID.randomUUID();
    final var restaurantUuid = UUID.randomUUID();

    final AddressInput addressInput = Mockito.mock(AddressInput.class);

    final RestaurantOpeningHoursInput oh1 = Mockito.mock(RestaurantOpeningHoursInput.class);
    final RestaurantOpeningHoursInput oh2 = Mockito.mock(RestaurantOpeningHoursInput.class);

    final RestaurantsInput input = Mockito.mock(RestaurantsInput.class);
    Mockito.when(input.getUsersUuid())
        .thenReturn(usersUuid);
    Mockito.when(input.getRestaurantsTypeUuid())
        .thenReturn(restaurantsTypeUuid);
    Mockito.when(input.getAddress())
        .thenReturn(addressInput);
    Mockito.when(input.getOpeningHours())
        .thenReturn(List.of(oh1, oh2));

    final User user = Mockito.mock(User.class);
    Mockito.when(user.getUuid())
        .thenReturn(usersUuid);
    Mockito.when(userGateway.findByUuid(usersUuid))
        .thenReturn(Optional.of(user));

    final RestaurantType restaurantType = Mockito.mock(RestaurantType.class);
    Mockito.when(restaurantTypeGateway.findById(restaurantsTypeUuid))
        .thenReturn(Optional.of(restaurantType));

    final Restaurant restaurantDomainToSave = Mockito.mock(Restaurant.class);
    Mockito.when(mapper.toDomain(input, restaurantType))
        .thenReturn(restaurantDomainToSave);

    final Restaurant savedRestaurant = Mockito.mock(Restaurant.class);
    Mockito.when(savedRestaurant.getUuid())
        .thenReturn(restaurantUuid);
    Mockito.when(repository.save(restaurantDomainToSave))
        .thenReturn(savedRestaurant);

    final AddressOutput addressOutput = Mockito.mock(AddressOutput.class);
    Mockito.when(createRestaurantsAddressUseCase.execute(requestUser, restaurantUuid, addressInput))
        .thenReturn(addressOutput);

    final UsersOutput usersOutput = Mockito.mock(UsersOutput.class);
    Mockito.when(createUsersRestaurantUseCase.execute(usersUuid, restaurantUuid))
        .thenReturn(usersOutput);

    final RestaurantOpeningHoursOutput ohOut1 = Mockito.mock(RestaurantOpeningHoursOutput.class);
    final RestaurantOpeningHoursOutput ohOut2 = Mockito.mock(RestaurantOpeningHoursOutput.class);

    Mockito.when(createRestaurantOpeningHoursUseCase.execute(requestUser, restaurantUuid, oh1))
        .thenReturn(ohOut1);
    Mockito.when(createRestaurantOpeningHoursUseCase.execute(requestUser, restaurantUuid, oh2))
        .thenReturn(ohOut2);

    final RestaurantsOutput finalOutput = Mockito.mock(RestaurantsOutput.class);
    Mockito.when(mapper.toOutput(
            Mockito.eq(savedRestaurant),
            Mockito.eq(List.of(ohOut1, ohOut2)),
            Mockito.eq(addressOutput),
            Mockito.eq(usersOutput)
        ))
        .thenReturn(finalOutput);

    final var result = useCase.execute(requestUser, input);

    Assertions.assertNotNull(result);
    Assertions.assertSame(finalOutput, result);

    Mockito.verify(guard, Mockito.times(1))
        .requireRole(requestUser, UsersType.OWNER.name());

    Mockito.verify(userGateway, Mockito.times(1))
        .findByUuid(usersUuid);

    Mockito.verify(restaurantTypeGateway, Mockito.times(1))
        .findById(restaurantsTypeUuid);

    Mockito.verify(mapper, Mockito.times(1))
        .toDomain(input, restaurantType);

    Mockito.verify(repository, Mockito.times(1))
        .save(restaurantDomainToSave);

    Mockito.verify(createRestaurantsAddressUseCase, Mockito.times(1))
        .execute(requestUser, restaurantUuid, addressInput);

    Mockito.verify(createUsersRestaurantUseCase, Mockito.times(1))
        .execute(usersUuid, restaurantUuid);

    Mockito.verify(createRestaurantOpeningHoursUseCase, Mockito.times(1))
        .execute(requestUser, restaurantUuid, oh1);

    Mockito.verify(createRestaurantOpeningHoursUseCase, Mockito.times(1))
        .execute(requestUser, restaurantUuid, oh2);

    Mockito.verify(mapper, Mockito.times(1))
        .toOutput(savedRestaurant, List.of(ohOut1, ohOut2), addressOutput, usersOutput);

    Mockito.verifyNoMoreInteractions(
        guard,
        userGateway,
        restaurantTypeGateway,
        mapper,
        repository,
        createRestaurantsAddressUseCase,
        createUsersRestaurantUseCase,
        createRestaurantOpeningHoursUseCase
    );
  }
}
