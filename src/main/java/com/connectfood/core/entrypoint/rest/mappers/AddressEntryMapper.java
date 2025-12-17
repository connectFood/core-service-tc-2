package com.connectfood.core.entrypoint.rest.mappers;

import com.connectfood.core.application.address.dto.AddressInput;
import com.connectfood.core.application.address.dto.AddressOutput;
import com.connectfood.core.entrypoint.rest.dto.address.AddressRequest;
import com.connectfood.core.entrypoint.rest.dto.address.AddressResponse;

import org.springframework.stereotype.Component;

@Component
public class AddressEntryMapper {

  public AddressEntryMapper() {
  }

  public AddressInput toInput(final AddressRequest request) {
    if (request == null) {
      return null;
    }

    return new AddressInput(
        request.getStreet(),
        request.getNumber(),
        request.getComplement(),
        request.getNeighborhood(),
        request.getCity(),
        request.getState(),
        request.getCountry(),
        request.getZipCode()
    );
  }

  public AddressResponse toResponse(final AddressOutput output) {
    if (output == null) {
      return null;
    }

    return new AddressResponse(
        output.getUuid(),
        output.getStreet(),
        output.getNumber(),
        output.getComplement(),
        output.getNeighborhood(),
        output.getCity(),
        output.getState(),
        output.getCountry(),
        output.getZipCode()
    );
  }
}
