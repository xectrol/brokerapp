package com.brokerapp.service;

import com.brokerapp.mapper.AssetMapper;
import com.brokerapp.model.Asset;
import com.brokerapp.persistence.AssetRepository;
import com.brokerapp.persistence.entity.AssetEntity;
import com.brokerapp.service.impl.AssetService;
import com.brokerapp.service.impl.CustomerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class AssetServiceTest {

    @Mock
    private AssetRepository assetRepository;

    @Mock
    private AssetMapper assetMapper;

    @Mock
    private CustomerService customerService;

    @InjectMocks
    private AssetService assetService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        assetService = new AssetService(assetRepository, assetMapper, customerService);
    }

    @Test
    void assetsByCustomerIdAndAssetName_found_shouldReturnMappedAsset() {
        // Arrange
        Long customerId = 77L;
        String assetName = "USD";

        AssetEntity entity = AssetEntity.builder().id(1L).customerId(customerId).assetName(assetName).build();
        Asset model = Asset.builder().id(1L).customerId(customerId).assetName(assetName).build();

        when(assetRepository.findByCustomerIdAndAssetName(customerId, assetName)).thenReturn(Optional.of(entity));
        when(assetMapper.assetEntityToAsset(entity)).thenReturn(model);

        // Act
        Asset result = assetService.assetsByCustomerIdAndAssetName(customerId, assetName);

        // Assert
        assertNotNull(result);
        assertEquals("USD", result.getAssetName());
    }

    @Test
    void assetsByCustomerIdAndAssetName_notFound_shouldReturnNull() {
        // Arrange
        Long customerId = 88L;
        String assetName = "TRY";

        when(assetRepository.findByCustomerIdAndAssetName(customerId, assetName)).thenReturn(Optional.empty());

        // Act
        Asset result = assetService.assetsByCustomerIdAndAssetName(customerId, assetName);

        // Assert
        assertNull(result);
    }
}
