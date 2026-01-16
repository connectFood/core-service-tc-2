package com.connectfood.infrastructure.rest.dto.restaurantstype;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "RestaurantsTypeRequest", description = "Request to create or update a Restaurants type")
public class RestaurantsTypeRequest {

  @Schema(
      description = "Restaurant type name",
      example = "STEAKHOUSE",
      requiredMode = Schema.RequiredMode.REQUIRED
  )
  @NotBlank(message = "name cannot be empty")
  @Size(min = 3, max = 255, message = "Name must be between 3 and 255 characters")
  private String name;

  @Schema(
      description = "Restaurant Type description",
      example = "Restaurant type that represents the type of food"
  )
  @Size(max = 255, message = "Description must be less than 255 characters")
  private String description;
}
