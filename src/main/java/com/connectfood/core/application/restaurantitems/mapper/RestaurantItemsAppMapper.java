package com.connectfood.core.application.restaurantitems.mapper;

import java.util.List;
import java.util.UUID;

import com.connectfood.core.application.restaurantitems.dto.RestaurantItemsInput;
import com.connectfood.core.application.restaurantitems.dto.RestaurantItemsOutput;
import com.connectfood.core.application.restaurants.mapper.RestaurantsAppMapper;
import com.connectfood.core.domain.model.RestaurantItems;
import com.connectfood.core.domain.model.RestaurantItemsImages;
import com.connectfood.core.domain.model.Restaurants;

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

  public RestaurantItems toDomain(final RestaurantItemsInput input, final Restaurants restaurants) {
    if (input == null || restaurants == null) {
      return null;
    }

    return new RestaurantItems(
        input.getName(),
        input.getDescription(),
        input.getValue(),
        input.getRequestType(),
        restaurants
    );
  }

  public RestaurantItems toDomain(final UUID uuid, final RestaurantItemsInput input, final Restaurants restaurants) {
    if (input == null || restaurants == null) {
      return null;
    }

    return new RestaurantItems(
        uuid,
        input.getName(),
        input.getDescription(),
        input.getValue(),
        input.getRequestType(),
        restaurants
    );
  }

  public RestaurantItemsOutput toOutput(final RestaurantItems model) {
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

  public RestaurantItemsOutput toOutput(final RestaurantItems model, final List<RestaurantItemsImages> images) {
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
