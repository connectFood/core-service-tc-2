package com.connectfood.core.entrypoint.rest.dto.restaurants;

import java.util.List;
import java.util.UUID;

import com.connectfood.core.entrypoint.rest.dto.address.AddressRequest;
import com.connectfood.core.entrypoint.rest.dto.restaurantopeninghours.RestaurantOpeningHoursRequest;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

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

  @Schema(
      description = "UUID of the user",
      example = "a912c110-3b3d-4fd7-bf00-8f77c15fd9ab",
      requiredMode = Schema.RequiredMode.REQUIRED
  )
  @NotNull(message = "User UUID is required")
  private UUID usersUuid;

  @Schema(
      description = "List of opening hours for the restaurant, including days of the week and time ranges",
      requiredMode = Schema.RequiredMode.REQUIRED
  )
  private List<RestaurantOpeningHoursRequest> openingHours;

  @Schema(
      description = "Address information of the restaurant, including street, city, state and postal code",
      requiredMode = Schema.RequiredMode.REQUIRED
  )
  private AddressRequest address;
}
