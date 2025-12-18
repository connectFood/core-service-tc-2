package com.connectfood.core.entrypoint.rest.dto.restaurants;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "RestaurantsRequest", description = "Request for creating or updating a Restaurant")
public class RestaurantsRequest {

  @Schema(
      description = "name of the restaurant",
      example = "Outback",
      requiredMode = Schema.RequiredMode.REQUIRED
  )
  @NotBlank(message = "name cannot be empty")
  @Size(min = 3, max = 255, message = "Name must be between 3 and 255 characters")
  private String name;

  @Schema(
      description = "UUID of the restaurant type",
      example = "a912c110-3b3d-4fd7-bf00-8f77c15fd9ab",
      requiredMode = Schema.RequiredMode.REQUIRED
  )
  @NotNull(message = "Restaurant type UUID is required")
  private UUID restaurantsTypeUuid;
}
