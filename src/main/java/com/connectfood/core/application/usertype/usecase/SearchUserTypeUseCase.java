package com.connectfood.core.application.usertype.usecase;

import java.util.List;

import com.connectfood.core.application.dto.commons.PageOutput;
import com.connectfood.core.application.usertype.dto.UsersTypeOutput;
import com.connectfood.core.application.usertype.mapper.UsersTypeAppMapper;
import com.connectfood.core.domain.repository.UsersTypeGateway;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class SearchUserTypeUseCase {

  private final UsersTypeGateway repository;
  private final UsersTypeAppMapper mapper;

  public SearchUserTypeUseCase(final UsersTypeGateway repository, final UsersTypeAppMapper mapper) {
    this.repository = repository;
    this.mapper = mapper;
  }

  @Transactional(readOnly = true)
  public PageOutput<List<UsersTypeOutput>> execute(final String name, final Integer page, final Integer size,
      final String sort, final String direction) {
    final var usersTypes = repository.findAll(name, page, size, sort, direction);

    final var results = usersTypes.content()
        .stream()
        .map(mapper::toOutput)
        .toList();

    return new PageOutput<>(results, usersTypes.total());
  }
}
