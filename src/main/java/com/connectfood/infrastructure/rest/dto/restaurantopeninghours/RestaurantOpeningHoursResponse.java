package com.connectfood.infrastructure.rest.dto.restaurantopeninghours;

import java.time.DayOfWeek;
import java.time.LocalTime;
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
    name = "RestaurantOpeningHoursResponse",
    description = "Response body with restaurant opening hours data"
)
public class RestaurantOpeningHoursResponse {

  @Schema(
      description = "Opening hours unique identifier",
      example = "9c2c2c8d-7a4f-4c39-9d32-52b371e2a910"
  )
  private UUID uuid;

  @Schema(
      description = "Day of week",
      example = "MONDAY",
      allowableValues = {
          "MONDAY", "TUESDAY", "WEDNESDAY", "THURSDAY",
          "FRIDAY", "SATURDAY", "SUNDAY"
      }
  )
  private DayOfWeek dayOfWeek;

  @Schema(
      description = "Start time (HH:mm:ss)",
      example = "09:00:00"
  )
  private LocalTime startTime;

  @Schema(
      description = "End time (HH:mm:ss)",
      example = "18:00:00"
  )
  private LocalTime endTime;
}
