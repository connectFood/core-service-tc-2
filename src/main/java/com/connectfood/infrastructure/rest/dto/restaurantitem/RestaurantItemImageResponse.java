package com.connectfood.infrastructure.rest.dto.restaurantitem;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonInclude;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(
    name = "RestaurantItemsImagesResponse",
    description = "Response body with restaurant item image data"
)
public class RestaurantItemImageResponse {

  @Schema(
      description = "Restaurant item image unique identifier",
      example = "9c2c2c8d-7a4f-4c39-9d32-52b371e2a910"
  )
  private UUID uuid;

  @Schema(
      description = "Image name",
      example = "Main Image"
  )
  private String name;

  @Schema(
      description = "Image description",
      example = "Front view of the burger"
  )
  private String description;

  @Schema(
      description = "Image path or URL",
      example = "/items/x-burger/main.png"
  )
  private String path;
}
