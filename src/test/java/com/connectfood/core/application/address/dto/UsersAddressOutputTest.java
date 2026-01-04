package com.connectfood.core.application.address.dto;

import java.util.UUID;

import com.connectfood.core.application.users.dto.UsersOutput;
import com.connectfood.core.application.usertype.dto.UsersTypeOutput;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class UsersAddressOutputTest {

  @Test
  @DisplayName("Deve criar UsersAddressOutput com todos os campos preenchidos corretamente")
  void shouldCreateUsersAddressOutputWithAllFields() {
    final UUID uuid = UUID.randomUUID();
    final String street = "Rua A";
    final String number = "123";
    final String complement = "Apto 10";
    final String neighborhood = "Centro";
    final String city = "São Paulo";
    final String state = "SP";
    final String country = "Brasil";
    final String zipCode = "01000-000";

    final UsersTypeOutput usersType = new UsersTypeOutput(
        UUID.randomUUID(),
        "ADMIN",
        "Administrador"
    );

    final UsersOutput users = new UsersOutput(
        UUID.randomUUID(),
        "Lucas Santos",
        "lucas@email.com",
        usersType
    );

    final UsersAddressOutput output = new UsersAddressOutput(
        uuid,
        street,
        number,
        complement,
        neighborhood,
        city,
        state,
        country,
        zipCode,
        users
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
    Assertions.assertEquals(users, output.getUsers());
  }

  @Test
  @DisplayName("Deve permitir campos opcionais nulos")
  void shouldAllowNullOptionalFields() {
    final UUID uuid = UUID.randomUUID();

    final UsersAddressOutput output = new UsersAddressOutput(
        uuid,
        "Rua B",
        "456",
        null,
        null,
        "Rio de Janeiro",
        "RJ",
        "Brasil",
        null,
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
    Assertions.assertNull(output.getUsers());
  }
}
