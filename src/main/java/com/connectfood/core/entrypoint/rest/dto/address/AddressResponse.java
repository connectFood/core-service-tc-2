package com.connectfood.core.entrypoint.rest.dto.address;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

import com.connectfood.core.entrypoint.rest.dto.users.UsersResponse;
import com.fasterxml.jackson.annotation.JsonInclude;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(name = "AddressResponse", description = "Response body with address data")
public class AddressResponse {

  @Schema(
      description = "Address unique identifier",
      example = "9c2c2c8d-7a4f-4c39-9d32-52b371e2a910"
  )
  private UUID uuid;

  @Schema(
      description = "Street name",
      example = "Rua das Estrelas"
  )
  private String street;

  @Schema(
      description = "Address number",
      example = "123"
  )
  private String number;

  @Schema(
      description = "Address complement",
      example = "Apartment 45"
  )
  private String complement;

  @Schema(
      description = "Neighborhood",
      example = "Centro"
  )
  private String neighborhood;

  @Schema(
      description = "City",
      example = "São Paulo"
  )
  private String city;

  @Schema(
      description = "State (UF)",
      example = "SP"
  )
  private String state;

  @Schema(
      description = "Country",
      example = "Brazil"
  )
  private String country;

  @Schema(
      description = "ZIP Code",
      example = "01000-000"
  )
  private String zipCode;
}
