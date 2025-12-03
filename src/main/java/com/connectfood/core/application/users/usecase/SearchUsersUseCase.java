package com.connectfood.core.application.users.usecase;

import java.util.List;
import java.util.UUID;

import com.connectfood.core.application.dto.commons.PageOutput;
import com.connectfood.core.application.users.dto.UsersOutput;
import com.connectfood.core.application.users.mapper.UsersAppMapper;
import com.connectfood.core.domain.repository.UsersRepository;

import org.springframework.stereotype.Component;

@Component
public class SearchUsersUseCase {

  private final UsersRepository repository;
  private final UsersAppMapper mapper;

  public SearchUsersUseCase(final UsersRepository repository, final UsersAppMapper mapper) {
    this.repository = repository;
    this.mapper = mapper;
  }

  public PageOutput<List<UsersOutput>> execute(final String fullName, final String email, final UUID usersTypeUuid,
      final Integer page, final Integer size, final String sort, final String direction) {
    final var users = repository.findAll(fullName, email, usersTypeUuid, page, size, sort, direction);

    final var results = users.content()
        .stream()
        .map(mapper::toOutput)
        .toList();

    return new PageOutput<>(results, users.total());
  }
}
