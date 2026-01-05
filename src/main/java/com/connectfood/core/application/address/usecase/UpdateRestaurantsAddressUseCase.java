package com.connectfood.core.application.address.usecase;

import java.util.UUID;

import com.connectfood.core.application.address.dto.AddressInput;
import com.connectfood.core.application.address.dto.RestaurantsAddressOutput;
import com.connectfood.core.application.address.mapper.AddressAppMapper;
import com.connectfood.core.application.address.mapper.RestaurantsAddressAppMapper;
import com.connectfood.core.domain.exception.NotFoundException;
import com.connectfood.core.domain.repository.AddressRepository;
import com.connectfood.core.domain.repository.RestaurantsAddressRepository;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class UpdateRestaurantsAddressUseCase {

  private final RestaurantsAddressRepository repository;
  private final RestaurantsAddressAppMapper mapper;
  private final AddressAppMapper addressMapper;
  private final AddressRepository addressRepository;

  public UpdateRestaurantsAddressUseCase(
      final RestaurantsAddressRepository repository,
      final RestaurantsAddressAppMapper mapper,
      final AddressAppMapper addressMapper,
      final AddressRepository addressRepository
  ) {
    this.repository = repository;
    this.mapper = mapper;
    this.addressMapper = addressMapper;
    this.addressRepository = addressRepository;
  }

  @Transactional
  public RestaurantsAddressOutput execute(
      final UUID uuid,
      final AddressInput addressInput
  ) {
    final var restaurantsAddress = repository.findByRestaurantsUuid(uuid)
        .orElseThrow(() -> new NotFoundException("Restaurants Address Not Found"));

    final var addressUpdated = addressRepository.update(uuid,
        addressMapper.toDomain(uuid, addressInput)
    );

    return null;
  }
}
