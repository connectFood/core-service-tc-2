package com.connectfood.core.domain.model;

import java.util.UUID;

import com.connectfood.core.domain.exception.BadRequestException;

import lombok.Getter;

@Getter
public class UserRestaurant {

    private final UUID uuid;
    private final User user;
    private final Restaurant restaurant;

    public UserRestaurant(final UUID uuid, final User user, final Restaurant restaurant) {
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

    public UserRestaurant(final User user, final Restaurant restaurant) {
        this(null, user, restaurant);
    }
}
