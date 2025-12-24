package com.connectfood.core.entrypoint.rest.dto.restaurantitems;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Schema(
    name = "RestaurantItemsImagesRequest",
    description = "Request for creating or updating restaurant item images"
)
public class RestaurantItemsImagesRequest {

  @Schema(
      description = "Image name",
      example = "Main Image",
      requiredMode = Schema.RequiredMode.REQUIRED
  )
  @NotBlank(message = "Image name cannot be empty")
  @Size(min = 3, max = 255, message = "Image name must be between 3 and 255 characters")
  private String name;

  @Schema(
      description = "Image description",
      example = "Front view of the burger",
      requiredMode = Schema.RequiredMode.NOT_REQUIRED
  )
  @Size(max = 500, message = "Description must have at most 500 characters")
  private String description;

  @Schema(
      description = "Image path or URL",
      example = "/items/x-burger/main.png",
      requiredMode = Schema.RequiredMode.REQUIRED
  )
  @NotBlank(message = "Image path is required")
  @Size(max = 500, message = "Path must have at most 500 characters")
  private String path;
}
