package com.connectfood.core.application.users.usecase;

import java.util.List;

import com.connectfood.core.application.users.dto.UsersOutput;
import com.connectfood.core.application.users.dto.commons.PageOutput;
import com.connectfood.core.application.users.mapper.UsersAppMapper;
import com.connectfood.core.domain.repository.UsersTypeRepository;

import org.springframework.stereotype.Component;

@Component
public class SearchUserTypeUseCase {

  private final UsersTypeRepository repository;
  private final UsersAppMapper mapper;

  public SearchUserTypeUseCase(final UsersTypeRepository repository, final UsersAppMapper mapper) {
    this.repository = repository;
    this.mapper = mapper;
  }

  public PageOutput<List<UsersOutput>> execute(final String name, final Integer page, final Integer size,
      String sort,
      String direction) {
    final var usersTypes = repository.findAll(name, page, size, sort, direction);

    final var results = usersTypes.content()
        .stream()
        .map(mapper::toOutput)
        .toList();

    return new PageOutput<>(results, usersTypes.total());
  }
}
