package com.connectfood.core.infrastructure.persistence.mappers;

import com.connectfood.core.domain.model.Address;
import com.connectfood.core.infrastructure.persistence.entity.AddressEntity;

import org.springframework.stereotype.Component;

@Component
public class AddressInfraMapper {


  public AddressInfraMapper() {
  }

  public Address toDomain(final AddressEntity entity) {
    if (entity == null) {
      return null;
    }

    return new Address(
        entity.getUuid(),
        entity.getStreet(),
        entity.getNumber(),
        entity.getComplement(),
        entity.getNeighborhood(),
        entity.getCity(),
        entity.getState(),
        entity.getCountry(),
        entity.getZipCode()
    );
  }

  public AddressEntity toEntity(final Address model) {
    if (model == null) {
      return null;
    }

    var entity = new AddressEntity();
    return setEntity(entity, model);
  }

  public AddressEntity toEntity(final Address model, final AddressEntity entity) {
    return setEntity(entity, model);
  }

  private AddressEntity setEntity(final AddressEntity entity, final Address model) {
    entity.setStreet(model.getStreet());
    entity.setNumber(model.getNumber());
    entity.setComplement(model.getComplement());
    entity.setNeighborhood(model.getNeighborhood());
    entity.setCity(model.getCity());
    entity.setState(model.getState());
    entity.setCountry(model.getCountry());
    entity.setZipCode(model.getZipCode());
    return entity;
  }
}
