package com.connectfood.infrastructure.persistence.mappers;

import java.util.UUID;

import com.connectfood.core.domain.model.UserAddress;
import com.connectfood.infrastructure.persistence.entity.AddressEntity;
import com.connectfood.infrastructure.persistence.entity.UserAddressEntity;
import com.connectfood.infrastructure.persistence.entity.UserEntity;

import org.springframework.stereotype.Component;

@Component
public class UserAddressInfraMapper {

  private final UserInfraMapper usersMapper;
  private final AddressInfraMapper addressMapper;

  public UserAddressInfraMapper(final UserInfraMapper usersMapper, final AddressInfraMapper addressMapper) {
    this.usersMapper = usersMapper;
    this.addressMapper = addressMapper;
  }

  public UserAddress toDomain(final UserAddressEntity entity) {
    if (entity == null) {
      return null;
    }

    return new UserAddress(
        entity.getUuid(),
        usersMapper.toDomain(entity.getUsers()),
        addressMapper.toDomain(entity.getAddress())
    );
  }

  public UserAddressEntity toEntity(final UUID uuid, final UserEntity userEntity,
                                    final AddressEntity addressEntity) {
    if (userEntity == null || addressEntity == null) {
      return null;
    }

    var entity = new UserAddressEntity();
    entity.setUuid(uuid);
    entity.setUsers(userEntity);
    entity.setAddress(addressEntity);
    return entity;
  }
}
