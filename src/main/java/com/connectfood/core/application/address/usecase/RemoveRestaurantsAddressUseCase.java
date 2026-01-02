package com.connectfood.core.application.address.usecase;

import com.connectfood.core.domain.exception.NotFoundException;
import com.connectfood.core.domain.repository.AddressRepository;
import com.connectfood.core.domain.repository.RestaurantsAddressRepository;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Component
public class RemoveRestaurantsAddressUseCase {

  private final RestaurantsAddressRepository repository;
  private final AddressRepository addressRepository;

  public RemoveRestaurantsAddressUseCase(
      final RestaurantsAddressRepository repository,
      final AddressRepository addressRepository) {
    this.repository = repository;
    this.addressRepository = addressRepository;
  }

  @Transactional
  public void execute(final UUID uuid) {
    final var model = repository.findByRestaurantsUuid(uuid)
        .orElseThrow(() -> new NotFoundException("Restaurants Address Not Found"));

    repository.delete(model.getUuid());

    addressRepository.delete(model.getAddress().getUuid());
  }
}
