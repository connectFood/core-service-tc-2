package com.connectfood.core.application.restaurantitems.mapper;

import java.util.List;
import java.util.UUID;

import com.connectfood.core.application.restaurantitems.dto.RestaurantItemsInput;
import com.connectfood.core.application.restaurantitems.dto.RestaurantItemsOutput;
import com.connectfood.core.application.restaurants.mapper.RestaurantsAppMapper;
import com.connectfood.core.domain.model.RestaurantItem;
import com.connectfood.core.domain.model.RestaurantItemImage;
import com.connectfood.core.domain.model.Restaurant;

import org.springframework.stereotype.Component;

@Component
public class RestaurantItemsAppMapper {

  private final RestaurantsAppMapper restaurantsMapper;
  private final RestaurantItemsImagesAppMapper restaurantItemsImagesMapper;

  public RestaurantItemsAppMapper(final RestaurantsAppMapper restaurantsMapper,
      final RestaurantItemsImagesAppMapper restaurantItemsImagesMapper) {
    this.restaurantsMapper = restaurantsMapper;
    this.restaurantItemsImagesMapper = restaurantItemsImagesMapper;
  }

  public RestaurantItem toDomain(final RestaurantItemsInput input, final Restaurant restaurant) {
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

  public RestaurantItem toDomain(final UUID uuid, final RestaurantItemsInput input, final Restaurant restaurant) {
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

  public RestaurantItemsOutput toOutput(final RestaurantItem model) {
    if (model == null) {
      return null;
    }

    return new RestaurantItemsOutput(
        model.getUuid(),
        model.getName(),
        model.getDescription(),
        model.getValue(),
        model.getRequestType(),
        model.getRestaurant() != null ? restaurantsMapper.toOutput(model.getRestaurant()) : null,
        model.getImages() != null ? model.getImages()
            .stream()
            .map(restaurantItemsImagesMapper::toOutput)
            .toList() : null
    );
  }

  public RestaurantItemsOutput toOutput(final RestaurantItem model, final List<RestaurantItemImage> images) {
    if (model == null) {
      return null;
    }

    return new RestaurantItemsOutput(
        model.getUuid(),
        model.getName(),
        model.getDescription(),
        model.getValue(),
        model.getRequestType(),
        model.getRestaurant() != null ? restaurantsMapper.toOutput(model.getRestaurant()) : null,
        images.stream()
            .map(restaurantItemsImagesMapper::toOutput)
            .toList()
    );
  }
}
