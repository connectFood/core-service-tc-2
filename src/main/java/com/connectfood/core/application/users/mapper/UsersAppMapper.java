package com.connectfood.core.application.users.mapper;

import java.util.UUID;

import com.connectfood.core.application.address.dto.AddressOutput;
import com.connectfood.core.application.address.mapper.AddressAppMapper;
import com.connectfood.core.application.users.dto.UsersInput;
import com.connectfood.core.application.users.dto.UsersOutput;
import com.connectfood.core.application.usertype.mapper.UsersTypeAppMapper;
import com.connectfood.core.domain.model.Address;
import com.connectfood.core.domain.model.User;
import com.connectfood.core.domain.model.UserType;

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

  public User toDomain(final UsersInput input, final String passwordHash, final UserType userType) {
    if (input == null) {
      return null;
    }

    return new User(
        input.getFullName(),
        input.getEmail(),
        passwordHash,
        userType
    );
  }

  public User toDomain(final UUID uuid, final UsersInput input, final String passwordHash, final UserType userType) {
    if (input == null) {
      return null;
    }

    return new User(
        uuid,
        input.getFullName(),
        input.getEmail(),
        passwordHash,
        userType
    );
  }

  public UsersOutput toOutput(final User model) {
    if (model == null) {
      return null;
    }

    return new UsersOutput(
        model.getUuid(),
        model.getFullName(),
        model.getEmail(),
        model.getUserType() != null ? usersTypeMapper.toOutput(model.getUserType()) : null
    );
  }

  public UsersOutput toOutput(final User model, final AddressOutput addressOutput) {
    if (model == null) {
      return null;
    }

    return new UsersOutput(
        model.getUuid(),
        model.getFullName(),
        model.getEmail(),
        model.getUserType() != null ? usersTypeMapper.toOutput(model.getUserType()) : null,
        addressOutput
    );
  }

  public UsersOutput toOutput(final User model, final Address address) {
    if (model == null || address == null) {
      return null;
    }

    return new UsersOutput(
        model.getUuid(),
        model.getFullName(),
        model.getEmail(),
        model.getUserType() != null ? usersTypeMapper.toOutput(model.getUserType()) : null,
        addressMapper.toOutput(address)
    );
  }
}
