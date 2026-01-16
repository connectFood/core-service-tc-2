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
@Table(name = "restaurant_address")
@Getter
@Setter
@NoArgsConstructor
public class RestaurantAddressEntity extends BaseEntity {

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "restaurant_id", nullable = false)
  private RestaurantEntity restaurants;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "address_id", nullable = false)
  private AddressEntity address;
}
