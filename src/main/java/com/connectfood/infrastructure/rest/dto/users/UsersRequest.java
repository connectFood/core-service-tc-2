package com.connectfood.infrastructure.rest.dto.users;

import java.util.UUID;

import com.connectfood.infrastructure.rest.dto.address.AddressRequest;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "UsersRequest", description = "Request for creating or updating a User")
public class UsersRequest {

  @Schema(
      description = "Full name of the user",
      example = "Arlen Vartamir",
      requiredMode = Schema.RequiredMode.REQUIRED
  )
  @NotBlank(message = "Full name cannot be empty")
  @Size(min = 3, max = 255, message = "Full name must be between 3 and 255 characters")
  private String fullName;

  @Schema(
      description = "User email",
      example = "arlen.vartamir@lunaris.dev",
      requiredMode = Schema.RequiredMode.REQUIRED
  )
  @NotBlank(message = "Email cannot be empty")
  @Email(message = "Invalid email format")
  private String email;

  @Schema(
      description = "User password",
      example = "s3cureP@ss",
      requiredMode = Schema.RequiredMode.REQUIRED
  )
  @NotBlank(message = "Password cannot be empty")
  @Size(min = 6, max = 255, message = "Password must contain at least 6 characters")
  private String password;

  @Schema(
      description = "UUID of the user type",
      example = "a912c110-3b3d-4fd7-bf00-8f77c15fd9ab",
      requiredMode = Schema.RequiredMode.REQUIRED
  )
  @NotNull(message = "User type UUID is required")
  private UUID usersTypeUuid;

  @Schema(
      description = "Address information of the user, including street, city, state and postal code",
      requiredMode = Schema.RequiredMode.REQUIRED
  )
  private AddressRequest address;
}
