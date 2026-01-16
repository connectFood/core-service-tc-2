package com.connectfood.core.application.restaurantitem.usecase;

import java.util.ArrayList;
import java.util.List;

import com.connectfood.core.application.restaurantitem.dto.RestaurantItemImageInput;
import com.connectfood.core.application.restaurantitem.dto.RestaurantItemInput;
import com.connectfood.core.application.restaurantitem.dto.RestaurantItemOutput;
import com.connectfood.core.application.restaurantitem.mapper.RestaurantItemAppMapper;
import com.connectfood.core.application.restaurantitem.mapper.RestaurantItemImageAppMapper;
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
public class CreateRestaurantItemUseCase {

  private final RestaurantItemGateway repository;
  private final RestaurantItemAppMapper mapper;
  private final RequestUserGuard guard;
  private final RestaurantGateway restaurantGateway;
  private final RestaurantItemImageAppMapper restaurantItemsImagesMapper;
  private final RestaurantItemImageGateway restaurantItemImageGateway;

  public CreateRestaurantItemUseCase(
      final RestaurantItemGateway repository,
      final RestaurantItemAppMapper mapper,
      final RequestUserGuard guard,
      final RestaurantGateway restaurantGateway,
      final RestaurantItemImageAppMapper restaurantItemsImagesMapper,
      final RestaurantItemImageGateway restaurantItemImageGateway) {
    this.repository = repository;
    this.mapper = mapper;
    this.guard = guard;
    this.restaurantGateway = restaurantGateway;
    this.restaurantItemsImagesMapper = restaurantItemsImagesMapper;
    this.restaurantItemImageGateway = restaurantItemImageGateway;
  }

  @Transactional
  public RestaurantItemOutput execute(final RequestUser requestUser, final RestaurantItemInput input) {
    guard.requireRole(requestUser, "OWNER");

    final var restaurants =
        restaurantGateway.findByUuid(input.getRestaurantUuid())
            .orElseThrow(() -> new NotFoundException("Restaurant not found"));

    var model = repository.save(mapper.toDomain(input, restaurants));

    final List<RestaurantItemImageInput> imagesInput = input.getImages() == null ? List.of() : input.getImages();
    List<RestaurantItemImage> images = new ArrayList<>();

    for (var image : imagesInput) {
      images.add(restaurantItemImageGateway.save(model.getUuid(), restaurantItemsImagesMapper.toDomain(image)));
    }

    return mapper.toOutput(model, images);
  }
}
