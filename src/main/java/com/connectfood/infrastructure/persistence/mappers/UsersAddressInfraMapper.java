package com.connectfood.infrastructure.persistence.mappers;

import java.util.UUID;

import com.connectfood.core.domain.model.UserAddress;
import com.connectfood.infrastructure.persistence.entity.AddressEntity;
import com.connectfood.infrastructure.persistence.entity.UsersAddressEntity;
import com.connectfood.infrastructure.persistence.entity.UsersEntity;

import org.springframework.stereotype.Component;

@Component
public class UsersAddressInfraMapper {

  private final UsersInfraMapper usersMapper;
  private final AddressInfraMapper addressMapper;

  public UsersAddressInfraMapper(final UsersInfraMapper usersMapper, final AddressInfraMapper addressMapper) {
    this.usersMapper = usersMapper;
    this.addressMapper = addressMapper;
  }

  public UserAddress toDomain(final UsersAddressEntity entity) {
    if (entity == null) {
      return null;
    }

    return new UserAddress(
        entity.getUuid(),
        usersMapper.toDomain(entity.getUsers()),
        addressMapper.toDomain(entity.getAddress())
    );
  }

  public UsersAddressEntity toEntity(final UUID uuid, final UsersEntity usersEntity,
      final AddressEntity addressEntity) {
    if (usersEntity == null || addressEntity == null) {
      return null;
    }

    var entity = new UsersAddressEntity();
    entity.setUuid(uuid);
    entity.setUsers(usersEntity);
    entity.setAddress(addressEntity);
    return entity;
  }
}
