package com.connectfood.core.domain.model;

import java.util.UUID;

import com.connectfood.core.domain.exception.BadRequestException;

import lombok.Getter;

@Getter
public class UsersRestaurant {

    private final UUID uuid;
    private final Users user;
    private final Restaurants restaurant;

    public UsersRestaurant(final UUID uuid, final Users user, final Restaurants restaurant) {
        if (user == null) {
            throw new BadRequestException("User is required");
        }

        if (restaurant == null) {
            throw new BadRequestException("Restaurant is required");
        }

        this.uuid = (uuid == null) ? UUID.randomUUID() : uuid;
        this.user = user;
        this.restaurant = restaurant;
    }

    public UsersRestaurant(final Users user, final Restaurants restaurant) {
        this(null, user, restaurant);
    }
}
