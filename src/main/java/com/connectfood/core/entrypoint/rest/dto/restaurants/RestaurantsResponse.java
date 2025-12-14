package com.connectfood.core.entrypoint.rest.dto.restaurants;

import com.connectfood.core.entrypoint.rest.dto.restaurantstype.RestaurantsTypeResponse;

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
@Schema(name = "RestaurantsResponse", description = "Response body with restaurant data")
public class RestaurantsResponse {

  @Schema(
      description = "Restaurant unique identifier",
      example = "a912c110-3b3d-4fd7-bf00-8f77c15fd9ab"
  )
  private UUID uuid;

  @Schema(
      description = "Restaurant's name",
      example = "Outback"
  )
  private String name;

  @Schema(
      description = "Restaurant type associated with the restaurant",
      implementation = RestaurantsTypeResponse.class
  )
  private RestaurantsTypeResponse restaurantsType;
}
