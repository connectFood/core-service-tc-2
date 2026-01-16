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
import com.connectfood.core.application.security.RequestUser;
import com.connectfood.core.application.security.RequestUserGuard;
import com.connectfood.core.domain.exception.NotFoundException;
import com.connectfood.core.domain.model.RestaurantItem;
import com.connectfood.core.domain.model.RestaurantItemImage;
import com.connectfood.core.domain.repository.RestaurantItemsImagesGateway;
import com.connectfood.core.domain.repository.RestaurantItemsGateway;

import org.springframework.stereotype.Component;

@Component
public class UpdateRestaurantItemsUseCase {

  private final RestaurantItemsGateway repository;
  private final RestaurantItemsAppMapper mapper;
  private final RequestUserGuard guard;
  private final RestaurantItemsImagesAppMapper restaurantItemsImagesMapper;
  private final RestaurantItemsImagesGateway restaurantItemsImagesGateway;

  public UpdateRestaurantItemsUseCase(
      final RestaurantItemsGateway repository,
      final RestaurantItemsAppMapper mapper,
      final RequestUserGuard guard,
      final RestaurantItemsImagesAppMapper restaurantItemsImagesMapper,
      final RestaurantItemsImagesGateway restaurantItemsImagesGateway) {
    this.repository = repository;
    this.mapper = mapper;
    this.guard = guard;
    this.restaurantItemsImagesMapper = restaurantItemsImagesMapper;
    this.restaurantItemsImagesGateway = restaurantItemsImagesGateway;
  }

  public RestaurantItemsOutput execute(final RequestUser requestUser, final UUID uuid,
      final RestaurantItemsInput input) {
    guard.requireRole(requestUser, "OWNER");

    final var model = repository.findByUuid(uuid)
        .orElseThrow(() -> new NotFoundException("Restaurant Items not found"));

    final var modelUpdated = repository.update(uuid, mapper.toDomain(uuid, input, model.getRestaurant()));

    final var images = syncImages(model, input);

    return mapper.toOutput(modelUpdated, images);
  }

  private List<RestaurantItemImage> syncImages(final RestaurantItem restaurantItem,
      final RestaurantItemsInput input) {
    final var currentByUuid = restaurantItem.getImages()
        .stream()
        .filter(img -> img.getUuid() != null)
        .collect(Collectors.toMap(RestaurantItemImage::getUuid, Function.identity(), (a, b) -> a));

    final var incomingUuids = new HashSet<UUID>();

    final var persistedImages = new ArrayList<RestaurantItemImage>();

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
          persistedImages.add(restaurantItemsImagesGateway.update(image.getUuid(), model));
        } else {
          persistedImages.add(current);
        }
      } else {
        persistedImages.add(restaurantItemsImagesGateway.save(restaurantItem.getUuid(), model));
      }
    }

    for (var current : restaurantItem.getImages()) {
      final var currentUuid = current.getUuid();
      if (currentUuid != null && !incomingUuids.contains(currentUuid)) {
        restaurantItemsImagesGateway.delete(currentUuid);
      }
    }

    return persistedImages;
  }
}
