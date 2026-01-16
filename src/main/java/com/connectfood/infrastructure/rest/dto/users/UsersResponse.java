package com.connectfood.infrastructure.rest.dto.users;

import java.util.UUID;

import com.connectfood.infrastructure.rest.dto.address.AddressResponse;
import com.connectfood.infrastructure.rest.dto.userstype.UsersTypeResponse;
import com.fasterxml.jackson.annotation.JsonInclude;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(name = "UsersResponse", description = "Response body with user data")
public class UsersResponse {

  @Schema(
      description = "User unique identifier",
      example = "9c2c2c8d-7a4f-4c39-9d32-52b371e2a910"
  )
  private UUID uuid;

  @Schema(
      description = "Full name of the user",
      example = "Arlen Vartamir"
  )
  private String fullName;

  @Schema(
      description = "User email",
      example = "arlen.vartamir@lunaris.dev"
  )
  private String email;

  @Schema(
      description = "User type associated with the user",
      implementation = UsersTypeResponse.class
  )
  private UsersTypeResponse usersType;

  @Schema(
      description = "Address associated with the user",
      implementation = UsersTypeResponse.class
  )
  private AddressResponse address;
}
