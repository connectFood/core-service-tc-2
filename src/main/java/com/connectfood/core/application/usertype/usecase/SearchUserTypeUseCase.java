package com.connectfood.core.application.usertype.usecase;

import java.util.List;

import com.connectfood.core.application.usertype.dto.UsersTypeOutput;
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

  public List<UsersTypeOutput> execute() {
    final var usersTypes = repository.findAll();

    return usersTypes.stream()
        .map(mapper::toOutput)
        .toList();
  }
}
