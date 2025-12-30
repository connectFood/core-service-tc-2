package com.connectfood.core.infrastructure.persistence.mappers;

import java.util.UUID;

import com.connectfood.core.domain.model.Address;
import com.connectfood.core.infrastructure.persistence.entity.AddressEntity;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class AddressInfraMapperTest {

  private final AddressInfraMapper mapper = new AddressInfraMapper();

  @Test
  @DisplayName("Deve criar o mapper pelo construtor padrão")
  void shouldCreateMapperUsingDefaultConstructor() {
    final var instance = new AddressInfraMapper();

    Assertions.assertNotNull(instance);
  }

  @Test
  @DisplayName("Deve retornar null ao converter entity null para domínio")
  void shouldReturnNullWhenToDomainReceivesNull() {
    final Address result = mapper.toDomain(null);

    Assertions.assertNull(result);
  }

  @Test
  @DisplayName("Deve converter entity para domínio corretamente")
  void shouldConvertEntityToDomainCorrectly() {
    final var uuid = UUID.randomUUID();

    final var entity = new AddressEntity();
    entity.setUuid(uuid);
    entity.setStreet("Av. Paulista");
    entity.setNumber("1000");
    entity.setComplement("Apto 101");
    entity.setNeighborhood("Bela Vista");
    entity.setCity("São Paulo");
    entity.setState("SP");
    entity.setCountry("BR");
    entity.setZipCode("01310-100");

    final Address result = mapper.toDomain(entity);

    Assertions.assertNotNull(result);
    Assertions.assertEquals(uuid, result.getUuid());
    Assertions.assertEquals("Av. Paulista", result.getStreet());
    Assertions.assertEquals("1000", result.getNumber());
    Assertions.assertEquals("Apto 101", result.getComplement());
    Assertions.assertEquals("Bela Vista", result.getNeighborhood());
    Assertions.assertEquals("São Paulo", result.getCity());
    Assertions.assertEquals("SP", result.getState());
    Assertions.assertEquals("BR", result.getCountry());
    Assertions.assertEquals("01310-100", result.getZipCode());
  }

  @Test
  @DisplayName("Deve retornar null ao converter model null para entity")
  void shouldReturnNullWhenToEntityReceivesNull() {
    final AddressEntity result = mapper.toEntity((Address) null);

    Assertions.assertNull(result);
  }

  @Test
  @DisplayName("Deve converter model para entity corretamente")
  void shouldConvertModelToEntityCorrectly() {
    final var uuid = UUID.randomUUID();

    final var model = new Address(
        uuid,
        "Rua A",
        "10",
        "Casa",
        "Centro",
        "Campinas",
        "SP",
        "BR",
        "13000-000"
    );

    final AddressEntity result = mapper.toEntity(model);

    Assertions.assertNotNull(result);
    Assertions.assertEquals(uuid, result.getUuid());
    Assertions.assertEquals("Rua A", result.getStreet());
    Assertions.assertEquals("10", result.getNumber());
    Assertions.assertEquals("Casa", result.getComplement());
    Assertions.assertEquals("Centro", result.getNeighborhood());
    Assertions.assertEquals("Campinas", result.getCity());
    Assertions.assertEquals("SP", result.getState());
    Assertions.assertEquals("BR", result.getCountry());
    Assertions.assertEquals("13000-000", result.getZipCode());
  }

  @Test
  @DisplayName("Deve atualizar a entity existente com base no model")
  void shouldUpdateExistingEntityUsingModel() {
    final var uuidOriginal = UUID.randomUUID();
    final var entity = new AddressEntity();
    entity.setUuid(uuidOriginal);
    entity.setStreet("Old Street");
    entity.setNumber("1");
    entity.setComplement("Old");
    entity.setNeighborhood("Old Neighborhood");
    entity.setCity("Old City");
    entity.setState("OS");
    entity.setCountry("OC");
    entity.setZipCode("00000-000");

    final var model = new Address(
        UUID.randomUUID(),
        "New Street",
        "999",
        "New Complement",
        "New Neighborhood",
        "New City",
        "NS",
        "NC",
        "99999-999"
    );

    final AddressEntity result = mapper.toEntity(model, entity);

    Assertions.assertSame(entity, result);
    Assertions.assertEquals(uuidOriginal, result.getUuid());
    Assertions.assertEquals("New Street", result.getStreet());
    Assertions.assertEquals("999", result.getNumber());
    Assertions.assertEquals("New Complement", result.getComplement());
    Assertions.assertEquals("New Neighborhood", result.getNeighborhood());
    Assertions.assertEquals("New City", result.getCity());
    Assertions.assertEquals("NS", result.getState());
    Assertions.assertEquals("NC", result.getCountry());
    Assertions.assertEquals("99999-999", result.getZipCode());
  }
}
