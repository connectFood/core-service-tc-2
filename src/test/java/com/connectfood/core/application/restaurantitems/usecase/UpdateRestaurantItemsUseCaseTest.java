package com.connectfood.core.application.restaurantitems.usecase;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.connectfood.core.application.restaurantitems.dto.RestaurantItemsImagesInput;
import com.connectfood.core.application.restaurantitems.dto.RestaurantItemsInput;
import com.connectfood.core.application.restaurantitems.dto.RestaurantItemsOutput;
import com.connectfood.core.application.restaurantitems.mapper.RestaurantItemsAppMapper;
import com.connectfood.core.application.restaurantitems.mapper.RestaurantItemsImagesAppMapper;
import com.connectfood.core.application.security.RequestUser;
import com.connectfood.core.application.security.RequestUserGuard;
import com.connectfood.core.domain.exception.NotFoundException;
import com.connectfood.core.domain.model.RestaurantItem;
import com.connectfood.core.domain.model.RestaurantItemImage;
import com.connectfood.core.domain.model.Restaurant;
import com.connectfood.core.domain.repository.RestaurantItemsImagesGateway;
import com.connectfood.core.domain.repository.RestaurantItemsGateway;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class UpdateRestaurantItemsUseCaseTest {

  @Mock
  private RestaurantItemsGateway repository;

  @Mock
  private RestaurantItemsAppMapper mapper;

  @Mock
  private RequestUserGuard guard;

  @Mock
  private RestaurantItemsImagesAppMapper restaurantItemsImagesMapper;

  @Mock
  private RestaurantItemsImagesGateway restaurantItemsImagesGateway;

  @InjectMocks
  private UpdateRestaurantItemsUseCase useCase;

  @Test
  @DisplayName("Deve lançar NotFoundException quando item não existir")
  void shouldThrowNotFoundExceptionWhenRestaurantItemsDoesNotExist() {
    final var requestUser = new RequestUser(UUID.randomUUID());
    final var uuid = UUID.randomUUID();
    final var input = Mockito.mock(RestaurantItemsInput.class);

    Mockito.when(repository.findByUuid(uuid))
        .thenReturn(Optional.empty());

    final var ex = Assertions.assertThrows(
        NotFoundException.class,
        () -> useCase.execute(requestUser, uuid, input)
    );
    Assertions.assertEquals("Restaurant Items not found", ex.getMessage());

    Mockito.verify(guard, Mockito.times(1))
        .requireRole(requestUser, "OWNER");

    Mockito.verify(repository, Mockito.times(1))
        .findByUuid(uuid);

    Mockito.verifyNoInteractions(mapper, restaurantItemsImagesMapper, restaurantItemsImagesGateway);
    Mockito.verifyNoMoreInteractions(guard, repository);
  }

  @Test
  @DisplayName("Deve atualizar quando existir, com input sem imagens e retornar output")
  void shouldUpdateRestaurantItemsWhenExistsAndReturnOutputWithoutImages() {
    final var requestUser = new RequestUser(UUID.randomUUID());
    final var uuid = UUID.randomUUID();
    final Restaurant restaurant = Mockito.mock(Restaurant.class);

    final RestaurantItem existingModel = Mockito.mock(RestaurantItem.class);
    Mockito.when(existingModel.getRestaurant())
        .thenReturn(restaurant);
    Mockito.when(existingModel.getImages())
        .thenReturn(List.of());

    Mockito.when(repository.findByUuid(uuid))
        .thenReturn(Optional.of(existingModel));

    final RestaurantItemsInput input = Mockito.mock(RestaurantItemsInput.class);
    Mockito.when(input.getImages())
        .thenReturn(List.of());

    final RestaurantItem domainToUpdate = Mockito.mock(RestaurantItem.class);
    Mockito.when(mapper.toDomain(uuid, input, restaurant))
        .thenReturn(domainToUpdate);

    final RestaurantItem updatedModel = Mockito.mock(RestaurantItem.class);
    Mockito.when(repository.update(uuid, domainToUpdate))
        .thenReturn(updatedModel);

    final RestaurantItemsOutput expected = Mockito.mock(RestaurantItemsOutput.class);
    Mockito.when(mapper.toOutput(updatedModel, List.of()))
        .thenReturn(expected);

    final var result = useCase.execute(requestUser, uuid, input);
    Assertions.assertSame(expected, result);

    Mockito.verify(guard, Mockito.times(1))
        .requireRole(requestUser, "OWNER");

    Mockito.verify(repository, Mockito.times(1))
        .findByUuid(uuid);
    Mockito.verify(mapper, Mockito.times(1))
        .toDomain(uuid, input, restaurant);
    Mockito.verify(repository, Mockito.times(1))
        .update(uuid, domainToUpdate);

    Mockito.verifyNoInteractions(restaurantItemsImagesMapper, restaurantItemsImagesGateway);

    Mockito.verify(mapper, Mockito.times(1))
        .toOutput(updatedModel, List.of());

    Mockito.verifyNoMoreInteractions(guard, repository, mapper);
  }

  @Test
  @DisplayName("Deve cobrir update, keep, save e delete e não deletar uuid null")
  void shouldSyncImagesUpdateKeepSaveDeleteAndSkipNullDelete() {
    final var requestUser = new RequestUser(UUID.randomUUID());
    final var itemUuid = UUID.randomUUID();
    final Restaurant restaurant = Mockito.mock(Restaurant.class);

    final var keepUuid = UUID.randomUUID();
    final var updateUuid = UUID.randomUUID();
    final var deleteUuid = UUID.randomUUID();

    final RestaurantItemImage currentKeep = Mockito.mock(RestaurantItemImage.class);
    Mockito.when(currentKeep.getUuid())
        .thenReturn(keepUuid);
    Mockito.when(currentKeep.getName())
        .thenReturn("keep-name");
    Mockito.when(currentKeep.getDescription())
        .thenReturn("keep-desc");

    final RestaurantItemImage currentUpdate = Mockito.mock(RestaurantItemImage.class);
    Mockito.when(currentUpdate.getUuid())
        .thenReturn(updateUuid);
    Mockito.when(currentUpdate.getName())
        .thenReturn("old-name");

    final RestaurantItemImage currentDelete = Mockito.mock(RestaurantItemImage.class);
    Mockito.when(currentDelete.getUuid())
        .thenReturn(deleteUuid);

    final RestaurantItemImage currentNullUuid = Mockito.mock(RestaurantItemImage.class);
    Mockito.when(currentNullUuid.getUuid())
        .thenReturn(null);

    final RestaurantItem existingModel = Mockito.mock(RestaurantItem.class);
    Mockito.when(existingModel.getRestaurant())
        .thenReturn(restaurant);
    Mockito.when(existingModel.getImages())
        .thenReturn(List.of(currentKeep, currentUpdate, currentDelete, currentNullUuid));
    Mockito.when(existingModel.getUuid())
        .thenReturn(itemUuid);

    Mockito.when(repository.findByUuid(itemUuid))
        .thenReturn(Optional.of(existingModel));

    final RestaurantItemsImagesInput inKeep = Mockito.mock(RestaurantItemsImagesInput.class);
    Mockito.when(inKeep.getUuid())
        .thenReturn(keepUuid);
    Mockito.when(inKeep.getName())
        .thenReturn("keep-name");
    Mockito.when(inKeep.getDescription())
        .thenReturn("keep-desc");

    final RestaurantItemsImagesInput inUpdate = Mockito.mock(RestaurantItemsImagesInput.class);
    Mockito.when(inUpdate.getUuid())
        .thenReturn(updateUuid);
    Mockito.when(inUpdate.getName())
        .thenReturn("new-name");

    final RestaurantItemsImagesInput inNew = Mockito.mock(RestaurantItemsImagesInput.class);
    Mockito.when(inNew.getUuid())
        .thenReturn(null);

    final RestaurantItemsInput input = Mockito.mock(RestaurantItemsInput.class);
    Mockito.when(input.getImages())
        .thenReturn(List.of(inKeep, inUpdate, inNew));

    final RestaurantItem domainToUpdate = Mockito.mock(RestaurantItem.class);
    Mockito.when(mapper.toDomain(itemUuid, input, restaurant))
        .thenReturn(domainToUpdate);

    final RestaurantItem updatedModel = Mockito.mock(RestaurantItem.class);
    Mockito.when(repository.update(itemUuid, domainToUpdate))
        .thenReturn(updatedModel);

    final RestaurantItemImage domainAny = Mockito.mock(RestaurantItemImage.class);
    Mockito.when(restaurantItemsImagesMapper.toDomain(Mockito.any(RestaurantItemsImagesInput.class)))
        .thenReturn(domainAny);

    final RestaurantItemImage persistedUpdated = Mockito.mock(RestaurantItemImage.class);
    Mockito.when(
            restaurantItemsImagesGateway.update(Mockito.eq(updateUuid), Mockito.any(RestaurantItemImage.class)))
        .thenReturn(persistedUpdated);

    final RestaurantItemImage persistedNew = Mockito.mock(RestaurantItemImage.class);
    Mockito.when(restaurantItemsImagesGateway.save(Mockito.eq(itemUuid), Mockito.any(RestaurantItemImage.class)))
        .thenReturn(persistedNew);

    final var expectedImages = List.of(currentKeep, persistedUpdated, persistedNew);

    final RestaurantItemsOutput expectedOutput = Mockito.mock(RestaurantItemsOutput.class);
    Mockito.when(mapper.toOutput(updatedModel, expectedImages))
        .thenReturn(expectedOutput);

    final var result = useCase.execute(requestUser, itemUuid, input);
    Assertions.assertSame(expectedOutput, result);

    Mockito.verify(guard, Mockito.times(1))
        .requireRole(requestUser, "OWNER");

    Mockito.verify(repository, Mockito.times(1))
        .findByUuid(itemUuid);
    Mockito.verify(mapper, Mockito.times(1))
        .toDomain(itemUuid, input, restaurant);
    Mockito.verify(repository, Mockito.times(1))
        .update(itemUuid, domainToUpdate);

    Mockito.verify(restaurantItemsImagesMapper, Mockito.times(3))
        .toDomain(Mockito.any(RestaurantItemsImagesInput.class));

    Mockito.verify(restaurantItemsImagesGateway, Mockito.never())
        .update(Mockito.eq(keepUuid), Mockito.any(RestaurantItemImage.class));

    Mockito.verify(restaurantItemsImagesGateway, Mockito.times(1))
        .update(Mockito.eq(updateUuid), Mockito.any(RestaurantItemImage.class));

    Mockito.verify(restaurantItemsImagesGateway, Mockito.times(1))
        .save(Mockito.eq(itemUuid), Mockito.any(RestaurantItemImage.class));

    Mockito.verify(restaurantItemsImagesGateway, Mockito.times(1))
        .delete(deleteUuid);
    Mockito.verify(restaurantItemsImagesGateway, Mockito.never())
        .delete(Mockito.isNull());

    Mockito.verify(mapper, Mockito.times(1))
        .toOutput(updatedModel, expectedImages);

    Mockito.verifyNoMoreInteractions(
        guard, repository, mapper, restaurantItemsImagesMapper, restaurantItemsImagesGateway
    );
  }

  @Test
  @DisplayName("Deve lançar NotFoundException quando incoming uuid não existir no currentByUuid")
  void shouldThrowNotFoundExceptionWhenIncomingImageUuidNotFoundInCurrent() {
    final var requestUser = new RequestUser(UUID.randomUUID());
    final var itemUuid = UUID.randomUUID();
    final Restaurant restaurant = Mockito.mock(Restaurant.class);

    final var currentUuid = UUID.randomUUID();
    final RestaurantItemImage current = Mockito.mock(RestaurantItemImage.class);
    Mockito.when(current.getUuid())
        .thenReturn(currentUuid);

    final RestaurantItem existingModel = Mockito.mock(RestaurantItem.class);
    Mockito.when(existingModel.getRestaurant())
        .thenReturn(restaurant);
    Mockito.when(existingModel.getImages())
        .thenReturn(List.of(current));

    Mockito.when(repository.findByUuid(itemUuid))
        .thenReturn(Optional.of(existingModel));

    final var unknownUuid = UUID.randomUUID();
    final RestaurantItemsImagesInput incoming = Mockito.mock(RestaurantItemsImagesInput.class);
    Mockito.when(incoming.getUuid())
        .thenReturn(unknownUuid);

    final RestaurantItemsInput input = Mockito.mock(RestaurantItemsInput.class);
    Mockito.when(input.getImages())
        .thenReturn(List.of(incoming));

    final RestaurantItem domainToUpdate = Mockito.mock(RestaurantItem.class);
    Mockito.when(mapper.toDomain(itemUuid, input, restaurant))
        .thenReturn(domainToUpdate);

    final RestaurantItem updatedModel = Mockito.mock(RestaurantItem.class);
    Mockito.when(repository.update(itemUuid, domainToUpdate))
        .thenReturn(updatedModel);

    Mockito.when(restaurantItemsImagesMapper.toDomain(incoming))
        .thenReturn(Mockito.mock(RestaurantItemImage.class));

    final var ex = Assertions.assertThrows(
        NotFoundException.class,
        () -> useCase.execute(requestUser, itemUuid, input)
    );
    Assertions.assertEquals("Restaurant Item Images not found", ex.getMessage());

    Mockito.verify(guard, Mockito.times(1))
        .requireRole(requestUser, "OWNER");

    Mockito.verify(repository, Mockito.times(1))
        .findByUuid(itemUuid);
    Mockito.verify(mapper, Mockito.times(1))
        .toDomain(itemUuid, input, restaurant);
    Mockito.verify(repository, Mockito.times(1))
        .update(itemUuid, domainToUpdate);

    Mockito.verify(restaurantItemsImagesMapper, Mockito.times(1))
        .toDomain(incoming);

    Mockito.verifyNoInteractions(restaurantItemsImagesGateway);

    Mockito.verify(mapper, Mockito.never())
        .toOutput(Mockito.any(), Mockito.any());

    Mockito.verifyNoMoreInteractions(guard, repository, mapper, restaurantItemsImagesMapper);
  }

  @Test
  @DisplayName("Deve executar merge (a,b)->a quando current tiver UUID duplicado")
  void shouldUseMergeFunctionWhenCurrentHasDuplicateUuid() {
    final var requestUser = new RequestUser(UUID.randomUUID());
    final var itemUuid = UUID.randomUUID();
    final Restaurant restaurant = Mockito.mock(Restaurant.class);

    final var dupUuid = UUID.randomUUID();

    final RestaurantItemImage currentA = Mockito.mock(RestaurantItemImage.class);
    Mockito.when(currentA.getUuid())
        .thenReturn(dupUuid);
    Mockito.when(currentA.getName())
        .thenReturn("same-name");
    Mockito.when(currentA.getDescription())
        .thenReturn("same-desc");

    final RestaurantItemImage currentB = Mockito.mock(RestaurantItemImage.class);
    Mockito.when(currentB.getUuid())
        .thenReturn(dupUuid);

    final RestaurantItemImage currentNull = Mockito.mock(RestaurantItemImage.class);
    Mockito.when(currentNull.getUuid())
        .thenReturn(null);

    final RestaurantItem existingModel = Mockito.mock(RestaurantItem.class);
    Mockito.when(existingModel.getRestaurant())
        .thenReturn(restaurant);
    Mockito.when(existingModel.getImages())
        .thenReturn(List.of(currentA, currentB, currentNull));

    Mockito.when(repository.findByUuid(itemUuid))
        .thenReturn(Optional.of(existingModel));

    final RestaurantItemsImagesInput incoming = Mockito.mock(RestaurantItemsImagesInput.class);
    Mockito.when(incoming.getUuid())
        .thenReturn(dupUuid);
    Mockito.when(incoming.getName())
        .thenReturn("same-name");
    Mockito.when(incoming.getDescription())
        .thenReturn("same-desc");

    final RestaurantItemsInput input = Mockito.mock(RestaurantItemsInput.class);
    Mockito.when(input.getImages())
        .thenReturn(List.of(incoming));

    final RestaurantItem domainToUpdate = Mockito.mock(RestaurantItem.class);
    Mockito.when(mapper.toDomain(itemUuid, input, restaurant))
        .thenReturn(domainToUpdate);

    final RestaurantItem updatedModel = Mockito.mock(RestaurantItem.class);
    Mockito.when(repository.update(itemUuid, domainToUpdate))
        .thenReturn(updatedModel);

    Mockito.when(restaurantItemsImagesMapper.toDomain(incoming))
        .thenReturn(Mockito.mock(RestaurantItemImage.class));

    final var expectedImages = List.of(currentA);

    final RestaurantItemsOutput expectedOutput = Mockito.mock(RestaurantItemsOutput.class);
    Mockito.when(mapper.toOutput(updatedModel, expectedImages))
        .thenReturn(expectedOutput);

    final var result = useCase.execute(requestUser, itemUuid, input);
    Assertions.assertSame(expectedOutput, result);

    Mockito.verify(guard, Mockito.times(1))
        .requireRole(requestUser, "OWNER");

    Mockito.verify(restaurantItemsImagesGateway, Mockito.never())
        .update(Mockito.any(), Mockito.any());
    Mockito.verify(restaurantItemsImagesGateway, Mockito.never())
        .save(Mockito.any(), Mockito.any());
    Mockito.verify(restaurantItemsImagesGateway, Mockito.never())
        .delete(Mockito.any());

    Mockito.verifyNoMoreInteractions(
        guard, repository, mapper, restaurantItemsImagesMapper, restaurantItemsImagesGateway
    );
  }

  @Test
  @DisplayName("Deve considerar changed quando name é igual e description é diferente")
  void shouldUpdateWhenDescriptionChangesEvenIfNameIsSame() {
    final var requestUser = new RequestUser(UUID.randomUUID());
    final var itemUuid = UUID.randomUUID();
    final Restaurant restaurant = Mockito.mock(Restaurant.class);

    final var imageUuid = UUID.randomUUID();

    final RestaurantItemImage current = Mockito.mock(RestaurantItemImage.class);
    Mockito.when(current.getUuid())
        .thenReturn(imageUuid);
    Mockito.when(current.getName())
        .thenReturn("same-name");
    Mockito.when(current.getDescription())
        .thenReturn("old-desc");

    final RestaurantItem existingModel = Mockito.mock(RestaurantItem.class);
    Mockito.when(existingModel.getRestaurant())
        .thenReturn(restaurant);
    Mockito.when(existingModel.getImages())
        .thenReturn(List.of(current));

    Mockito.when(repository.findByUuid(itemUuid))
        .thenReturn(Optional.of(existingModel));

    final RestaurantItemsImagesInput incoming = Mockito.mock(RestaurantItemsImagesInput.class);
    Mockito.when(incoming.getUuid())
        .thenReturn(imageUuid);
    Mockito.when(incoming.getName())
        .thenReturn("same-name");
    Mockito.when(incoming.getDescription())
        .thenReturn("new-desc");

    final RestaurantItemsInput input = Mockito.mock(RestaurantItemsInput.class);
    Mockito.when(input.getImages())
        .thenReturn(List.of(incoming));

    final RestaurantItem domainToUpdate = Mockito.mock(RestaurantItem.class);
    Mockito.when(mapper.toDomain(itemUuid, input, restaurant))
        .thenReturn(domainToUpdate);

    final RestaurantItem updatedModel = Mockito.mock(RestaurantItem.class);
    Mockito.when(repository.update(itemUuid, domainToUpdate))
        .thenReturn(updatedModel);

    final RestaurantItemImage imageDomain = Mockito.mock(RestaurantItemImage.class);
    Mockito.when(restaurantItemsImagesMapper.toDomain(incoming))
        .thenReturn(imageDomain);

    final RestaurantItemImage persistedUpdated = Mockito.mock(RestaurantItemImage.class);
    Mockito.when(restaurantItemsImagesGateway.update(imageUuid, imageDomain))
        .thenReturn(persistedUpdated);

    final var expectedImages = List.of(persistedUpdated);

    final RestaurantItemsOutput expectedOutput = Mockito.mock(RestaurantItemsOutput.class);
    Mockito.when(mapper.toOutput(updatedModel, expectedImages))
        .thenReturn(expectedOutput);

    final var result = useCase.execute(requestUser, itemUuid, input);
    Assertions.assertSame(expectedOutput, result);

    Mockito.verify(guard, Mockito.times(1))
        .requireRole(requestUser, "OWNER");

    Mockito.verify(restaurantItemsImagesGateway, Mockito.times(1))
        .update(imageUuid, imageDomain);
    Mockito.verify(restaurantItemsImagesGateway, Mockito.never())
        .save(Mockito.any(), Mockito.any());
    Mockito.verify(restaurantItemsImagesGateway, Mockito.never())
        .delete(Mockito.any());

    Mockito.verifyNoMoreInteractions(
        guard, repository, mapper, restaurantItemsImagesMapper, restaurantItemsImagesGateway
    );
  }
}
