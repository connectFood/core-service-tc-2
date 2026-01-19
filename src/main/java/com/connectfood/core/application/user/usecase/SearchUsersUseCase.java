package com.connectfood.core.application.user.usecase;

import java.util.List;
import java.util.UUID;

import com.connectfood.core.application.dto.commons.PageOutput;
import com.connectfood.core.application.user.dto.UserOutput;
import com.connectfood.core.application.user.mapper.UserAppMapper;
import com.connectfood.core.domain.repository.UserGateway;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SearchUsersUseCase {

  private final UserGateway repository;
  private final UserAppMapper mapper;

  public SearchUsersUseCase(final UserGateway repository, final UserAppMapper mapper) {
    this.repository = repository;
    this.mapper = mapper;
  }

  @Transactional(readOnly = true)
  public PageOutput<List<UserOutput>> execute(final String fullName, final String email, final UUID usersTypeUuid,
      final Integer page, final Integer size, final String sort, final String direction) {
    final var users = repository.findAll(fullName, email, usersTypeUuid, page, size, sort, direction);

    final var results = users.content()
        .stream()
        .map(mapper::toOutput)
        .toList();

    return new PageOutput<>(results, users.total());
  }
}
