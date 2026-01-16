package com.connectfood.infrastructure.persistence.adapter;

import com.connectfood.core.domain.model.UsersRestaurant;
import com.connectfood.core.domain.repository.UsersRestaurantGateway;
import com.connectfood.infrastructure.persistence.jpa.JpaRestaurantsRepository;
import com.connectfood.infrastructure.persistence.jpa.JpaUsersRepository;
import com.connectfood.infrastructure.persistence.jpa.JpaUsersRestaurantRepository;
import com.connectfood.infrastructure.persistence.mappers.UsersRestaurantInfraMapper;

import org.springframework.stereotype.Repository;

@Repository
public class UsersRestaurantGatewayAdapter implements UsersRestaurantGateway {

  private final JpaUsersRestaurantRepository repository;
  private final UsersRestaurantInfraMapper mapper;
  private final JpaUsersRepository usersRepository;
  private final JpaRestaurantsRepository restaurantsRepository;

  public UsersRestaurantGatewayAdapter(final JpaUsersRestaurantRepository repository,
                                       final UsersRestaurantInfraMapper mapper,
                                       final JpaUsersRepository usersRepository,
                                       final JpaRestaurantsRepository restaurantsRepository) {
    this.repository = repository;
    this.mapper = mapper;
    this.usersRepository = usersRepository;
    this.restaurantsRepository = restaurantsRepository;
  }

  @Override
  public UsersRestaurant save(final UsersRestaurant usersAddress) {
    final var users = usersRepository.findByUuid(usersAddress.getUser()
            .getUuid())
        .orElseThrow();
    final var address = restaurantsRepository.findByUuid(usersAddress.getRestaurant()
            .getUuid())
        .orElseThrow();

    final var entity = repository.save(mapper.toEntity(usersAddress.getUuid(), users, address));

    return mapper.toDomain(entity);
  }
}
