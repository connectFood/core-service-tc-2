package com.connectfood.infrastructure.persistence.mappers;

import com.connectfood.core.domain.model.RestaurantsAddress;

import com.connectfood.infrastructure.persistence.entity.AddressEntity;
import com.connectfood.infrastructure.persistence.entity.RestaurantsAddressEntity;

import com.connectfood.infrastructure.persistence.entity.RestaurantsEntity;

import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class RestaurantsAddressInfraMapper {

  private final RestaurantsInfraMapper restaurantsMapper;
  private final AddressInfraMapper addressMapper;

  public RestaurantsAddressInfraMapper(final RestaurantsInfraMapper restaurantsMapper,
      final AddressInfraMapper addressMapper) {
    this.restaurantsMapper = restaurantsMapper;
    this.addressMapper = addressMapper;
  }

  public RestaurantsAddress toDomain(final RestaurantsAddressEntity entity) {
    if(entity == null) {
      return null;
    }

    return new RestaurantsAddress(
        entity.getUuid(),
        restaurantsMapper.toDomain(entity.getRestaurants()),
        addressMapper.toDomain(entity.getAddress())
    );
  }

  public RestaurantsAddressEntity toEntity(final UUID uuid, final RestaurantsEntity restaurantsEntity, final AddressEntity addressEntity) {

    if(restaurantsEntity == null || addressEntity == null) {
      return null;
    }

    var entity = new RestaurantsAddressEntity();
    entity.setUuid(uuid);
    entity.setRestaurants(restaurantsEntity);
    entity.setAddress(addressEntity);

    return entity;
  }
}
