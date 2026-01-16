package com.connectfood.infrastructure.rest.dto.usertype;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "UsersTypeRequest", description = "Request to create or update a Users Type")
public class UserTypeRequest {

  @Schema(
      description = "User type name",
      example = "OWNER",
      requiredMode = Schema.RequiredMode.REQUIRED
  )
  @NotBlank(message = "Name cannot be empty")
  @Size(min = 3, max = 255, message = "Name must be between 3 and 255 characters")
  private String name;

  @Schema(
      description = "User type description",
      example = "User type that represents the restaurant owner"
  )
  @Size(max = 255, message = "Description must be less than 255 characters")
  private String description;
}
