package com.connectfood.infrastructure.persistence.entity;


import com.connectfood.infrastructure.persistence.entity.commons.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "restaurant_type")
@Getter
@Setter
@NoArgsConstructor
public class RestaurantTypeEntity extends BaseEntity {

  @Column(name = "name", nullable = false)
  private String name;

  @Column(name = "description")
  private String description;
}
