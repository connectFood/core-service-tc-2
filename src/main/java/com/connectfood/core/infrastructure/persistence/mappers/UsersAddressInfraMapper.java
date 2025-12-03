package com.connectfood.core.infrastructure.persistence.mappers;

import com.connectfood.core.domain.model.UsersAddress;
import com.connectfood.core.infrastructure.persistence.entity.UsersAddressEntity;

import org.springframework.stereotype.Component;

@Component
public class UsersAddressInfraMapper {

  private final UsersInfraMapper usersMapper;
  private final AddressInfraMapper addressMapper;

  public UsersAddressInfraMapper(final UsersInfraMapper usersMapper, final AddressInfraMapper addressMapper) {
    this.usersMapper = usersMapper;
    this.addressMapper = addressMapper;
  }

  public UsersAddress toDomain(final UsersAddressEntity entity) {
    if (entity == null) {
      return null;
    }

    return new UsersAddress(
        entity.getUuid(),
        usersMapper.toDomain(entity.getUsers()),
        addressMapper.toDomain(entity.getAddress())
    );
  }

  public UsersAddressEntity toEntity(final UsersAddress model) {
    if (model == null) {
      return null;
    }

    var entity = new UsersAddressEntity();
//    entity.setUsers(usersMapper.toEntity(model.getUsers()));
    entity.setAddress(addressMapper.toEntity(model.getAddress()));
    return entity;
  }
}
