package com.connectfood.core.application.restaurantitems.usecase;

import java.util.ArrayList;
import java.util.List;

import com.connectfood.core.application.restaurantitems.dto.RestaurantItemsImagesInput;
import com.connectfood.core.application.restaurantitems.dto.RestaurantItemsInput;
import com.connectfood.core.application.restaurantitems.dto.RestaurantItemsOutput;
import com.connectfood.core.application.restaurantitems.mapper.RestaurantItemsAppMapper;
import com.connectfood.core.application.restaurantitems.mapper.RestaurantItemsImagesAppMapper;
import com.connectfood.core.application.security.RequestUser;
import com.connectfood.core.application.security.RequestUserGuard;
import com.connectfood.core.domain.exception.NotFoundException;
import com.connectfood.core.domain.model.RestaurantItemsImages;
import com.connectfood.core.domain.repository.RestaurantItemsImagesGateway;
import com.connectfood.core.domain.repository.RestaurantItemsGateway;
import com.connectfood.core.domain.repository.RestaurantsGateway;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class CreateRestaurantItemsUseCase {

  private final RestaurantItemsGateway repository;
  private final RestaurantItemsAppMapper mapper;
  private final RequestUserGuard guard;
  private final RestaurantsGateway restaurantsGateway;
  private final RestaurantItemsImagesAppMapper restaurantItemsImagesMapper;
  private final RestaurantItemsImagesGateway restaurantItemsImagesGateway;

  public CreateRestaurantItemsUseCase(
      final RestaurantItemsGateway repository,
      final RestaurantItemsAppMapper mapper,
      final RequestUserGuard guard,
      final RestaurantsGateway restaurantsGateway,
      final RestaurantItemsImagesAppMapper restaurantItemsImagesMapper,
      final RestaurantItemsImagesGateway restaurantItemsImagesGateway) {
    this.repository = repository;
    this.mapper = mapper;
    this.guard = guard;
    this.restaurantsGateway = restaurantsGateway;
    this.restaurantItemsImagesMapper = restaurantItemsImagesMapper;
    this.restaurantItemsImagesGateway = restaurantItemsImagesGateway;
  }

  @Transactional
  public RestaurantItemsOutput execute(final RequestUser requestUser, final RestaurantItemsInput input) {
    guard.requireRole(requestUser, "OWNER");

    final var restaurants =
        restaurantsGateway.findByUuid(input.getRestaurantUuid())
            .orElseThrow(() -> new NotFoundException("Restaurant not found"));

    var model = repository.save(mapper.toDomain(input, restaurants));

    final List<RestaurantItemsImagesInput> imagesInput = input.getImages() == null ? List.of() : input.getImages();
    List<RestaurantItemsImages> images = new ArrayList<>();

    for (var image : imagesInput) {
      images.add(restaurantItemsImagesGateway.save(model.getUuid(), restaurantItemsImagesMapper.toDomain(image)));
    }

    return mapper.toOutput(model, images);
  }
}
