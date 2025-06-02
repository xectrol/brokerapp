package com.brokerapp.controller;

import com.brokerapp.controller.AssetController;
import com.brokerapp.controller.resource.AssetResource;
import com.brokerapp.mapper.AssetMapper;
import com.brokerapp.model.Asset;
import com.brokerapp.service.IAssetService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AssetControllerTest {

    @InjectMocks
    private AssetController assetController;

    @Mock
    private IAssetService assetService;

    @Mock
    private AssetMapper assetMapper;

    private UsernamePasswordAuthenticationToken principal;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        principal = new UsernamePasswordAuthenticationToken("admin", null,
                List.of(new SimpleGrantedAuthority("ROLE_ADMIN")));
    }

    @Test
    void listAssets_shouldReturnMappedAssetResources() {
        Long customerId = 1L;

        Asset asset = Asset.builder()
                .id(1L)
                .customerId(customerId)
                .assetName("BTC")
                .size(10L)
                .usableSize(10L)
                .build();

        AssetResource resource = AssetResource.builder()
                .id(1L)
                .customerId(customerId)
                .assetName("BTC")
                .size(10L)
                .usableSize(10L)
                .build();

        when(assetService.listAssetsByCustomerId(customerId, principal)).thenReturn(List.of(asset));
        when(assetMapper.assetToAssetResource(asset)).thenReturn(resource);

        List<AssetResource> result = assetController.listAssets(customerId, principal);

        assertEquals(1, result.size());
        assertEquals("BTC", result.get(0).getAssetName());
        verify(assetService).listAssetsByCustomerId(customerId, principal);
        verify(assetMapper).assetToAssetResource(asset);
    }
}
