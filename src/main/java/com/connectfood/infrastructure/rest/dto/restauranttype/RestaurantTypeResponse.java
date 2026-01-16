package com.connectfood.infrastructure.rest.dto.restauranttype;

import com.fasterxml.jackson.annotation.JsonInclude;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(name = "RestaurantsTypeResponse", description = "Response body with restaurants type data")
public class RestaurantTypeResponse {

  @Schema(
      description = "Restaurant type unique identifier",
      example = "5d102112-c916-43b6-b732-7d1618eb1136"
  )
  private UUID uuid;

  @Schema(
      description = "Restaurants type name",
      example = "STEAKHOUSE"
  )
  private String name;

  @Schema(
      description = "Restaurants type description",
      example = "Restaurant type that specializes in meats"
  )
  private String description;
}
