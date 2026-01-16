package com.connectfood.infrastructure.persistence.mappers;

import com.connectfood.core.domain.model.RestaurantsAddress;

import com.connectfood.infrastructure.persistence.entity.AddressEntity;
import com.connectfood.infrastructure.persistence.entity.RestaurantAddressEntity;

import com.connectfood.infrastructure.persistence.entity.RestaurantEntity;

import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class RestaurantAddressInfraMapper {

  private final RestaurantInfraMapper restaurantsMapper;
  private final AddressInfraMapper addressMapper;

  public RestaurantAddressInfraMapper(final RestaurantInfraMapper restaurantsMapper,
                                      final AddressInfraMapper addressMapper) {
    this.restaurantsMapper = restaurantsMapper;
    this.addressMapper = addressMapper;
  }

  public RestaurantsAddress toDomain(final RestaurantAddressEntity entity) {
    if(entity == null) {
      return null;
    }

    return new RestaurantsAddress(
        entity.getUuid(),
        restaurantsMapper.toDomain(entity.getRestaurants()),
        addressMapper.toDomain(entity.getAddress())
    );
  }

  public RestaurantAddressEntity toEntity(final UUID uuid, final RestaurantEntity restaurantEntity, final AddressEntity addressEntity) {

    if(restaurantEntity == null || addressEntity == null) {
      return null;
    }

    var entity = new RestaurantAddressEntity();
    entity.setUuid(uuid);
    entity.setRestaurants(restaurantEntity);
    entity.setAddress(addressEntity);

    return entity;
  }
}
