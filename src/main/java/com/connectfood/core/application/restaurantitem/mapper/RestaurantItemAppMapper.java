package com.connectfood.core.application.restaurantitem.mapper;

import java.util.List;
import java.util.UUID;

import com.connectfood.core.application.restaurantitem.dto.RestaurantItemInput;
import com.connectfood.core.application.restaurantitem.dto.RestaurantItemOutput;
import com.connectfood.core.application.restaurant.mapper.RestaurantAppMapper;
import com.connectfood.core.domain.model.RestaurantItem;
import com.connectfood.core.domain.model.RestaurantItemImage;
import com.connectfood.core.domain.model.Restaurant;

import org.springframework.stereotype.Component;

@Component
public class RestaurantItemAppMapper {

  private final RestaurantAppMapper restaurantsMapper;
  private final RestaurantItemImageAppMapper restaurantItemsImagesMapper;

  public RestaurantItemAppMapper(final RestaurantAppMapper restaurantsMapper,
                                 final RestaurantItemImageAppMapper restaurantItemsImagesMapper) {
    this.restaurantsMapper = restaurantsMapper;
    this.restaurantItemsImagesMapper = restaurantItemsImagesMapper;
  }

  public RestaurantItem toDomain(final RestaurantItemInput input, final Restaurant restaurant) {
    if (input == null || restaurant == null) {
      return null;
    }

    return new RestaurantItem(
        input.getName(),
        input.getDescription(),
        input.getValue(),
        input.getRequestType(),
        restaurant
    );
  }

  public RestaurantItem toDomain(final UUID uuid, final RestaurantItemInput input, final Restaurant restaurant) {
    if (input == null || restaurant == null) {
      return null;
    }

    return new RestaurantItem(
        uuid,
        input.getName(),
        input.getDescription(),
        input.getValue(),
        input.getRequestType(),
        restaurant
    );
  }

  public RestaurantItemOutput toOutput(final RestaurantItem model) {
    if (model == null) {
      return null;
    }

    return new RestaurantItemOutput(
        model.getUuid(),
        model.getName(),
        model.getDescription(),
        model.getValue(),
        model.getRequestType(),
        model.getRestaurant() != null ? restaurantsMapper.toOutputAll(model.getRestaurant()) : null,
        model.getImages() != null ? model.getImages()
            .stream()
            .map(restaurantItemsImagesMapper::toOutput)
            .toList() : null
    );
  }

  public RestaurantItemOutput toOutput(final RestaurantItem model, final List<RestaurantItemImage> images) {
    if (model == null) {
      return null;
    }

    return new RestaurantItemOutput(
        model.getUuid(),
        model.getName(),
        model.getDescription(),
        model.getValue(),
        model.getRequestType(),
        model.getRestaurant() != null ? restaurantsMapper.toOutputAll(model.getRestaurant()) : null,
        images.stream()
            .map(restaurantItemsImagesMapper::toOutput)
            .toList()
    );
  }
}
