package com.connectfood.core.application.address.mapper;

import java.util.UUID;

import com.connectfood.core.application.address.dto.AddressInput;
import com.connectfood.core.application.address.dto.AddressOutput;
import com.connectfood.core.domain.model.Address;

import org.springframework.stereotype.Component;

@Component
public class AddressAppMapper {

  public AddressAppMapper() {
  }

  public Address toDomain(final AddressInput input) {
    if (input == null) {
      return null;
    }

    return new Address(
        input.getStreet(),
        input.getNumber(),
        input.getComplement(),
        input.getNeighborhood(),
        input.getCity(),
        input.getState(),
        input.getCountry(),
        input.getZipCode()
    );
  }

  public Address toDomain(final UUID uuid, final AddressInput input) {
    if (input == null) {
      return null;
    }

    return new Address(
        uuid,
        input.getStreet(),
        input.getNumber(),
        input.getComplement(),
        input.getNeighborhood(),
        input.getCity(),
        input.getState(),
        input.getCountry(),
        input.getZipCode()
    );
  }

  public AddressOutput toOutput(final Address model) {
    if (model == null) {
      return null;
    }

    return new AddressOutput(
        model.getUuid(),
        model.getStreet(),
        model.getNumber(),
        model.getComplement(),
        model.getNeighborhood(),
        model.getCity(),
        model.getState(),
        model.getCountry(),
        model.getZipCode()
    );
  }
}
