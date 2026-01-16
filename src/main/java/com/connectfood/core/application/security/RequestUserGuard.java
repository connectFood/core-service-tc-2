package com.connectfood.core.application.security;

import com.connectfood.core.domain.exception.BadRequestException;
import com.connectfood.core.domain.exception.ForbiddenException;
import com.connectfood.core.domain.exception.NotFoundException;
import com.connectfood.core.domain.model.User;
import com.connectfood.core.domain.repository.UserGateway;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class RequestUserGuard {

  private final UserGateway repository;

  public RequestUserGuard(final UserGateway repository) {
    this.repository = repository;
  }

  public User requireUser(final RequestUser request) {
    if (request == null || request.uuid() == null) {
      throw new BadRequestException("Request-User-Uuid header is required");
    }

    return repository.findByUuid(request.uuid())
        .orElseThrow(() -> new NotFoundException("User not found"));
  }

  @Transactional(readOnly = true)
  public User requireRole(final RequestUser request, final String requiredRole) {
    final var user = requireUser(request);

    final var hasRole = user.getUserType()
        .getName()
        .equals(requiredRole);

    if (!hasRole) {
      throw new ForbiddenException("User does not have permission for this operation");
    }

    return user;
  }
}
