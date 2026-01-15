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
import com.connectfood.core.domain.repository.RestaurantItemsImagesRepository;
import com.connectfood.core.domain.repository.RestaurantItemsRepository;
import com.connectfood.core.domain.repository.RestaurantsRepository;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class CreateRestaurantItemsUseCase {

  private final RestaurantItemsRepository repository;
  private final RestaurantItemsAppMapper mapper;
  private final RequestUserGuard guard;
  private final RestaurantsRepository restaurantsRepository;
  private final RestaurantItemsImagesAppMapper restaurantItemsImagesMapper;
  private final RestaurantItemsImagesRepository restaurantItemsImagesRepository;

  public CreateRestaurantItemsUseCase(
      final RestaurantItemsRepository repository,
      final RestaurantItemsAppMapper mapper,
      final RequestUserGuard guard,
      final RestaurantsRepository restaurantsRepository,
      final RestaurantItemsImagesAppMapper restaurantItemsImagesMapper,
      final RestaurantItemsImagesRepository restaurantItemsImagesRepository) {
    this.repository = repository;
    this.mapper = mapper;
    this.guard = guard;
    this.restaurantsRepository = restaurantsRepository;
    this.restaurantItemsImagesMapper = restaurantItemsImagesMapper;
    this.restaurantItemsImagesRepository = restaurantItemsImagesRepository;
  }

  @Transactional
  public RestaurantItemsOutput execute(final RequestUser requestUser, final RestaurantItemsInput input) {
    guard.requireRole(requestUser, "OWNER");

    final var restaurants =
        restaurantsRepository.findByUuid(input.getRestaurantUuid())
            .orElseThrow(() -> new NotFoundException("Restaurant not found"));

    var model = repository.save(mapper.toDomain(input, restaurants));

    final List<RestaurantItemsImagesInput> imagesInput = input.getImages() == null ? List.of() : input.getImages();
    List<RestaurantItemsImages> images = new ArrayList<>();

    for (var image : imagesInput) {
      images.add(restaurantItemsImagesRepository.save(model.getUuid(), restaurantItemsImagesMapper.toDomain(image)));
    }

    return mapper.toOutput(model, images);
  }
}
