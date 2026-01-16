package com.connectfood.infrastructure.persistence.adapter;

import java.util.UUID;

import com.connectfood.core.domain.model.Address;
import com.connectfood.infrastructure.persistence.adapter.AddressGatewayAdapter;
import com.connectfood.infrastructure.persistence.entity.AddressEntity;
import com.connectfood.infrastructure.persistence.jpa.JpaAddressRepository;
import com.connectfood.infrastructure.persistence.mappers.AddressInfraMapper;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class AddressGatewayAdapterTest {

  @Mock
  private JpaAddressRepository repository;

  @Mock
  private AddressInfraMapper mapper;

  @InjectMocks
  private AddressGatewayAdapter adapter;

  @Test
  @DisplayName("Deve salvar e retornar o model mapeado")
  void saveShouldPersistAndReturnMappedModel() {
    final Address model = Mockito.mock(Address.class);

    final AddressEntity entityToSave = Mockito.mock(AddressEntity.class);
    final AddressEntity savedEntity = Mockito.mock(AddressEntity.class);

    final Address mappedDomain = Mockito.mock(Address.class);

    Mockito.when(mapper.toEntity(model))
        .thenReturn(entityToSave);
    Mockito.when(repository.save(entityToSave))
        .thenReturn(savedEntity);
    Mockito.when(mapper.toDomain(savedEntity))
        .thenReturn(mappedDomain);

    final Address result = adapter.save(model);

    Assertions.assertSame(mappedDomain, result);

    Mockito.verify(mapper, Mockito.times(1))
        .toEntity(model);
    Mockito.verify(repository, Mockito.times(1))
        .save(entityToSave);
    Mockito.verify(mapper, Mockito.times(1))
        .toDomain(savedEntity);
    Mockito.verifyNoMoreInteractions(repository, mapper);
  }

  @Test
  @DisplayName("Deve deletar pelo uuid")
  void deleteShouldDeleteByUuid() {
    final UUID uuid = UUID.randomUUID();

    adapter.delete(uuid);

    Mockito.verify(repository, Mockito.times(1))
        .deleteByUuid(uuid);
    Mockito.verifyNoInteractions(mapper);
    Mockito.verifyNoMoreInteractions(repository);
  }
}
