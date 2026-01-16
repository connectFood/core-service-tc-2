package com.connectfood.infrastructure.persistence.entity;

import com.connectfood.infrastructure.persistence.entity.commons.BaseEntity;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "users_restaurant")
@Getter
@Setter
@NoArgsConstructor
public class UserRestaurantEntity extends BaseEntity {

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id", nullable = false)
  private UserEntity users;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "restaurant_id", nullable = false)
  private RestaurantEntity restaurants;
}
