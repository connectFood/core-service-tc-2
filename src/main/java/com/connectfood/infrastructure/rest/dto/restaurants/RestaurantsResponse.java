package com.connectfood.infrastructure.rest.dto.restaurants;

import java.util.List;
import java.util.UUID;

import com.connectfood.infrastructure.rest.dto.address.AddressResponse;
import com.connectfood.infrastructure.rest.dto.restaurantopeninghours.RestaurantOpeningHoursResponse;
import com.connectfood.infrastructure.rest.dto.restaurantstype.RestaurantsTypeResponse;
import com.connectfood.infrastructure.rest.dto.users.UsersResponse;
import com.fasterxml.jackson.annotation.JsonInclude;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

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

  @Schema(
      description = "List of opening hours configured for the restaurant, grouped by day of the week"
  )
  private List<RestaurantOpeningHoursResponse> openingHours;

  @Schema(
      description = "Address details of the restaurant"
  )
  private AddressResponse address;

  @Schema(
      description = "User associated with the restaurant",
      implementation = UsersResponse.class
  )
  private UsersResponse users;
}
