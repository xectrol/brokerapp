package com.brokerapp.controller;

import com.brokerapp.controller.resource.AssetResource;
import com.brokerapp.mapper.AssetMapper;
import com.brokerapp.model.Asset;
import com.brokerapp.service.IAssetService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/assets")
@RequiredArgsConstructor
public class AssetController {
    private final IAssetService assetService;
    private final AssetMapper mapper;

    @GetMapping
    public List<AssetResource> listAssets(@RequestParam(required = false) Long customerId, UsernamePasswordAuthenticationToken principal) {
        List<Asset> assets = assetService.listAssetsByCustomerId(customerId, principal);
        return assets.stream().map(mapper::assetToAssetResource).toList();
    }
}

