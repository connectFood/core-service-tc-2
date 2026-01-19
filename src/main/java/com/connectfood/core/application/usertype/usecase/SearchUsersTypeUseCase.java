package com.connectfood.core.application.usertype.usecase;

import java.util.List;

import com.connectfood.core.application.dto.commons.PageOutput;
import com.connectfood.core.application.usertype.dto.UserTypeOutput;
import com.connectfood.core.application.usertype.mapper.UserTypeAppMapper;
import com.connectfood.core.domain.repository.UserTypeGateway;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SearchUsersTypeUseCase {

  private final UserTypeGateway repository;
  private final UserTypeAppMapper mapper;

  public SearchUsersTypeUseCase(final UserTypeGateway repository, final UserTypeAppMapper mapper) {
    this.repository = repository;
    this.mapper = mapper;
  }

  @Transactional(readOnly = true)
  public PageOutput<List<UserTypeOutput>> execute(final String name, final Integer page, final Integer size,
      final String sort, final String direction) {
    final var usersTypes = repository.findAll(name, page, size, sort, direction);

    final var results = usersTypes.content()
        .stream()
        .map(mapper::toOutput)
        .toList();

    return new PageOutput<>(results, usersTypes.total());
  }
}
