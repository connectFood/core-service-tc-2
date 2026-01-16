package com.connectfood.infrastructure.rest.dto.user;

import java.util.UUID;

import com.connectfood.infrastructure.rest.dto.address.AddressResponse;
import com.connectfood.infrastructure.rest.dto.usertype.UserTypeResponse;
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
public class UserResponse {

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
      implementation = UserTypeResponse.class
  )
  private UserTypeResponse usersType;

  @Schema(
      description = "Address associated with the user",
      implementation = UserTypeResponse.class
  )
  private AddressResponse address;
}
