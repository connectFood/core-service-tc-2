package com.connectfood.core.application.restaurantitems.usecase;

import java.util.ArrayList;
import java.util.List;

import com.connectfood.core.application.restaurantitems.dto.RestaurantItemsInput;
import com.connectfood.core.application.restaurantitems.dto.RestaurantItemsOutput;
import com.connectfood.core.application.restaurantitems.mapper.RestaurantItemsAppMapper;
import com.connectfood.core.application.restaurantitems.mapper.RestaurantItemsImagesAppMapper;
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
  private final RestaurantsRepository restaurantsRepository;
  private final RestaurantItemsImagesAppMapper restaurantItemsImagesMapper;
  private final RestaurantItemsImagesRepository restaurantItemsImagesRepository;

  public CreateRestaurantItemsUseCase(
      final RestaurantItemsRepository repository,
      final RestaurantItemsAppMapper mapper,
      final RestaurantsRepository restaurantsRepository,
      final RestaurantItemsImagesAppMapper restaurantItemsImagesMapper,
      final RestaurantItemsImagesRepository restaurantItemsImagesRepository) {
    this.repository = repository;
    this.mapper = mapper;
    this.restaurantsRepository = restaurantsRepository;
    this.restaurantItemsImagesMapper = restaurantItemsImagesMapper;
    this.restaurantItemsImagesRepository = restaurantItemsImagesRepository;
  }

  @Transactional
  public RestaurantItemsOutput execute(final RestaurantItemsInput input) {
    final var restaurants =
        restaurantsRepository.findByUuid(input.getRestaurantUuid())
            .orElseThrow(() -> new NotFoundException("Restaurant not found"));

    var model = repository.save(mapper.toDomain(input, restaurants));

    List<RestaurantItemsImages> images = new ArrayList<>();

    for (var image : input.getImages()) {
      images.add(restaurantItemsImagesRepository.save(model.getUuid(), restaurantItemsImagesMapper.toDomain(image)));
    }

    return mapper.toOutput(model, images);
  }
}
