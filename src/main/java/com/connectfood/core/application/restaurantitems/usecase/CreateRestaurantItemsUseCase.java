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
import com.connectfood.core.domain.model.RestaurantItemImage;
import com.connectfood.core.domain.repository.RestaurantItemImageGateway;
import com.connectfood.core.domain.repository.RestaurantItemGateway;
import com.connectfood.core.domain.repository.RestaurantGateway;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class CreateRestaurantItemsUseCase {

  private final RestaurantItemGateway repository;
  private final RestaurantItemsAppMapper mapper;
  private final RequestUserGuard guard;
  private final RestaurantGateway restaurantGateway;
  private final RestaurantItemsImagesAppMapper restaurantItemsImagesMapper;
  private final RestaurantItemImageGateway restaurantItemImageGateway;

  public CreateRestaurantItemsUseCase(
      final RestaurantItemGateway repository,
      final RestaurantItemsAppMapper mapper,
      final RequestUserGuard guard,
      final RestaurantGateway restaurantGateway,
      final RestaurantItemsImagesAppMapper restaurantItemsImagesMapper,
      final RestaurantItemImageGateway restaurantItemImageGateway) {
    this.repository = repository;
    this.mapper = mapper;
    this.guard = guard;
    this.restaurantGateway = restaurantGateway;
    this.restaurantItemsImagesMapper = restaurantItemsImagesMapper;
    this.restaurantItemImageGateway = restaurantItemImageGateway;
  }

  @Transactional
  public RestaurantItemsOutput execute(final RequestUser requestUser, final RestaurantItemsInput input) {
    guard.requireRole(requestUser, "OWNER");

    final var restaurants =
        restaurantGateway.findByUuid(input.getRestaurantUuid())
            .orElseThrow(() -> new NotFoundException("Restaurant not found"));

    var model = repository.save(mapper.toDomain(input, restaurants));

    final List<RestaurantItemsImagesInput> imagesInput = input.getImages() == null ? List.of() : input.getImages();
    List<RestaurantItemImage> images = new ArrayList<>();

    for (var image : imagesInput) {
      images.add(restaurantItemImageGateway.save(model.getUuid(), restaurantItemsImagesMapper.toDomain(image)));
    }

    return mapper.toOutput(model, images);
  }
}
