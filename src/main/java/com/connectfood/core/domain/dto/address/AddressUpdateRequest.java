package com.connectfood.core.domain.dto.address;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AddressUpdateRequest {

  private String street;
  private String number;
  private String complement;
  private String neighborhood;
  private String city;
  private String state;
  private String zipCode;
  private String country;
  private String addressType;
  private Boolean isDefault;
}
