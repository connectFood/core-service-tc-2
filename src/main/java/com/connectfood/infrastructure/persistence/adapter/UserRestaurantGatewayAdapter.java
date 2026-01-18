package com.connectfood.infrastructure.persistence.adapter;

import java.util.UUID;

import com.connectfood.core.domain.model.UserRestaurant;
import com.connectfood.core.domain.repository.UserRestaurantGateway;
import com.connectfood.infrastructure.persistence.jpa.JpaRestaurantRepository;
import com.connectfood.infrastructure.persistence.jpa.JpaUserRepository;
import com.connectfood.infrastructure.persistence.jpa.JpaUserRestaurantRepository;
import com.connectfood.infrastructure.persistence.mappers.UserRestaurantInfraMapper;

import org.springframework.stereotype.Repository;

@Repository
public class UserRestaurantGatewayAdapter implements UserRestaurantGateway {

  private final JpaUserRestaurantRepository repository;
  private final UserRestaurantInfraMapper mapper;
  private final JpaUserRepository usersRepository;
  private final JpaRestaurantRepository restaurantsRepository;

  public UserRestaurantGatewayAdapter(final JpaUserRestaurantRepository repository,
      final UserRestaurantInfraMapper mapper,
      final JpaUserRepository usersRepository,
      final JpaRestaurantRepository restaurantsRepository) {
    this.repository = repository;
    this.mapper = mapper;
    this.usersRepository = usersRepository;
    this.restaurantsRepository = restaurantsRepository;
  }

  @Override
  public UserRestaurant save(final UserRestaurant usersAddress) {
    final var users = usersRepository.findByUuid(usersAddress.getUser()
            .getUuid())
        .orElseThrow();
    final var address = restaurantsRepository.findByUuid(usersAddress.getRestaurant()
            .getUuid())
        .orElseThrow();

    final var entity = repository.save(mapper.toEntity(usersAddress.getUuid(), users, address));

    return mapper.toDomain(entity);
  }

  @Override
  public boolean existsByRestaurantsUuid(final UUID restaurantUuid) {
    return repository.existsByRestaurantsUuid(restaurantUuid);
  }

  @Override
  public void deleteByRestaurantsUuid(final UUID restaurantUuid) {
    repository.deleteByRestaurantsUuid(restaurantUuid);
  }
}
