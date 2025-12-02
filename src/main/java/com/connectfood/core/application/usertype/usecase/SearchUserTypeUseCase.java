package com.connectfood.core.application.usertype.usecase;

import java.util.List;

import com.connectfood.core.application.usertype.dto.UsersTypeOutput;
import com.connectfood.core.application.usertype.dto.commons.PageOutput;
import com.connectfood.core.application.usertype.mapper.UsersTypeAppMapper;
import com.connectfood.core.domain.repository.UsersTypeRepository;

import org.springframework.stereotype.Component;

@Component
public class SearchUserTypeUseCase {

  private final UsersTypeRepository repository;
  private final UsersTypeAppMapper mapper;

  public SearchUserTypeUseCase(final UsersTypeRepository repository, final UsersTypeAppMapper mapper) {
    this.repository = repository;
    this.mapper = mapper;
  }

  public PageOutput<List<UsersTypeOutput>> execute(final String name, final Integer page, final Integer size,
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
