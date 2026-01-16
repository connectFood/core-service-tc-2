package com.connectfood.infrastructure.rest.dto.address;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "AddressRequest", description = "Request for creating or updating a user address")
public class AddressRequest {

  @Schema(
      description = "Street name",
      example = "Rua das Estrelas",
      requiredMode = Schema.RequiredMode.REQUIRED
  )
  @NotBlank(message = "Street cannot be empty")
  @Size(min = 3, max = 255, message = "Street must be between 3 and 255 characters")
  private String street;

  @Schema(
      description = "Address number",
      example = "123",
      requiredMode = Schema.RequiredMode.REQUIRED
  )
  @NotBlank(message = "Number cannot be empty")
  @Size(min = 1, max = 20, message = "Number must be between 1 and 20 characters")
  private String number;

  @Schema(
      description = "Address complement",
      example = "Apartment 45"
  )
  @Size(max = 255, message = "Complement must be at most 255 characters")
  private String complement;

  @Schema(
      description = "Neighborhood",
      example = "Centro",
      requiredMode = Schema.RequiredMode.REQUIRED
  )
  @NotBlank(message = "Neighborhood cannot be empty")
  @Size(min = 3, max = 255, message = "Neighborhood must be between 3 and 255 characters")
  private String neighborhood;

  @Schema(
      description = "City",
      example = "São Paulo",
      requiredMode = Schema.RequiredMode.REQUIRED
  )
  @NotBlank(message = "City cannot be empty")
  @Size(min = 2, max = 255, message = "City must be between 2 and 255 characters")
  private String city;

  @Schema(
      description = "State (UF)",
      example = "SP",
      requiredMode = Schema.RequiredMode.REQUIRED
  )
  @NotBlank(message = "State cannot be empty")
  @Size(min = 2, max = 2, message = "State must contain exactly 2 characters")
  private String state;

  @Schema(
      description = "Country",
      example = "Brazil",
      requiredMode = Schema.RequiredMode.REQUIRED
  )
  @NotBlank(message = "Country cannot be empty")
  @Size(min = 2, max = 255, message = "Country must be between 2 and 255 characters")
  private String country;

  @Schema(
      description = "ZIP Code",
      example = "01000-000",
      requiredMode = Schema.RequiredMode.REQUIRED
  )
  @NotBlank(message = "Zip code cannot be empty")
  @Size(min = 5, max = 20, message = "Zip code must be between 5 and 20 characters")
  private String zipCode;
}
