package com.connectfood.core.domain.repository;

import com.connectfood.core.domain.model.UserRestaurant;

public interface UserRestaurantGateway {

  UserRestaurant save(UserRestaurant userRestaurant);
}
