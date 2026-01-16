package com.connectfood.infrastructure.rest.dto.restaurantitems;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import com.connectfood.core.domain.model.enums.RestaurantItemServiceType;
import com.connectfood.infrastructure.rest.dto.restaurants.RestaurantsResponse;
import com.fasterxml.jackson.annotation.JsonInclude;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(name = "RestaurantItemsResponse", description = "Response body with restaurant item data")
public class RestaurantItemsResponse {

  @Schema(
      description = "Restaurant item unique identifier",
      example = "9c2c2c8d-7a4f-4c39-9d32-52b371e2a910"
  )
  private UUID uuid;

  @Schema(
      description = "Item name",
      example = "X-Burger"
  )
  private String name;

  @Schema(
      description = "Item description",
      example = "Burger with beef, cheese and lettuce"
  )
  private String description;

  @Schema(
      description = "Item price",
      example = "29.90"
  )
  private BigDecimal value;

  @Schema(
      description = "Request type of the item",
      example = "DELIVERY",
      allowableValues = {"DELIVERY", "LOCAL_ONLY"}
  )
  private RestaurantItemServiceType requestType;

  @Schema(
      description = "Restaurant associated with the item",
      implementation = RestaurantsResponse.class
  )
  private RestaurantsResponse restaurant;

  @Schema(
      description = "Images associated with the restaurant item"
  )
  private List<RestaurantItemsImagesResponse> images;
}
