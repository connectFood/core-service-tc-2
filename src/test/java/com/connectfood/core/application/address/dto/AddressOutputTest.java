package com.connectfood.core.application.address.dto;

import java.util.UUID;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class AddressOutputTest {

  @Test
  @DisplayName("Deve criar AddressOutput com todos os campos preenchidos corretamente")
  void shouldCreateAddressOutputWithAllFields() {
    final UUID uuid = UUID.randomUUID();
    final String street = "Rua A";
    final String number = "123";
    final String complement = "Apto 10";
    final String neighborhood = "Centro";
    final String city = "São Paulo";
    final String state = "SP";
    final String country = "Brasil";
    final String zipCode = "01000-000";

    final AddressOutput output = new AddressOutput(
        uuid,
        street,
        number,
        complement,
        neighborhood,
        city,
        state,
        country,
        zipCode
    );

    Assertions.assertEquals(uuid, output.getUuid());
    Assertions.assertEquals(street, output.getStreet());
    Assertions.assertEquals(number, output.getNumber());
    Assertions.assertEquals(complement, output.getComplement());
    Assertions.assertEquals(neighborhood, output.getNeighborhood());
    Assertions.assertEquals(city, output.getCity());
    Assertions.assertEquals(state, output.getState());
    Assertions.assertEquals(country, output.getCountry());
    Assertions.assertEquals(zipCode, output.getZipCode());
  }

  @Test
  @DisplayName("Deve permitir campos opcionais nulos")
  void shouldAllowNullOptionalFields() {
    final UUID uuid = UUID.randomUUID();

    final AddressOutput output = new AddressOutput(
        uuid,
        "Rua B",
        "456",
        null,
        null,
        "Rio de Janeiro",
        "RJ",
        "Brasil",
        null
    );

    Assertions.assertEquals(uuid, output.getUuid());
    Assertions.assertEquals("Rua B", output.getStreet());
    Assertions.assertEquals("456", output.getNumber());
    Assertions.assertNull(output.getComplement());
    Assertions.assertNull(output.getNeighborhood());
    Assertions.assertEquals("Rio de Janeiro", output.getCity());
    Assertions.assertEquals("RJ", output.getState());
    Assertions.assertEquals("Brasil", output.getCountry());
    Assertions.assertNull(output.getZipCode());
  }
}
