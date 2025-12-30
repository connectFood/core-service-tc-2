package com.connectfood.core.domain.model;

import java.util.UUID;

import com.connectfood.core.domain.exception.BadRequestException;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class AddressTest {

  @Test
  @DisplayName("Deve criar um endereço com UUID explícito e dados válidos")
  void shouldCreateAddressWithExplicitUuid() {
    final var uuid = UUID.randomUUID();

    final var street = "Av. Paulista";
    final var number = "1000";
    final var complement = "Apto 101";
    final var neighborhood = "Bela Vista";
    final var city = "São Paulo";
    final var state = "SP";
    final var country = "Brasil";
    final var zipCode = "01310-100";

    final var address = new Address(
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

    Assertions.assertEquals(uuid, address.getUuid());
    Assertions.assertEquals(street, address.getStreet());
    Assertions.assertEquals(number, address.getNumber());
    Assertions.assertEquals(complement, address.getComplement());
    Assertions.assertEquals(neighborhood, address.getNeighborhood());
    Assertions.assertEquals(city, address.getCity());
    Assertions.assertEquals(state, address.getState());
    Assertions.assertEquals(country, address.getCountry());
    Assertions.assertEquals(zipCode, address.getZipCode());
  }

  @Test
  @DisplayName("Deve criar um endereço sem UUID explícito e dados válidos")
  void shouldCreateAddressWithoutExplicitUuid() {
    final var street = "Av. Paulista";
    final var number = "1000";
    final var complement = "Apto 101";
    final var neighborhood = "Bela Vista";
    final var city = "São Paulo";
    final var state = "SP";
    final var country = "Brasil";
    final var zipCode = "01310-100";

    final var address = new Address(
        street,
        number,
        complement,
        neighborhood,
        city,
        state,
        country,
        zipCode
    );

    Assertions.assertNotNull(address.getUuid());
    Assertions.assertEquals(street, address.getStreet());
    Assertions.assertEquals(number, address.getNumber());
    Assertions.assertEquals(complement, address.getComplement());
    Assertions.assertEquals(neighborhood, address.getNeighborhood());
    Assertions.assertEquals(city, address.getCity());
    Assertions.assertEquals(state, address.getState());
    Assertions.assertEquals(country, address.getCountry());
    Assertions.assertEquals(zipCode, address.getZipCode());
  }

  @Test
  @DisplayName("Não deve criar um endereço sem street e lançar BadRequestException")
  void shouldNotCreateAddressWithoutStreetAndThrowBadRequestException() {
    final var exception = Assertions.assertThrows(
        BadRequestException.class,
        () -> new Address(
            UUID.randomUUID(),
            null,
            "1000",
            "Apto 101",
            "Bela Vista",
            "São Paulo",
            "SP",
            "Brasil",
            "01310-100"
        )
    );

    Assertions.assertEquals("Street is required", exception.getMessage());
  }

  @Test
  @DisplayName("Não deve criar um endereço com street em branco e lançar BadRequestException")
  void shouldNotCreateAddressWithStreetBlankAndThrowBadRequestException() {
    final var exception = Assertions.assertThrows(
        BadRequestException.class,
        () -> new Address(
            UUID.randomUUID(),
            "   ",
            "1000",
            "Apto 101",
            "Bela Vista",
            "São Paulo",
            "SP",
            "Brasil",
            "01310-100"
        )
    );

    Assertions.assertEquals("Street is required", exception.getMessage());
  }

  @Test
  @DisplayName("Não deve criar um endereço sem number e lançar BadRequestException")
  void shouldNotCreateAddressWithoutNumberAndThrowBadRequestException() {
    final var exception = Assertions.assertThrows(
        BadRequestException.class,
        () -> new Address(
            UUID.randomUUID(),
            "Av. Paulista",
            null,
            "Apto 101",
            "Bela Vista",
            "São Paulo",
            "SP",
            "Brasil",
            "01310-100"
        )
    );

    Assertions.assertEquals("Number is required", exception.getMessage());
  }

  @Test
  @DisplayName("Não deve criar um endereço com number em branco e lançar BadRequestException")
  void shouldNotCreateAddressWithNumberBlankAndThrowBadRequestException() {
    final var exception = Assertions.assertThrows(
        BadRequestException.class,
        () -> new Address(
            UUID.randomUUID(),
            "Av. Paulista",
            "",
            "Apto 101",
            "Bela Vista",
            "São Paulo",
            "SP",
            "Brasil",
            "01310-100"
        )
    );

    Assertions.assertEquals("Number is required", exception.getMessage());
  }

  @Test
  @DisplayName("Não deve criar um endereço sem neighborhood e lançar BadRequestException")
  void shouldNotCreateAddressWithoutNeighborhoodAndThrowBadRequestException() {
    final var exception = Assertions.assertThrows(
        BadRequestException.class,
        () -> new Address(
            UUID.randomUUID(),
            "Av. Paulista",
            "1000",
            "Apto 101",
            null,
            "São Paulo",
            "SP",
            "Brasil",
            "01310-100"
        )
    );

    Assertions.assertEquals("Neighborhood is required", exception.getMessage());
  }

  @Test
  @DisplayName("Não deve criar um endereço com neighborhood em branco e lançar BadRequestException")
  void shouldNotCreateAddressWithNeighborhoodBlankAndThrowBadRequestException() {
    final var exception = Assertions.assertThrows(
        BadRequestException.class,
        () -> new Address(
            UUID.randomUUID(),
            "Av. Paulista",
            "1000",
            "Apto 101",
            "   ",
            "São Paulo",
            "SP",
            "Brasil",
            "01310-100"
        )
    );

    Assertions.assertEquals("Neighborhood is required", exception.getMessage());
  }

  @Test
  @DisplayName("Não deve criar um endereço sem city e lançar BadRequestException")
  void shouldNotCreateAddressWithoutCityAndThrowBadRequestException() {
    final var exception = Assertions.assertThrows(
        BadRequestException.class,
        () -> new Address(
            UUID.randomUUID(),
            "Av. Paulista",
            "1000",
            "Apto 101",
            "Bela Vista",
            null,
            "SP",
            "Brasil",
            "01310-100"
        )
    );

    Assertions.assertEquals("City is required", exception.getMessage());
  }

  @Test
  @DisplayName("Não deve criar um endereço com city em branco e lançar BadRequestException")
  void shouldNotCreateAddressWithCityBlankAndThrowBadRequestException() {
    final var exception = Assertions.assertThrows(
        BadRequestException.class,
        () -> new Address(
            UUID.randomUUID(),
            "Av. Paulista",
            "1000",
            "Apto 101",
            "Bela Vista",
            " ",
            "SP",
            "Brasil",
            "01310-100"
        )
    );

    Assertions.assertEquals("City is required", exception.getMessage());
  }

  @Test
  @DisplayName("Não deve criar um endereço sem state e lançar BadRequestException")
  void shouldNotCreateAddressWithoutStateAndThrowBadRequestException() {
    final var exception = Assertions.assertThrows(
        BadRequestException.class,
        () -> new Address(
            UUID.randomUUID(),
            "Av. Paulista",
            "1000",
            "Apto 101",
            "Bela Vista",
            "São Paulo",
            null,
            "Brasil",
            "01310-100"
        )
    );

    Assertions.assertEquals("State is required", exception.getMessage());
  }

  @Test
  @DisplayName("Não deve criar um endereço com state em branco e lançar BadRequestException")
  void shouldNotCreateAddressWithStateBlankAndThrowBadRequestException() {
    final var exception = Assertions.assertThrows(
        BadRequestException.class,
        () -> new Address(
            UUID.randomUUID(),
            "Av. Paulista",
            "1000",
            "Apto 101",
            "Bela Vista",
            "São Paulo",
            "",
            "Brasil",
            "01310-100"
        )
    );

    Assertions.assertEquals("State is required", exception.getMessage());
  }

  @Test
  @DisplayName("Não deve criar um endereço sem country e lançar BadRequestException")
  void shouldNotCreateAddressWithoutCountryAndThrowBadRequestException() {
    final var exception = Assertions.assertThrows(
        BadRequestException.class,
        () -> new Address(
            UUID.randomUUID(),
            "Av. Paulista",
            "1000",
            "Apto 101",
            "Bela Vista",
            "São Paulo",
            "SP",
            null,
            "01310-100"
        )
    );

    Assertions.assertEquals("Country is required", exception.getMessage());
  }

  @Test
  @DisplayName("Não deve criar um endereço com country em branco e lançar BadRequestException")
  void shouldNotCreateAddressWithCountryBlankAndThrowBadRequestException() {
    final var exception = Assertions.assertThrows(
        BadRequestException.class,
        () -> new Address(
            UUID.randomUUID(),
            "Av. Paulista",
            "1000",
            "Apto 101",
            "Bela Vista",
            "São Paulo",
            "SP",
            "   ",
            "01310-100"
        )
    );

    Assertions.assertEquals("Country is required", exception.getMessage());
  }

  @Test
  @DisplayName("Não deve criar um endereço sem zipCode e lançar BadRequestException")
  void shouldNotCreateAddressWithoutZipCodeAndThrowBadRequestException() {
    final var exception = Assertions.assertThrows(
        BadRequestException.class,
        () -> new Address(
            UUID.randomUUID(),
            "Av. Paulista",
            "1000",
            "Apto 101",
            "Bela Vista",
            "São Paulo",
            "SP",
            "Brasil",
            null
        )
    );

    Assertions.assertEquals("Zip code is required", exception.getMessage());
  }

  @Test
  @DisplayName("Não deve criar um endereço com zipCode em branco e lançar BadRequestException")
  void shouldNotCreateAddressWithZipCodeBlankAndThrowBadRequestException() {
    final var exception = Assertions.assertThrows(
        BadRequestException.class,
        () -> new Address(
            UUID.randomUUID(),
            "Av. Paulista",
            "1000",
            "Apto 101",
            "Bela Vista",
            "São Paulo",
            "SP",
            "Brasil",
            ""
        )
    );

    Assertions.assertEquals("Zip code is required", exception.getMessage());
  }
}
