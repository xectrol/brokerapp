package com.brokerapp.service.impl;

import com.brokerapp.mapper.AssetMapper;
import com.brokerapp.model.Asset;
import com.brokerapp.model.Customer;
import com.brokerapp.persistence.AssetRepository;
import com.brokerapp.persistence.entity.AssetEntity;
import com.brokerapp.service.IAssetService;
import com.brokerapp.util.Util;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class AssetService implements IAssetService {
    private final AssetRepository assetRepository;
    private final AssetMapper assetMapper;
    private final CustomerService customerService;

    public List<Asset> listAssetsByCustomerId(Long customerId, UsernamePasswordAuthenticationToken principal) {
        if (Util.isAdmin(principal)) {
            Customer customer = customerService.findByUsername(principal.getName());
            customerId = customer.getId();
        }
        return assetRepository.findByCustomerId(customerId).stream().map(assetMapper::assetEntityToAsset).toList();
    }

    public Asset assetsByCustomerIdAndAssetName(Long customerId, String assetName) {
        Optional<AssetEntity> asset = assetRepository.findByCustomerIdAndAssetName(customerId, assetName);
        return asset.map(assetMapper::assetEntityToAsset).orElse(null);
    }
}

