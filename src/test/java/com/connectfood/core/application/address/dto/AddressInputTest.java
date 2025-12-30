package com.connectfood.core.application.address.dto;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class AddressInputTest {

  @Test
  @DisplayName("Deve criar AddressInput com todos os campos preenchidos corretamente")
  void shouldCreateAddressInputWithAllFields() {
    final var street = "Rua A";
    final var number = "123";
    final var complement = "Apto 10";
    final var neighborhood = "Centro";
    final var city = "São Paulo";
    final var state = "SP";
    final var country = "Brasil";
    final var zipCode = "01000-000";

    final AddressInput input = new AddressInput(
        street,
        number,
        complement,
        neighborhood,
        city,
        state,
        country,
        zipCode
    );

    Assertions.assertEquals(street, input.getStreet());
    Assertions.assertEquals(number, input.getNumber());
    Assertions.assertEquals(complement, input.getComplement());
    Assertions.assertEquals(neighborhood, input.getNeighborhood());
    Assertions.assertEquals(city, input.getCity());
    Assertions.assertEquals(state, input.getState());
    Assertions.assertEquals(country, input.getCountry());
    Assertions.assertEquals(zipCode, input.getZipCode());
  }

  @Test
  @DisplayName("Deve permitir campos opcionais nulos")
  void shouldAllowNullOptionalFields() {
    final AddressInput input = new AddressInput(
        "Rua B",
        "456",
        null,
        null,
        "Rio de Janeiro",
        "RJ",
        "Brasil",
        null
    );

    Assertions.assertEquals("Rua B", input.getStreet());
    Assertions.assertEquals("456", input.getNumber());
    Assertions.assertNull(input.getComplement());
    Assertions.assertNull(input.getNeighborhood());
    Assertions.assertEquals("Rio de Janeiro", input.getCity());
    Assertions.assertEquals("RJ", input.getState());
    Assertions.assertEquals("Brasil", input.getCountry());
    Assertions.assertNull(input.getZipCode());
  }
}
