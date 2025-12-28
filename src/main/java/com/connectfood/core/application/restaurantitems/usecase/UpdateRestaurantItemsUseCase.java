package com.connectfood.core.application.restaurantitems.usecase;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.connectfood.core.application.restaurantitems.dto.RestaurantItemsInput;
import com.connectfood.core.application.restaurantitems.dto.RestaurantItemsOutput;
import com.connectfood.core.application.restaurantitems.mapper.RestaurantItemsAppMapper;
import com.connectfood.core.application.restaurantitems.mapper.RestaurantItemsImagesAppMapper;
import com.connectfood.core.domain.exception.NotFoundException;
import com.connectfood.core.domain.model.RestaurantItems;
import com.connectfood.core.domain.model.RestaurantItemsImages;
import com.connectfood.core.domain.repository.RestaurantItemsImagesRepository;
import com.connectfood.core.domain.repository.RestaurantItemsRepository;

import org.springframework.stereotype.Component;

@Component
public class UpdateRestaurantItemsUseCase {

  private final RestaurantItemsRepository repository;
  private final RestaurantItemsAppMapper mapper;
  private final RestaurantItemsImagesAppMapper restaurantItemsImagesMapper;
  private final RestaurantItemsImagesRepository restaurantItemsImagesRepository;

  public UpdateRestaurantItemsUseCase(
      final RestaurantItemsRepository repository,
      final RestaurantItemsAppMapper mapper,
      final RestaurantItemsImagesAppMapper restaurantItemsImagesMapper,
      final RestaurantItemsImagesRepository restaurantItemsImagesRepository) {
    this.repository = repository;
    this.mapper = mapper;
    this.restaurantItemsImagesMapper = restaurantItemsImagesMapper;
    this.restaurantItemsImagesRepository = restaurantItemsImagesRepository;
  }

  public RestaurantItemsOutput execute(final UUID uuid, final RestaurantItemsInput input) {
    final var model = repository.findByUuid(uuid)
        .orElseThrow(() -> new NotFoundException("Restaurant Items not found"));

    final var modelUpdated = repository.update(uuid, mapper.toDomain(uuid, input, model.getRestaurant()));

    final var images = syncImages(model, input);

    return mapper.toOutput(modelUpdated, images);
  }

  private List<RestaurantItemsImages> syncImages(final RestaurantItems restaurantItems,
      final RestaurantItemsInput input) {
    final var currentByUuid = restaurantItems.getImages()
        .stream()
        .filter(img -> img.getUuid() != null)
        .collect(Collectors.toMap(RestaurantItemsImages::getUuid, Function.identity(), (a, b) -> a));

    final var incomingUuids = new HashSet<UUID>();

    final var persistedImages = new ArrayList<RestaurantItemsImages>();

    for (var image : input.getImages()) {
      final var model = restaurantItemsImagesMapper.toDomain(image);

      if (image.getUuid() != null) {
        incomingUuids.add(image.getUuid());

        final var current = currentByUuid.get(image.getUuid());
        if (current == null) {
          throw new NotFoundException("Restaurant Item Images not found");
        }

        final var changed = !Objects.equals(current.getName(), image.getName()) || !Objects.equals(
            current.getDescription(), image.getDescription());

        if (changed) {
          persistedImages.add(restaurantItemsImagesRepository.update(image.getUuid(), model));
        } else {
          persistedImages.add(current);
        }
      } else {
        persistedImages.add(restaurantItemsImagesRepository.save(restaurantItems.getUuid(), model));
      }
    }

    for (var current : restaurantItems.getImages()) {
      final var currentUuid = current.getUuid();
      if (currentUuid != null && !incomingUuids.contains(currentUuid)) {
        restaurantItemsImagesRepository.delete(currentUuid);
      }
    }

    return persistedImages;
  }
}
