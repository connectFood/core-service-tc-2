package com.connectfood.core.application.users.mapper;

import java.util.UUID;

import com.connectfood.core.application.address.mapper.AddressAppMapper;
import com.connectfood.core.application.users.dto.UsersInput;
import com.connectfood.core.application.users.dto.UsersOutput;
import com.connectfood.core.application.usertype.mapper.UsersTypeAppMapper;
import com.connectfood.core.domain.model.Address;
import com.connectfood.core.domain.model.Users;
import com.connectfood.core.domain.model.UsersType;

import org.springframework.stereotype.Component;

@Component
public class UsersAppMapper {

  private final UsersTypeAppMapper usersTypeMapper;
  private final AddressAppMapper addressMapper;

  public UsersAppMapper(
      final UsersTypeAppMapper usersTypeMapper,
      final AddressAppMapper addressMapper) {
    this.usersTypeMapper = usersTypeMapper;
    this.addressMapper = addressMapper;
  }

  public Users toDomain(final UsersInput input, final String passwordHash, final UsersType usersType) {
    if (input == null) {
      return null;
    }

    return new Users(
        input.getFullName(),
        input.getEmail(),
        passwordHash,
        usersType
    );
  }

  public Users toDomain(final UUID uuid, final UsersInput input, final String passwordHash, final UsersType usersType) {
    if (input == null) {
      return null;
    }

    return new Users(
        uuid,
        input.getFullName(),
        input.getEmail(),
        passwordHash,
        usersType
    );
  }

  public UsersOutput toOutput(final Users model) {
    if (model == null) {
      return null;
    }

    return new UsersOutput(
        model.getUuid(),
        model.getFullName(),
        model.getEmail(),
        model.getUsersType() != null ? usersTypeMapper.toOutput(model.getUsersType()) : null
    );
  }

  public UsersOutput toOutput(final Users model, final Address address) {
    if (model == null || address == null) {
      return null;
    }

    return new UsersOutput(
        model.getUuid(),
        model.getFullName(),
        model.getEmail(),
        model.getUsersType() != null ? usersTypeMapper.toOutput(model.getUsersType()) : null,
        addressMapper.toOutput(address)
    );
  }
}
