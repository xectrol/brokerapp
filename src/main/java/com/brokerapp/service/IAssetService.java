package com.brokerapp.service;


import com.brokerapp.model.Asset;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import java.util.List;

public interface IAssetService {
    List<Asset> listAssetsByCustomerId(Long customerId, UsernamePasswordAuthenticationToken principal);
}
