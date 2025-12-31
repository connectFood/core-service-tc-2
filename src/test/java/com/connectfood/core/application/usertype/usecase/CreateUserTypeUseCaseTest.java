package com.connectfood.core.application.usertype.usecase;

import com.connectfood.core.application.usertype.dto.UsersTypeInput;
import com.connectfood.core.application.usertype.dto.UsersTypeOutput;
import com.connectfood.core.application.usertype.mapper.UsersTypeAppMapper;
import com.connectfood.core.domain.model.UsersType;
import com.connectfood.core.domain.repository.UsersTypeRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

@ExtendWith(MockitoExtension.class)
class CreateUserTypeUseCaseTest {

  @Mock
  private UsersTypeRepository repository;

  @Mock
  private UsersTypeAppMapper mapper;

  @InjectMocks
  private CreateUserTypeUseCase useCase;

  @Test
  @DisplayName("Deve criar UserType com sucesso")
  void shouldCreateUserTypeSuccessfully() {

    final var input = new UsersTypeInput("ADMIN", "Administrador");
    final var domainObj = new UsersType(UUID.randomUUID(), "ADMIN", "Administrador");
    final var output = new UsersTypeOutput(domainObj.getUuid(), "ADMIN", "Administrador");

    Mockito.when(mapper.toDomain(input)).thenReturn(domainObj);
    Mockito.when(repository.save(domainObj)).thenReturn(domainObj);
    Mockito.when(mapper.toOutput(domainObj)).thenReturn(output);

    // Ação
    final var result = useCase.execute(input);

    // Verificação (Assert)
    Assertions.assertNotNull(result);
    Assertions.assertEquals(output.getUuid(), result.getUuid());

    // Verifica se o método save foi chamado exatamente 1 vez
    Mockito.verify(repository, Mockito.times(1)).save(domainObj);
  }
}
