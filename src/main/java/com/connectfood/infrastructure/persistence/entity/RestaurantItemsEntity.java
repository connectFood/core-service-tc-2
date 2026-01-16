package com.connectfood.infrastructure.persistence.entity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.connectfood.infrastructure.persistence.entity.commons.BaseEntity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "restaurant_items")
@Getter
@Setter
@NoArgsConstructor
public class RestaurantItemsEntity extends BaseEntity {

  @Column(name = "name", nullable = false)
  private String name;

  @Column(name = "description")
  private String description;

  @Column(name = "value", nullable = false)
  private BigDecimal value;

  @Column(name = "request_type", nullable = false)
  private String requestType;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "restaurant_id", nullable = false)
  private RestaurantsEntity restaurant;

  @OneToMany(
      mappedBy = "restaurantItems",
      fetch = FetchType.LAZY,
      cascade = CascadeType.ALL,
      orphanRemoval = true
  )
  private List<RestaurantItemsImagesEntity> images = new ArrayList<>();
}
