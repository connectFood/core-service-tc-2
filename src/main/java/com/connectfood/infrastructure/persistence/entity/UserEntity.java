package com.connectfood.infrastructure.persistence.entity;

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
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
public class UserEntity extends BaseEntity {

  @Column(name = "full_name", nullable = false)
  private String fullName;

  @Column(name = "email", nullable = false)
  private String email;

  @Column(name = "password", nullable = false)
  private String password;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "users_type_id", nullable = false)
  private UserTypeEntity usersType;
}
