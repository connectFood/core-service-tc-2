package com.connectfood.infrastructure.persistence.entity;

import java.util.HashSet;
import java.util.Set;

import com.connectfood.infrastructure.persistence.entity.commons.BaseEntity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "restaurant")
@Getter
@Setter
@NoArgsConstructor
public class RestaurantEntity extends BaseEntity {

  @Column(name = "name", nullable = false)
  private String name;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "restaurant_type_id", nullable = false)
  private RestaurantTypeEntity restaurantsType;

  @OneToMany(mappedBy = "restaurant", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
  private Set<RestaurantOpeningHourEntity> openingHours = new HashSet<>();

  @OneToOne(fetch = FetchType.LAZY)
  @JoinTable(name = "restaurant_address",
      joinColumns = @JoinColumn(name = "restaurant_id", referencedColumnName = "id"),
      inverseJoinColumns = @JoinColumn(name = "address_id", referencedColumnName = "id"))
  private AddressEntity address;

  @OneToOne(fetch = FetchType.LAZY)
  @JoinTable(name = "users_restaurant",
      joinColumns = @JoinColumn(name = "restaurant_id", referencedColumnName = "id"),
      inverseJoinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"))
  private UserEntity users;
}
