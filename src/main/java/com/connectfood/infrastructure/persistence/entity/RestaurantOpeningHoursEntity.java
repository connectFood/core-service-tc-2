package com.connectfood.infrastructure.persistence.entity;

import java.time.DayOfWeek;
import java.time.LocalTime;

import com.connectfood.infrastructure.persistence.entity.commons.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "restaurant_opening_hours")
@Getter
@Setter
@NoArgsConstructor
public class RestaurantOpeningHoursEntity extends BaseEntity {

  @Column(name = "day_week")
  private Integer dayWeek;

  @Column(name = "start_time", nullable = false)
  private LocalTime startTime;

  @Column(name = "end_time", nullable = false)
  private LocalTime endTime;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "restaurant_id", nullable = false)
  private RestaurantsEntity restaurant;

  public DayOfWeek getDayOfWeek() {
    return DayOfWeek.of(this.dayWeek);
  }

  public void setDayWeek(DayOfWeek dayWeek) {
    this.dayWeek = dayWeek.getValue();
  }
}
