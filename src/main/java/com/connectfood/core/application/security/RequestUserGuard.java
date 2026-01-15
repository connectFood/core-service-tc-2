package com.connectfood.core.application.security;

import com.connectfood.core.domain.exception.BadRequestException;
import com.connectfood.core.domain.exception.ForbiddenException;
import com.connectfood.core.domain.exception.NotFoundException;
import com.connectfood.core.domain.model.Users;
import com.connectfood.core.domain.repository.UsersRepository;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class RequestUserGuard {

  private final UsersRepository repository;

  public RequestUserGuard(final UsersRepository repository) {
    this.repository = repository;
  }

  public Users requireUser(final RequestUser request) {
    if (request == null || request.uuid() == null) {
      throw new BadRequestException("Request-User-Uuid header is required");
    }

    return repository.findByUuid(request.uuid())
        .orElseThrow(() -> new NotFoundException("User not found"));
  }

  @Transactional(readOnly = true)
  public Users requireRole(final RequestUser request, final String requiredRole) {
    final var user = requireUser(request);

    final var hasRole = user.getUsersType()
        .getName()
        .equals(requiredRole);

    if (!hasRole) {
      throw new ForbiddenException("User does not have permission for this operation");
    }

    return user;
  }
}
