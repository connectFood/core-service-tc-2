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
import com.connectfood.core.application.users.dto.UsersOutput;
import com.connectfood.core.domain.exception.NotFoundException;
import com.connectfood.core.domain.model.Restaurants;
import com.connectfood.core.domain.model.RestaurantsType;
import com.connectfood.core.domain.model.Users;
import com.connectfood.core.domain.repository.RestaurantsRepository;
import com.connectfood.core.domain.repository.RestaurantsTypeRepository;
import com.connectfood.core.domain.repository.UsersRepository;

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
  private RestaurantsRepository repository;

  @Mock
  private RestaurantsAppMapper mapper;

  @Mock
  private RestaurantsTypeRepository restaurantsTypeRepository;

  @Mock
  private CreateRestaurantsAddressUseCase createRestaurantsAddressUseCase;

  @Mock
  private CreateRestaurantOpeningHoursUseCase createRestaurantOpeningHoursUseCase;

  @Mock
  private UsersRepository usersRepository;

  @Mock
  private CreateUsersRestaurantUseCase createUsersRestaurantUseCase;

  @InjectMocks
  private CreateRestaurantsUseCase useCase;

  @Test
  @DisplayName("Não deve criar restaurante quando usuário não existir")
  void shouldThrowExceptionWhenUserNotFound() {
    final var usersUuid = UUID.randomUUID();

    final RestaurantsInput input = Mockito.mock(RestaurantsInput.class);
    Mockito.when(input.getUsersUuid())
        .thenReturn(usersUuid);

    Mockito.when(usersRepository.findByUuid(usersUuid))
        .thenReturn(Optional.empty());

    final var ex = Assertions.assertThrows(
        NotFoundException.class,
        () -> useCase.execute(input)
    );

    Assertions.assertEquals("User not found", ex.getMessage());

    Mockito.verify(usersRepository, Mockito.times(1))
        .findByUuid(usersUuid);
    Mockito.verifyNoInteractions(
        restaurantsTypeRepository,
        repository,
        mapper,
        createRestaurantsAddressUseCase,
        createRestaurantOpeningHoursUseCase,
        createUsersRestaurantUseCase
    );
    Mockito.verifyNoMoreInteractions(usersRepository);
  }

  @Test
  @DisplayName("Não deve criar restaurante quando tipo de restaurante não existir")
  void shouldThrowExceptionWhenRestaurantsTypeNotFound() {
    final var usersUuid = UUID.randomUUID();
    final var restaurantsTypeUuid = UUID.randomUUID();

    final RestaurantsInput input = Mockito.mock(RestaurantsInput.class);
    Mockito.when(input.getUsersUuid())
        .thenReturn(usersUuid);
    Mockito.when(input.getRestaurantsTypeUuid())
        .thenReturn(restaurantsTypeUuid);

    final Users users = Mockito.mock(Users.class);
    Mockito.when(usersRepository.findByUuid(usersUuid))
        .thenReturn(Optional.of(users));

    Mockito.when(restaurantsTypeRepository.findById(restaurantsTypeUuid))
        .thenReturn(Optional.empty());

    final var ex = Assertions.assertThrows(
        NotFoundException.class,
        () -> useCase.execute(input)
    );

    Assertions.assertEquals("Restaurants Type not found", ex.getMessage());

    Mockito.verify(usersRepository, Mockito.times(1))
        .findByUuid(usersUuid);
    Mockito.verify(restaurantsTypeRepository, Mockito.times(1))
        .findById(restaurantsTypeUuid);

    Mockito.verifyNoInteractions(
        repository,
        mapper,
        createRestaurantsAddressUseCase,
        createRestaurantOpeningHoursUseCase,
        createUsersRestaurantUseCase
    );
    Mockito.verifyNoMoreInteractions(usersRepository, restaurantsTypeRepository);
  }

  @Test
  @DisplayName("Deve criar restaurante, endereço, vínculo usuário-restaurante e horários, retornando output final")
  void shouldCreateRestaurantWhenDataIsValid() {
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

    final Users users = Mockito.mock(Users.class);
    Mockito.when(users.getUuid())
        .thenReturn(usersUuid);
    Mockito.when(usersRepository.findByUuid(usersUuid))
        .thenReturn(Optional.of(users));

    final RestaurantsType restaurantsType = Mockito.mock(RestaurantsType.class);
    Mockito.when(restaurantsTypeRepository.findById(restaurantsTypeUuid))
        .thenReturn(Optional.of(restaurantsType));

    final Restaurants restaurantsDomainToSave = Mockito.mock(Restaurants.class);
    Mockito.when(mapper.toDomain(input, restaurantsType))
        .thenReturn(restaurantsDomainToSave);

    final Restaurants savedRestaurants = Mockito.mock(Restaurants.class);
    Mockito.when(savedRestaurants.getUuid())
        .thenReturn(restaurantUuid);
    Mockito.when(repository.save(restaurantsDomainToSave))
        .thenReturn(savedRestaurants);

    final AddressOutput addressOutput = Mockito.mock(AddressOutput.class);
    Mockito.when(createRestaurantsAddressUseCase.execute(restaurantUuid, addressInput))
        .thenReturn(addressOutput);

    final UsersOutput usersOutput = Mockito.mock(UsersOutput.class);
    Mockito.when(createUsersRestaurantUseCase.execute(usersUuid, restaurantUuid))
        .thenReturn(usersOutput);

    final RestaurantOpeningHoursOutput ohOut1 = Mockito.mock(RestaurantOpeningHoursOutput.class);
    final RestaurantOpeningHoursOutput ohOut2 = Mockito.mock(RestaurantOpeningHoursOutput.class);

    Mockito.when(createRestaurantOpeningHoursUseCase.execute(restaurantUuid, oh1))
        .thenReturn(ohOut1);
    Mockito.when(createRestaurantOpeningHoursUseCase.execute(restaurantUuid, oh2))
        .thenReturn(ohOut2);

    final RestaurantsOutput finalOutput = Mockito.mock(RestaurantsOutput.class);
    Mockito.when(mapper.toOutput(
            Mockito.eq(savedRestaurants),
            Mockito.eq(List.of(ohOut1, ohOut2)),
            Mockito.eq(addressOutput),
            Mockito.eq(usersOutput)
        ))
        .thenReturn(finalOutput);

    final var result = useCase.execute(input);

    Assertions.assertNotNull(result);
    Assertions.assertSame(finalOutput, result);

    Mockito.verify(usersRepository, Mockito.times(1))
        .findByUuid(usersUuid);
    Mockito.verify(restaurantsTypeRepository, Mockito.times(1))
        .findById(restaurantsTypeUuid);

    Mockito.verify(mapper, Mockito.times(1))
        .toDomain(input, restaurantsType);
    Mockito.verify(repository, Mockito.times(1))
        .save(restaurantsDomainToSave);

    Mockito.verify(createRestaurantsAddressUseCase, Mockito.times(1))
        .execute(restaurantUuid, addressInput);
    Mockito.verify(createUsersRestaurantUseCase, Mockito.times(1))
        .execute(usersUuid, restaurantUuid);

    Mockito.verify(createRestaurantOpeningHoursUseCase, Mockito.times(1))
        .execute(restaurantUuid, oh1);
    Mockito.verify(createRestaurantOpeningHoursUseCase, Mockito.times(1))
        .execute(restaurantUuid, oh2);

    Mockito.verify(mapper, Mockito.times(1))
        .toOutput(savedRestaurants, List.of(ohOut1, ohOut2), addressOutput, usersOutput);

    Mockito.verifyNoMoreInteractions(
        usersRepository,
        restaurantsTypeRepository,
        mapper,
        repository,
        createRestaurantsAddressUseCase,
        createUsersRestaurantUseCase,
        createRestaurantOpeningHoursUseCase
    );
  }
}
