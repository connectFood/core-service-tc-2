package com.connectfood.core.application.restaurants.usecase;

import java.util.Optional;
import java.util.UUID;

import com.connectfood.core.application.address.dto.AddressInput;
import com.connectfood.core.application.address.dto.AddressOutput;
import com.connectfood.core.application.address.mapper.AddressAppMapper;
import com.connectfood.core.application.restaurants.mapper.RestaurantsAddressAppMapper;
import com.connectfood.core.domain.exception.NotFoundException;
import com.connectfood.core.domain.model.Address;
import com.connectfood.core.domain.model.Restaurants;
import com.connectfood.core.domain.model.RestaurantsAddress;
import com.connectfood.core.domain.repository.AddressRepository;
import com.connectfood.core.domain.repository.RestaurantsAddressRepository;
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
class CreateRestaurantsAddressUseCaseTest {

  @Mock
  private AddressRepository repository;

  @Mock
  private AddressAppMapper mapper;

  @Mock
  private RestaurantsRepository restaurantsRepository;

  @Mock
  private RestaurantsAddressRepository restaurantsAddressRepository;

  @Mock
  private RestaurantsAddressAppMapper restaurantsAddressMapper;

  @InjectMocks
  private CreateRestaurantsAddressUseCase useCase;

  @Test
  @DisplayName("Deve criar endereço e vincular ao restaurante quando restaurante existir")
  void shouldCreateAddressAndLinkToRestaurantWhenRestaurantExists() {
    final var restaurantUuid = UUID.randomUUID();

    final AddressInput input = Mockito.mock(AddressInput.class);

    final Restaurants restaurants = Mockito.mock(Restaurants.class);
    Mockito.when(restaurantsRepository.findByUuid(restaurantUuid))
        .thenReturn(Optional.of(restaurants));

    final Address addressDomainToSave = Mockito.mock(Address.class);
    Mockito.when(mapper.toDomain(input))
        .thenReturn(addressDomainToSave);

    final Address savedAddress = Mockito.mock(Address.class);
    Mockito.when(repository.save(addressDomainToSave))
        .thenReturn(savedAddress);

    final RestaurantsAddress restaurantsAddressDomain = Mockito.mock(RestaurantsAddress.class);
    Mockito.when(restaurantsAddressMapper.toDomain(restaurants, savedAddress))
        .thenReturn(restaurantsAddressDomain);

    final RestaurantsAddress savedRestaurantsAddress = Mockito.mock(RestaurantsAddress.class);
    Mockito.when(restaurantsAddressRepository.save(restaurantsAddressDomain))
        .thenReturn(savedRestaurantsAddress);

    final Address linkedAddress = Mockito.mock(Address.class);
    Mockito.when(savedRestaurantsAddress.getAddress())
        .thenReturn(linkedAddress);

    final AddressOutput output = Mockito.mock(AddressOutput.class);
    Mockito.when(mapper.toOutput(linkedAddress))
        .thenReturn(output);

    final var result = useCase.execute(restaurantUuid, input);

    Assertions.assertNotNull(result);
    Assertions.assertSame(output, result);

    Mockito.verify(restaurantsRepository, Mockito.times(1))
        .findByUuid(restaurantUuid);
    Mockito.verify(mapper, Mockito.times(1))
        .toDomain(input);
    Mockito.verify(repository, Mockito.times(1))
        .save(addressDomainToSave);
    Mockito.verify(restaurantsAddressMapper, Mockito.times(1))
        .toDomain(restaurants, savedAddress);
    Mockito.verify(restaurantsAddressRepository, Mockito.times(1))
        .save(restaurantsAddressDomain);
    Mockito.verify(mapper, Mockito.times(1))
        .toOutput(linkedAddress);
    Mockito.verifyNoMoreInteractions(
        restaurantsRepository, mapper, repository, restaurantsAddressMapper, restaurantsAddressRepository
    );
  }

  @Test
  @DisplayName("Deve lançar NotFoundException quando restaurante não existir")
  void shouldThrowNotFoundExceptionWhenRestaurantDoesNotExist() {
    final var restaurantUuid = UUID.randomUUID();
    final AddressInput input = Mockito.mock(AddressInput.class);

    Mockito.when(restaurantsRepository.findByUuid(restaurantUuid))
        .thenReturn(Optional.empty());

    final var ex = Assertions.assertThrows(
        NotFoundException.class,
        () -> useCase.execute(restaurantUuid, input)
    );

    Assertions.assertEquals("Restaurant not found", ex.getMessage());

    Mockito.verify(restaurantsRepository, Mockito.times(1))
        .findByUuid(restaurantUuid);
    Mockito.verifyNoInteractions(mapper, repository, restaurantsAddressMapper, restaurantsAddressRepository);
    Mockito.verifyNoMoreInteractions(restaurantsRepository);
  }
}
