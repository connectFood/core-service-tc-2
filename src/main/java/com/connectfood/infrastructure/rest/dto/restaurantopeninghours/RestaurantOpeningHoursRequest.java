package com.connectfood.infrastructure.rest.dto.restaurantopeninghours;

import java.time.DayOfWeek;
import java.time.LocalTime;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Schema(
    name = "RestaurantOpeningHoursRequest",
    description = "Request for creating or updating restaurant opening hours"
)
public class RestaurantOpeningHoursRequest {

  @Schema(
      description = "Day of week",
      example = "MONDAY",
      requiredMode = Schema.RequiredMode.REQUIRED,
      allowableValues = {
          "MONDAY", "TUESDAY", "WEDNESDAY", "THURSDAY",
          "FRIDAY", "SATURDAY", "SUNDAY"
      }
  )
  @NotNull(message = "Day of week is required")
  private DayOfWeek dayOfWeek;

  @Schema(
      description = "Start time (HH:mm:ss)",
      example = "09:00:00",
      requiredMode = Schema.RequiredMode.REQUIRED
  )
  @NotNull(message = "Start time is required")
  private LocalTime startTime;

  @Schema(
      description = "End time (HH:mm:ss)",
      example = "18:00:00",
      requiredMode = Schema.RequiredMode.REQUIRED
  )
  @NotNull(message = "End time is required")
  private LocalTime endTime;
}
