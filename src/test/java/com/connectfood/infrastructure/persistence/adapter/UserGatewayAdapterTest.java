package com.connectfood.infrastructure.persistence.adapter;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

import com.connectfood.core.domain.model.User;
import com.connectfood.core.domain.model.UserType;
import com.connectfood.core.domain.model.commons.PageModel;
import com.connectfood.infrastructure.persistence.entity.UsersEntity;
import com.connectfood.infrastructure.persistence.entity.UsersTypeEntity;
import com.connectfood.infrastructure.persistence.jpa.JpaUsersRepository;
import com.connectfood.infrastructure.persistence.jpa.JpaUsersTypeRepository;
import com.connectfood.infrastructure.persistence.mappers.UsersInfraMapper;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

@ExtendWith(MockitoExtension.class)
class UserGatewayAdapterTest {

  @Mock
  private JpaUsersRepository repository;

  @Mock
  private UsersInfraMapper mapper;

  @Mock
  private JpaUsersTypeRepository usersTypeRepository;

  @InjectMocks
  private UserGatewayAdapter adapter;

  @Test
  @DisplayName("Deve salvar e retornar o model mapeado")
  void saveShouldPersistAndReturnMappedModel() {
    final User user = Mockito.mock(User.class);
    final UserType userTypeDomain = Mockito.mock(UserType.class);

    final var usersTypeUuid = UUID.randomUUID();
    Mockito.when(userTypeDomain.getUuid())
        .thenReturn(usersTypeUuid);
    Mockito.when(user.getUserType())
        .thenReturn(userTypeDomain);

    final UsersTypeEntity usersTypeEntity = Mockito.mock(UsersTypeEntity.class);

    final UsersEntity entityToSave = Mockito.mock(UsersEntity.class);
    final UsersEntity savedEntity = Mockito.mock(UsersEntity.class);

    final User mappedDomain = Mockito.mock(User.class);

    Mockito.when(usersTypeRepository.findByUuid(usersTypeUuid))
        .thenReturn(Optional.of(usersTypeEntity));
    Mockito.when(mapper.toEntity(user, usersTypeEntity))
        .thenReturn(entityToSave);
    Mockito.when(repository.save(entityToSave))
        .thenReturn(savedEntity);
    Mockito.when(mapper.toDomain(savedEntity))
        .thenReturn(mappedDomain);

    final var result = adapter.save(user);

    Assertions.assertSame(mappedDomain, result);

    Mockito.verify(usersTypeRepository, Mockito.times(1))
        .findByUuid(usersTypeUuid);
    Mockito.verify(mapper, Mockito.times(1))
        .toEntity(user, usersTypeEntity);
    Mockito.verify(repository, Mockito.times(1))
        .save(entityToSave);
    Mockito.verify(mapper, Mockito.times(1))
        .toDomain(savedEntity);
    Mockito.verifyNoMoreInteractions(repository, mapper, usersTypeRepository);
  }

  @Test
  @DisplayName("Deve lançar exceção quando tipo de usuário não existir ao salvar")
  void saveShouldThrowWhenUsersTypeDoesNotExist() {
    final User user = Mockito.mock(User.class);
    final UserType userTypeDomain = Mockito.mock(UserType.class);

    final var usersTypeUuid = UUID.randomUUID();
    Mockito.when(userTypeDomain.getUuid())
        .thenReturn(usersTypeUuid);
    Mockito.when(user.getUserType())
        .thenReturn(userTypeDomain);

    Mockito.when(usersTypeRepository.findByUuid(usersTypeUuid))
        .thenReturn(Optional.empty());

    Assertions.assertThrows(NoSuchElementException.class, () -> adapter.save(user));

    Mockito.verify(usersTypeRepository, Mockito.times(1))
        .findByUuid(usersTypeUuid);
    Mockito.verifyNoInteractions(repository, mapper);
    Mockito.verifyNoMoreInteractions(usersTypeRepository);
  }

  @Test
  @DisplayName("Deve atualizar mantendo o tipo existente quando o uuid do tipo for o mesmo")
  void updateShouldKeepExistingTypeWhenUuidIsSame() {
    final var uuid = UUID.randomUUID();

    final User user = Mockito.mock(User.class);
    final UserType userTypeDomain = Mockito.mock(UserType.class);

    final var usersTypeUuid = UUID.randomUUID();
    Mockito.when(userTypeDomain.getUuid())
        .thenReturn(usersTypeUuid);
    Mockito.when(user.getUserType())
        .thenReturn(userTypeDomain);

    final UsersEntity foundEntity = Mockito.mock(UsersEntity.class);

    final UsersTypeEntity existingTypeEntity = Mockito.mock(UsersTypeEntity.class);
    Mockito.when(existingTypeEntity.getUuid())
        .thenReturn(usersTypeUuid);
    Mockito.when(foundEntity.getUsersType())
        .thenReturn(existingTypeEntity);

    final UsersTypeEntity fetchedTypeEntity = Mockito.mock(UsersTypeEntity.class);
    Mockito.when(fetchedTypeEntity.getUuid())
        .thenReturn(usersTypeUuid);

    Mockito.when(repository.findByUuid(uuid))
        .thenReturn(Optional.of(foundEntity));
    Mockito.when(usersTypeRepository.findByUuid(usersTypeUuid))
        .thenReturn(Optional.of(fetchedTypeEntity));

    final UsersEntity entityToSave = Mockito.mock(UsersEntity.class);
    final UsersEntity savedEntity = Mockito.mock(UsersEntity.class);
    final User mappedDomain = Mockito.mock(User.class);

    Mockito.when(mapper.toEntity(user, foundEntity, existingTypeEntity))
        .thenReturn(entityToSave);
    Mockito.when(repository.save(entityToSave))
        .thenReturn(savedEntity);
    Mockito.when(mapper.toDomain(savedEntity))
        .thenReturn(mappedDomain);

    final var result = adapter.update(uuid, user);

    Assertions.assertSame(mappedDomain, result);

    Mockito.verify(repository, Mockito.times(1))
        .findByUuid(uuid);
    Mockito.verify(usersTypeRepository, Mockito.times(1))
        .findByUuid(usersTypeUuid);
    Mockito.verify(mapper, Mockito.times(1))
        .toEntity(user, foundEntity, existingTypeEntity);
    Mockito.verify(repository, Mockito.times(1))
        .save(entityToSave);
    Mockito.verify(mapper, Mockito.times(1))
        .toDomain(savedEntity);
    Mockito.verifyNoMoreInteractions(repository, mapper, usersTypeRepository);
  }

  @Test
  @DisplayName("Deve atualizar trocando o tipo quando o uuid do tipo for diferente")
  void updateShouldReplaceTypeWhenUuidIsDifferent() {
    final var uuid = UUID.randomUUID();

    final User user = Mockito.mock(User.class);
    final UserType userTypeDomain = Mockito.mock(UserType.class);

    final var newTypeUuid = UUID.randomUUID();
    Mockito.when(userTypeDomain.getUuid())
        .thenReturn(newTypeUuid);
    Mockito.when(user.getUserType())
        .thenReturn(userTypeDomain);

    final UsersEntity foundEntity = Mockito.mock(UsersEntity.class);

    final var existingTypeUuid = UUID.randomUUID();
    final UsersTypeEntity existingTypeEntity = Mockito.mock(UsersTypeEntity.class);
    Mockito.when(existingTypeEntity.getUuid())
        .thenReturn(existingTypeUuid);
    Mockito.when(foundEntity.getUsersType())
        .thenReturn(existingTypeEntity);

    final UsersTypeEntity fetchedTypeEntity = Mockito.mock(UsersTypeEntity.class);
    Mockito.when(fetchedTypeEntity.getUuid())
        .thenReturn(newTypeUuid);

    Mockito.when(repository.findByUuid(uuid))
        .thenReturn(Optional.of(foundEntity));
    Mockito.when(usersTypeRepository.findByUuid(newTypeUuid))
        .thenReturn(Optional.of(fetchedTypeEntity));

    final UsersEntity entityToSave = Mockito.mock(UsersEntity.class);
    final UsersEntity savedEntity = Mockito.mock(UsersEntity.class);
    final User mappedDomain = Mockito.mock(User.class);

    Mockito.when(mapper.toEntity(user, foundEntity, fetchedTypeEntity))
        .thenReturn(entityToSave);
    Mockito.when(repository.save(entityToSave))
        .thenReturn(savedEntity);
    Mockito.when(mapper.toDomain(savedEntity))
        .thenReturn(mappedDomain);

    final var result = adapter.update(uuid, user);

    Assertions.assertSame(mappedDomain, result);

    Mockito.verify(repository, Mockito.times(1))
        .findByUuid(uuid);
    Mockito.verify(usersTypeRepository, Mockito.times(1))
        .findByUuid(newTypeUuid);
    Mockito.verify(mapper, Mockito.times(1))
        .toEntity(user, foundEntity, fetchedTypeEntity);
    Mockito.verify(repository, Mockito.times(1))
        .save(entityToSave);
    Mockito.verify(mapper, Mockito.times(1))
        .toDomain(savedEntity);
    Mockito.verifyNoMoreInteractions(repository, mapper, usersTypeRepository);
  }

  @Test
  @DisplayName("Deve lançar exceção quando uuid não existir ao atualizar")
  void updateShouldThrowWhenUserDoesNotExist() {
    final var uuid = UUID.randomUUID();
    final User user = Mockito.mock(User.class);

    Mockito.when(repository.findByUuid(uuid))
        .thenReturn(Optional.empty());

    Assertions.assertThrows(NoSuchElementException.class, () -> adapter.update(uuid, user));

    Mockito.verify(repository, Mockito.times(1))
        .findByUuid(uuid);
    Mockito.verifyNoInteractions(mapper, usersTypeRepository);
    Mockito.verifyNoMoreInteractions(repository);
  }

  @Test
  @DisplayName("Deve lançar exceção quando tipo não existir ao atualizar")
  void updateShouldThrowWhenUsersTypeDoesNotExist() {
    final var uuid = UUID.randomUUID();

    final User user = Mockito.mock(User.class);
    final UserType userTypeDomain = Mockito.mock(UserType.class);

    final var typeUuid = UUID.randomUUID();
    Mockito.when(userTypeDomain.getUuid())
        .thenReturn(typeUuid);
    Mockito.when(user.getUserType())
        .thenReturn(userTypeDomain);

    final UsersEntity foundEntity = Mockito.mock(UsersEntity.class);
    Mockito.when(repository.findByUuid(uuid))
        .thenReturn(Optional.of(foundEntity));

    Mockito.when(usersTypeRepository.findByUuid(typeUuid))
        .thenReturn(Optional.empty());

    Assertions.assertThrows(NoSuchElementException.class, () -> adapter.update(uuid, user));

    Mockito.verify(repository, Mockito.times(1))
        .findByUuid(uuid);
    Mockito.verify(usersTypeRepository, Mockito.times(1))
        .findByUuid(typeUuid);
    Mockito.verifyNoInteractions(mapper);
    Mockito.verifyNoMoreInteractions(repository, usersTypeRepository);
  }

  @Test
  @DisplayName("Deve retornar Optional vazio quando não encontrar por uuid")
  void findByUuidShouldReturnEmptyWhenNotFound() {
    final var uuid = UUID.randomUUID();

    Mockito.when(repository.findByUuid(uuid))
        .thenReturn(Optional.empty());

    final var result = adapter.findByUuid(uuid);

    Assertions.assertNotNull(result);
    Assertions.assertTrue(result.isEmpty());

    Mockito.verify(repository, Mockito.times(1))
        .findByUuid(uuid);
    Mockito.verifyNoInteractions(mapper, usersTypeRepository);
    Mockito.verifyNoMoreInteractions(repository);
  }

  @Test
  @DisplayName("Deve retornar model quando encontrar por uuid")
  void findByUuidShouldReturnMappedModelWhenFound() {
    final var uuid = UUID.randomUUID();

    final UsersEntity foundEntity = Mockito.mock(UsersEntity.class);
    final User mappedDomain = Mockito.mock(User.class);

    Mockito.when(repository.findByUuid(uuid))
        .thenReturn(Optional.of(foundEntity));
    Mockito.when(mapper.toDomain(foundEntity))
        .thenReturn(mappedDomain);

    final var result = adapter.findByUuid(uuid);

    Assertions.assertTrue(result.isPresent());
    Assertions.assertSame(mappedDomain, result.get());

    Mockito.verify(repository, Mockito.times(1))
        .findByUuid(uuid);
    Mockito.verify(mapper, Mockito.times(1))
        .toDomain(foundEntity);
    Mockito.verifyNoMoreInteractions(repository, mapper, usersTypeRepository);
  }

  @Test
  @DisplayName("Deve listar paginado com defaults quando sort e direction forem null")
  void findAllShouldReturnPagedResultWithDefaultsWhenSortAndDirectionAreNull() {
    final var usersTypeUuid = UUID.randomUUID();

    final UsersEntity entity1 = Mockito.mock(UsersEntity.class);
    final UsersEntity entity2 = Mockito.mock(UsersEntity.class);

    final User domain1 = Mockito.mock(User.class);
    final User domain2 = Mockito.mock(User.class);

    Mockito.when(mapper.toDomain(entity1))
        .thenReturn(domain1);
    Mockito.when(mapper.toDomain(entity2))
        .thenReturn(domain2);

    final Page<UsersEntity> pageResult =
        new PageImpl<>(List.of(entity1, entity2), Pageable.unpaged(), 2);

    final ArgumentCaptor<Pageable> pageableCaptor = ArgumentCaptor.forClass(Pageable.class);

    Mockito.when(repository.findAll(
            ArgumentMatchers.<Specification<UsersEntity>>any(),
            ArgumentMatchers.any(Pageable.class)
        ))
        .thenReturn(pageResult);

    final PageModel<List<User>> result =
        adapter.findAll(
            "Lucas",
            "lucas@email.com",
            usersTypeUuid,
            0,
            10,
            null,
            null
        );

    Assertions.assertNotNull(result);
    Assertions.assertEquals(2, result.content()
        .size()
    );
    Assertions.assertEquals(List.of(domain1, domain2), result.content());
    Assertions.assertEquals(2L, result.total());

    Mockito.verify(repository, Mockito.times(1))
        .findAll(ArgumentMatchers.<Specification<UsersEntity>>any(), pageableCaptor.capture());

    final Pageable usedPageable = pageableCaptor.getValue();
    Assertions.assertEquals(0, usedPageable.getPageNumber());
    Assertions.assertEquals(10, usedPageable.getPageSize());
    Assertions.assertEquals(Sort.by(Sort.Direction.ASC, "id"), usedPageable.getSort());

    Mockito.verify(mapper, Mockito.times(1))
        .toDomain(entity1);
    Mockito.verify(mapper, Mockito.times(1))
        .toDomain(entity2);
    Mockito.verifyNoMoreInteractions(repository, mapper, usersTypeRepository);
  }

  @Test
  @DisplayName("Deve listar paginado respeitando sort e direction informados")
  void findAllShouldReturnPagedResultUsingProvidedSortAndDirection() {
    final UsersEntity entity = Mockito.mock(UsersEntity.class);
    final User domain = Mockito.mock(User.class);

    Mockito.when(mapper.toDomain(entity))
        .thenReturn(domain);

    final Page<UsersEntity> pageResult =
        new PageImpl<>(List.of(entity), Pageable.unpaged(), 1);

    final ArgumentCaptor<Pageable> pageableCaptor = ArgumentCaptor.forClass(Pageable.class);

    Mockito.when(repository.findAll(
            ArgumentMatchers.<Specification<UsersEntity>>any(),
            ArgumentMatchers.any(Pageable.class)
        ))
        .thenReturn(pageResult);

    final PageModel<List<User>> result =
        adapter.findAll(
            null,
            null,
            null,
            1,
            5,
            "fullName",
            "DESC"
        );

    Assertions.assertNotNull(result);
    Assertions.assertEquals(1, result.content()
        .size()
    );
    Assertions.assertEquals(List.of(domain), result.content());
    Assertions.assertEquals(1L, result.total());

    Mockito.verify(repository, Mockito.times(1))
        .findAll(ArgumentMatchers.<Specification<UsersEntity>>any(), pageableCaptor.capture());

    final Pageable usedPageable = pageableCaptor.getValue();
    Assertions.assertEquals(1, usedPageable.getPageNumber());
    Assertions.assertEquals(5, usedPageable.getPageSize());
    Assertions.assertEquals(Sort.by(Sort.Direction.DESC, "fullName"), usedPageable.getSort());

    Mockito.verify(mapper, Mockito.times(1))
        .toDomain(entity);
    Mockito.verifyNoMoreInteractions(repository, mapper, usersTypeRepository);
  }

  @Test
  @DisplayName("Deve deletar pelo uuid")
  void deleteShouldDeleteByUuid() {
    final var uuid = UUID.randomUUID();

    adapter.delete(uuid);

    Mockito.verify(repository, Mockito.times(1))
        .deleteByUuid(uuid);
    Mockito.verifyNoInteractions(mapper, usersTypeRepository);
    Mockito.verifyNoMoreInteractions(repository);
  }
}
