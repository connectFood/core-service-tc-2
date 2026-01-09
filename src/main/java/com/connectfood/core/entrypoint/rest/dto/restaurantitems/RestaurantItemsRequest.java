package com.connectfood.core.entrypoint.rest.dto.restaurantitems;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import com.connectfood.core.domain.model.enums.RestaurantItemServiceType;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Schema(
    name = "RestaurantItemsRequest",
    description = "Request for creating or updating restaurant items"
)
public class RestaurantItemsRequest {

  @Schema(
      description = "Item name",
      example = "X-Burger",
      requiredMode = Schema.RequiredMode.REQUIRED
  )
  @NotBlank(message = "Item name cannot be empty")
  @Size(min = 3, max = 255, message = "Item name must be between 3 and 255 characters")
  private String name;

  @Schema(
      description = "Item description",
      example = "Burger with beef, cheese and lettuce",
      requiredMode = Schema.RequiredMode.NOT_REQUIRED
  )
  @Size(max = 500, message = "Description must have at most 500 characters")
  private String description;

  @Schema(
      description = "Item price",
      example = "29.90",
      requiredMode = Schema.RequiredMode.REQUIRED
  )
  @NotNull(message = "Item value is required")
  @DecimalMin(value = "0.01", message = "Item value must be greater than zero")
  private BigDecimal value;

  @Schema(
      description = "Service type of the item",
      example = "DELIVERY",
      allowableValues = {"DELIVERY", "LOCAL_ONLY"},
      requiredMode = Schema.RequiredMode.REQUIRED
  )
  @NotNull(message = "Service type is required")
  private RestaurantItemServiceType requestType;

  @Schema(
      description = "UUID of the restaurant",
      example = "c1c6b2d2-3b45-4f4e-9c4c-4c8b0c8c5a12",
      requiredMode = Schema.RequiredMode.REQUIRED
  )
  @NotNull(message = "Restaurant UUID is required")
  private UUID restaurantUuid;

  @Schema(
      description = "Images associated with the restaurant item",
      requiredMode = Schema.RequiredMode.NOT_REQUIRED
  )
  @Valid
  private List<RestaurantItemsImagesRequest> images;
}
