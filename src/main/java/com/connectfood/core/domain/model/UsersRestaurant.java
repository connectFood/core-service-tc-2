package com.connectfood.core.domain.model;

import java.util.UUID;

import com.connectfoodcore.domain.exception.BadRequestException;

import lombok.Getter;

@Getter
public class UsersRestaurant{
    
    private final UUID uuid;
    private final Users users;
    private final Restaurants restaurants;

    public UsersRestaurant(final UUID uuid, final Users users, final Restaurants restaurants) {

        if (users == null) {
            throw new BadRequestException("Users is required");
        }

        if (restaurants == null) {
            throw new BadRequestException("Restaurant is required");
        }

        this.uuid = uuid == null ? UUID.randomUUID() : uuid;
        this.users = users;
        this.restaurants = restaurants;
    }

    public UsersRestaurant(final Users users, final Restaurants restaurants) {
        this(null, users, restaurants);
    }
}