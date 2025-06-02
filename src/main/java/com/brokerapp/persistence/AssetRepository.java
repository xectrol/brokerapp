package com.brokerapp.persistence;


import com.brokerapp.persistence.entity.AssetEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface AssetRepository extends CrudRepository<AssetEntity, Long> {
    List<AssetEntity> findByCustomerId(Long customerId);
    Optional<AssetEntity> findByCustomerIdAndAssetName(Long customerId, String assetName);
}