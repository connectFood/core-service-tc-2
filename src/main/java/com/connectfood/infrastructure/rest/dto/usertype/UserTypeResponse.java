package com.connectfood.infrastructure.rest.dto.usertype;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonInclude;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(name = "UsersTypeResponse", description = "Response body with user type data")
public class UserTypeResponse {

  @Schema(
      description = "User type unique identifier",
      example = "5d102112-c916-43b6-b732-7d1618eb1136"
  )
  private UUID uuid;

  @Schema(
      description = "User type name",
      example = "OWNER"
  )
  private String name;

  @Schema(
      description = "User type description",
      example = "User type that represents the restaurant owner"
  )
  private String description;
}
