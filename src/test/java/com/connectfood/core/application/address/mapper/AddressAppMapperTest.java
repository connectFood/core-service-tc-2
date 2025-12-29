package com.connectfood.core.application.address.mapper;

import java.util.UUID;

import com.connectfood.core.application.address.dto.AddressInput;
import com.connectfood.core.application.address.dto.AddressOutput;
import com.connectfood.core.domain.model.Address;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class AddressAppMapperTest {

  @InjectMocks
  private AddressAppMapper mapper;

  @Test
  @DisplayName("Deve retornar null quando input for null")
  void shouldReturnNullWhenInputIsNull() {
    final var result = mapper.toDomain((AddressInput) null);

    Assertions.assertNull(result);
  }

  @Test
  @DisplayName("Deve mapear input para domain corretamente")
  void shouldMapInputToDomain() {
    final var input = new AddressInput(
        "Rua A",
        "123",
        "Apto 10",
        "Centro",
        "São Paulo",
        "SP",
        "Brasil",
        "01000-000"
    );

    final var result = mapper.toDomain(input);

    Assertions.assertNotNull(result);
    Assertions.assertNotNull(result.getUuid()); // gerado no construtor do domínio
    Assertions.assertEquals("Rua A", result.getStreet());
    Assertions.assertEquals("123", result.getNumber());
    Assertions.assertEquals("Apto 10", result.getComplement());
    Assertions.assertEquals("Centro", result.getNeighborhood());
    Assertions.assertEquals("São Paulo", result.getCity());
    Assertions.assertEquals("SP", result.getState());
    Assertions.assertEquals("Brasil", result.getCountry());
    Assertions.assertEquals("01000-000", result.getZipCode());
  }

  @Test
  @DisplayName("Deve retornar null quando input for null com UUID informado")
  void shouldReturnNullWhenInputIsNullWithUuid() {
    final var uuid = UUID.randomUUID();

    final var result = mapper.toDomain(uuid, null);

    Assertions.assertNull(result);
  }

  @Test
  @DisplayName("Deve mapear input para domain corretamente com UUID informado")
  void shouldMapInputToDomainWithUuid() {
    final var uuid = UUID.randomUUID();

    final var input = new AddressInput(
        "Rua B",
        "456",
        null,
        "Bairro X",
        "Rio de Janeiro",
        "RJ",
        "Brasil",
        "20000-000"
    );

    final var result = mapper.toDomain(uuid, input);

    Assertions.assertNotNull(result);
    Assertions.assertEquals(uuid, result.getUuid());
    Assertions.assertEquals("Rua B", result.getStreet());
    Assertions.assertEquals("456", result.getNumber());
    Assertions.assertNull(result.getComplement());
    Assertions.assertEquals("Bairro X", result.getNeighborhood());
    Assertions.assertEquals("Rio de Janeiro", result.getCity());
    Assertions.assertEquals("RJ", result.getState());
    Assertions.assertEquals("Brasil", result.getCountry());
    Assertions.assertEquals("20000-000", result.getZipCode());
  }

  @Test
  @DisplayName("Deve retornar null quando model for null")
  void shouldReturnNullWhenModelIsNull() {
    final var result = mapper.toOutput(null);

    Assertions.assertNull(result);
  }

  @Test
  @DisplayName("Deve mapear domain para output corretamente")
  void shouldMapDomainToOutput() {
    final var uuid = UUID.randomUUID();

    final Address model = new Address(
        uuid,
        "Av. Paulista",
        "1000",
        "Conjunto 101",
        "Bela Vista",
        "São Paulo",
        "SP",
        "Brasil",
        "01310-100"
    );

    final AddressOutput result = mapper.toOutput(model);

    Assertions.assertNotNull(result);
    Assertions.assertEquals(uuid, result.getUuid());
    Assertions.assertEquals("Av. Paulista", result.getStreet());
    Assertions.assertEquals("1000", result.getNumber());
    Assertions.assertEquals("Conjunto 101", result.getComplement());
    Assertions.assertEquals("Bela Vista", result.getNeighborhood());
    Assertions.assertEquals("São Paulo", result.getCity());
    Assertions.assertEquals("SP", result.getState());
    Assertions.assertEquals("Brasil", result.getCountry());
    Assertions.assertEquals("01310-100", result.getZipCode());
  }
}
