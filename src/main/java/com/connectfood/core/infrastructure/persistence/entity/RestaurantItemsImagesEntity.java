package com.connectfood.core.infrastructure.persistence.entity;

import com.connectfood.core.infrastructure.persistence.entity.commons.BaseEntity;

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
@Table(name = "restaurant_items_images")
@Getter
@Setter
@NoArgsConstructor
public class RestaurantItemsImagesEntity extends BaseEntity {

  @Column(name = "name", nullable = false)
  private String name;

  @Column(name = "description")
  private String description;

  @Column(name = "path", nullable = false)
  private String path;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "restaurant_items_id", nullable = false)
  private RestaurantItemsEntity restaurantItems;
}
