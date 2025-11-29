package com.connectfood.core.infrastructure.persistence.jpa;

import com.connectfood.core.infrastructure.persistence.entity.AddressEntity;
import com.connectfood.core.infrastructure.persistence.jpa.commons.JpaCommonRepository;

public interface JpaAddressRepository extends JpaCommonRepository<AddressEntity, Long> {
}
