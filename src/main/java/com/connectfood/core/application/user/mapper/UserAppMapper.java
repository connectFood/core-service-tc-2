package com.connectfood.core.application.user.mapper;

import java.util.UUID;

import com.connectfood.core.application.address.dto.AddressOutput;
import com.connectfood.core.application.address.mapper.AddressAppMapper;
import com.connectfood.core.application.user.dto.UserInput;
import com.connectfood.core.application.user.dto.UserOutput;
import com.connectfood.core.application.usertype.mapper.UserTypeAppMapper;
import com.connectfood.core.domain.model.Address;
import com.connectfood.core.domain.model.User;
import com.connectfood.core.domain.model.UserType;

import org.springframework.stereotype.Component;

@Component
public class UserAppMapper {

  private final UserTypeAppMapper usersTypeMapper;
  private final AddressAppMapper addressMapper;

  public UserAppMapper(
      final UserTypeAppMapper usersTypeMapper,
      final AddressAppMapper addressMapper) {
    this.usersTypeMapper = usersTypeMapper;
    this.addressMapper = addressMapper;
  }

  public User toDomain(final UserInput input, final String passwordHash, final UserType userType) {
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

  public User toDomain(final UUID uuid, final UserInput input, final String passwordHash, final UserType userType) {
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

  public UserOutput toOutput(final User model) {
    if (model == null) {
      return null;
    }

    return new UserOutput(
        model.getUuid(),
        model.getFullName(),
        model.getEmail(),
        model.getUserType() != null ? usersTypeMapper.toOutput(model.getUserType()) : null
    );
  }

  public UserOutput toOutput(final User model, final AddressOutput addressOutput) {
    if (model == null) {
      return null;
    }

    return new UserOutput(
        model.getUuid(),
        model.getFullName(),
        model.getEmail(),
        model.getUserType() != null ? usersTypeMapper.toOutput(model.getUserType()) : null,
        addressOutput
    );
  }

  public UserOutput toOutput(final User model, final Address address) {
    if (model == null || address == null) {
      return null;
    }

    return new UserOutput(
        model.getUuid(),
        model.getFullName(),
        model.getEmail(),
        model.getUserType() != null ? usersTypeMapper.toOutput(model.getUserType()) : null,
        addressMapper.toOutput(address)
    );
  }
}
