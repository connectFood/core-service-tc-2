package com.connectfood.core.entrypoint.rest.mappers;

import java.util.UUID;

import com.connectfood.core.application.address.dto.AddressInput;
import com.connectfood.core.application.address.dto.AddressOutput;
import com.connectfood.core.entrypoint.rest.dto.address.AddressRequest;
import com.connectfood.core.entrypoint.rest.dto.address.AddressResponse;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class AddressEntryMapperTest {

  private final AddressEntryMapper mapper = new AddressEntryMapper();

  @Test
  @DisplayName("Não deve converter para AddressInput quando request for null")
  void shouldReturnNullWhenRequestIsNull() {
    final AddressInput result = mapper.toInput(null);

    Assertions.assertNull(result);
  }

  @Test
  @DisplayName("Deve converter AddressRequest para AddressInput quando dados forem válidos")
  void shouldConvertRequestToInputWhenDataIsValid() {
    final AddressRequest request = buildRequest();

    final AddressInput result = mapper.toInput(request);

    Assertions.assertNotNull(result);
    Assertions.assertEquals("Street", result.getStreet());
    Assertions.assertEquals("123", result.getNumber());
    Assertions.assertEquals("Apt 10", result.getComplement());
    Assertions.assertEquals("Downtown", result.getNeighborhood());
    Assertions.assertEquals("City", result.getCity());
    Assertions.assertEquals("State", result.getState());
    Assertions.assertEquals("Country", result.getCountry());
    Assertions.assertEquals("00000-000", result.getZipCode());
  }

  @Test
  @DisplayName("Não deve converter para AddressResponse quando output for null")
  void shouldReturnNullWhenOutputIsNull() {
    final AddressResponse result = mapper.toResponse(null);

    Assertions.assertNull(result);
  }

  @Test
  @DisplayName("Deve converter AddressOutput para AddressResponse quando dados forem válidos")
  void shouldConvertOutputToResponseWhenDataIsValid() {
    final AddressOutput output = buildOutput();

    final AddressResponse result = mapper.toResponse(output);

    Assertions.assertNotNull(result);
    Assertions.assertEquals(output.getUuid(), result.getUuid());
    Assertions.assertEquals("Street", result.getStreet());
    Assertions.assertEquals("123", result.getNumber());
    Assertions.assertEquals("Apt 10", result.getComplement());
    Assertions.assertEquals("Downtown", result.getNeighborhood());
    Assertions.assertEquals("City", result.getCity());
    Assertions.assertEquals("State", result.getState());
    Assertions.assertEquals("Country", result.getCountry());
    Assertions.assertEquals("00000-000", result.getZipCode());
  }

  private AddressRequest buildRequest() {
    return new AddressRequest(
        "Street",
        "123",
        "Apt 10",
        "Downtown",
        "City",
        "State",
        "Country",
        "00000-000"
    );
  }

  private AddressOutput buildOutput() {
    return new AddressOutput(
        UUID.randomUUID(),
        "Street",
        "123",
        "Apt 10",
        "Downtown",
        "City",
        "State",
        "Country",
        "00000-000"
    );
  }
}
